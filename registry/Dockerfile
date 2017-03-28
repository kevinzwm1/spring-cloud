FROM java:8-jre
MAINTAINER kevin kevin <kevin.zwm@gmail.com.com>

ADD ./target/registry.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/registry.jar"]

EXPOSE 8761