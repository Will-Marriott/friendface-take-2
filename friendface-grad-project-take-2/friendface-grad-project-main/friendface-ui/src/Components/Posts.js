import React from 'react'
import { useState } from 'react'
import { addPost } from '../Store/Features/postsSlice'
import { useSelector } from "react-redux"


function Posts() {
  //creating the default state
  // const [posts, setPosts] = useState([ 
  //   {id: 1, authorFirstName: "Will 1", authorSurname: "Marriott 1", contents: "Test content A", date: 2023, likeCount: 0,},
  //   {id: 2, authorFirstName: "Will 2", authorSurname: "Marriott 2", contents: "Test content B", date: 2023, likeCount: 1,}])
  
    const posts = useSelector(state => state.posts.value)  
  
    return (
    <div>
            {posts.map((post) => 
          
            <h3>{post.authorFirstName} {post.authorSurname} said: <br/>{post.contents}</h3>)}
    </div>
  )
}


export default Posts
