# DP Project 2 - <need name>
Team Members: **Ioan BAȘNIC, Sebastian BELEA, Petra HORVATH**
## What is it?
Free online market for all.

## Architecture
Client that communicates with a Middleware which communicates with the Microservices' server,

## Technologies
Angular, Java Spring Boot.

## Main Features
```
Client (one or more)
This is basically the frontend of the application. Anything a buyer or a seller can see is provided by it. It also serves as an informational hub about our brand and product, provided by the Main and About Project pages.
Google API

Middleware Server (one)
The middleware server handles almost all the data that gets inputted into the site (all besides the messages sent between clients), storing and retrieving data from the database, authentication and communication between the client and server.
Email verification through ML API

Microservices’ Server (one)
This is the service that takes care of all add-on features that would be too much for the middleware to handle.
These are the all the features that we managed to include:
Image Upload - imgur API
Welcome Email notification and Back and Forth Message email notification - JavaMail API
The microservices’ server is completely separated from any direct database accesses. All the data that it uses is retrieved through API’s from the client or the middleware.
```

## Authors
* **Ioan BAȘNIC** - [GitHub Profile](https://github.com/IoanBasnic)
* **Sebastian BELEA** - [GitHub Profile](https://github.com/belea-sebastian)
* **Petra HORVATH** - [GitHub Profile](https://github.com/Petrified0110)


