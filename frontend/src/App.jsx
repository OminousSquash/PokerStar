import { useState } from 'react'
import './App.css'

function App() {
  const [response, setResponse] = useState("")

  const sendPost = () => {
    fetch("http://localhost:8080/table/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        tableName: "My First Table",
        smallBlind: 10,
        bigBlind: 20,
        startingAmt: 1000
      })
    })
      .then(res => res.text())
      .then(data => setResponse(data))
      .catch(err => alert(err))
  }

  const sendGet = () => {
    fetch("http://localhost:8080/pokerTable/test",
      {
        method:"GET"
      })
      .then(res => res.text())
      .then(data => setResponse(data))
      .catch(err => alert(err))
  }
  
  return (
    <>
      <button onClick={sendPost}>
        Click Here
      </button>
      <p>
        {response}
      </p>
    </>
  )
}

export default App
