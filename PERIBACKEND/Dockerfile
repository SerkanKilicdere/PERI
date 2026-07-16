FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:25-jdk
WORKDIR /app

RUN mkdir -p /app/uploaded
COPY --from=build /app/target/*SNAPSHOT.jar /app/app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
