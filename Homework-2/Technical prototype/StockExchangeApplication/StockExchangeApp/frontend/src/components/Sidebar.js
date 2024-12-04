// src/components/Sidebar.js
import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Sidebar.css';  // Import the Sidebar styles
import '../styles/global.css'
const Sidebar = () => {
    return (
        <div className="sidebar">
            <h2>Stock Exchange</h2>
            <ul>
                <li><Link to="/dashboard">Dashboard</Link></li>
                <li><Link to="/historic-data">Historic Data</Link></li>
                <li><Link to="/favorites">Favorites</Link></li>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
            </ul>
        </div>
    );
};

export default Sidebar;
