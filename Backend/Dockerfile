# Etapa 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Ahora que el Root Directory es Back/Back, el archivo está en la raíz actual
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.updateDependencies=true

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
# Optimizaciones de JVM para contenedor de 512MB RAM y CPU compartida en Render
ENTRYPOINT ["java", "-XX:+UseSerialGC", "-XX:TieredStopAtLevel=1", "-Xms128m", "-Xmx256m", "-XX:MaxMetaspaceSize=128m", "-XX:ReservedCodeCacheSize=64m", "-Xss512k", "-jar", "app.jar"]