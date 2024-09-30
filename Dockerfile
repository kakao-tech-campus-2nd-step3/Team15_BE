FROM openjdk:21-slim

WORKDIR /app

ARG JAR_PATH=./build/libs

COPY ${JAR_PATH}/*.jar app.jar

ENV PROFILE=${PROFILE}

CMD ["java", "-jar", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=${PROFILE}", "/app/app.jar"]
