# FriendFace Web backend, in Spring

Spring is a popular framework for making applications in Java.

If you use Spring's conventions, you can achieve a lot of common tasks using very little code. Moreover, your code will be cohesive and decoupled.

## Project setup

Let's lookup how Spring recommend we setup a new project.

https://start.spring.io/

Fill in the Spring Initializr form.

- Project: Maven
- Language: Java
- Spring Boot: 2.2.2
  - Or whatever the latest non-snapshot release is
- Group: `com.scottlogic.grad-training`
- Artifact: `friendface-web`
  - Options: Java 11
- Dependencies: Web

Hit Download, and explore the .zip file that it generates for you.

In particular, look at the `pom.xml`.

### Integrate the Spring project into our Maven group

Let's see if we can add this `pom.xml` into our multi-module Maven project.

There's a problem. It already has a parent (`org.springframework.boot:spring-boot-starter-parent`), but we need to change the parent to `com.scottlogic.grad-training:root` if we want to include it in our multi-module project.

What does `spring-boot-starter-parent` do?

https://search.maven.org/artifact/org.springframework.boot/spring-boot-starter-parent/2.2.2.RELEASE/pom

For the most part, it recommends versions of libraries you might want (for logging, or building JSON strings). It recommends configurations for Maven plugins (e.g. to compile and package your projects).

#### Give the root POM a parent

Let's give our root `pom.xml` a parent. That will apply `spring-boot-starter-parent`'s recommendations to every artifact in our group.

Put this in the `<project/>` node of the root `pom.xml`:

```xml
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>
```

That's actually helped us a bit; there's some simplifications we can now make.

#### Simplify the root POM

Delete from the root `pom.xml` the `<dependencyManagement/>` and `<pluginManagement/>` sections. Because `spring-boot-starter-parent` declares very similar sections, and we've now inherited those.

Delete the properties `project.build.sourceEncoding` and `junit-jupiter-version` from the root `pom.xml`. `spring-boot-starter-parent` gives us those properties already.

Add a property `<java.version>11</java.version>` to the root `pom.xml`. Spring Boot uses this variable to configure the `maven-compiler-plugin`.

#### Add the friendface-web Maven project

Now we are ready to add to our Maven group, the `pom.xml` that Spring Initializr gave us.

Make a new folder, `friendface-web`. This should be a sibling to your existing folder, `annotations`.

Put the generated `pom.xml` into the folder `friendface-web`.

Modify the `<parent/>` node of `friendface-web/pom.xml` to the following:

```xml
  <parent>
    <artifactId>root</artifactId>
    <groupId>com.scottlogic.grad-training</groupId>
    <version>1.0-SNAPSHOT</version>
  </parent>
```

Add `<module>friendface-web</module>` to the `<modules/>` node of the root `pom.xml`.

If IntelliJ prompts you to Import changes, do so.

Also, copy the `.gitignore` from the "annotations" project into this project.

## Prepare your tooling

We're about to outgrow IntelliJ IDEA Community Edition.

IntelliJ IDEA Ultimate Edition provides a lot of intelligence that helps us navigate, debug and develop Java applications which use the Spring Framework. Moreover, it provides a brilliant interface for interacting with databases (which we'll need pretty soon).

If you can get it for free (by virtue of being a recently-graduated student), now would be a good time to do so. Or if there's an Early Access Program running, you can get a free trial for a beta release (without losing the ability to download more trials in future).

## Get your Spring project to Hello World

The Spring Framework includes a number of modules. We will rely on "Spring Boot" to _automatically discover features_ that we've loaded into our Java runtime.

You will notice that the `pom.xml` includes `spring-boot-starter-web`:

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
```

Spring Boot will automatically discover that this exists, and will start up a web server for us. We do not need to register it or configure it in any way, because Spring Boot prefers "convention over configuration".

How do we run our application and handle a web request?

### Running the application

Make a file `Application.java`, in the package `com.scottlogic.grad_training.friendface`:

```java
@SpringBootApplication
public class Application {
    
  public static void main(String[] args) {
    SpringApplication.run(Application.class);
  }
}
```

This annotation means:

- this class may declare components
- enable auto-configuration
- search the other classes for components

### Accept a web request

Now we need to create a component which handles web requests.

Make a file, `GreetingController.java`:

```java
@RestController
public class GreetingController {

  @GetMapping
  public String index() {
    return "Sup";
  }
}
```

The `RestController` annotation is itself annotated with `@Component`, so Spring should be able to discover this class during its component scan.

Run your application, using the main method in `Application`.

By default, Spring Web binds to port 8080 of all your IP addresses.  
So your server should be accessible via `localhost:8080`.

Visit http://localhost:8080 in your web browser; you should see the string `Sup` returned.

## Give your controller a namespace

`GreetingController` won't be our only REST controller, so let's give it a namespace.  
Add the annotation `@RequestMapping("/greetings")` to the class `GreetingController`, and re-run your application.

Your `index()` endpoint has now moved to:

http://localhost:8080/greetings

## Add a parameterised endpoint

We'd like for the user to be able to provide input to us. For starters, let's pass arguments via the URL.

Add this method:

```java
@GetMapping("/{locale}")
public String localized(@PathVariable String locale) {
  if (locale.equals("fr")) {
    return "Bonjour";
  }
  return "Hello";
}
```

Redeploy your API, and try visiting these URLs in your browser:

http://localhost:8080/greetings/fr
http://localhost:8080/greetings/en

## Add an endpoint that returns a data class as JSON

Make a new class, `Employee`:

```java
public class Employee {
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
```

We have written this class to follow JavaBean conventions. It has a public no-args constructor, and it provides public access to its properties.

Note: this is not a _Spring Bean_, because we have not registered it with the dependency injector (we did not annotate it with `@Component`).

We will use this to demonstrate how we can return a Java object from a controller, and Spring will _marshal_ that object into a JSON string.

Add this endpoint to `GreetingController`:

```java
@GetMapping("/employee")
public Employee employeeOfTheMonth() {
  final var employee = new Employee();
  employee.setName(System.getProperty("user.name"));
  return employee;
}
```

This endpoint returns an `Employee` object, whose name is set to the username of your computer.  
Now redeploy, and visit:

http://localhost:8080/greetings/employee

You should see a JSON object:

```json
{
  "name": "birch"
}
```

_Note: your browser may present a tree view of the JSON, rather than showing the raw string._

### How does Spring make the JSON?

The process of turning a Java object into a JSON string is called "marshalling". Spring Web's convention is that if you return a Java Object from a REST endpoint, then Spring Web will marshal it into a JSON string using whatever marshaller is registered.

When Spring Boot started up your application, you asked for auto-configuration.

Spring Boot looked to see if we'd included a marshaller in the Java runtime. It found Jackson (`com.fasterxml.jackson.core:jackson-core`), and registered that as the marshaller that we'd use in our application.

Now it's becoming evident how much boilerplate code we are freed from writing.

## Accept a payload

So we can return JSON. Can we accept JSON as input, too?

```java
@PostMapping("/welcome")
public String welcome(@RequestBody Employee employee) {
  return String.format("Welcome %s!", employee.getName());
}
```

This endpoint accepts an Employee, inspects its name, and returns a String, greeting the employee by name.

We'll need something more powerful than the address bar of our web browser to test this.

```bash
curl 'http://localhost:8080/greetings/welcome' \
-H 'Content-Type: application/json' \
-d '{
  "name": "Joshua Bloch"
}'
```

We'll need to run this curl command. It sends an HTTP POST request, with a JSON payload.

You may eventually use Postman to manage and save these requests. It is useful to get familiar with cUrl beforehand.

### Installing curl

`curl` is a UNIX utility that lets us send HTTP requests. We don't have `curl` installed by default. In fact, the default `cmd` shell on Windows doesn't support single-quoted strings, which it difficult to compose JSON arguments.

Now would be a good time to install a UNIX environment. Ideally [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10), otherwise Git for Windows: https://gitforwindows.org/

Git for Windows provides a good shell but only a basic terminal. I recommend [Microsoft Terminal](https://www.microsoft.com/en-us/p/windows-terminal-preview/9n0dx20hk701?activetab=pivot:overviewtab). You will need to upgrade Windows for this (use the installer in "System Requirements"), but it's worth it. Otherwise, [Cmder](https://cmder.net/) is a popular terminal.



### Once curl is installed

Assuming all's gone well, you should receive the output:

```
Welcome Joshua Bloch!
```

So, our API was able to unmarshal the JSON string into a Java object, and read its `name `property.

## Separating concerns into services

Ideally, a Controller's job should be: receive inputs, send them to a service for processing, and return the outputs from the service.

This enables services to be composed together. For example "determine employee of the month" could be used as just one step in a larger process, like "generate company newsletter".

Let's make a new class, `EmployeeOfTheMonthService`:

```java
@Service
public class EmployeeOfTheMonthService {
  public Employee computeEmployeeOfTheMonth() {
    final var employee = new Employee();
    employee.setName(System.getProperty("user.name"));
    return employee;
  }
}
```

This encapsulates some of the business logic that previously we'd entrusted to `GreetingController#employeeOfTheMonth`.

The `Service` stereotype is itself annotated with `@Component`, so this class will be discovered by Spring's component scan. `Service` isn't practically different to `Component`, and just indicates that "this is our service-layer".

I've used the verb "compute" in favour of "get", which I prefer to reserve for property accessors. As much as possible, I want to encourage a developer's intuition that "get doesn't perform computation, it just returns references to objects that already exist".

### Consuming the service

Any class marked with `@Component` is a "Spring Bean", and will be registered with Spring's Inversion of Control Container, which means Spring will be able to inject an instance of it into any class which depends on it.

Let's rely on Spring's dependency injection, and modify `GreetingController` so that expresses a dependency on `EmployeeOfTheMonthService`. Give it a field and a constructor:

```java
public class GreetingController {

  private final EmployeeOfTheMonthService employeeOfTheMonthService;

  public GreetingController(EmployeeOfTheMonthService employeeOfTheMonthService) {
    this.employeeOfTheMonthService = employeeOfTheMonthService;
  }
```

Incidentally, now that we have explicitly declared a constructor: the compiler no longer generates a public no-args constructor for us. The only way to construct a `GreetingController` now is to provide it with an `EmployeeOfTheMonthService`.

Fortunately, Spring will be able to discover `EmployeeOfTheMonthService` during its component scan, so it has everything it needs to construct a `GreetingController`.

Now update `GreetingController#employeeOfTheMonth()` to forward calls to this service:

```java
public Employee employeeOfTheMonth() {
  return employeeOfTheMonthService.computeEmployeeOfTheMonth();
}
```

Congratulations; you've successfully moved business logic out of the controller-layer and into a reusable service.

## Spring Beans

`@Component` is fine when it's obvious how to construct your class. If you have a no-args constructor, or if every constructor argument is a component that Spring knows how to create: Spring's dependency injector will have everything it needs to construct your class.

What if your component class's constructor demands an argument that Spring couldn't possibly know how to provide?

Change your `EmployeeOfTheMonthService` to accept a boolean `formatNamesInUpperCase`; this will be a way to configure the service. And remove the `@Service` annotation:

```java
public class EmployeeOfTheMonthService {
  private final boolean formatNamesInUpperCase;

  public EmployeeOfTheMonthService(boolean formatNamesInUpperCase) {
    this.formatNamesInUpperCase = formatNamesInUpperCase;
  }

  public Employee computeEmployeeOfTheMonth() {
    final var employee = new Employee();
    final var nameNominal = System.getProperty("user.name");
    final var nameFormatted = formatNamesInUpperCase
      ? nameNominal.toUpperCase()
      : nameNominal;
    employee.setName(nameFormatted);
    return employee;
  }
}
```

Spring couldn't possibly know what value of boolean we want to pass into the constructor of this service. It cannot be constructed by Spring's dependency injector.

We need a way to manually register objects with Spring's Inversion of Control Container.

### Registering a Spring Bean manually

Modify the class `Application`, and add this method to it:

```java
@Bean
public EmployeeOfTheMonthService employeeOfTheMonthService() {
  return new EmployeeOfTheMonthService(true);
}
```

We are constructing `EmployeeOfTheMonthService` ourselves, because we are the only person who knows what boolean it should be configured with.

Annotating this method with `@Bean` means that it will be run once, and whatever object is returned will be registered with Spring's IoC container.

Run your application, then try this curl command:

```bash
curl http://localhost:8080/greetings/employee
```

The GreetingController should return uppercase output this time:

```json
{"name":"BIRCH"}
```

### Tidying up your beans

`Application` is not the only class in which we can declare beans. It'll get messy if we put every bean in one place, so let's see how we can organise this.

Remove the method `Application#employeeOfTheMonthService()`, and make a new class, `ServiceConfiguration`:

```java
@Configuration
public class ServiceConfiguration {
  @Bean
  public EmployeeOfTheMonthService employeeOfTheMonthService() {
    return new EmployeeOfTheMonthService(true);
  }
}
```

We've tidied up the bean declaration, so that we declare _service_ beans in this class, instead of inside `Application`.

Any class which is annotated with `@Configuration` will be scanned for beans. It happens that `Application` had this annotation too (it's one of many annotations that `@SpringBootApplication` implies).

### Bean declarations can rely on dependency injection

Bean declaration needn't be full-manual. Imagine if we needed to give a `Logger` to the `EmployeeOfTheMonthService`, and it happened that `Logger` was an object that Spring could provide for us via Dependency Injection.

```java
@Bean
public EmployeeOfTheMonthService employeeOfTheMonthService(Logger logger) {
  return new EmployeeOfTheMonthService(logger, true);
}
```

Our bean declaration could specify a parameter `Logger`, and Spring would attempt to provide one to us automatically. It could then make use of that logger when constructing an `EmployeeOfTheMonthService`.

See [this Medium article](https://medium.com/simars/inject-loggers-in-spring-java-or-kotlin-87162d02d068) if you wish to actually do this. `Logger` is a complex example because each class demands a bespoke Logger just for them (i.e. so that the printed message can mention the name of the class). So `Logger`, too, would need a complicated bean declaration.

## Summary

You've now learned the basics of:

- Spring Web
- Spring Boot's auto-configuration
- Spring's component scan
- Spring's Inversion of Control Container
- Invoking HTTP APIs via curl

Next: let's play with databases!