import React from 'react'
import AddPostForm from './AddPostForm'
import SortersAndFilters from './SortersAndFilters'


function CreatePost() {

  return (
    <div className='create-post'>
      <div>
        <AddPostForm />
      </div>
      <div>
        <SortersAndFilters />
        </div>
    </div>
  )
}

export default CreatePost