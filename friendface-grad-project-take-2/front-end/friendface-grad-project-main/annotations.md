# Annotations in Java

Annotations are a Java language feature.  
They enable us to talk about our code:

```java
@Deprecated
class Main {
}
```

We've tagged this class as deprecated. Nothing actually happens; annotations don't run code. But an outsider (the developer, the compiler, or the program itself) can _search for code which has this annotation_.

## Annotation type definitions

What does an annotation type definition look like?  
At its simplest:

```java
@interface Deprecated {
}
```

Trivia:

- `@interface` is _two_ keywords, `@` and `interface`.  
  - You can put a space between them (don't)
  - `@` is a pun (`@` = AT = Annotation Type)
- It's a valid interface too; you can implement it (don't)

So far what we've seen is equally possible with a normal, empty interface (e.g. `Main implements Deprecated`). This is called a marker interface.

### Annotation Type Target

Annotations have tricks up their sleeve that distinguish them from regular interfaces.

For example, you can put annotations in all sorts of places:

```java
@Deprecated
class Main {
  @Deprecated
  void method() {
    @Deprecated var x = 1;
  }
}
```

You can configure the _target_ of the annotation:

```java
@Target(ElementType.METHOD)
@interface Deprecated {
}
```

This specifies that the annotation may only be applied to _methods_.

### Annotation Type Retention

Do we want our annotations to be available during runtime, so our program can lookup information about itself? (Usually: yes)

```java
@Retention(RetentionPolicy.RUNTIME)
@interface Deprecated {
}
```

You can tell the compiler to "retain" the annotation, rather than discarding it.

Perhaps we annotate our source code with license information. This is information we won't need when compiling or running our program. (see `RetentionPolicy.SOURCE`).

Perhaps we annotate our source code with documentation. This is information we may want to retain during compile-time, to generate a documentation website. (see `RetentionPolicy.CLASS`; this is the default).

### Annotation Type Parameters

Annotations take arguments:

```java
@Author("Rachel Kroll")
class Main {
}
```

So we can ask "which classes have the Author annotation? And what value did they have?"

The default, unnamed parameter is named "value":

```java
@interface Author {
  String value();
}
```

We can also have multiple named parameters:

```java
@interface Copyright {
  String owner();
  String date();
}

@Copyright(
  owner = "Scott Logic",
  date = "2020-01-01"
)
class Proprietary {
}
```

Side-note: we cannot use a real Date type here, because annotations may only take compile-time constants (primitives or strings initialized with constant expressions).

#### Retrieving annotation values

If you have a reference to the class which has been annotated, you can retrieve values from the annotation:

```java
if (Main.class.isAnnotationPresent(Author.class)) {
  final Author annotation = Main.class.getAnnotation(Author.class);
  final String authorName = annotation.value();
}
```

If you acquired your class in a dynamic way, then it will have a wildcard generic type `Class<?>`, but that's still fine:

```java
final Class<?> discoveredClass = Class.forName("java.lang.String");
if (discoveredClass.isAnnotationPresent(Author.class))
```

The more reflection we use, the less type-safety (and performance) we have. But it can enable programming styles that solve worse problems (maintainability).

## Searching for annotated classes

Our program can _search_ for classes which have annotations upon them.

The Java Base Libraries don't provide any _nice_ way to do this. So we need to bring in another library to help us out.

### Install `org.reflections:reflections`

Add this dependency to the `<dependencies/>` node of `annotations/pom.xml`:

```xml
    <dependency>
      <groupId>org.reflections</groupId>
      <artifactId>reflections</artifactId>
      <version>0.9.12</version>
    </dependency>
```

This is a library made by Google, called Reflections. Generally, 'reflection' refers to when a program looks at itself. This is a type of metaprogramming. Java provides some reflective powers in the Java Base Libraries, and this library gives us a bit more.

You could lift the version number into the `<dependencyManagement/>` section of the root `pom.xml`, but I'd not bother in this case since it's an obscure enough dependency that no other project will need it.

Why version 0.9.12? Check search.maven.org to see if there's anything newer. https://search.maven.org/search?q=g:org.reflections%20AND%20a:reflections

If IntelliJ presents a dialog "Maven projects need to be imported", click "Import Changes", to tell IntelliJ to renew its understanding of your Maven project.

### Search for annotated classes

Let's make our first Java class in our "annotations" project.

We'll organise our code into a package unique to this project, `com.scottlogic.grad_training.annotations`. Similar to our Maven artefact descriptor, but we've changed the `-` to a `_` to make it legal in Java.

You'll need to make folders that correspond to each subpackage:

```
annotations
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── scottlogic
│   │   │           └── grad_training
│   │   │               └── annotations
│   │   │                   └── Main.java
```

Let's use the `@Deprecated` annotation that comes with the Java Base Libraries (from java.lang).

We'll use the `reflections` library to search for every class in our program that has `@Deprecated` upon it, and to print the names of each such class.

Try running this basic program:

```java
package com.scottlogic.grad_training.annotations;

import org.reflections.Reflections;

import java.util.Set;

@Deprecated
public class Main {
  public static void main (String[] args) {
    final var reflections = new Reflections("com.scottlogic");
    final Set<Class<?>> deprecatedClasses = reflections.get(TypesAnnotated.with(Deprecated.class).asClass());
    System.out.printf("Deprecated classes: %s%n", deprecatedClasses);
  }
}
```

It should output:

```
Deprecated classes: [class com.scottlogic.grad_training.annotations.Main]
```

There are probably more deprecated classes inside our runtime, but we told Reflections to restrict its search to the `com.scottlogic` package (and descendants thereof).

## Exercise 1: list authors

- Make an `@Author` annotation type
  - it should accept an author name as a parameter
  - it should be retained until runtime
  - it should target _types_ only (i.e. we can put it upon classes and interfaces)
- Make a couple of empty classes, put `@Author` annotations on them
  - use a variety of names
  - these classes should be inside the package `com.scottlogic.grad_training.annotations`
- Scan the package `com.scottlogic.grad_training.annotations` for classes which are annotated with `@Author`
- Lookup the names of each author
- Print each distinct author name

Sample output:

```
This program was brought to you by: [Lin Clark, Jessie Frazelle]!
```

The minimum deliverable is a class with a main function that prints author names to the console. This is a small program (possible with one semicolon).

Better encapsulation would be a class which returns a set of strings. This would enable you to write a unit test which asserts on the contents.

## Component scan

Let's look at how annotations can enable entirely new programming styles.

```java
package com.scottlogic.grad_training.annotations;

@Component
public class Task {
  @Scheduled(1000)
  public void sayHello() {
    System.out.println("Hello!");
  }
}
```

The programmer is trying to say "run `sayHello()` every 1000ms".

`@Component` is just a marker to say "look for me when you're scanning for classes".

Naturally, nothing happens. It's gonna be _our job_ to create the infrastructure that finds `Task`, invokes `new Task()`, and runs `sayHello()` every second.

In this scenario, `@Component` is a convention **we've** proposed, and it's loaded with some assumptions. We intend that your class should have a public no-args constructor.  
_Note: declaring **no constructor at all** auto-generates a public no-args constructor at compile-time_.

Likewise: the convention we propose with `@Scheduled` is that you will put it on a public no-args method.

## Invoking a constructor reflectively

As before, we can find every class that's annotated with Component like so:

```java
final var reflections = new Reflections("com.scottlogic");
final Set<Class<?>> annotatedClasses = reflections.get(SubTypes.of(TypesAnnotated.with(Component.class)).asClass());;
for (Class<?> annotatedClass : annotatedClasses) {
  // we are now looping through every class that was found
}
```

We have a `Class<?>`. We don't know which class it is.

How do we run `new WhateverTheClassIs()`?

```java
final Constructor<?> constructor = annotatedClass.getDeclaredConstructor();
final Object component = constructor.newInstance();
```

We ask "do you have a public, no-args constructor?".  
If there's no constructor, `NoSuchMethodException` is thrown.  
If all's good: we invoke said constructor, to create a new instance of that class.

We don't know what class it was, so we are forced to bind it to the most vague type, `Object`!

## Invoking a method reflectively

How could this `Object` be useful to us? We don't know anything about it.

Let's sniff the object, to see what kind of stuff it can do.

```java
for (Method method : annotatedClass.getDeclaredMethods()) {
  if (method.isAnnotationPresent(Scheduled.class)) {
    method.invoke(component);
  }
}
```

If the method is annotated with `@Scheduled`, then we know what to expect. This is a convention that we designed, so we're expecting it to be upon a public, no-args function.

We don't know the name of the method (`sayHello()`), so the syntax is a bit backwards. Instead of `component.sayHello()`, we do `method.invoke(component)`.

If all of our assumptions hold: then this should work just fine. Otherwise, exceptions can be thrown.

## Exercise 2: Run `@Scheduled` methods every n milliseconds

`java.util.Timer` and `java.util.TimerTask` can be used to run a function every n milliseconds.

You will find that you need to make a subclass of TimerTask, and give it a way to invoke `Method` on an `Object`. It would be good to encapsulate this work into a Runnable, and give that Runnable to your TimerTask on construction.  
Then you will need to ask a Timer to schedule that TimerTask.

Declare a class, 'Task' (note: despite the similar name, this is unrelated to TimerTask):

```java
@Component
public class Task {
  @Scheduled(1000)
  public void sayHello() {
    System.out.println("Hello!");
  }
}
```

Make two packages:

- `com.scottlogic.grad_training.annotations.product`
  - classes annotated with @Component should reside here
- `com.scottlogic.grad_training.annotations.framework`
  - classes which _discover_ components and _implement_ scheduling, should reside here

- Make a `@Component` annotation type
  - decide appropriate values for target, retention and parameters
- Make a `@Scheduled` annotation type
  - decide appropriate values for target, retention and parameters
- Make another @Component class with another @Scheduled method
  - which prints a different message
  - do not provide a numeric argument to this @Scheduled annotation
    - instead, research how to define a _default value_ in your annotation type
- Scan the package `com.scottlogic.grad_training.product` for classes which are annotated with `@Component`
- Scan each `@Component` for methods annotated with `@Scheduled`
- Invoke each such `@Scheduled` method every n milliseconds

Sample output:

```
Hello!
Hello!
Hello!
```

The program will never terminate.

Test is not a focus of this exercise. The metaprogramming creates some test friction: typically a test would want to control how `Task` is created, so that it can supply a mock instead (and ask how many times its method was called). But changing anything about how `Task` is created would defeat the purpose of this program. So, this is not a good exercise to learn test practices on.

The minimum deliverable is a class with a main function that prints messages to the console forever (on a schedule). It's possible to do this in about 30 lines.

There are a lot of checked exceptions that can be thrown. Let's not focus our effort on exception handling; just get it out of the way.

### Extension task

If you've time, there's an opportunity here to practice general Java programming and design patterns.

Revisit your exception handling. Should you continue on error? does the error indicate that a user is misusing your conventions, and is there advice you can give?

Decouple your code into cohesive units whose outputs can be more easily tested.  
A good unit would be a class/method which discovers components.

Feature discovery could be another unit. You could generalize feature-discoverers (of which a `@Scheduled` method discoverer is just one), and feature-implementors (of which a `@Scheduled` implementor is just one). Perhaps these would be coupled together in a "feature-handler".

Another example of a "feature" you could implement is `@RunOnce` (i.e. immediately invoke this method once). It's simpler than `@Scheduled`, and will be compatible if your generalization is successful.

A harder "feature" to implement is `@Profiled`, which would mean "every time this is run, I want to know how many milliseconds it took to run it".  
This would need a careful design, because it cannot exist on its own; it only makes sense when combined with `@Scheduled` or `@RunOnce`.

## Dependency injection

So far we've boldly run `System.out.println("Hello!");`, printing straight to the console. That's somewhat opinionated. What if the developer wants to log to a file, or suppress logging, or put timestamps on every message?

The developer may have a _standard way of logging_ that she wants us to use. How do we _invert the control_ so that outsiders decide how we do logging? How can outsiders give us our dependencies?

### Logger abstraction

First, we need an abstraction for a logger. Create this interface:

```java
@FunctionalInterface
public interface Logger {
  void log(String message);
}
```

You'll notice the `@FunctionalInterface` annotation - this is a relatively new (Java 8) language feature, and it tends to be a good idea to put it on any interface that has exactly one method.

It means that you don't need to _be_ a `Logger` to be compatible with this interface. _Any_ method whose return type and parameters match `void log(String message)` is accepted as a `Logger`.

Some examples of objects that are compatible with this interface:

```java
Logger logger;
// method references
logger = System.out::println;
logger = System.err::println;

// lambda functions
logger = message -> {
  System.out.printf("[%s] %s\n", LocalDateTime.now(), message);
};
// do-nothing logger
logger = message -> {};

// anonymous class instances
logger = new Logger() {
  @Override
  public void log(String message) {
    System.out.println(message);
  }
};
```

So, `@FunctionalInterface` interfaces are more flexible. We don't force the caller to inherit from the `Logger` type. This means that even methods that we cannot modify (`System.out::println`) can be made compatible.

### Invert control

Now that we have a `Logger` abstraction to implement against, let's invert the control of the `Task` class. The person _outside_ should tell us which `Logger` to use. They'll pass it into our constructor when they construct an instance of `Task`.

```java
@Component
public class Task {
  private final Logger logger;

  public Task(Logger logger) {
    this.logger = logger;
  }

  @RunOnce
  public void sayHello() {
    logger.log("Hello!");
  }
}
```

Notice how we now use `logger.log("Hello!");` instead of `System.out.println("Hello!");`.

### Constructing `Task`

Recall that we've been using _reflection_ to construct `Task`.

```java
final Constructor<?> constructor = annotatedClass.getDeclaredConstructor();
final Object component = constructor.newInstance();
```

We looked for a constructor which took no arguments. Then we invoked the constructor, passing in no arguments.

We'll need to recognise the more complicated situation: where your constructor has parameters.

```java
final Constructor<?> constructor = annotatedClass.getDeclaredConstructor(Logger.class);
final Logger logger = System.out::println;
final Object component = constructor.newInstance(logger);
```

We are now doing _dependency injection_. We (the dependency injector), from the outside, injected a `Logger` implementation into an instance of `Task`.

We arbitrarily decided that `System.out::println` was our favourite implementation of `Logger`. We'll need to come up with a more complete solution for _registering dependencies with the injector_.

We also haven't solved the more general problem of "what about _every other possible constructor_?". That's a bigger problem that entails building and traversing a graph of dependencies. It's out-of-scope for any exercise we'll do here.

### Discovering `Logger` implementations

We can solve the problem we just created, using more annotations.

```java
@Component
public class ConsoleLogger implements Logger {
  @Override
  public void log(String message) {
    System.out.println(message);
  }
}
```

As before, `@Component` just means "discover me".

If our component scan discover this class, it can sniff it to discover that it `implements Logger`.

So we'll register this implementation of `Logger` with our "dependency injector".

### The dependency injector

Our "dependency injector" can be super simple.

```java
final Map<Class<?>, Object> components = new HashMap<>();
```

If we do a component scan, and discover a class which implements `Logger`, we can register it with our "dependency injector":

```java
components.put(Logger.class, loggerImplementation);
```

Or if we do a component scan, and discover a class which _wants_ an implementation of `Logger`, we can check if our "dependency injector" has one yet:

```java
if (components.containsKey(Logger.class)) {
final Logger loggerImplementation = (Logger) components.get(Logger.class);
}
```

We're at the mercy of "what happens if I discover the classes in the wrong order"? That's a hard problem to solve generally (build a graph of dependencies, traverse the graph). That's not the focus of this lesson, so we can make it easier for ourselves.

## Exercise 3: dependency injection

Declare an interface, `Logger`:

```java
@FunctionalInterface
public interface Logger {
  void log(String message);
}
```

Declare a class, `TaskWithDependencies`:

```java
@Component
public class TaskWithDependencies {
  private final Logger logger;

  public TaskWithDependencies(Logger logger) {
    this.logger = logger;
  }

  @RunOnce
  public void sayHello() {
    logger.log("Hello!");
  }
}
```

Continue to use the package conventions from Exercise 2 (separation of product and framework).

- Declare a class which implements `Logger`
  - Annotate it with `@Component`
  - It should print messages to console **prefixed with the current date and time**.
- Scan the package `com.scottlogic.grad_training.product` for classes which are annotated with `@Component`
  - Look for classes which have public no-args constructors
  - Construct instances of each such class
  - If the class implements the `Logger` interface:
    - Register the `Logger` with your "dependency injector" (put the constructed instance into a HashMap)
- Restart the component scan
  - Look for classes which have a public 1-arg constructor which takes a `Logger`
  - Construct an instance of each such class
    - Using a `Logger` implementation retrieved from our "dependency injector"
  - Scan each such class for methods annotated with `@RunOnce`
  - Invoke each such `@RunOnce` method

Sample output:

```
2020-01-06 12:00:00 Hello!
```

The focus of this task is inverting control, discovering classes that can be used as dependencies for other classes, and injecting dependencies where needed.

### Extension tasks

Split the product and framework into two separate Maven projects (they can be siblings of this "annotations" project, in the same group of artifacts). Product should depend on framework.

Write tests. If the framework allows users to _choose_ which package is scanned, then you can use a package of test classes. You can ensure that the only `Logger` in the package, is one that the test can access in some way.

Think of an algorithm to avoid the "restart the component scan" step. It should be possible to do in one go, if you keep track of "what components have I discovered so far that I cannot construct yet".  
Test it by iterating through components in one direction, and confirm that it also works when iterating in the reverse direction.

## Summary

You have now seen behind the veil. What's often dismissed as "magic" in enterprise Java development, is now in clear view. You have built a tiny version of Spring, the application framework with which we will be building FriendFace.
