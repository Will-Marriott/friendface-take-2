# friendface-take-2
Attempt 2 at friendface

# Friendface Full Stack

## Project Goal

Create a Spring API which communicates between a frontend client and database storage.

## Taught Skills

- React and Redux basics
- Fundamentals of HTTP-based REST API
- Introduction to Spring
- Relational Database
- Client, Server & Database responsibilities
- Controllers, Services & Repositories

## Tasks

### Task 1 - [React](https://reactjs.org/) + [Redux](https://redux.js.org/)
This first task will involve recreating the frontend you did as part of [friendface-html-grad-project](https://github.com/ScottLogic/friendface-html-grad-project) in React and Redux.

Check out the broken-down task list [here](react.md).

### Task 2 - [Spring](https://spring.io/), [Maven](https://maven.apache.org/) & [Swagger](https://swagger.io/)

The second task is to revisit some Java & Maven principles to set up your development environment and project structure for a Spring API.

- Follow the tutorial for setting up the project through [Maven](maven.md).
- Follow the tutorial for [Spring](spring.md).
- Follow the tutorial for [Spring Data](spring-data.md).
- Follow the tutorial for [Swagger](swagger.md).
  - Note Swagger provides you with an auto-generated ui for interacting with your API code. It is a useful tool for visualising what your code exposes and for directly calling the api methods from a web browser.

### Tasks 3 - FriendFace

The overall goal here is to get a full stack social network application working. There will be several components we need to put in place that will touch every layer: database, api & client.

Revisit your React application from task 1. This should be a good starting point to hook up to your new API.

With regards to testing, you should fully unit test _all code in the "service" layer of code_. This is what we consider to be business logic and where any calculation or logic should live. We can consider the "repository" layer (database access) as not necessary to unit test since we will likely be using a framework for this. Tests for the "controller" layer will likely be for validation and checking that the service is called correctly.

- Load all user posts in the database to the front end:
  - Create a table in the database to store user posts. This table should have a foreign key to the existing `users` table.
  - Create a user post object (or copy the object from the [post-sorting](https://github.com/ScottLogic/post-sorting-grad-project) task) in the API.
  - Create a repository which has a method for returning all user posts from the database. For now, we want to return the newest post at the top of the list.
  - Create a service that encapsulates the repository and provides a `getUserPosts` method.
  - Create a new controller which will provide a http `get` method which returns all user posts and uses the service above.
  - Using your React frontend, call the API when your main feed component first mounts and set this into the component's state.
- Asynchronously post new user posts from the frontend to the database and have them appear on the screen.
  - You will need a new controller method, service method & repository method for `POST`ing new user posts back to the database. You will also want to refresh the feed list, so we can see the new post at the top of the listing.
- Adjust the get methods above to show a specific number of posts from the database according to a drop-down value in the UI (10, 25, 50). Allow the user to specify the sort method for these user posts.


## Extension Tasks

Well done for completing the tasks! You now have a fully functioning, fully tested, fullstack application 🎉

The extension tasks below can be quite lengthy and with you going on grad project soon, you may only complete 1 or 2 max. Now is the time to explore something you have an interest in learning more about. Pick up an extension task, or go off-road and look into anything you'd like.

- Read through the content in [annotations](annotations.md) - this is a deep dive into how annotations work behind the scenes.
- Add user login functionality:
  - Check out JWT tokens and OAuth2 for guidance.
- Get freaky with the UI:
  - Use a component library (like [materialUI](https://mui.com/)) to jazz up your buttons, fields, navBars etc.
  - Create your own components that do awesome things.
- Explore automated/performance testing frameworks:
  - Add UI tests.
  - Add integration tests.
  - Add end-to-end tests.
  - Check out [Gherkin/Cucumber](https://cucumber.io/docs/gherkin/).
  - Check out a performance testing tool (e.g. [Gatling](https://gatling.io/))
