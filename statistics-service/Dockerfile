FROM java:8-jre
MAINTAINER kevin kevin <kevin.zwm@gmail.com.com>
ADD ./target/statistics-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/statistics-service.jar"]

EXPOSE 7000