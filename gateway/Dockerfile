FROM java:8-jre
MAINTAINER kevin kevin <kevin.zwm@gmail.com.com>

ADD ./target/gateway.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/gateway.jar"]

EXPOSE 4000