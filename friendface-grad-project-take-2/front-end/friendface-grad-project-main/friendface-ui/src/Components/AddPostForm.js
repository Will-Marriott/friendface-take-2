import React, { useState } from 'react'
import { useDispatch } from 'react-redux';
import { addPost } from '../Store/Features/postsSlice';
import { useSelector } from 'react-redux/es/hooks/useSelector';

function AddPostForm() {
  
  
  //Setting the states for each element of form
  const [author, setAuthor] = useState('')
  const [content, setContent] = useState('')
  const [date, setDate] = useState('')
  const [colour, setColour] = useState('')
  const dispatch = useDispatch()  
  
  //Adds new post to DB and state
  const addPostToServer = async () => {
    const post={
      "author": author,
      "colour": colour || '#000000',
      "content": content,
      "date": date,
      "likes": 0,
      "id": posts.length + 1
    }
    
    const res = await fetch('http://localhost:8080/posts-api/posts', {
      method: 'POST',
      headers: {
        'Content-type': 'application/json',
      },
      body: JSON.stringify(post),
    })
    dispatch(addPost(post))
  }

  
  const posts = useSelector(state => state.posts.value) 

  
  const onSubmit = (e) => {
    e.preventDefault()
    addPostToServer();
    setAuthor('')
    setContent('')
    setDate('')
    setColour('')
  }

  //Logs post state
  const onClick = () => console.log(posts)

  
  return (
    // form copied over from html project, with some adjustments
    <div>
      <form id="add-post" className='add-form' onSubmit={onSubmit}>
        
        Author:
        <input 
        id="author-field" 
        type="text"
        placeholder="Enter your name here..."
        value={author}
        onChange={(e) => {
          setAuthor(e.target.value)
        }}
        required/>
        
        Avatar colour:
        <input 
        type="color" 
        id="avatar-colour"
        onChange={(e) => {
          setColour(e.target.value)}}
        /> 
        
        Date:
        <input 
        id="date-field" 
        type="date"
        value={date}
        onChange={(e) => {
          setDate(e.target.value)}} 
        required />
        <br />
        
        Content:
        <br />
        <textarea 
        rows="4" 
        cols="100" 
        id="content-field"
        value={content} 
        type="text" 
        placeholder='Share your thoughts...'
        onChange={(e) => {
          setContent(e.target.value)}}
        required></textarea>
        <br />
        
        <input type='submit' value='Post' className='btn-submit'/>

      </form>
      <input type='button' value='Log posts state'  onClick={onClick} />
    </div>

  )
}

export default AddPostForm