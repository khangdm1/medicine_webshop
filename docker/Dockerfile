FROM maven:3.8.4-openjdk-17 as build    
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests


FROM tomcat:9.0-jdk17
RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
