FROM java:8
VOLUME /tmp
ADD configserver-0.0.1-SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8888
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
