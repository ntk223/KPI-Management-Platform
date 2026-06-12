# ====== BUILD STAGE ======
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy libs first — mandatory for system-scope dependencies
COPY libs ./libs

COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins

# Copy code
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests

# ====== RUNTIME STAGE ======
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy fat jar
COPY --from=build /app/target/*.jar app.jar

# Staff has system-scope libs → must include libs folder
COPY libs ./libs

EXPOSE 8888
USER 1001

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]