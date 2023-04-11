FROM adoptopenjdk:11-jre-hotspot 
COPY target/*.jar /usr/src/komi-inbound.jar 
RUN mkdir -p /home/logs 
VOLUME /home/logs
WORKDIR /usr/src 
EXPOSE 9001
ENTRYPOINT ["java","-jar","/usr/src/komi-inbound.jar"]

# mvn clean package -DskipTests
# docker build -t fransmzh/komi-inbound:1.8 .
# docker push fransmzh/komi-inbound:1.8