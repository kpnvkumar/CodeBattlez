// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import axios from 'axios';
// import './CreateRoom.css';

// const CreateRoom = () => {
//   const [name, setName] = useState('');
//   const [question, setQuestion] = useState('');
//   const [testCases, setTestCases] = useState([{ input: '', expectedOutput: '' }]);
//   const [loading, setLoading] = useState(false);
//   const navigate = useNavigate();

//   const addTestCase = () => {
//     setTestCases([...testCases, { input: '', expectedOutput: '' }]);
//   };

//   const removeTestCase = (index) => {
//     if (testCases.length > 1) {
//       setTestCases(testCases.filter((_, i) => i !== index));
//     }
//   };

//   const updateTestCase = (index, field, value) => {
//     const updated = testCases.map((tc, i) =>
//       i === index ? { ...tc, [field]: value } : tc
//     );
//     setTestCases(updated);
//   };

//   const createRoom = async () => {
//     if (!name.trim()) {
//       alert('Please enter a room name');
//       return;
//     }

//     if (!question.trim()) {
//       alert('Please enter a question');
//       return;
//     }

//     const validTestCases = testCases.filter(tc =>
//       tc.input.trim() && tc.expectedOutput.trim()
//     );

//     if (validTestCases.length === 0) {
//       alert('Please add at least one valid test case');
//       return;
//     }

//     setLoading(true);
//     try {
//       const response = await axios.post('http://localhost:8080/api/rooms/create', {
//         name,
//         owner: 'anonymous',
//         question,
//         testCases: validTestCases
//       });

//       const roomId = response.data.id;
//       alert(`Room created successfully! Room ID: ${roomId}`);
//       navigate(`/editor/${roomId}`);
//     } catch (error) {
//       console.error('Error creating room:', error);
//       alert('Failed to create room');
//     } finally {
//       setLoading(false);
//     }
//   };

//   return (
//     <div className="create-room-container">
//       <div className="create-room-content">
//         <h2>Create Coding Battle Room</h2>

//         {/* Room Name */}
//         <div className="form-section">
//           <label htmlFor="roomName">Room Name:</label>
//           <input
//             id="roomName"
//             type="text"
//             value={name}
//             onChange={(e) => setName(e.target.value)}
//             placeholder="Enter room name..."
//           />
//         </div>

//         {/* Question */}
//         <div className="form-section">
//           <label htmlFor="question">Question:</label>
//           <textarea
//             id="question"
//             value={question}
//             onChange={(e) => setQuestion(e.target.value)}
//             placeholder="Enter your coding problem statement..."
//             rows={6}
//           />
//         </div>

//         {/* Test Cases */}
//         <div className="form-section">
//           <h3>Test Cases:</h3>
//           {testCases.map((tc, index) => (
//             <div key={index} className="test-case">
//               <div className="test-case-header">
//                 <h4>Test Case {index + 1}</h4>
//                 {testCases.length > 1 && (
//                   <button
//                     className="remove-btn"
//                     onClick={() => removeTestCase(index)}
//                   >
//                     Remove
//                   </button>
//                 )}
//               </div>

//               <div className="test-case-inputs">
//                 <div className="input-group">
//                   <label>Input:</label>
//                   <textarea
//                     value={tc.input}
//                     onChange={(e) => updateTestCase(index, 'input', e.target.value)}
//                     placeholder="Enter test input..."
//                     rows={3}
//                   />
//                 </div>

//                 <div className="input-group">
//                   <label>Expected Output:</label>
//                   <textarea
//                     value={tc.expectedOutput}
//                     onChange={(e) => updateTestCase(index, 'expectedOutput', e.target.value)}
//                     placeholder="Enter expected output..."
//                     rows={3}
//                   />
//                 </div>
//               </div>
//             </div>
//           ))}

//           <button className="add-test-case-btn" onClick={addTestCase}>
//             Add Test Case
//           </button>
//         </div>

//         {/* Action Buttons */}
//         <div className="form-actions">
//           <button
//             className="back-btn"
//             onClick={() => navigate('/')}
//           >
//             Back
//           </button>
//           <button
//             className="create-btn"
//             onClick={createRoom}
//             disabled={loading}
//           >
//             {loading ? 'Creating...' : 'Create Room'}
//           </button>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default CreateRoom;
// frontend/src/components/CreateRoom.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './CreateRoom.css';

const CreateRoom = () => {
    const [name, setName] = useState('');
    const [question, setQuestion] = useState('');
    const [testCases, setTestCases] = useState([{ input: '', expectedOutput: '' }]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    // ... (addTestCase, removeTestCase, updateTestCase functions remain the same)
    const addTestCase = () => {
      setTestCases([...testCases, { input: '', expectedOutput: '' }]);
    };
  
    const removeTestCase = (index) => {
      if (testCases.length > 1) {
        setTestCases(testCases.filter((_, i) => i !== index));
      }
    };
  
    const updateTestCase = (index, field, value) => {
      const updated = testCases.map((tc, i) =>
        i === index ? { ...tc, [field]: value } : tc
      );
      setTestCases(updated);
    };

    const createRoom = async () => {
        // ... (validation logic remains the same)
        if (name.trim().length < 3) {
            alert('Please enter a room name with at least 3 characters.');
            return;
        }
    
        if (question.trim().length < 10) {
            alert('Please enter a question with at least 10 characters.');
            return;
        }
    
        const validTestCases = testCases.filter(tc =>
            tc.input.trim() && tc.expectedOutput.trim()
        );
    
        if (validTestCases.length === 0) {
            alert('Please add at least one valid test case with non-empty input and output.');
            return;
        }

        setLoading(true);
        try {
            const response = await axios.post('/api/rooms/create', {
                name,
                owner: 'anonymous',
                question,
                testCases: validTestCases
            });

            // UPDATED: Use the 'shortId' from the response for navigation and display
            const roomId = response.data.shortId; 
            alert(`Room created successfully! Room ID: ${roomId}`);
            navigate(`/`);

        } catch (error) {
            // ... (error handling logic remains the same)
            console.error('Error creating room:', error);
            if (error.response && error.response.data) {
                const errorData = error.response.data;
                if (errorData.details) {
                    const errorMessages = Object.values(errorData.details).join('\n');
                    alert(`Failed to create room:\n${errorMessages}`);
                } else {
                    alert(`Failed to create room: ${errorData.message || 'An unknown error occurred.'}`);
                }
            } else {
                alert('Failed to create room. Please check your connection and try again.');
            }
        } finally {
            setLoading(false);
        }
    };

    // ... (The JSX for the return statement remains exactly the same)
    return (
      <div className="create-room-container">
        <div className="create-room-content">
          <h2>Create Coding Battle Room</h2>
  
          {/* Room Name */}
          <div className="form-section">
            <label htmlFor="roomName">Room Name:</label>
            <input
              id="roomName"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter room name (at least 3 characters)..."
            />
          </div>
  
          {/* Question */}
          <div className="form-section">
            <label htmlFor="question">Question:</label>
            <textarea
              id="question"
              value={question}
              onChange={(e) => setQuestion(e.target.value)}
              placeholder="Enter your coding problem statement (at least 10 characters)..."
              rows={6}
            />
          </div>
  
          {/* Test Cases */}
          <div className="form-section">
            <h3>Test Cases:</h3>
            {testCases.map((tc, index) => (
              <div key={index} className="test-case">
                <div className="test-case-header">
                  <h4>Test Case {index + 1}</h4>
                  {testCases.length > 1 && (
                    <button
                      className="remove-btn"
                      onClick={() => removeTestCase(index)}
                    >
                      Remove
                    </button>
                  )}
                </div>
  
                <div className="test-case-inputs">
                  <div className="input-group">
                    <label>Input:</label>
                    <textarea
                      value={tc.input}
                      onChange={(e) => updateTestCase(index, 'input', e.target.value)}
                      placeholder="Enter test input..."
                      rows={3}
                    />
                  </div>
  
                  <div className="input-group">
                    <label>Expected Output:</label>
                    <textarea
                      value={tc.expectedOutput}
                      onChange={(e) => updateTestCase(index, 'expectedOutput', e.target.value)}
                      placeholder="Enter expected output..."
                      rows={3}
                    />
                  </div>
                </div>
              </div>
            ))}
  
            <button className="add-test-case-btn" onClick={addTestCase}>
              Add Test Case
            </button>
          </div>
  
          {/* Action Buttons */}
          <div className="form-actions">
            <button
              className="back-btn"
              onClick={() => navigate('/')}
            >
              Back
            </button>
            <button
              className="create-btn"
              onClick={createRoom}
              disabled={loading}
            >
              {loading ? 'Creating...' : 'Create Room'}
            </button>
          </div>
        </div>
      </div>
    );
};

export default CreateRoom;