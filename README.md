V2: Acoplamos los microservicios, refactorizamos cada uno de estos para que funcionen de forma independiente en cada microservicio, usuario se agregaron los tests, se hizo el yml, el config y el script de creacion y poblado de tablas


V3: Se modifica la estructura de Producto (Javi) Creación del archivo de configuración .yml Implementación de los tests correspondientes para los métodos de los services existentes.

V4 creamos un nuevo microservicio llamado gateway este ayuda a la hora de ejecutar los distintos microservicios, permitiendonos ejecutarlos desde un puerto 8080 en lugar de en un puerto especifico de cada microservicio, en si tenemos las bases para poder crear y usar eureka, esta nos permitirá conectarnos sin tener que usar un puerto predefinido, eureka se encargará de encontrar cada microservicio
