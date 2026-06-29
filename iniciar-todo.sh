#!/bin/bash

echo "Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/eureka\" && ./mvnw spring-boot:run"'

echo "Esperando 12 segundos a que Eureka se estabilice..."
sleep 12

echo "Iniciando API Gateway..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/gateway\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Jedis..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/producto\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Sables..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/usuarios\" && ./mvnw spring-boot:run"'

echo "Ecosistema lanzado. Dashboard disponible en http://localhost:8761"