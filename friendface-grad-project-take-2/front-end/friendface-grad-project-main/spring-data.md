# Spring Data JPA

Let's give our backend a database to talk to.

Spring Framework includes a module "Spring Data JPA", where JPA refers to Java Persistence Annotations.  
It's a set of conventions from Jakarta EE, for persisting data to a database, without writing SQL queries.

The intention of JPA is that your code reads as though there's no database at all, and that you're just creating Java objects. Moreover: your code becomes database-agnostic, so that you don't get locked in to any particular technology.

## Installing a database

Let's install MariaDB. This is a fork of MySQL (open-source, with a permissive license).

https://mariadb.com/downloads/

Get the latest GA (General Availability) release for "MS Windows (64-bit)".

Run the installer. Follow [this guide](https://mariadb.com/kb/en/installing-mariadb-msi-packages-on-windows/) for an explanation of the options in the wizard.  
Be sure to remember the root password that you specify.  
I recommend installing MariaDB as a service (so that it starts up automatically when you login to Windows).

If you're given an opportunity: set the default charset to `utf8mb4` (so that you can store foreign text and emoji), and the default collation to `utf8mb4_unicode_ci` (so that sorts put é, e and E together, and so that foreign text can be sorted).

### Run MariaDB

Press Windows R to open Windows' "Run" dialog. Type in `services.msc`, and search for the service named "MariaDB". Start it if it's not already running.

If you did not install MariaDB as a service, the other way to start it up is as follows:

- Find MariaDB in Program Files.  
- Use your command-line shell to run the executable `bin\mysqld.exe` (MySQL Daemon).

### Connect to MariaDB

Open IntelliJ IDEA Ultimate Edition.

Open the "Database" toolwindow (should be in the top-right of your workspace, along the right edge). Add a new MariaDB Data Source.  
Download missing driver files if required.

Test Connection to localhost:3306, with user root and with the password you specified during installation of MariaDB.

Once you've confirmed that the connection works: press OK to save this Data Source. _If connection fails, try troubleshooting it using some of the hints in [failed_sql_connection.md](failed_sql_connection.md)_.

## Provisioning a database

Assuming you've successfully setup a Data Source in IntelliJ: let's make our first database schema and tables.

Use the Database toolwindow in IntelliJ, and connect to your Data Source. Add a new schema, `friendface` like so:

```sql
create schema friendface charset utf8mb4 collate utf8mb4_unicode_ci;
```

There's no user interface to choose charset and collation, so we'll have to type the query manually.

### Add a table

Let's add our first table, `users`. This should be possible from the UI. Specify columns such that you create a table like this:

```sql
create table users
(
  username varchar(50) not null
    primary key
);
```

## Add some data

Now click the "QL" button to get a console into which to write SQL queries.

Insert some users like so:

```sql
insert into users
    (username)
values
    ('frodo'),
    ('sam');
```

We'll now have to setup our Java application to connect to the database, so that it can attempt to retrieve this data.

## Connect to your database from Java

Add the following dependencies to `friendface-web`'s `pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.mariadb.jdbc</groupId>
  <artifactId>mariadb-java-client</artifactId>
  <version>2.5.2</version>
</dependency>
```

`spring-boot-starter-data-jpa` provides features which enable our application to access a database using a familiar Java programming style. Specifically: it provides JPA annotations and interfaces, and an implementation of JPA, Hibernate.

JPA is abstract, and doesn't favour a particular database (rather DBMS, Database Management System). It connects to databases via the `jdbc` (Java database client) interfaces. We need to provide an _implementation_ of said jdbc interfaces, one that is suitable for MariaDB. Hence, we install `mariadb-java-client`.

Spring Boot knows how to discover the functionality provided by these libraries (because their classes follow agreed conventions, like implementing a certain interface, or being annotated appropriately), and automatically registers them for use in your application.

Check on https://search.maven.org/ to see whether there's a newer version of `mariadb-java-client` that we should be using.

Consider lifting the `mariadb-java-client`'s version number into the parent `pom.xml`'s `<dependencyManagement/>` section. If we make other Maven projects in the same group: we will want this version number standardised.

### Configure database connection

We need to provide some configuration, so that Spring Boot knows how to connect to our database.

Create inside `src/main/resources` a file `application.yml`:

```yaml
spring.datasource.url: jdbc:mariadb://localhost:3306/friendface
spring.datasource.username: root
spring.datasource.password: ''
spring.datasource.driver-class-name: org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto: validate
```

The `spring.datasource` properties pertain to the MariaDB connection, whereas the `spring.jpa` concerns JPA, or "how we will represent database entities as Java objects".

Update `spring.datasource.password` to match whatever root password you chose.

### Verify database connection

Launch your application (click the Play button next to your class `Main`).

If connection succeeds, the application will stay alive, and will print this message:

```
Started Main in 3.542 seconds
```

Whereas if connection fails, you should expect the application to terminate, citing an error like this:

```
Error creating bean with name 'entityManagerFactory'
```

This is unlikely to be the first fatal Spring error that you encounter, so let's pick it apart. _If you didn't encounter this error: deliberately change your configured password to something incorrect, then redeploy so that you can enjoy this error too._

## Reading a Spring error message

Recall when we made a Spring Bean in the previous exercise. Recall also the classes that we've annotated with `@Component`, as both are ways to register a Spring Bean with Spring's Inversion of Control Container.

Spring is telling us that it attempted to create a bean `entityManagerFactory`, but failed. What's an EntityManagerFactory?

### Understanding the consequence

When using JPA conventions to write data to a database: we do not "insert records into a database"; we "save Java entities to a persistence context". This decouples us from thinking about databases and queries, and instead we write our program entirely like Java, with an object-oriented style.

Critically: in order to retrieve and save entities, we will need an "Entity Manager". This remembers things like "where did this Java object come from? Is it a record from the `users` table?".

Why would we need an `EntityManagerFactory`? This implies that we need a way to make _many_ EntityManagers. Perhaps one per database connection. We could have a pool of 10 database connections, to support many concurrent requests.

Why did Spring fail to create the `EntityManagerFactory`? Let's figure out how to read this error.

### Understanding the cause

In Java generally, the most important parts of a logged error are at the top and at the bottom.

Exceptions can be caused by other exceptions. The root cause will be at the bottom, whereas the consequence will be at the top.

Exceptions are logged with two portions: the message, and the stack trace. Spring error messages are frequently so long that they go off-screen in our IntelliJ console. We should scroll horizontally to ensure we read the entire message.

#### Hiding noise

Stack traces in Java are frequently noisy. Most of the stack frames happen inside code that we didn't write (i.e. inside the bowels of Spring). It's usually helpful to hide this noise.

You'll notice that there's many lines beginning with `at org.springframework.` — so many that we cannot fit the entire exception chain into our console.

Let's select an occurrence of `at org.springframework.` from our IntelliJ console. Right-click and choose "Fold Lines Like This". IntelliJ will collapse noise like this from now on.

#### Reading the root cause

With the noisy lines folded, the stack trace becomes a lot easier to read.

Assuming you've got the same error as I do (wrong password), the pertinent parts look like this:

```
Error creating bean with name 'entityManagerFactory'
Caused by: Unable to create requested service [JdbcEnvironment]
Caused by: Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
```

Note that this doesn't go as far as to say "wrong password". It's actually saying that Hibernate (our JPA implementation) doesn't know which SQL dialect to use (i.e. which particular database protocol and version should it talk in).

Suspicion: it failed to connect (due to wrong password), and couldn't read the error message (because it didn't know which dialect to use).

Let's fix the dialect problem first, so we can read the real error.

#### Iterating on the problem

Let's tell Hibernate to talk using a MariaDB 10.3 dialect. Add this line to your `application.yml`:

```yml
spring.jpa.properties.hibernate.dialect: org.hibernate.dialect.MariaDB103Dialect
```

Redeploy. With the first problem solved (unknown dialect), we should get closer to the heart of the error. Perhaps you'll get something like this (I've paraphrased a bit):

```
Error creating bean with name 'entityManagerFactory'
Caused by: Unable to build Hibernate SessionFactory
Caused by: Unable to open JDBC Connection for DDL execution
Caused by: Could not connect to address=(host=localhost)(port=3306)(type=master) : Access denied for user 'root'@'localhost'
```

Now the error is clearer: access denied. It's not a networking error (so `localhost:3306` is correct), it's an authentication/authorization problem.

#### Why can we get "access denied" in MySQL/MariaDB?

Most likely we got our password wrong (especially if you did so deliberately).  
It's worth knowing though that in MySQL/MariaDB: it's possible to have the correct username and password, but be connecting from a disallowed host. Notice how your username was printed as `'root'@'localhost'`.

Recall how the MariaDB installation advised you to restrict the root user to be accessible from localhost only. This is worth remembering, in case you wish to connect to the databases hosted on your colleagues' computers.

#### Resolving the error

If you reached this error deliberately: put your configured password back to normal. Otherwise: seek assistance (or have a crack at interpreting your error yourself).

You'll notice that if our password's correct: we can get away with not specifying a `hibernate.dialect`; Spring Boot can autoconfigure a dialect in this circumstance. I would still recommend keeping the explicit `hibernate.dialect` that we configured, in case having no dialect masks other errors in future.

## Secure the password

Recall that we stored `spring.datasource.password` in our `application.yml`. We should treat this as sensitive information; having access to our source code shouldn't give you access to our infrastructure.

How can we ensure that this configuration is never committed to our repository, and never pushed to GitHub?

We'll put sensitive information into a separate file, and use `.gitignore` to prevent that file's being version-controlled.

Add this line to `friendface-web`'s `.gitignore`:

```
src/main/resources/application-local.yml
```

Create a new file, `application-local.yml`, inside `src/main/resources` of `friendface-web`. This will house our _local developer environment_. We will put our password there:

```yaml
spring.datasource.password: ''
```

Likewise, _remove_ `spring.datasource.password` from `application.yml`.  
You could also consider moving `spring.datasource.user` into `application-local.yml`. It's not sensitive, but it _is_ environment-specific (i.e. it's only useful on your computer).

Our `application-local.yml` will not yet be used by Spring Boot. We have to activate it first.

### Where Spring looks for configuration

How does the file `application-local.yml` relate to our existing `application.yml`?

Spring has a concept of "profiles". By default, the `default` profile is active, and entails that Spring Boot should search for configuration in `application.yml`.

If more profiles are activated, then Spring Boot will search for configuration in more places. By convention, it loads: `application-{profile}.yml`

There are [17 places](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) in which Spring Boot searches for configuration. Fortunately we don't need that many.

#### Activating profiles in Spring

We need to find a way to activate the `local` profile.

We want it to be activated any time we run the application on our computer.
So, let's modify our IntelliJ run configuration.

You'll notice at the top of the IntelliJ window, there's a drop-down menu with "Main" selected. This is where you choose Run Configurations, and it already has a Run Configuration, from back when you clicked the Play button on your Main class.

If the Run Configuration is greyed out: choose "Save 'Main' configuration" (since IntelliJ only maintains a few recent unsaved configurations).  
Next, choose 'Edit Configurations...' and modify the Main configuration. Specify in its VM options:

```bash
-Dspring.profiles.active=local
```

This provides an argument to the Java Virtual Machine on startup, which will be visible to your Java program as a System Property. This is one place which Spring Boot searches for configuration.

Press OK to commit your changes to this Run configuration, and run your application again.

Your application should now start up, and you should see a message logged:

```
The following profiles are active: local
```

## Define some entities

Make a class `User`. This will be an Entity, which is a JPA concept saying "this Java class represents a record in a database":

```java
@Entity
@Table(name="users")
public class User {
  @Id
  @Column
  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
```

When importing: prefer `javax.persistence` packages over `org.hibernate`, because `javax.persistence` is less implementation-specific. The moment we use Hibernate-specific functionality, we get locked in to Hibernate as our JPA implementation (and denies us the freedom to move to competing implementations like EclipseLink).

We use `@Table` to specify which database table this class maps to.

Likewise, we've used `@Column` to map the property `username` to the database column of the same name.  
We used `@Id` also, to tell Hibernate that the database's `username` column is a primary key.

We're also relying on JavaBeans conventions (no-args public constructor, all properties publicly accessible).

This is not a Spring Bean. Spring does not look for this class, but Hibernate does.

## Define a repository

Spring Data JPA knows how to provide basic database querying support — create, read, update, delete ("CRUD").

We don't need to write any SQL queries to talk to the database. Nor do we need to write any Java code. Spring Data JPA can generate a lot of helpful functionality for us, based on its templates.

Create an interface which extends `JpaRepository`:

```java
public interface UserRepository extends JpaRepository<User, String> {
}
```

The generic bounds need to be an entity (`User`) and the type of its primary key (in our case, the primary key is `username`, a `String`).

At runtime: Spring Data JPA will discover this interface, and will automatically generate an implementation of it for us, and register that implementation into Spring's IoC container (so we'll be able to inject a UserRepository wherever one is needed).

## Define a controller

Similar to GreetingController, but let's express a dependency on `UserRepository`. Spring will inject an auto-generated implementation of `UserRepository` into our `UserController`'s constructor.

```java
@RestController
@RequestMapping("users")
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<User> getUsers() {
    return userRepository.findAll();
  }
}
```

We'll make use of `userRepository.findAll()`. This is one of many methods that `UserRepository` inherits from the `JpaRepository` interface.

Visit http://localhost:8080/users in your web browser.

You should see a JSON list returned, documenting our two hobbits.

```json
[
  { "username": "frodo" },
  { "username": "sam" }
]
```

## View the generated SQL

We just saw that Spring Data JPA just queried our database, but we didn't see what SQL query it used to do so. Let's get more detail on what it's doing.

We'll want to add some more properties to our application config, but first let's tidy it a little, to avoid repetition.

Your current `application.yml` can be reshaped to group properties together under common keys:

```yaml
spring.datasource:
  url: jdbc:mariadb://localhost:3306/friendface
  username: root
  driver-class-name: org.mariadb.jdbc.Driver
spring.jpa:
  hibernate.ddl-auto: validate
  properties.hibernate:
    dialect: org.hibernate.dialect.MariaDB103Dialect
```

Now let's add a few more properties, inside `spring.jpa.properties.hibernate`:

```yaml
    show_sql: true
    format_sql: true
    query.substitutions: true
```

This should turn on query logging. We can see what MariaDB query is composed by Hibernate, every time we use JPA abstractions to fetch data from our database.

Visit http://localhost:8080/users in your web browser again, and watch the console in IntelliJ.

You should see the following query printed:

```sql
select
    user0_.username as username1_0_ 
from
    users user0_
```

This is the SQL that Hibernate generated when we invoked our JPA repository's `userRepository.findAll()`.

## JPQL

IntelliJ Ultimate enables you to interactively write JPQL queries. This is extremely helpful when trying to design a complex query. Instead of guessing and redeploying our whole application: we can take advantage of IntelliJ's auto-completion.

### Create a `friendface@localhost` Data Source

First we'll need a new Data Source. We want one that defaults to using the `friendface` schema (our current Data Source does not select any particular schema when we connect).

Open the Database toolwindow.  
Duplicate your `@localhost` Data Source. Write `friendface` into the field "Database", and name this Data Source `friendface@localhost`.

### Assign the Data Source to a persistence unit

Let's assign our new Data Source to our persistence unit:  
Open the Persistence toolwindow.  
Right-click `friendface-web`. Choose 'Assign Data Sources...'. Assign `friendface@localhost` to `entityManagerFactory`.

### Open the JPA Console

We're configured. Let's open a JPA Console:
Expand `friendface-web`. Click `entityManagerFactory`. Click the "QL" icon, open a JPA Console with `<empty>` Run Configuration.

### Run a JPQL query

Type this query into the JPA Console:

```sql
select u from User u
```

Press Ctrl Enter to submit the query.

You should see two records returned (frodo and sam).

