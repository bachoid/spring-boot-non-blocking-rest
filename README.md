# spring-boot-non-blocking-rest

Non Blocking Rest Api Demo, Reactive Java, Webflux.

Build:
mvn clean spring-boot:run

Test from browser:

http://localhost:8080/userposts/{id}

returns user+posts by user id

http://localhost:8080/userposts/

returns all users with their posts.

Data is received from external rest
service(https://jsonplaceholder.typicode.com) by Spring WebClient.

