# React

The first task will be to implement your FriendFace UI from the [first project](https://github.com/ScottLogic/friendface-html-grad-project) in React.

> Things to note:
> - We'd recommend doing 1 PR per main task below.
> - We want to be committing components using React’s _functional_ style as opposed to React’s _class_ style. The tutorial mainly follows the functional style but feel free to experiment with the other style.

### Task 1 - React JS Crash Course - Skeleton + Header 
Tutorial found [here](https://www.youtube.com/watch?v=w7ejDZ8SWv8).

The first step is to familiarise yourself with React. The result will be creating the skeleton app with a styled header. Follow the tutorial, recreating the steps on your machine to learn about the features and structure of the React framework.

> Note: don’t follow too closely (only commit code that is relevant to the FriendFace UI). We’re not creating a Task Tracker app here.

1. Watch part 1  - Intro and Slides.
2. Watch part 2:
   1. Use the `create-react-app tool` to create a React project called `friendface-ui` in the root of this repo.
   2. Install the React Developer Tools.
   3. Install any plugins for the IDE of your choice (VSCode or IntelliJ Ultimate recommended).
3. Watch part 3:
   1. Change your App’s title to `FriendFace`.
   2. Update the description in the `<meta>` tag to something more appropriate.
4. Watch part 4 - Delete the code and tidy the files as the instructor does.
5. Watch part 5 - Play around with JSX, creating consts, using conditionals.
6. Watch part 6:
   1. Create a components folder.
   2. Create your first component!  Create a `Header` component with an appropriate title and subtitle.
   3. Try out the component style if you like, although not needed for this task.
7. Watch part 7 and 8 - have a play around with props, default props and propTypes and add to your `Header`.
8. Watch part 9 - Style your Header as you see fit using index.css.

### Task 2 - React JS Crash Course - Buttons + Events
In this step, we will create a new component and add a submit button.

1. Watch part 10:
   1. Create a new component that will contain your create-a-post functionality. Name it something appropriate. This will house everything needed to create a post, but for now will just include the submit button.
   2. Create a button component and style it appropriately using your css file. Add propTypes and default props as appropriate.
   3. Add the Button component to the component from 1.
2. Watch part 11 - Add an onClick event as a prop that just logs something for now. This will eventually ping off a request to your backend API.
3. Watch part 12 + 13:
   1. Create a new component, as the instructor does, but call it `Posts` instead. This will contain the posts that you've made.
   2. Add a list of dummy Post objects with all the values it needs (id, author, date, etc.).
   3. Add the Posts component to your `App`.
   4. In Posts, map through your dummy posts adding the fields to the screen. Style as preferred.
4. Watch part 14 + 15:
   1. Change the dummy posts to use your first hook - `useState`.
   2. Instead of using a global state in the App like the instructor does, put this PR up and move onto the next step - Redux.

### Task 3 - Redux React Tutorial - State Management
Tutorial found [here](https://www.youtube.com/watch?v=k68j9xlbHHk).

In this task we will add a state store using redux. You may want to consider using the Redux DevTools extension for Google Chrome.

1. Watch part 1 and 2 - Intro + What is State Management?
2. Watch part 3:
   1. Install redux.
   2. Install react-redux.
   3. Install redux-toolkit.
3. Watch part 4. You've already done this bit but nice to follow what’s going on.
4. Watch part 5:
   1. Create a new folder called 'store'. Within this folder, create your store in a file called 'store.js'. Add an empty reducer to the store.
   2. Import your store to app.js. Wrap your App in a provider that references your store.
   3. Understand what a reducer is.
   4. Create a folder called features and add a `postsSlice.js` file.
5. Watch part 6:
   1. Create a posts slice. Unlike the tutorial, we want this to be a list. It may be more readable to create a const called `initialState` which is called in your `createSlice()` method.
      ```js
      const initialState = {
         posts : [
            {author: "Seth"}, //add some dummy data in here, for now, for all the necessary fields of a post
            {author: "Ed" }
         ]
      }
      ```
   2. Create an `addPost` reducer that will `push()` an `action.payload()` (a post) to your list state. It is generally not advised to mutate (modify in-place) data stored in the state. Therefore, you will need to make a copy of the data in the state first - you may find the [spread operator](https://www.w3schools.com/react/react_es6_spread.asp) useful for this.
   3. Import your reducer to your Posts component.
6. Watch part 7:
   1. Import the `useSelector` hook.
   2. Create a posts variable and set it to the value of your posts in your store. See your UI be updated with what's in the initial state.
   3. Update your `initialState` in your redux slice to be an empty list.
7. Watch part 8:
   1. Import `useDispatch` and `addPost` in your component that creates a post.
   2. Set up your submit button to dispatch a dummy post to your store. Do not expect the page to be updated to display the post yet - this will be implemented in task 4.
8. Feel free to watch the rest of the tutorial to see how to add multiple reducers.


## Task 4 - React JS Crash Course
Tutorial found [here](https://www.youtube.com/watch?v=w7ejDZ8SWv8), same one as Task 1 and 2.

You now have most of the knowledge you need to complete this part of the project. The steps in this task will be mostly up to you as developers. We'd recommend watching the rest of the tutorial where you take or leave the functionality that you want. You will have to manage branching strategies, when to make a PR etc. yourselves.

Here is a list of the Acceptance Criteria - things that must be met before you can put down React and move onto API work:
* Add basically everything that you had in the [friendface-html-grad-project](https://github.com/ScottLogic/friendface-html-grad-project). Some of this can be copied across but must be written in React.
  * Create post input fields (part 20 + 21 of the tutorial covers forms. Remember to use Redux for state management!).
  * Add UI validation to fields.
  * Add a sort button (you don't need to add functionality as this will be handled by the backend).
  * Add a filter button (you don't need to add functionality as this will be handled by the backend).
  * Add a like button.
* Components are written as functions.
* Redux is used as your state store.
* The UI must be able to call out to your backend. Use the `useEffect` hook to do this.
  * Part 27, 28 and 30 of the tutorial show you how to do this with a mock backend. We can switch this to your real backend in the next task. It may be useful to look at the [JS Async](https://www.w3schools.com/js/js_callback.asp) pages on W3schools for this.

We've added a list of some steps that will be nice, but not essential, to add:
1. Part 16 - Create a post component to tidy your posts code, passing the post in as a prop.
2. Part 17 - Play around with icons.
3. Part 19 - Add a message if there are no posts (conditional components).
4. Part 32 - Add Routing if you're using multiple pages.
5. Update the Favicon to something more relevant to your app.
