FROM eclipse-temurin:11
VOLUME /tmp
ADD *.jar /app.jar
ADD wait-for-it.sh /wait-for-it.sh
RUN bash -c 'touch /app.jar'
RUN bash -c 'chmod 777 /wait-for-it.sh'
EXPOSE 6161
CMD ["java","-Dspring.profiles.active=docker","-jar","/app.jar"]
