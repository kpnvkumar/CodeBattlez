import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from 'axios';

const EditorPage = () => {
    const { roomId } = useParams();
    const [room, setRoom] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [code, setCode] = useState("// Write your code here");
    const [language, setLanguage] = useState("java");

    useEffect(() => {
        if (roomId) {
            axios.get(`/api/rooms/${roomId}`)
                .then(response => {
                    setRoom(response.data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error("Error fetching room data:", err);
                    setError("Could not load room data. Please check the Room ID or try again later.");
                    setLoading(false);
                });
        }
    }, [roomId]);

    const submit = async () => {
        // This function is for final submission against test cases
        alert("Submit functionality is not yet fully implemented.");
    };
    
    if (loading) return <div>Loading room...</div>;
    if (error) return <div>Error: {error}</div>;
    if (!room) return <div>Room not found.</div>;

    return (
        <div>
            <h2>Room: {room.name} (ID: {room.shortId})</h2>
            <h3>Question:</h3>
            <p>{room.question}</p>
            <h3>Test Cases:</h3>
            <pre>{JSON.stringify(room.testCases, null, 2)}</pre>
            <hr />
            <h3>Editor</h3>
            <select value={language} onChange={e => setLanguage(e.target.value)}>
                <option value="c">C</option>
                <option value="cpp">C++</option>
                <option value="java">Java</option>
                <option value="python">Python</option>
                <option value="javascript">JavaScript</option>
            </select>
            <textarea style={{ width: "100%", height: 300 }} value={code} onChange={e => setCode(e.target.value)} />
            <button onClick={submit}>Submit Code</button>
        </div>
    );
};

export default EditorPage;
