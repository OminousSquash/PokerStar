import React, { useState } from "react";
import { Field } from '@base-ui/react/field';
import { Fieldset } from '@base-ui/react/fieldset';
import Button from "@mui/material/Button";

const CreatePlayerPopup = ({ onSubmit }) => {

    const [username, setUsername] = useState("");

    return (
        <>
            <Fieldset.Root>
                <Fieldset.Legend>Create Player</Fieldset.Legend>
                <Field.Root>
                    <Field.Label>Username</Field.Label>
                    <Field.Control 
                        placeholder="Enter username" 
                        value={username} 
                        onChange={(e) => setUsername(e.target.value)}/>
                </Field.Root>
            </Fieldset.Root>
            <Button variant="contained" color="success" onClick={() => {onSubmit(username)}}>Submit</Button>
        </>
    ) 
}

export default CreatePlayerPopup;