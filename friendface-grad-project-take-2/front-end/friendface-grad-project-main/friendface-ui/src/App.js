
import CreatePost from "./Components/CreatePost";
import Header from "./Components/Header";
import Posts from "./Components/Posts";
import "./index.css"


function App() {
  
  return (

    <div className='container'>
      <Header />
      <CreatePost/>
      <div className="posts-section">
        <div>
          <Posts/>
        </div>
        <div>
        </div>
      </div>
    </div>

  );
}

export default App;
