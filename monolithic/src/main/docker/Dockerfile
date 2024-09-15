FROM eclipse-temurin:11
VOLUME /tmp
ADD *.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8080
CMD ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
