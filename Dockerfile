FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/ktor-itami-chat.jar
ENTRYPOINT ["java","-jar","/app/fat.jar"]