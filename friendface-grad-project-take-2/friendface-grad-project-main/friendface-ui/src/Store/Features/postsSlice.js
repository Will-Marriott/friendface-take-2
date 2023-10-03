import { createSlice } from "@reduxjs/toolkit";


const initialState = []




export const postsSlice = createSlice({
    name: "posts",
    initialState: {value: initialState},
    reducers: {
        addPost: (state, action) => {
            
            // let id = state.posts.length
            let currentPosts = [...state.value];
            let id = currentPosts.length+1
            let newPost = {...action.payload, id: id};
            currentPosts.push(newPost);

            // return {currentState, value:currentPosts}
            return {...state, value: currentPosts}
            
        },
        addLike: (state, action) => {
            //payload will be post.id
            let targetPost = [...state.value][action.payload-1];

            let updatedPost = {...targetPost, likes: targetPost.likes++}
            let currentPosts = [...state.value]
            currentPosts[action.payload-1] = updatedPost
        },
        initialSetPosts: (state, action) => {
            //payload will be an array of posts fetched from DB
            let currentPosts = [...action.payload]
            return {value: currentPosts}
        }
    }
});



export const { addPost, addLike, initialSetPosts } = postsSlice.actions



export default postsSlice.reducer