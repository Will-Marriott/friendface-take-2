import { configureStore } from "@reduxjs/toolkit"
import postsSlice from "./Features/postsSlice"

export const store = configureStore({
    reducer: {
        posts: postsSlice,
        
    },
})