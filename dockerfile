# Stage 1: Build (Aapko local pe Maven install karne ki zarurat nahi padegi)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Sabse pehle sirf pom.xml copy karein taaki dependencies cache ho sakein
COPY pom.xml .
COPY src ./src

# Build command - isse 'target' folder aur JAR file banegi
RUN mvn clean package -DskipTests

# Stage 2: Runtime (Final image choti hogi)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Build stage se JAR file copy karein
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]