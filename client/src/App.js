import './App.css';
import Login from './components/Login'
import Users from './components/Users'
import {Route, Routes} from "react-router-dom"
import Home from "./components/Home";
import Dashboard from "./components/Dashboard";

function App() {
    return (
        <>
            <Routes>
                <Route path="/" element={<Home/>}>
                    <Route path="dashboard" element={<Users/>}/>
                    <Route path="users" element={<Users/>}/>
                </Route>
                <Route path="/login" element={<Login/>}/>

            </Routes>
        </>


    );
}

export default App;
