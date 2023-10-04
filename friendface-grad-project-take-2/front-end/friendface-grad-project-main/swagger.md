# Swagger

## What is it?
Documentation is an essential part of building RESTful APIs to ensure our services are clear and useful.
Swagger is an implementation of OpenAPI, an open-source specification to help us design, build and document our APIs.
Through this documentation, "Swagger" and "OpenAPI" may be used interchangeably.
We will be using [springdoc-openapi](https://springdoc.org/) to do this.

![Swagger UI](swagger.png)

## Dependencies
Let's go ahead and import our springdoc dependency:

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.0.4</version>
</dependency>
```

There's also a dependency we need called `hibernate-validator`. This can be found in the following starter:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

In Spring world, a starter is a collection of dependencies that include things that we need for a specific task,
without having to hunt down the right ones explicitly, and manage their versions.
There are some benefits of using starters such as:
- increase pom manageability and readability.
- production-ready, tested & supported dependency configurations.
- decrease the overall configuration time for the project.

There are also a few cons:
- black box effect, you might not know everything you're importing.
- bloating, as you may import more than needed.

But you should not need to worry about these for now.

Rerun your spring application and head to [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
Here you should be able to see the endpoints you have created.

Have a play around with this UI. You can actually call your application from this webpage! Call your `getUsers` endpoint
to see the users in the DB. This is extremely useful for manual dev testing.

## Make a new endpoint

Let's add a new POST endpoint to our UserController and see the change in our swagger UI.

> **_Note:_** this example is here for simplicity and will get refactored later to respect the coding principles highlighted below.

```java
@PostMapping
public ResponseEntity<User> addUser(@RequestParam @NotBlank String username) {
    User user = new User(username);
    userRepository.save(user);
    return new ResponseEntity<>(user, HttpStatus.OK);
}
```
When this endpoint is called, it validates that the username is not null, empty string, or a string full only of whitespace.
The method then constructs a `User` from the `username` and `save`s it to the DB.

Here we are using `ResponseEntity` as the return type. This is optional, but it is useful to tell the service that calls
our endpoints if the request was successful, and what was created. The http status `OK` has a value of 200.
You can see what the other codes are used for [here](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes).
It also passes `user` into the request body so whatever called the endpoint can see what was created. If you like this pattern, update your GET endpoint to use `ResponseEntity` too.

Add unit tests for this endpoint. Be sure to consider the `@NotBlank` cases!

Call the new POST endpoint from the swagger UI and check your DB updates as expected.

## Controller, Service, Repository Pattern

A common pattern in development is the Controller, Service, Repository pattern. This splits up your application into classes with separate concerns.
Have a read of [this](https://tom-collings.medium.com/controller-service-repository-16e29a4684e5) medium article.

Add the service layer to your Friendface app.
- The controller should call the service layer, then the service should call the repository.
- The repository returns DB data to the service, then the service returns it to the controller.
- The controller and repository should now be totally separate, and not know about each other.
- The service should be annotated with `@Service`. This is similar to the `@Controller` and `@Repository` annotations we've seen before.
They all use the `@Component` annotation which marks the class as a Spring Bean, allowing Spring to inject an instance of it into any class which depends on it.

It may seem a waste of time as our endpoints are really simple at the moment, but as the app scales up in size and features,
this separation will keep Friendface tidy, readable and maintainable.
Refactor your current controller tests and add service tests to adhere to the following patterns:
- The controller unit tests mocks the service layer. These tests are only concerned about API things.
- The service unit tests mocks the repository layer. These tests are only concerned about business logic.
- At the moment, you probably don't need repository unit tests as the standard `findAll()` and `save()` have been used
  (we trust Spring have tested them appropriately). These tests will only be concerned about DB stuff.

Nothing should change in your swagger UI as we haven't changed any behaviour, we just moved it somewhere else.

## DTOs

Currently, we are sending the Entity to whatever calls our endpoints. But what if the client doesn't care about what the User looks like in the DB?
What if the client needs more fields than what we have in the User table? The same applies for fewer fields too.

As an example, our entity could look like this:
```json
{
  "id": b4a3efbc-032a-4638-adc1-c2fdba18ec63,
  "username": "samsepi0l",
  "email": "sam@mrrobot.net",
  "age": 26
}
```
The UI may not care about the email field, so probably not best to send it in this case.
The UI may want to get a list of all the posts associated to a user, but they might be stored in a different table.
To fix this, we use Data Transfer Objects (DTO) and add appropriate mapping between DTOs and Entities in the service layer.

DTO example:
```json
{
  "id": b4a3efbc-032a-4638-adc1-c2fdba18ec63,
  "username": "samsepi0l",
  "age": 26,
  "posts": [
    {
      "id": f5c08e9e-3d1c-4441-aee5-66f4e8971602,
      "author": "samsepi0l",
      "likes": 3,
      "content": "hello friend"
    }
  ]
}
```

While Entities are used to store a representation of things in a database, DTOs are used to carry just 
the data we need between our application and a client.

Add a UserDTO to your application. It doesn't need to have any or all of the fields above, that's just an example.
Your controller should only worry about DTOs and your repository should only see entities.
In the service layer, add the mapping logic to turn your DTO into an Entity and vice versa.

You may also want to add constraints to your DTOs like the `@NotBlank` annotation we used in our POST endpoint.
This is why we imported `hibernate-validator` at the start - check
[this](https://hibernate.org/validator/documentation/getting-started/) out for how to use and how to unit test.

This time we can see a change in our swagger UI as we're changing the return types of our endpoints.
Note that swagger has no idea about the DB architecture as it's nicely hidden away.

## Summary

You should now have a swagger UI that nicely documents your endpoints. On a real project,
other teams working on different services can see how to interact with Friendface, without you having to write up any long and boring documentation.
You've also future-proofed the app by splitting up the different classes more appropriately.