FROM java:8-jre
MAINTAINER kevin kevin <kevin.zwm@gmail.com.com>

ADD ./target/monitoring.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/monitoring.jar"]

EXPOSE 8989 8080