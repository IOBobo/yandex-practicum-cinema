# ------------------------------------------------------------
# Build stage
# ------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

# Copy pom first to cache downloaded dependencies.
COPY pom.xml ./

# RUN mvn -B dependency:go-offline

COPY src ./src

# Maven build runs inside the Docker image.
RUN mvn -B clean package -DskipTests


# ------------------------------------------------------------
# Runtime stage
# ------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=build /build/target/proxy-1.0.jar /app/app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
