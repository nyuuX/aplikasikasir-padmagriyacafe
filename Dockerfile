# ---- Tahap 1: build WAR dengan Maven ----
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY web ./web
RUN mvn -q -DskipTests package

# ---- Tahap 2: jalankan di Tomcat 9 (javax.servlet) ----
FROM tomcat:9.0-jdk11-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/PadmagriyaCafe.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
