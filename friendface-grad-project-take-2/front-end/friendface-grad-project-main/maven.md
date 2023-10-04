# A larger Java project

Let's unlock more of Maven's power.  
We've used it for installing libraries (like JUnit). Now let's see how it can be used to manage larger projects.

## Groups

Maven enables us to split a product into modules. If we built a FriendFace backend, the core business logic could be one module. The web layer would be a different module, and would depend on that core. Microservices could be made, which would also depend on core.

The motivation for modularising our product is release management. A developer can build/test just the parts she cares about. Whereas automation would undergo a more complete process: build all modules (in the right order), and package them for release.

Let's build our first group of Maven artifacts.  
Each module will contain a Java exercise.

### Parent project with no children

Every Maven project has its own folder with a `pom.xml` (Project Object Model) inside it.  
The root module will live in a folder named `grad-training`, with the following files:

```
grad-training
├── .gitignore
└── pom.xml
```

`pom.xml` should start like this (similar output to "empty Maven Project" in IntelliJ):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.scottlogic.grad-training</groupId>
  <artifactId>root</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>pom</packaging>
</project>
```

Note the `<packaging>pom</packaging>`; we are overriding the default (`<packaging>jar</packaging>`), because we do not want this Maven project to build a Java .jar package. Its role is just to be a parent to other Maven modules.

We've named the group `com.scottlogic.grad-training`, because that's a unique name that describes what's common among every artifact we're building.

Put this into your `.gitignore`, to ignore all the project files that IntelliJ will create:

```
.idea/
**/*.iml
```

If you've not done so already: open the folder `grad-training` in IntelliJ. IntelliJ will generate the .idea folder (which describes the IntelliJ project, "grad-training") and the .iml file (which describes the IntelliJ module, "grad-training").

If IntelliJ presents a dialog "Maven projects need to be imported", click "Import Changes", to tell IntelliJ to renew its understanding of your Maven project.

### Parent project with one children

The empty project is fun, but hold onto your seats — we're about to put another empty project inside it.

Let's make a module "annotations" (this will house a future Java exercise):

```
grad-training
├── annotations
│   ├── .gitignore
│   ├── pom.xml
├── .gitignore
└── pom.xml
```

#### Create the child project

Setup `annotations/pom.xml` like so (same output as "new (empty) Maven module" in IntelliJ):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <artifactId>root</artifactId>
    <groupId>com.scottlogic.grad-training</groupId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  
  <artifactId>annotations</artifactId>
</project>
```

We have hooked up the "annotations" artifact to its parent, "root".

We are happy to accept the default packaging (`<packaging>jar</packaging>`), because this module should produce a Java .jar package.

Let's also create `annotations/.gitignore`:

```
target/
```

This should hide this Maven module's build output from git.

#### Update the parent project

We need to setup the parent-child relationship in `pom.xml` (the root pom) too. Maven may automatically do this, but it's good to check it's there.

Add this inside the root pom's `<project/>` node:

```xml
  <modules>
    <module>annotations</module>
  </modules>
```

If IntelliJ presents a dialog "Maven projects need to be imported", click "Import Changes", to tell IntelliJ to renew its understanding of your Maven project.

IntelliJ will create an IntelliJ module "annotations" from our Maven module.  
IntelliJ also creates some directories under `annotations/src`.

## Effective POM

IntelliJ created under `annotations/src` some directories for Java sources, test sources, and resources:

```
annotations
├── .gitignore
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   └── resources
    └── test
        └── java
```

IntelliJ chose this directory structure because that's what our pom.xml asked for (we received Maven's defaults).

You can see what our defaults resolve to by using "Show Effective POM"; find IntelliJ's "Maven" toolwindow (usually in the top-right), right-click the "annotations" project and choose "Show Effective POM".  
In the Effective POM, you'll be able to see how `project.build.sourceDirectory` resolves to src/main/java.

## Java test libraries

Eventually we'll want to write Java tests for the Maven projects in our group. We need libraries for that (for unit testing, assertions and mocking).

We also want to ensure that all Maven projects in the `com.scottlogic.grad-training` group, agree to use the same versions of libraries. This makes it easy to keep our dependencies up-to-date, and ensures that if problems are found or fixes introduced in a version of a library, we know that it affects all of our projects equally.

This is a strength of Maven multi-module projects. We can propose standard versions in the root `pom.xml`, and children can subscribe to those standards if they wish.

### Propose standard version in root POM

For now, let's standardise our versions of JUnit Jupiter.

Add this `<properties/>` section into the `<project/>` node of your root `pom.xml`:

```xml
  <properties>
    <junit-jupiter-version>5.5.2</junit-jupiter-version>
  </properties>
```

This doesn't do anything; it's just a variable so that we don't have to repeat ourself in the next step:

Add this `<dependencyManagement/>` section into the `<project/>` node of your root `pom.xml`:

```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>${junit-jupiter-version}</version>
        <scope>test</scope>
      </dependency>
      <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>${junit-jupiter-version}</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
```

Thanks to our `junit-jupiter-version` variable, we can ensure that all artifacts from the `org.junit.jupiter` group get aligned to the same version.

The `<dependencyManagement/>` section does _not_ install dependencies in this project, nor does it install dependencies in any of our child projects.

Instead, `<dependencyManagement/>` tells children "if you ask for this dependency, you don't need to specify a version; here's our recommended version".

### Declare dependency in child POM

Now let's tell our "annotations" project to express a dependency on JUnit Jupiter.

Add this `<dependencies/>` section into the `<project/>` node of `annotations/pom.xml`:

```xml
  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
    </dependency>
  </dependencies>
```

We didn't need to specify a version or scope, because the child project can fallback to the versions recommended in the root project.

Now our child project can use JUnit Jupiter to write Java tests.

`junit-jupiter-api` provides APIs (classes, interfaces, annotations) for us to write in our Java test.

`junit-jupiter-engine` is not needed for running JUnit tests in IntelliJ, but _is_ needed for running JUnit tests in Maven.

### Lock version of Maven test runner

Currently we're leaving to chance "which version of Maven's test runner will be used". We want to align our team on that, rather than using whatever their computer defaults to. If it were too old, it would not support JUnit Jupiter.

Modify the root `pom.xml`; add this inside the `<project/>` node:

```xml
  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-surefire-plugin</artifactId>
          <version>2.22.1</version>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
```

Any child project that uses the `maven-surefire-plugin` (this is the standard Maven plugin for running tests), will be advised to use version 2.22.1 (unless the child overrides this).

In this particular case, we do not need to add a section in our child project to _explicitly_ ask for the maven-surefire-plugin to be installed:

```xml
  <!-- We do not need to add a section like this to our child -->
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
```

Because we get this for free by virtue of having `<packaging>jar</packaging>`. See the Effective POM of the "annotations" project for evidence of this.

## It goes to 11

We haven't specified which Java version we want. Maven will default to 1.5, which is no bueno. A developer may override this in her IntelliJ settings, but her team won't benefit from that. We want Maven to be in charge of versions, since pom.xml is the only build config that we share with our team.

Let's treat ourselves to some semi-recent Java language features (`var`) and Java Base Libary APIs (`List.of()`).

Modify the root `pom.xml`; add this inside the `/project/build/pluginManagement/plugins` node:

```xml
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.8.1</version>
          <configuration>
            <release>11</release>
          </configuration>
        </plugin>
```

This configures Maven's (Java) compiler, such that this Maven project targets JDK 11, the latest LTS release (and therefore the newest version that our customers would be comfortable using).

Click "Import Changes" if IntelliJ prompts you, and it will update the Java language level in each IntelliJ modules to match that in the Maven project that it tracks.

Incidentally: why version 3.8.1?  
Maybe there's a newer version available. Lookup `maven-compiler-plugin` on https://search.maven.org/ and see if we can do better.

## Test-drive

Let's install our empty project, and see if anything goes bang.

Open IntelliJ's Maven toolwindow, locate the "root" project. Expand the "Lifecycle" node, and double-click the "install" goal.

Maven will run through most of its lifecycle stages. It will attempt to compile, test, package, and install our (empty) product.

### Warnings

You'll notice that `maven-resources-plugin` and `maven-compiler-plugin` issue this warning:

```
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
```

We can resolve this by saying "yes, that default of UTF-8 sounds good to me."  
Add this property into the `<properties/>` node of your root `pom.xml`:

```xml
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
```

This property will be inherited by all child projects, and that's acceptable.

This also means we can put emoji into our Java source code, and be confident that our product will still build on any computer.  
This is legitimately useful for unit tests.

## Summary

We've now organised our code into a multi-module Maven project. At present there's no benefit compared to a single-module project, but we have a good place to put any future modules that we create.

Moreover, we can ensure that all modules agree to use the same version of Java, and align them to use the same versions of libraries like JUnit.

Multi-module Maven projects make most sense when all modules are logically part of the same product, and are "released" together.

## Vocabulary

It's good to disentangle "which parts are Maven" versus "which parts are IntelliJ". IntelliJ's project model is different to Maven's, but maps well enough that a developer can rely on IntelliJ's conventions for most of her working activities (compiling, testing, running code).

- A Maven project
  - is an "artifact"
    - packaged as (usually) a .jar file
  - is a member of a group of artifacts
  - is represented by a pom.xml
  - can be a Maven "module", if it has a parent
- An IntelliJ project
  - is the folder inside which your `.idea` folder resides
    - typically the folder in which you initialise your workspace via `File > Open`
  - is an IntelliJ "module"
  - frequently has more IntelliJ modules within it
  - owns project-level settings (e.g. JDK path, Java language level)
- An IntelliJ module
  - is the folder inside which a `.iml` file resides
  - may override some project-level settings (e.g. Java language level)
  - can be based on a Maven project
    - import again any time the Maven project changes

## Next

You've completed the Maven task! Make a PR with what you have achieved. Leave the annotations module in as you may need it for an extension task.