FROM gradle:jdk21 AS builder
WORKDIR /build

COPY . .
RUN gradle jar

FROM openjdk:21

WORKDIR /app

COPY --from=builder /build/build/libs/lab1-1.0.jar /app/app.jar

CMD ["java","-DFCGI_PORT=9000","-jar","/app/app.jar"]