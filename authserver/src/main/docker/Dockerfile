FROM java:8
VOLUME /tmp
ADD authserver-0.0.1-SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9000
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
