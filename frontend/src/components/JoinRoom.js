// // components/JoinRoom.js
// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
// import './JoinRoom.css';

// const JoinRoom = () => {
//   const [roomId, setRoomId] = useState('');
//   const [loading, setLoading] = useState(false);
//   const navigate = useNavigate();

//   const joinRoom = async () => {
//     if (!roomId.trim()) {
//       alert('Please enter a room ID');
//       return;
//     }

//     setLoading(true);
//     try {
//       const response = await axios.get(`http://localhost:8080/api/rooms/${roomId}/exists`);
      
//       if (response.data) {
//         navigate(`/editor/${roomId}`);
//       } else {
//         alert('Room not found! Please check the room ID.');
//       }
//     } catch (error) {
//       console.error('Error joining room:', error);
//       alert('Failed to join room');
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <div className="join-room-container">
//       <div className="join-room-content">
//         <h2>Join Coding Battle Room</h2>
        
//         <div className="form-section">
//           <label htmlFor="roomId">Room ID:</label>
//           <input
//             type="text"
//             id="roomId"
//             value={roomId}
//             onChange={(e) => setRoomId(e.target.value)}
//             placeholder="Enter room ID..."
//             onKeyPress={(e) => e.key === 'Enter' && joinRoom()}
//           />
//         </div>

//         <div className="form-actions">
//           <button 
//             className="back-btn" 
//             onClick={() => navigate('/')}
//           >
//             Back
//           </button>
//           <button 
//             className="join-btn" 
//             onClick={joinRoom}
//             disabled={loading}
//           >
//             {loading ? 'Joining...' : 'Join Room'}
//           </button>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default JoinRoom;
// frontend/src/components/JoinRoom.js
import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate

export default function JoinRoom() {
    const [roomId, setRoomId] = useState("");
    const navigate = useNavigate(); // Initialize navigate

    const join = async () => {
        if (!roomId.trim()) {
            alert("Please enter a Room ID.");
            return;
        }
        try {
            // UPDATED: Check for the room using the /exists endpoint
            const res = await fetch(`/api/rooms/${roomId.trim()}/exists`);
            if (!res.ok) {
                throw new Error("Network response was not ok.");
            }
            const exists = await res.json();
            if (exists) {
                // Use navigate for client-side routing
                navigate(`/editor/${roomId.trim()}`); 
            } else {
                alert("Room not found. Please check the ID and try again.");
            }
        } catch (error) {
            console.error("Failed to check room existence:", error);
            alert("Could not connect to the server. Please try again later.");
        }
    };

    return (
        <div>
            <h2>Join Room</h2>
            <input
                value={roomId}
                onChange={e => setRoomId(e.target.value)}
                placeholder="Enter Room ID"
            />
            <button onClick={join}>Join</button>
        </div>
    );
}
