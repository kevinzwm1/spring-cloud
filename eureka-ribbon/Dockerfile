FROM java:8-jre
MAINTAINER kevin kevin <kevin.zwm@gmail.com.com>

ADD ./target/eureka-ribbon-1.0.0.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eureka-ribbon-1.0.0.jar"]

EXPOSE 6000