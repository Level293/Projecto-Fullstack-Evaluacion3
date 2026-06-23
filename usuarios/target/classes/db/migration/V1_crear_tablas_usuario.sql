-- 1. Tabla: Roles
CREATE TABLE roles (
    ID_Roles INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
);

-- 2. Tabla: Usuarios (Depende de Roles)
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(255) NOT NULL,
    rol_id INT,
    FOREIGN KEY (rol_id) REFERENCES roles(ID_Roles)
);

-- 3. Tabla independiente: Reservas
CREATE TABLE reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE,
    fecha_fin DATE,
    estado VARCHAR(50),
    total INT NOT NULL
);

-- 4. Tabla: Pago (Depende de Reservas)
CREATE TABLE pago (
    ID_pago INT AUTO_INCREMENT PRIMARY KEY,
    ID_reserva INT NOT NULL,
    monto INT NOT NULL,
    fecha_pago DATE NOT NULL,
    metodo VARCHAR(20) NOT NULL,
    estado BOOLEAN NOT NULL,
    FOREIGN KEY (ID_reserva) REFERENCES reservas(id_reserva)
);


--Inserto valores en las tablas
INSERT INTO roles (nombre) VALUES ('Administracion');
INSERT INTO roles (nombre) VALUES ('Limpieza');
INSERT INTO roles (nombre) VALUES ('Cliente');

INSERT INTO usuarios (nombre, correo, rol_id) VALUES ('Julian Pepeman', 'Jpepeman2@gmail.com', 1);
INSERT INTO usuarios (nombre, correo, rol_id) VALUES ('Maria Soto', 'MariaSotot82@hotmail.com', 2);
INSERT INTO usuarios (nombre, correo, rol_id) VALUES ('Bat man', 'Batman32@gmail.com', 3);

INSERT INTO reservas (fecha_inicio, fecha_fin, estado, total) VALUES ('2024-05-10', '2024-05-15', 'Confirmada', 35000);
INSERT INTO reservas (fecha_inicio, fecha_fin, estado, total) VALUES ('2024-05-11', '2024-05-18', 'Pendiente', 57000);


INSERT INTO pago (ID_reserva, monto, fecha_pago, metodo, estado) VALUES (1, 35000, '2026-05-20', 'Efectivo', true);
INSERT INTO pago (ID_reserva, monto, fecha_pago, metodo, estado) VALUES (2, 57000, '2026-04-30', 'Tarjeta', false);