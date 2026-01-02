import { useParams } from "react-router-dom";
import CreatePlayerPopup from "./CreatePlayerPopup";
import { useEffect, useState } from "react";
import Button from "@mui/material/Button";
const Game = () => {
    const {id: tableId} = useParams()
    const [showPlayerPopup, setShowPlayerPopup] = useState(true)
    const [buyIn, setBuyIn] = useState(null)

    async function fetchBuyin() {
        try {
            const res = await fetch(`http://localhost:8080/table/${tableId}/buyIn`)
            const data = await res.json()
            setBuyIn(data)
        } catch(err) {
            console.log(err)
        }
    }

    useEffect(() => {
        fetchBuyin()
    },[tableId])

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
            await addPlayer({playerId: data.id})
        } catch(err) {
            alert(err)
        }
        setShowPlayerPopup(false)
    }

    const addPlayer = async ({playerId})  => {
        const obj = {
            "playerId": playerId,
            "tableId": tableId
        }
        try {
            await fetch("http://localhost:8080/table/addPlayer", {
                method: "POST",
                body: JSON.stringify(obj),
                headers: {
                    "Content-Type": "application/json"
                }
            })
            localStorage.setItem("playerId", playerId)
        } catch(err) {
            console.log(err)
        }
    }

    const removePlayer = async () => {
        try {
            const obj = {
                "playerId": localStorage.getItem("playerId"),
                "tableId": tableId
            }
            const res = await fetch("http://localhost:8080/table/removePlayer", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(obj)
            })
            console.log(await res.json())
        } catch (err) {
            console.log(err)
        }
    }

    return (
        <>
            <p>Welcome to game: {tableId}</p>
            {showPlayerPopup ? (
                <CreatePlayerPopup onSubmit={sendData}/>
            ) : 
                <Button color='error' onClick={removePlayer}>Leave game</Button>
            }
        </>
    )
}

export default Game;
