
GO
CREATE DATABASE EstacionEspacial

GO
USE EstacionEspacial

GO
CREATE TABLE Modulos(
	IdModulo UNIQUEIDENTIFIER PRIMARY KEY NOT NULL
	,Proposito varchar(60) NOT NULL
	,NombrePaisPertenece varchar(30) NOT NULL
)

CREATE TABLE Dormitorios(
		IdSala UNIQUEIDENTIFIER PRIMARY KEY NOT NULL
		,metrosCubicos Decimal(3) NOT NULL
		,NumeroCamasDisponibles int NOT NULL
		,IdModulo UNIQUEIDENTIFIER NOT NULL
		,CONSTRAINT FK_ModuloDormitorio FOREIGN KEY (IdModulo) REFERENCES Modulos(IdModulo) ON DELETE CASCADE
		,CONSTRAINT CK_metrosCubicosNegativos CHECK (metrosCubicos>0)
		,CONSTRAINT CK_NumeroCamas CHECK (NumeroCamasDisponibles>0)
)

CREATE TABLE Tripulantes(
	IdTripulante UNIQUEIDENTIFIER PRIMARY KEY NOT NULL
	,Nombre Varchar(30) NOT NULL
	,Apellidos Varchar(40) NOT NULL
	,FechaNacimiento Date NOT NULL
	,IdDormitorio UNIQUEIDENTIFIER NULL
	CONSTRAINT FK_DormitorioTripulante FOREIGN KEY (IdDormitorio) REFERENCES Dormitorios(IdSala) ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE Cientificos(
	IdTripulante UNIQUEIDENTIFIER  PRIMARY KEY NOT NULL
	,NombreUniversidad Varchar(40) NOT NULL
	,AnhiosExperiencia  smallint NOT NULL
	,CONSTRAINT FK_TripulanteCientifico FOREIGN KEY (IdTripulante) REFERENCES Tripulantes(IdTripulante) ON DELETE CASCADE ON UPDATE CASCADE
)

CREATE TABLE Ingenieros(
	IdTripulante UNIQUEIDENTIFIER  PRIMARY KEY NOT NULL
	,Especializacion Varchar(40) NOT NULL
	,CONSTRAINT FK_TripulanteIngeniero FOREIGN KEY (IdTripulante) REFERENCES Tripulantes(IdTripulante) ON DELETE CASCADE ON UPDATE CASCADE
)


CREATE TABLE Provisiones(
	IdProvision UNIQUEIDENTIFIER  PRIMARY KEY NOT NULL
	,Nombre Varchar(35)NOT NULL
	,Cantidad int NOT NULL
	,CONSTRAINT CK_CantidadNegativa CHECK (Cantidad>=0)
)


CREATE TABLE Despensas(
		IdSala UNIQUEIDENTIFIER PRIMARY KEY NOT NULL
		,metrosCubicos Decimal(3) NOT NULL
		,Capacidad int NOT NULL
		,IdModulo UNIQUEIDENTIFIER NOT NULL
		,CONSTRAINT FK_ModuloDespensa FOREIGN KEY (IdModulo) REFERENCES Modulos(IdModulo) ON DELETE CASCADE ON UPDATE CASCADE
		,CONSTRAINT CK_capacidadNegativa CHECK (Capacidad>=0)
)
CREATE TABLE Almacenaje(
		IdAlmacenaje UNIQUEIDENTIFIER NOT NULL
		,IdProvision UNIQUEIDENTIFIER NOT NULL
		,IdDespensa UNIQUEIDENTIFIER NOT NULL
		,cantidadAlmacenada int NOT NULL
		,CONSTRAINT PK_Almacenaje PRIMARY KEY (IdAlmacenaje)
	    ,CONSTRAINT FK_ProvisionEsAlmacenada FOREIGN KEY (IdProvision) REFERENCES Provisiones(IdProvision) ON DELETE NO ACTION ON UPDATE NO ACTION
	    ,CONSTRAINT FK_DespensaAlmacenaProvision FOREIGN KEY (IdDespensa) REFERENCES Despensas(IdSala) ON DELETE CASCADE ON UPDATE CASCADE
		,CONSTRAINT CK_CantidadAlmacenadaNegativa CHECK (CantidadAlmacenada>0)
)

CREATE TABLE Consumiciones(
		IdConsumicion UNIQUEIDENTIFIER  NOT NULL
		,IdConsumidor UNIQUEIDENTIFIER NOT NULL
		,IdProvision UNIQUEIDENTIFIER NOT NULL
		,CantidadConsumida int NOT NULL
		,IdDespensa UNIQUEIDENTIFIER NOT NULL
		,CONSTRAINT PK_Consumiciones PRIMARY KEY (IdConsumicion)
	    ,CONSTRAINT FK_ProvisionEsConsumida FOREIGN KEY (IdConsumidor) REFERENCES Tripulantes(IdTripulante)ON DELETE NO ACTION ON UPDATE NO ACTION
		,CONSTRAINT FK_DespensaDeDondeSeConsumio FOREIGN KEY (IdDespensa) REFERENCES Despensas(IdSala) ON DELETE CASCADE ON UPDATE CASCADE
	    ,CONSTRAINT FK_TripulanteConsumeProvision FOREIGN KEY (IdProvision) REFERENCES Provisiones(IdProvision) ON DELETE CASCADE ON UPDATE CASCADE 
		,CONSTRAINT CK_CantidadConsumidaNegativa CHECK (CantidadConsumida>0)
)

GO

CREATE LOGIN x WITH PASSWORD='x',
DEFAULT_DATABASE=EstacionEspacial
USE EstacionEspacial
CREATE USER x FOR LOGIN x
GRANT EXECUTE, INSERT, UPDATE, DELETE,
SELECT TO x

GO


CREATE OR ALTER TRIGGER TInsertarOActualizarAlmacenaje ON Almacenaje AFTER INSERT,UPDATE AS
BEGIN
		DECLARE @IdAlmacenaje UNIQUEIDENTIFIER,@cantidad int,@IdProvision UNIQUEIDENTIFIER,@IdDespensa UNIQUEIDENTIFIER,@cantidadAlmacenada int
		DECLARE CursorCantidadInsertada CURSOR FOR SELECT*FROM inserted
		OPEN  CursorCantidadInsertada
		FETCH CursorCantidadInsertada INTO @IdAlmacenaje,@IdProvision,@IdDespensa,@cantidad
		WHILE(@@FETCH_STATUS=0)
			BEGIN
				SELECT @cantidadAlmacenada=SUM(cantidadAlmacenada) FROM Almacenaje WHERE IdProvision=@IdProvision--COMPRUEBO LA CANTIDAD QUE YA HA SIDO ALMACENADA PARA USARLO JUSTO DEBAJO EN EL IF Y ASÍ SABER SI ESTAMOS INSERTANDO UNA CANTIDAD DE PROVISION QUE NO TENEMOS YA QUE HA SIDO ALMACENADA
				IF((SELECT Capacidad FROM Despensas WHERE IdSala=@IdDespensa)<@cantidad OR (SELECT Cantidad FROM Provisiones WHERE @IdProvision=IdProvision)<@cantidadAlmacenada)
				BEGIN
					ROLLBACK
				END
				ELSE
					BEGIN
						IF( EXISTS (SELECT * FROM deleted WHERE IdAlmacenaje=@IdAlmacenaje))
							BEGIN
							IF((SELECT cantidadAlmacenada FROM deleted WHERE IdAlmacenaje=@IdAlmacenaje)>@cantidad)
								BEGIN
									UPDATE Despensas SET Capacidad+=((SELECT cantidadAlmacenada FROM deleted WHERE IdAlmacenaje=@IdAlmacenaje)-@cantidad) WHERE IdSala=@IdDespensa
								END
							ELSE
								BEGIN
									UPDATE Despensas SET Capacidad-=((SELECT cantidadAlmacenada FROM deleted WHERE IdAlmacenaje=@IdAlmacenaje)-@cantidad) WHERE IdSala=@IdDespensa
								END
						END
						ELSE
							BEGIN
								UPDATE Despensas SET Capacidad-=@cantidad WHERE IdSala=@IdDespensa
							END
					END
				FETCH CursorCantidadInsertada INTO @IdAlmacenaje,@IdProvision,@IdDespensa,@cantidad
			END
			CLOSE CursorCantidadInsertada
			DEALLOCATE CursorCantidadInsertada
END

GO

CREATE OR ALTER TRIGGER TInsertarOActualizarConsumiciones ON Consumiciones AFTER INSERT,UPDATE AS
BEGIN--ESTE TRIGGER TIENE 2 CURSORES UNO RECORRE LA TABLA INSERTED Y OTRO LA TABLA ALMACENAJE PARA RESTAR LAS CANTIDADES CONSUMIDAS Y ASÍ SI CONSUMES ALGO ESTE DEJE DE ALMACENARSE
	DECLARE @IdProvision UNIQUEIDENTIFIER,@IdDespensa UNIQUEIDENTIFIER,@CantidadConsumida int,@IdAlmacenaje UNIQUEIDENTIFIER,@CantidadQueAlmacena int
	DECLARE CursorCantidadConsumida CURSOR FOR SELECT IdProvision,CantidadConsumida,IdDespensa FROM inserted
	
		OPEN  CursorCantidadConsumida
		FETCH CursorCantidadConsumida INTO @IdProvision,@CantidadConsumida,@IdDespensa
		WHILE(@@FETCH_STATUS=0)
			BEGIN
				IF((SELECT Cantidad  FROM Provisiones WHERE IdProvision=@IdProvision)<@CantidadConsumida OR NOT EXISTS (SELECT IdDespensa,IdProvision FROM Almacenaje WHERE IdDespensa=@IdDespensa AND IdProvision=@IdProvision GROUP BY IdDespensa,IdProvision HAVING SUM(cantidadAlmacenada)>=@CantidadConsumida))
					BEGIN--EN LAS CONDICIONES DEL IF ANTERIOR COMPRUEBO SI NO SE ESTA CONSUMIENDO MAYOR CANTIDAD DE LA EXISTENTE EN LA ESTACIÓN Y LA OTRA CONDICIÓN COMPRUEBO SI NO SE ESTA CONSUMIENDO MÁS DE LO QUE HAY ALMACENADO EN DICHA DESPENSA  
						ROLLBACK
					END
				ELSE
					BEGIN
						UPDATE Provisiones SET Cantidad-=@CantidadConsumida WHERE IdProvision=@IdProvision
					END
			FETCH CursorCantidadConsumida INTO @IdProvision,@CantidadConsumida,@IdDespensa
			END
		CLOSE CursorCantidadConsumida
		DEALLOCATE CursorCantidadConsumida
END


GO

CREATE OR ALTER TRIGGER TEliminarAlmacenaje ON Almacenaje AFTER DELETE AS
BEGIN
	DECLARE @IdDespensa UNIQUEIDENTIFIER,@CantidadAlmacenada int
	DECLARE CursorAlmacenajeEliminado CURSOR FOR SELECT IdDespensa,cantidadAlmacenada FROM deleted
	OPEN CursorAlmacenajeEliminado
	FETCH CursorAlmacenajeEliminado INTO @IdDespensa,@CantidadAlmacenada 
	WHILE(@@FETCH_STATUS=0)
		BEGIN
			UPDATE Despensas SET Capacidad+=@cantidadAlmacenada WHERE IdSala=@IdDespensa
			FETCH CursorAlmacenajeEliminado INTO @IdDespensa,@CantidadAlmacenada 
		END
		CLOSE CursorAlmacenajeEliminado
		DEALLOCATE CursorAlmacenajeEliminado
END


GO
CREATE OR ALTER TRIGGER TEliminarConsumicion ON Consumiciones AFTER DELETE AS
BEGIN
	DECLARE @IdProvision UNIQUEIDENTIFIER,@CantidadConsumida int,@IdDespensa UNIQUEIDENTIFIER
	DECLARE CursorConsumicionEliminada CURSOR FOR SELECT IdProvision,CantidadConsumida,IdDespensa FROM deleted
	OPEN CursorConsumicionEliminada
	FETCH CursorConsumicionEliminada INTO @IdProvision,@CantidadConsumida,@IdDespensa
	WHILE(@@FETCH_STATUS=0)
		BEGIN
			UPDATE Provisiones SET Cantidad+=@CantidadConsumida WHERE IdProvision=@IdProvision
			FETCH CursorConsumicionEliminada INTO @IdProvision,@CantidadConsumida,@IdDespensa
		END
		CLOSE CursorConsumicionEliminada 
		DEALLOCATE CursorConsumicionEliminada 
END

GO
CREATE OR ALTER TRIGGER TEliminarTripulante ON Tripulantes AFTER DELETE AS
BEGIN
	DECLARE @IdDormitorio UNIQUEIDENTIFIER
	DECLARE CursorTripulanteEliminado CURSOR FOR SELECT IdDormitorio  FROM deleted
	OPEN CursorTripulanteEliminado
	FETCH CursorTripulanteEliminado INTO @IdDormitorio
	WHILE(@@FETCH_STATUS=0)
			BEGIN
				IF(@IdDormitorio!=NULL)
					BEGIN
						UPDATE Dormitorios SET NumeroCamasDisponibles+=1 WHERE IdSala=@IdDormitorio
					END
			FETCH CursorTripulanteEliminado INTO @IdDormitorio
			END
	CLOSE CursorTripulanteEliminado
	DEALLOCATE CursorTripulanteEliminado
END
GO
CREATE OR ALTER TRIGGER TEliminarDormitorios ON Dormitorios AFTER DELETE AS
BEGIN
	DECLARE @IdDormitorio UNIQUEIDENTIFIER
	DECLARE CursorDormitorioEliminado CURSOR FOR SELECT IdSala FROM deleted
	OPEN CursorDormitorioEliminado
	FETCH CursorDormitorioEliminado INTO @IdDormitorio
	WHILE(@@FETCH_STATUS=0)
		BEGIN
			UPDATE Tripulantes SET IdDormitorio=NULL WHERE IdDormitorio=@IdDormitorio
			FETCH CursorDormitorioEliminado INTO @IdDormitorio
		END
	CLOSE CursorDormitorioEliminado
	DEALLOCATE CursorDormitorioEliminado
END
GO

CREATE OR ALTER TRIGGER TInsertarOActualizarTripulante ON Tripulantes AFTER INSERT,UPDATE AS
BEGIN
	DECLARE @IdDormitorio UNIQUEIDENTIFIER
	DECLARE CursorTripulanteInsertado CURSOR FOR SELECT IdDormitorio FROM inserted
	OPEN CursorTripulanteInsertado
	FETCH CursorTripulanteInsertado INTO @IdDormitorio
	WHILE(@@FETCH_STATUS=0)
			BEGIN
				IF(@IdDormitorio!=NULL)
					BEGIN
					IF((SELECT NumeroCamasDisponibles-1 FROM Dormitorios WHERE IdSala=@IdDormitorio)>-1)
						BEGIN
							UPDATE Dormitorios SET NumeroCamasDisponibles-=1 WHERE IdSala=@IdDormitorio
						END
					ELSE
						BEGIN
							ROLLBACK
						END
					END
				FETCH CursorTripulanteInsertado INTO @IdDormitorio
			END
			CLOSE CursorTripulanteInsertado
			DEALLOCATE CursorTripulanteInsertado
END

GO
CREATE OR ALTER TRIGGER TInsertarOActualizarProvision ON Provisiones AFTER UPDATE AS
BEGIN
	DECLARE @IdProvision UNIQUEIDENTIFIER,@Cantidad int,@VCB int,@IdAlmacenaje UNIQUEIDENTIFIER,@CantidadAlmacenada int
	DECLARE CursorProvisionesInsertadas CURSOR FOR SELECT IdProvision,Cantidad FROM inserted
	DECLARE CursorModificarAlmacenaje CURSOR FOR SELECT IdAlmacenaje,cantidadAlmacenada FROM Almacenaje
	OPEN CursorProvisionesInsertadas
	FETCH CursorProvisionesInsertadas INTO @IdProvision,@Cantidad
	WHILE(@@FETCH_STATUS=0)
		BEGIN
		IF((SELECT SUM(cantidadAlmacenada) FROM Almacenaje WHERE IdProvision=@IdProvision) IS NOT NULL AND (SELECT SUM(cantidadAlmacenada) FROM Almacenaje WHERE IdProvision=@IdProvision)>@Cantidad)
			BEGIN
			OPEN CursorModificarAlmacenaje
			FETCH CursorModificarAlmacenaje INTO @IdAlmacenaje,@CantidadAlmacenada
				WHILE( @@FETCH_STATUS=0 )
					BEGIN
						IF(@Cantidad>0)
							BEGIN
								IF(@CantidadAlmacenada-@Cantidad)>0
									BEGIN
										UPDATE Almacenaje SET cantidadAlmacenada=@Cantidad WHERE IdAlmacenaje=@IdAlmacenaje
										SET @Cantidad=0
									END
								ELSE
									BEGIN 
										SET @Cantidad-=@CantidadAlmacenada
									END
							END
						ELSE
							BEGIN
								DELETE FROM Almacenaje WHERE IdAlmacenaje=@IdAlmacenaje
							END
						FETCH CursorModificarAlmacenaje INTO @IdAlmacenaje,@CantidadAlmacenada
					END
			CLOSE CursorModificarAlmacenaje
		END
		DEALLOCATE CursorModificarAlmacenaje
		FETCH CursorProvisionesInsertadas INTO @IdProvision,@Cantidad
		END
	CLOSE CursorProvisionesInsertadas 
	DEALLOCATE CursorProvisionesInsertadas 
END
