import { createSlice } from "@reduxjs/toolkit";
import Posts, { posts } from "../../Components/Posts";

const initialState = []

// [ 
//     {id: 1, authorFirstName: "Will 1", authorSurname: "Marriott 1", contents: "Test content A", date: 2023, likeCount: 0,},
//     {id: 2, authorFirstName: "Will 2", authorSurname: "Marriott 2", contents: "Test content B", date: 2023, likeCount: 1,},
//     {id: 2, authorFirstName: "Will 2", authorSurname: "Marriott 2", contents: "Test content B", date: 2023, likeCount: 1,}];

// export const postsSlice = createSlice({
//     name: "posts",
//     initialState: {value: initialState},
//     reducers: {
//         // addPost: (currentState, submitPost) => {
            
//         //     let currentState = [...initialState.value];
//         //     let newPost = submitPost.payload;
//         //     currentState.push(newPost); 
//         //     return {currentState, value: submitPost.payload}
            
//         // },
//         addPost: (state, action) => {
//             state = initialState;
//             const newPost = action.payload.value
//             const newState = {...initialState, value: newPost}
//             return newState
//         }
    
//     }
// });

export const postsSlice = createSlice({
    name: "posts",
    initialState: {value: initialState},
    reducers: {
        addPost: (state, action) => {
            
            
            let currentPosts = [...state.value];
            let newPost = {...action.payload};
            currentPosts.push(newPost);
            console.log("this is the currentPosts variable before the return" + currentPosts);

            // return {currentState, value:currentPosts}
            return {...state, value: currentPosts,}
            
        }
    }
});



export const { addPost } = postsSlice.actions

export default postsSlice.reducer