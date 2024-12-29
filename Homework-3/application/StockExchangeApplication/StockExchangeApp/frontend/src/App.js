// src/App.js
import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Historicdata from './pages/Historicdata';
import Favorites from './pages/Favorites';
import Sidebar from './components/Sidebar';
import Register from "./pages/Register";
import Login from "./pages/Login";
import Predictions from "./pages/Predictions"
function App() {
  return (
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

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
            <Route
                path="/predictions"
                element={
                    <div style={{ display: 'flex' }}>
                        <Sidebar />
                        <div style={{ marginLeft: '250px', padding: '20px', flex: 1 }}>
                            <Predictions />
                        </div>
                    </div>
                }
            />
        </Routes>
      </Router>
  );
}

export default App;
