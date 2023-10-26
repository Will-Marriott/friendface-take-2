import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { initialSetPosts } from '../Store/Features/postsSlice';
import { useSelector } from 'react-redux';
import styles from './AddPostForm.css'; // Import the CSS file

function AddPostForm() {
  const [author, setAuthor] = useState('');
  const [content, setContent] = useState('');
  const [date, setDate] = useState('');
  const [colour, setColour] = useState('#000000');
  const dispatch = useDispatch();
  const posts = useSelector((state) => state.posts.value);

  const handleColorChange = (event) => {
    setColour(event.target.value);
  };

  const clearForm = () => {
    setAuthor('');
    setContent('');
    setDate('');
    setColour('#000000');
  };

  const setPostsFromApi = async () => {
    try {
      const response = await fetch('http://localhost:8080/posts-api/posts');
      if (response.ok) {
        const data = await response.json();
        dispatch(initialSetPosts(data));
      } else {
        console.error('Failed to fetch posts:', response.status);
      }
    } catch (error) {
      console.error('Error fetching posts:', error);
    }
  };

  const addPostToServer = async () => {
    const post = {
      author: author,
      colour: colour,
      content: content,
      date: date,
      likes: 0,
      id: posts.length + 1,
    };

    try {
      const response = await fetch('http://localhost:8080/posts-api/posts', {
        method: 'POST',
        headers: {
          'Content-type': 'application/json',
        },
        body: JSON.stringify(post),
      });

      if (response.ok) {
        setPostsFromApi();
        clearForm();
      } else {
        console.error('Failed to add a new post:', response.status);
      }
    } catch (error) {
      console.error('Error adding a new post:', error);
    }
  };

  const logPostsState = () => {
    console.log(posts);
  };

  return (
    <div>
      <form id="add-post" className="add-form" onSubmit={addPostToServer}>
        <label htmlFor="author-field">Author:  </label>
        <input
          id="author-field"
          className="input-field-author"
          type="text"
          placeholder="Enter your name here..."
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
          required
        />
        <label htmlFor="avatar-colour">Avatar colour:   </label>
        <input
          type="color"
          id="avatar-colour"
          value={colour}
          onChange={handleColorChange}
        />
        
        <label htmlFor="date-field">Date:  </label>
        <input
          id="date-field"
          className="input-field"
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          required
        />
        <br />
        <label htmlFor="content-field">Content:</label>
        <br />
        <textarea
          rows="4"
          cols="100"
          id="content-field"
          className="input-field"
          value={content}
          type="text"
          placeholder="Share your thoughts..."
          onChange={(e) => setContent(e.target.value)}
          required
        />
        <div>
          <button type="submit" className="btn-submit">
            Post
          </button>
          <button type="button" onClick={clearForm}>
            Clear
          </button>
          <button type="button" onClick={logPostsState}>
            Log posts state
          </button>
        </div>
      </form>
    </div>
  );
}

export default AddPostForm;
