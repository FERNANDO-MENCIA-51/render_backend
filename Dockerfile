# Etapa de build
FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

# Actualiza los paquetes para reducir vulnerabilidades
RUN apk update && apk upgrade && mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Actualiza los paquetes para reducir vulnerabilidades
RUN apk update && apk upgrade

# Copiar el archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Copiar el wallet (si es necesario, recuerda no subirlo a GitHub)
COPY src/main/resources/Wallet_SitemaDeVentas ./Wallet_SitemaDeVentas

# Configurar variables de entorno (usa variables de entorno en Render para mayor seguridad)
ENV TNS_ADMIN=/app/Wallet_SitemaDeVentas
ENV SPRING_DATASOURCE_URL=jdbc:oracle:thin:@sitemadeventas_high?TNS_ADMIN=${TNS_ADMIN}
ENV SPRING_DATASOURCE_USERNAME=fernando
ENV SPRING_DATASOURCE_PASSWORD=Haliongaming123

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]