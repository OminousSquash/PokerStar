import { BrowserRouter, Routes, Route } from "react-router-dom";
import Home from './components/Home'
import Game from "./components/Game";

export default function Router() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path='/' element={<Home />} />
                <Route path="/game/:id" element={<Game />}></Route>
            </Routes>
        </BrowserRouter>
    )
}