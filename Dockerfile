
# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Ensure Maven wrapper is executable
RUN chmod +x mvnw

# Pre-download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests for speed)
RUN ./mvnw clean package -DskipTests -B

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the final jar
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 2006

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
