import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

import React, { useEffect } from 'react';
import { useState } from 'react';

const Home = () => {

    const [gameRows, setGameRows] = useState([])

    const fetchGames = async () => {
        try {
            const res = await fetch("http://localhost:8080/table/all");
            const data = await res.json()
            setGameRows(data)
        } catch(err) {
            console.log("Failed to fetch", err)
        }
    }
    
    useEffect(() => {
        fetchGames();
    }, [])

    return (
        <TableContainer>
            <Table sx={{ minWidth: 650 }} aria-label="simple table">
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell align="right">Small Blind</TableCell>
                        <TableCell align="right">Big Blind</TableCell>
                        <TableCell align="right">Buy in</TableCell>
                        <TableCell align="right">Num Players</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {gameRows.map((row) => (
                        <TableRow key={row.id}>
                            <TableCell component="th" scope="row">
                                {row.name}
                            </TableCell>
                            <TableCell align="right">{row.smallBlind}</TableCell>
                            <TableCell align="right">{row.bigBlind}</TableCell>
                            <TableCell align="right">{row.startingAmt}</TableCell>
                            <TableCell align="right">{row.players.length}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    )    
}


export default Home;

