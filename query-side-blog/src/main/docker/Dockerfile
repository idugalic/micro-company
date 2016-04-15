FROM java:8
VOLUME /tmp
ADD query-side-blog-0.0.1-SNAPSHOT.jar /app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8081
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
