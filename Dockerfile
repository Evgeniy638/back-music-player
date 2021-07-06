FROM openjdk:16
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "-Dapp.redirect_uri=$REDIRECT_URI", "-Dapp.client_id=$CLIENT_ID", "-Dapp.client_secret=$CLIENT_SECRET", "-Dapp.front_login_url=$FRONT_URL_LOGIN", "application.jar"]
