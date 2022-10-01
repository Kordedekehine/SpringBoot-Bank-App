
#Define Base Docker Image

#specify the JDK we're using
FROM openjdk:18

#specify the name of the maintainer
LABEL maintainer="akoredethefirst"

#add the target file and the app name .jar
ADD target/bank-0.0.1-SNAPSHOT.jar bank-0.0.1-SNAPSHOT.jar

#the host
EXPOSE 8080

#then specify the entry point of docker into the app
ENTRYPOINT ["java","-jar","bank-0.0.1-SNAPSHOT.jar"]


###
#THE KEYS USED TO DOCKERIZE THE APP...REMEMBER THE NAME OF MY APP IS Global-Corona-Alerter
#first we track the file area in our local pc in the cmd..remember you can only use small or lowercase
#docker build -t bank:latest .

#we build the docker images
#docker images

#Note the last command-9090-is the port we wanna give it---while 8080 is the original port of the app
#docker run -p 5050:8080 bank