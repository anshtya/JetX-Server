FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY gradle/ ./gradle/
COPY gradlew settings.gradle.kts build.gradle.kts ./
RUN chmod +x gradlew
COPY src/ ./src/
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app/app.jar"]