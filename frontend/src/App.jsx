import { useState } from 'react'
import './App.css'

function App() {
  const [response, setResponse] = useState("")

  const sendGet = () => {
    fetch("http://localhost:8080/api/hello", {
      method: "GET",
    })
      .then(res => res.text())
      .then(data => setResponse(data))
      .catch(err => alert(err))
  }
  
  return (
    <>
      <button onClick={sendGet}>
        Click Here
      </button>
      <p>
        {response}
      </p>
    </>
  )
}

export default App
