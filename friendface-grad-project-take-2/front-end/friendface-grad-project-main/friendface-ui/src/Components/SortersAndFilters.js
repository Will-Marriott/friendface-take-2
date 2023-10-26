import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import { initialSetPosts } from '../Store/Features/postsSlice';
import './SortersAndFilters.css'; // Import the CSS file

function SortersAndFilters() {
  const [selectedOption, setSelectedOption] = useState('');
  const [filterOption, setFilterOption] = useState('');
  const [filterValue, setFilterValue] = useState('');
  const [filterFromValue, setFilterFromValue] = useState('');
  const [filterToValue, setFilterToValue] = useState('');
  const [loading, setLoading] = useState(false);
  const dispatch = useDispatch();

  //Retrieve posts from back end
  const fetchData = async (url) => {
    try {
      setLoading(true);
      const res = await fetch(url);
      if (!res.ok) {
        throw new Error(`Failed to fetch data. Status: ${res.status}`);
      }
      const data = await res.json()
      data.reverse();
      dispatch(initialSetPosts(data));
    } catch (error) {
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleApply = async () => {
    if (selectedOption === '') {
      return;
    }

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

    fetchData(`http://localhost:8080/sorter-api/${endpoint}`);
  };

  const handleFilterApply = async () => {
    if (filterOption === 'Filter by Keyword') {
      fetchData(`http://localhost:8080/filter-api/keyword-filter?keyword=${filterValue}`);
    } else if (filterOption === 'Filter by Date') {
      fetchData(
        `http://localhost:8080/filter-api/date-filter?fromDate=${filterFromValue}&toDate=${filterToValue}`
      );
    }
  };

  const handleOptionChange = (event) => {
    setSelectedOption(event.target.value);
  };

  const handleFilterOptionChange = (event) => {
    setFilterOption(event.target.value);
  };

  const handleFilterValueChange = (event) => {
    setFilterValue(event.target.value);
  };

  const handleFilterFromValueChange = (event) => {
    setFilterFromValue(event.target.value);
  };

  const handleFilterToValueChange = (event) => {
    setFilterToValue(event.target.value);
  };

  const handleSubmit = (event) => {
    event.preventDefault(); // Prevent the form from submitting
    handleFilterApply(); // Call the filter apply function
  };

  return (
    <div className="sorters-and-filters">
      <form>
        <label htmlFor="sort-posts-option">Sort posts by:</label>
        <select
          name="sort-posts-option"
          id="sort-posts-option"
          onChange={handleOptionChange}
          value={selectedOption}
        >
          <option value="">Please select an option</option>
          <optgroup label="Sort by Author">
            <option value="author-ascending">Sort by Author (A-Z)</option>
            <option value="author-descending">Sort by Author (Z-A)</option>
          </optgroup>
          <optgroup label="Sort by Date posted">
            <option value="date-ascending">Sort by Date (Newest First)</option>
            <option value="date-descending">Sort by Date (Oldest First)</option>
          </optgroup>
        </select>
        <button type="button" onClick={handleApply} className="btn-submit" disabled={loading}>
          {loading ? 'Applying...' : 'Apply Sort'}
        </button>
      </form>
      <form onSubmit={handleSubmit}>
        <label htmlFor="filter-posts-option">Filter posts by:</label>
        <select
          name="filter-posts-option"
          id="filter-posts-option"
          onChange={handleFilterOptionChange}
          value={filterOption}
        >
          <option value="">Select Filter Option</option>
          <option value="Filter by Keyword">Filter by Keyword</option>
          <option value="Filter by Date">Filter by Date</option>
        </select>
        {filterOption === 'Filter by Keyword' && (
          <input
            type="text"
            name="keyword"
            id="keyword"
            placeholder="Enter keyword..."
            value={filterValue}
            onChange={handleFilterValueChange}
          />
        )}
        {filterOption === 'Filter by Date' && (
          <div>
            <label htmlFor="from-date">From Date: </label>
            <input
              type="date"
              name="from-date"
              id="from-date"
              value={filterFromValue}
              onChange={handleFilterFromValueChange}
            />
            <br />
            <label htmlFor="to-date">To Date: </label>
            <input
              type="date"
              name="to-date"
              id="to-date"
              value={filterToValue}
              onChange={handleFilterToValueChange}
            />
          </div>
        )}
        <button type="button" onClick={handleFilterApply} className="btn-submit" disabled={loading}>
          {loading ? 'Applying...' : 'Apply Filter'}
        </button>
      </form>
    </div>
  );
}

export default SortersAndFilters;