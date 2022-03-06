# Introduction

This project contains the code and docs for running an API to hook the notification with related payment endpoints.

# <a href="build-api"></a> Build the API

* Clone this repo into a directory `git clone https://github.com/JohnnyQuan10/digital-wallet-service.git`
* Change directory to the newly cloned repo: `cd digital-wallet-service`
* Run the build `./gradlew clean build`
* The build will produce an JAR at `build/libs/digital-wallet-service-0.1.0.jar`

# <a href="test-api"></a> Test the API

* Launch the spring boot application by simply edit the main class: `Application`.
* Open Swagger Doc by `http://localhost:8080/swagger-ui.html` and you can play around over there.
* Open H2 in-memory DB console by `http://localhost:8080/h2-console`. You can find the DB related info 
by accessing `application.proproties`.
