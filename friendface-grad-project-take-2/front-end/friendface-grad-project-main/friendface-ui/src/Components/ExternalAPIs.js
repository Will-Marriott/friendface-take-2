function ExternalAPIs() {
    const [randomFact, setRandomFact] = useState('');
   
   const getExternalAPI = async () => {
    fetch('https://uselessfacts/api/v2/facts/random?language=en', {
        method: 'GET',
        headers: {
          'Content-type': 'application/json',
        },
        body: JSON.stringify(post),
      })
    set
  }


}