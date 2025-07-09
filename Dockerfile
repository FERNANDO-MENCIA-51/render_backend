# Etapa de build
FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apk update && apk upgrade && mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN apk update && apk upgrade

# Copiar el archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Copiar el wallet de Oracle
COPY src/main/resources/Wallet_SitemaDeVentas ./Wallet_SistemaDeVentas

# Solo fija la ruta del wallet, el resto lo pones en Render
ENV TNS_ADMIN=/app/Wallet_SistemaDeVentas

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]