import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { initialSetPosts } from '../Store/Features/postsSlice';

function SortersAndFilters() {
  const [selectedOption, setSelectedOption] = useState('Please select an option');
  const dispatch = useDispatch();

  const handleApply = async () => {
    if (selectedOption === 'Please select an option') {
      return;
    }

    try {
      let endpoint = '';
      switch (selectedOption) {
        case 'author-ascending':
          endpoint = 'sort-author-asc';
          break;
        case 'author-descending':
          endpoint = 'sort-author-desc';
          break;
        case 'date-ascending':
          endpoint = 'sort-date-newest-first';
          break;
        case 'date-descending':
          endpoint = 'sort-date-oldest-first';
          break;
        default:
          return;
      }

      const res = await fetch(`http://localhost:8080/sorter-api/${endpoint}`);
      const data = await res.json();

      data.reverse();
      dispatch(initialSetPosts(data));
    } catch (error) {
      console.error('Error fetching and sorting posts:', error);
    }
  };

  const handleOptionChange = (event) => {
    setSelectedOption(event.target.value);
  };

  return (
    <div>
      <form>
        <label htmlFor="sort-posts-apply">Sort posts by:</label>
        <select
          name="sort-posts-option"
          id="sort-posts-option"
          onChange={handleOptionChange}
          value={selectedOption}
        >
          <option value="Please select an option">Please select an option</option>
          <optgroup label="Sort by Author">
            <option value="author-ascending">Sort by Author (A-Z)</option>
            <option value="author-descending">Sort by Author (Z-A)</option>
          </optgroup>
          <optgroup label="Sort by Date posted">
            <option value="date-ascending">Sort by Date (Newest First)</option>
            <option value="date-descending">Sort by Date (Oldest First)</option>
          </optgroup>
        </select>
        <br />
        <button type="button" onClick={handleApply}>
          Apply
        </button>
      </form>
    </div>
  );
}

export default SortersAndFilters;
