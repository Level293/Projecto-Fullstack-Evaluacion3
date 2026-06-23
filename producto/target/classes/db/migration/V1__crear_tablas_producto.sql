-- 1. Tabla independiente: Tipo de Habitacion
CREATE TABLE tipo_habitacion (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY, -- Coincide perfectamente con idTipo en Java
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    capacidad INT,
    precio INT
);

-- 2. Tabla independiente: Servicio
CREATE TABLE servicio (
    id_servicio INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL
);

-- 3. Tabla Habitacion (Depende de Tipo de Habitacion)
CREATE TABLE habitacion (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    id_hotel INT NOT NULL,       -- Tu referencia al microservicio externo
    id_tipo INT NOT NULL,        -- Nombre unificado y limpio
    numero INT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_tipo) REFERENCES tipo_habitacion(id_tipo)
);

-- 4. Tabla Intermedia: Hab_Servicio (Solo depende de Habitacion y Servicio)
CREATE TABLE hab_servicio (
    id_hab_servicio INT AUTO_INCREMENT PRIMARY KEY,
    id_habitacion INT NOT NULL,
    id_servicio INT NOT NULL,
    FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion),
    FOREIGN KEY (id_servicio) REFERENCES servicio(id_servicio)
    -- Se elimina id_tipo porque ya se conoce a través de id_habitacion
);

-- 5. Tabla Reserva Habitacion (Depende de Habitacion)
CREATE TABLE reserva_habitacion (
    id_reserva_hab INT AUTO_INCREMENT PRIMARY KEY,
    precio_noche INT NOT NULL,   -- Cambiado a snake_case standard
    id_reserva INT NOT NULL,     -- Tu referencia externa a la reserva general
    id_habitacion INT NOT NULL,
    FOREIGN KEY (id_habitacion) REFERENCES habitacion(id_habitacion)
);