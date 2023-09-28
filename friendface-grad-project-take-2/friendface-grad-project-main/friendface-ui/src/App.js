
import CreatePost from "./Components/CreatePost";
import Header from "./Components/Header";
import Posts from "./Components/Posts";
import "./index.css"


function App() {
  
  return (

    <div className='container'>
      <Header />
      <CreatePost/>
      <Posts/>
    </div>

  );
}

export default App;
