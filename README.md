## Java Simple Backend Application 

### Project Goal

- Create a series of classes which can filter and sort a list of `User Post` objects.

### Required Skills

- Understand how to use Git to manage code

### Taught Skills

- Writing Java code
- Unit testing for Java
- Introduction to Object Oriented Programming
- Debugging in IntelliJ

### Getting Started

#### Set up your JDK

This section aims to provide a few steps to configure your JDK (Java Development Kit) for use with Maven and IntelliJ. Feel free to skip ahead to Installing Maven if you are already comfortable with this, or you've used IntelliJ to install Java. 

- Install [Amazon Corretto 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)
   - The installer will probably add a `%JAVA_HOME%` environment variable pointing at the installation directory of the JDK e.g. "C:\Program Files\Amazon Corretto\jdk11.0.12_7"
   - Verify this with the command `echo %JAVA_HOME%` on Windows cmd prompt or `echo $JAVA_HOME` on Bash, both should print out the JDK installation folder
   - `Java.exe` and `Javac.exe` should also be accessible on your `PATH` so add the bin directory from your JDK installation folder to your `PATH` variable

#### Install Maven

Maven is a build automation tool for Java projects, which we can use to compile the code we have written into something that can be run by a computer. There are many ways to compile the project that we will create, but Maven gives us a convention to follow that other developers will also be familiar with. When we need to use a library (a set of code that someone has written and we can reuse to save time) that is not included in the JDK, Maven gives us a way to define that _dependency_ and automatically download it from Maven's central repository when we build our project. 

You can download the latest release [here](https://maven.apache.org/download.cgi), if you are using windows then you will need the most recent zip file. [This guide](https://maven.apache.org/install.html) covers how to install Maven. 

#### Install and Configure IntelliJ

- Install [IntelliJ Community](https://www.jetbrains.com/idea/download/)
- Fork this repository and clone your fork to a Dev location (for example `C:\Dev\posting-sorting-grad-project`)
- Import the project
  - Click File -> New -> Project from existing sources...
  - Navigate to the directory where you cloned your forked version of the repository and click "OK"
  - Select the option to "Import project from external model" and then choose Maven, and click "Finish" 
- Run the application by clicking the play button or by going to "Run > Run 'Main'"
  - This may fail if the language setting isn't compatible with the Java SDK running
  - If you are getting the error message `java: error: release version 17 not supported` or similar, try right-clicking the source folder > Open Project Settings, and change the language level to 11.
- Look at the Run window for a list of the example posts

### Tasks

We're going to implement a mechanism to sort posts by their various attributes. We'll also convert the project to the structure used by Maven so that we can use JUnit to test our post sorters. 

#### 1. Writing Unit Tests

Our project is set up to use the popular testing framework, JUnit. Like many other packages and frameworks, it is available as a jar (Java Archive) file that we can add to our project. We would need to download it and put it in the right place so that the compiler can find it when we try to use it. After we start using it, our project will _depend_ upon it being there, which is why this would be called a _dependency_. Rather than doing this ourselves (you are very welcome to try this in another project), we are using Maven to take care of this for us. 

> **❕ Note: Maven relies on a master pom (Project Object Model) which assumes that the Java language version is 5**  
> We're using Java 11 and some of our code may rely on its features which version 5 won't support.

- Have a look at our project's `pom.xml`, it contains the configuration to:
  - Specify that we're using Java 11 
  - Specify that our source and output are encoded in UTF-8
  - Specify JUnit as a dependency

> **ⓘ Unit Testing**
> Unit testing is introduced nicely in this [short blog post](https://blog.testlodge.com/what-is-unit-testing/). Many people find it helpful to structure their tests using the [Arrange/Act/Assert](https://java-design-patterns.com/patterns/arrange-act-assert/) pattern.

The first extension task, below, is to refactor the code you’re about to write using streams. Don’t worry about these (yet), but it is useful to appreciate at this point that you should be able to refactor your code and have the tests still pass. The tests shouldn’t care how your code works; just that the expected outcome is the same.

- Locate `com.scottlogic.AuthorPostSorterTest`, which is the class that holds the unit tests for `AuthorPostSorter`
  - Run the existing unit tests by right clicking on the class name and clicking "Run AuthorPostSorterTest"
  - See the results:
    - `sort_sortedList_returnsSortedList()` has passed
    - `sort_unsortedList_returnsSortedList()` has failed - because AuthorPostSorter's sort method doesn't have any code yet! Lets get going on that

#### 2. Implement AuthorPostSorter

> **❕ Note: Complete this task without the use of Java 8 Streams!**  
> When you get to the extension tasks, the first one is to refactor the code you’re about to write using streams. It is useful to appreciate at this point that you should be able to refactor your code and have the unit tests still pass, i.e. the tests don’t care how your code works; just that the expected outcome is the same.

- Locate the class called `AuthorPostSorter` which implements the PostSorter interface
- Write the implementation for its `sort` method which returns the list of user posts in order of author name, ascending alphabetically
  - Keep running your tests as you write this method, you should also add to them: 
    - Check that the method can sort user posts by author name without case sensitivity
    - Check that the method can cope with bad input - e.g. a `null` or empty input list

> **❕ For the remainder of this project you should include new unit tests in addition to every new method/change in behaviour added to classes. Best practice is to write your unit tests before you write/change your methods implementation, watch them fail, and then watch them pass once you finish coding.**

- A `SortOrder` enum has been included in the solution. Adjust the PostSorter interface so that the sort method can be sorted in ascending or descending order and edit the `AuthorPostSorter` and tests accordingly
  - Write classes which each provide a method for sorting by:
    - Date submitted
    - Number of likes
    - Length of content in post
    - Author name extended - if a surname is provided, sort by surname, otherwise sort by first name.

- Write a new interface called `PostFilter` for classes that can filter an input list and return a reduced list. Write classes which can filter by:
  - Author
  - Keyword in post content
  - Posts made between certain dates
  - Only posts which have likes

- Write classes for combining other filters using AND and OR
  - The AND and OR filters are filters and so should also implement `PostFilter`

- Now that you've created some filters and sorters, let's reuse these to build more advanced functionality. Create classes that:
  - Sorts the posts first by the author name, and then each authors' posts are sorted by the date posted
  - Takes a keyword as input and returns the posts ordered by the number of times the keyword appears in the `Post` content

- Write a new class which will extract topics from the content of a `Post` using regex or otherwise:

> Here we are considering a topic as a keyword that is not a stop word and is the most common word. E.g. if a post mentions “owl” “owl” and “owls” (and there is no more-frequent word), then “owl” would be the keyword. As usual, the unit tests should be agnostic to the implementation here
 
- Write a method to group user posts into their topics and return the list sorted by the most popular topics

> If you have five posts about Owls and four about Pandas, then sorting ascending by topic should return the Panda posts first, and then the Owls. Within the Owl posts, the ones that mention owls the most should be first, with decreasing "owliness".

### Extension Tasks

> Streams are wrappers around a data source which make bulk operations on the data source more convenient. Don't worry, we'll use them to demonstrate how important good unit test design is later. Read more about them [here](https://www.oracle.com/technical-resources/articles/Java/ma14-Java-se-8-streams.html).

- Rewrite the above sort and filter classes using Java streams. You shouldn't need to modify your unit tests and they should all still pass after you've finished.
- Write a class which can take a JSON representation of a filter and sort method and produce the runtime filter/sort for this
