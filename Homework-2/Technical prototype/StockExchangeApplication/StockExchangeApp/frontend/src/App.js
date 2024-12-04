// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginRegister from './pages/LoginRegister';
import Dashboard from './pages/Dashboard';
import Historicdata from './pages/Historicdata';
import Favorites from './pages/Favorites';
import Sidebar from './components/Sidebar'; // Import Sidebar

function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<LoginRegister />} />

          <Route
              path="/dashboard"
              element={
                <div style={{ display: 'flex' }}>
                  <Sidebar />
                  <div style={{ marginLeft: '250px', padding: '20px', flex: 1 }}>
                    <Dashboard />
                  </div>
                </div>
              }
          />
          <Route
              path="/historic-data"
              element={
                <div style={{ display: 'flex' }}>
                  <Sidebar />
                  <div style={{ marginLeft: '250px', padding: '20px', flex: 1 }}>
                    <Historicdata />
                  </div>
                </div>
              }
          />
          <Route
              path="/favorites"
              element={
                <div style={{ display: 'flex' }}>
                  <Sidebar />
                  <div style={{ marginLeft: '250px', padding: '20px', flex: 1 }}>
                    <Favorites />
                  </div>
                </div>
              }
          />
        </Routes>
      </Router>
  );
}

export default App;
