// components/HomePage.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      <div className="home-content">
        <h1 className="title">Coding Battle Room</h1>
        <p className="subtitle">Challenge yourself and compete with others!</p>
        
        <div className="button-container">
          <button 
            className="action-button create-btn"
            onClick={() => navigate('/create-room')}
          >
            Create Room
          </button>
          
          <button 
            className="action-button join-btn"
            onClick={() => navigate('/join-room')}
          >
            Join Room
          </button>
        </div>
      </div>
    </div>
  );
};

export default HomePage;