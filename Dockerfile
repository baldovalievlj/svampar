# --------- Stage 1: Angular build ---------
FROM node:20 AS frontend
WORKDIR /app/m-client
COPY m-client/ ./
RUN npm install
RUN NODE_OPTIONS=--max_old_space_size=2048 npm run build

# --------- Stage 2: Ktor backend build ---------
FROM gradle:8-jdk17 AS backend
WORKDIR /app
COPY . .
RUN ./gradlew shadowJar -x test --no-daemon --max-workers=1

# --------- Stage 3: Runtime image ---------
FROM eclipse-temurin:17
WORKDIR /app

# Copy fat jar
COPY --from=backend /app/build/libs/http-api-all.jar app.jar

# Copy Angular build output
COPY --from=frontend /app/m-client/dist/m-client /app/m-client/dist/m-client

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]