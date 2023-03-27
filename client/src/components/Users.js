import {useEffect, useState} from "react";
import axios from "axios";
import Container from "@mui/material/Container";


export default function Login() {
    const [users, setUsers] = useState();

    useEffect(() => {
        let isMounted = true
        const controller = new AbortController()

        const getUsers = async () => {
            try {
                const response = await axios.get("/users", {
                    signal: controller.signal
                });
                isMounted && setUsers(response.data)
            } catch (err) {
                console.error(err)
            }

        }
        getUsers();

        return () => {
            isMounted = false;
            controller.abort()
        }
    },[])
    return(
            <Container>
                Test
            </Container>
    )
}