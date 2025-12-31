import { useParams } from "react-router-dom";
import CreatePlayerPopup from "./CreatePlayerPopup";
import { useEffect, useState } from "react";
const Game = () => {
    const {id} = useParams()
    const [showPlayerPopup, setShowPlayerPopup] = useState(true)
    const [buyIn, setBuyIn] = useState(null)

    async function fetchBuyin() {
        try {
            const res = await fetch(`http://localhost:8080/table/${id}/buyIn`)
            const data = await res.json()
            setBuyIn(data)
        } catch(err) {
            console.log(err)
        }
    }

    useEffect(() => {
        fetchBuyin()
    },[id])

    const sendData = async (username) => {
        console.log("Username: ", username)
        const playerObj = {
            "username": username,
            "money": buyIn
        }
        try {
            const res = await fetch("http://localhost:8080/player/create", {
                method: "POST",
                body: JSON.stringify(playerObj),
                headers: {
                    "Content-Type": "application/json"
                }
            })
            const data = await res.json()
            console.log(data)
        } catch(err) {
            alert(err)
        }
        setShowPlayerPopup(false)
    }

    return (
        <>
            <p>Welcome to game: {id}</p>
            {showPlayerPopup ? (
                <CreatePlayerPopup onSubmit={sendData}/>
            ) : null}
        </>
    )
}

export default Game;
