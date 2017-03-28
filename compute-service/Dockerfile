FROM java:8-jre
MAINTAINER kevin kevin <kevin.zwm@gmail.com.com>

ADD ./target/compute-service-1.0.0.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/compute-service-1.0.0.jar"]

EXPOSE 6000