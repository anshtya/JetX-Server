FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY gradle/ ./gradle/
COPY gradlew settings.gradle.kts build.gradle.kts ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon
COPY src/ ./src/
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
RUN --mount=type=secret,id=firebase-service-account_json,dst=/etc/secrets/firebase-service-account.json
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dfirebase.config.path=${FIREBASE_CONFIG_PATH}", "-jar", "/app/app.jar"]