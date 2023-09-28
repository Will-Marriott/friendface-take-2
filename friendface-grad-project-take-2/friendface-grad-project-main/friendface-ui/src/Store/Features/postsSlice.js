import { createSlice } from "@reduxjs/toolkit";
import Posts, { posts } from "../../Components/Posts";
import { useSelector } from "react-redux/es/hooks/useSelector";

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
            return {...state, value: currentPosts,}
            
        }
    }
});



export const { addPost } = postsSlice.actions

export default postsSlice.reducer