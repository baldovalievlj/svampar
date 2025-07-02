# --------- Stage 1: Angular build ---------
FROM node:20 AS frontend
WORKDIR /app/m-client
COPY m-client/ ./
RUN npm install && npm run build

# --------- Stage 2: Ktor backend build ---------
FROM gradle:8-jdk21 AS backend
WORKDIR /app
COPY . .
RUN gradle shadowJar -x test

# --------- Stage 3: Runtime image ---------
FROM eclipse-temurin:21
WORKDIR /app

# Copy fat jar
COPY --from=backend /app/build/libs/*.jar app.jar

# Copy Angular build output to match your staticFiles() path
COPY --from=frontend /app/m-client/dist/m-client /app/m-client/dist/m-client

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]