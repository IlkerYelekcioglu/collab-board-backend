## 1. Aşama: Derleme (Build)
#FROM eclipse-temurin:17-jdk-alpine AS builder
#WORKDIR /app
## Maven wrapper'ı ve pom.xml'i kopyala
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
## Bağımlılıkları indir (önbelleğe alınır)
#RUN ./mvnw dependency:go-offline
## Kaynak kodları kopyala ve derle
#COPY src ./src
#RUN ./mvnw clean package -DskipTests
#
## 2. Aşama: Çalıştırma (Runtime)
#FROM eclipse-temurin:17-jre-alpine
#WORKDIR /app
## Sadece derlenmiş JAR dosyasını kopyala
#COPY --from=builder /app/target/*.jar app.jar
## Uygulamanın çalışacağı portu belirt
#EXPOSE 8080
## JAR'ı çalıştır
#ENTRYPOINT ["java", "-jar", "app.jar"]

## 1. Aşama: Derleme (Build)
#FROM eclipse-temurin:17-jdk-jammy AS builder
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#RUN ./mvnw clean package -DskipTests
#
## 2. Aşama: Çalıştırma (Runtime)
#FROM eclipse-temurin:17-jre-jammy
#WORKDIR /app
#COPY --from=builder /app/target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]

# 1. Aşama: Derleme (Build)
#FROM eclipse-temurin:17-jdk-jammy AS builder
#WORKDIR /app
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#
## YENİ EKLENEN SATIR: mvnw dosyasına çalıştırma izni ver
#RUN chmod +x mvnw
#
#RUN ./mvnw dependency:go-offline
#COPY src ./src
#RUN ./mvnw clean package -DskipTests
#
## 2. Aşama: Çalıştırma (Runtime)
#FROM eclipse-temurin:17-jre-jammy
#WORKDIR /app
#COPY --from=builder /app/target/*.jar app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]


# 1. Build Stage
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Maven wrapper
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Maven wrapper permission
RUN chmod +x mvnw

# Dependencies
RUN ./mvnw dependency:go-offline -B

# Source code
COPY src ./src

# Build application
RUN ./mvnw clean package -DskipTests -B

# 2. Runtime Stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
