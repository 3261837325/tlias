FROM eclipse-temurin:17-jre

WORKDIR /app

ENV TZ=Asia/Shanghai
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Shanghai"

COPY target/tlias-web-management-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
