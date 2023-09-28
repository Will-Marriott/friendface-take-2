import React from 'react'
import { useDispatch } from 'react-redux'
import { addPost } from '../Store/Features/postsSlice';




function Button() {    

const dispatch = useDispatch();



  return (

    <div>
        <button 
        onClick={() => {dispatch(addPost({id: 1, authorName: "Will Dispatch", content: "Test content A", date: 2023, likeCount: 0, colour: "red"})  )}}>
          Create post</button>
        
    </div>
  )
}

export default Button