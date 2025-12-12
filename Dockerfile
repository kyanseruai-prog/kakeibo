# ビルドステージ
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 実行ステージ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/kakeibo-1.0.0.jar app.jar

# ポートを公開
EXPOSE 8080

# アプリケーションを起動
ENTRYPOINT ["java", "-jar", "app.jar"]
