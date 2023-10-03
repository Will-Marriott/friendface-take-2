import React from 'react'
import { useEffect } from 'react';
import { useDispatch, useSelector } from "react-redux"
import SortersAndFilters from './SortersAndFilters'
import { addLike, initialSetPosts } from '../Store/Features/postsSlice';


function Posts() {

    const dispatch = useDispatch();
    const posts = useSelector(state => state.posts.value)

    //returns target post
    const fetchPost = async (id) => {
      const res = await fetch(`http://localhost:5000/posts/${id}`)
      const data = await res.json()
      return data
    }


    //Adds likes to state and to DB
    const onLike = async (id) => {
      const postToLike = await fetchPost(id)
      const updPost = { ...postToLike}
      updPost.likes++
      const test = () => {console.log(updPost)}
      test()

      const res = await fetch(`http://localhost:5000/posts/${id}`, {
      method: 'PUT',
      headers: {
        'Content-type': 'application/json'
      },
      body: JSON.stringify(updPost),
    })
    dispatch(addLike(id))

    }
    
    
    //Setting the initial posts state as the DB
    useEffect(() => {
      const fetchPosts = async () => {
        const res = await fetch('http://localhost:5000/posts');
        const data = await res.json();
        dispatch(initialSetPosts(data))
      };
      fetchPosts();
  
    }, [dispatch]);
   
    return (
    <div>    
    <SortersAndFilters />
            {posts.map((post) => 
          
            <div key={post.id}>
                        
            <div className='postContainer'>
            <div className='avatarColouredBox'>
                    
              <svg viewBox="0 0 532 532" xmlns="http://www.w3.org/2000/svg">
              <g fill="#a0616a">
              <circle cx="270.76" cy="260.93" r="86.349"/>
              <polygon points="199.29 366.61 217.29 320.61 310.29 306.61 320.28 408.44 226.28 410.44"/>
              </g>
              <path d="m357.94 276.86c-1.1207 4.4896-3.386 15.18-6.9238 15.232-2.8902 0.04208-5.6567-46.335-2.7695-54.006 3.3164-8.8127-5.3989-19.961-11.964-25.683-11.804-10.289-38.007 11.805-64.651 1.7959-0.70633-0.26482-0.56558-0.23502-8.9793-3.5917-25.89-10.33-27.251-10.628-28.734-10.775-12.55-1.2417-27.867 9.0284-34.121 21.55-6.5017 13.017-1.0694 24.181-7.1835 55.672-0.71246 3.6706-1.8314 8.9022-3.5917 8.9793-3.2182 0.14029-6.3605-17.048-7.1835-21.55-3.4479-18.862-6.7722-37.047 0-57.468 0.73878-2.2273 5.2916-10.495 14.367-26.938 13.074-23.688 19.65-35.577 21.55-37.713 13.629-15.326 38.436-29.307 59.264-23.346 10.527 3.013 8.6395 7.8569 21.55 12.571 23.008 8.4006 43.005-1.873 46.693 5.3876 1.9537 3.846-3.5124 7.0169-3.5917 14.367-0.13593 12.611 15.814 16.256 25.142 28.734 5.0145 6.7082 13.598 6.7801-8.8723 96.782l3e-5 3e-5z" fill="#2f2e41"/>
              <path d="m464.92 442.61c-3.4802 3.9102-7.0901 7.7402-10.83 11.48-50.24 50.239-117.04 77.909-188.09 77.909-61.41 0-119.64-20.67-166.75-58.72-0.03003-0.01953-0.05005-0.04004-0.07983-0.07031-6.25-5.0391-12.3-10.399-18.14-16.06 0.10986-0.87988 0.22998-1.75 0.35986-2.6104 0.82007-5.7998 1.7302-11.33 2.75-16.42 8.3501-41.72 118.22-85.52 121.08-86.66 0.04004-0.00977 0.06006-0.01953 0.06006-0.01953s14.14 52.12 74.73 51.45c41.27-0.4502 33.27-51.45 33.27-51.45s0.5 0.09961 1.4399 0.2998c11.92 2.5303 94.68 20.71 127.33 45.521 9.95 7.5596 17.09 23.66 22.22 42.859 0.21997 0.82031 0.42993 1.6602 0.65015 2.4902z" fill= {post.colour}/>
              <path d="m454.09 77.91c-50.24-50.239-117.04-77.91-188.09-77.91s-137.85 27.671-188.09 77.91c-50.24 50.24-77.91 117.04-77.91 188.09 0 64.851 23.05 126.16 65.29 174.57 4.03 4.6299 8.24 9.1396 12.62 13.521 1.03 1.0293 2.0701 2.0596 3.1201 3.0596 5.8401 5.6602 11.89 11.021 18.14 16.06 0.02979 0.03027 0.0498 0.05078 0.07983 0.07031 47.11 38.05 105.34 58.72 166.75 58.72 71.05 0 137.85-27.67 188.09-77.909 3.74-3.7402 7.3498-7.5703 10.83-11.48 43.37-48.72 67.08-110.84 67.08-176.61 0-71.05-27.67-137.85-77.91-188.09zm10.18 362.21c-7.8699 8.9502-16.33 17.37-25.33 25.181-17.07 14.85-36.07 27.529-56.56 37.63-7.1902 3.5498-14.56 6.7793-22.1 9.6699-29.29 11.24-61.08 17.399-94.28 17.399-32.04 0-62.76-5.7393-91.19-16.239-11.67-4.3008-22.95-9.4102-33.78-15.261-1.5901-0.85938-3.1699-1.7295-4.74-2.6191-8.26-4.6807-16.25-9.79-23.92-15.311-10.99-7.8799-21.35-16.59-30.98-26.03-5.3999-5.29-10.56-10.8-15.49-16.529-39.81-46.24-63.9-106.36-63.9-172.01 0-145.57 118.43-264 264-264s264 118.43 264 264c0 66.66-24.83 127.62-65.73 174.12z" fill="#3f3d56"/>
              </svg>

            </div>
            <div className='postBodyContainer'>
              <div className='postTitle'>
              {post.author} on {post.date} said:
              </div>
        
              <div className='postContent'>
              {post.content}<br/>
              </div>
              
              <button onClick={() => onLike(post.id)}>Like</button>
              <div>{post.likes} like(s)</div>
            
            
            </div>
          </div>
          </div>
            
            )}
    </div>
  )
}


export default Posts
