import React from 'react'
import Button from './Button'

function AddPostForm() {

  
  return (
    <div>
    <form className='add-post-form'>
        Author:
        <input id="author-field" type="text" required/>
        Avatar colour:
        <input type="color" id="avatar-colour"/>
        Date:
        <input id="date-field" type="date" required />
        <br />
        Content:
        <br />
        <textarea rows="4" cols="100" id="content-field" type="text" required></textarea>
        <br />
        <Button type="submit" />
      </form>
    </div>

  )
}

export default AddPostForm