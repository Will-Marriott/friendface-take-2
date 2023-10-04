import React from 'react'

function SortersAndFilters() {
  return (
    <div>
        <form>
          <label htmlFor="sort-posts-apply">Sort posts by:</label>
          <select name="sort-posts-option" id="sort-posts-option">
          <optgroup label="Sort by Author">
            <option value="author-ascending">Author A-Z</option>
            <option value="author-descending"> Author Z-A</option>
          </optgroup>
          <optgroup label="Sort by Date posted">
            <option value="date-ascending">Newest Posts First</option>
            <option value="date-descending">Oldest Posts First</option>
          </optgroup>
          </select>
          <br />
          <input id="sort-posts-apply" type="button" value="Apply"></input>
        </form>
    </div>
  )
}

export default SortersAndFilters