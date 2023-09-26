import React from 'react'
import { useDispatch } from 'react-redux'
import { addPost } from '../Store/Features/postsSlice';



function Button() {    

const onClick = () => console.log("button clicked")
const dispatch = useDispatch();

  return (

    <div>
        <button 
        onClick={() => {dispatch(addPost({id: 1, authorFirstName: "Will Dispatch", authorSurname: "Marriott 1", contents: "Test content A", date: 2023, likeCount: 0,})  )}}>
          Create post</button>
        
    </div>
  )
}

export default Button