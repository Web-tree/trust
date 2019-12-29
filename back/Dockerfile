#FROM openjdk:8-jdk-alpine
FROM openjdk:10-jre
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

#RUN apk add --no-cache bash
#RUN apt-get  --no-cache bash
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#"/wait-for-it.sh", "--timeout=0" ,"", "--",
