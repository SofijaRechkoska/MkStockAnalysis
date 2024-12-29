import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';  // Import the Sidebar styles
import '../styles/global.css'
import axios from "axios";

const Sidebar = () => {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await axios.post("http://localhost:5432/api/auth/logout", {}, { withCredentials: true });
            alert("You have been logged out.");
            localStorage.removeItem("user");
            navigate("/login");
        } catch (error) {
            console.error("Error during logout:", error);
            alert("Logout failed. Please try again.");
        }
    };

    const handleUpdateData = () => {
        setLoading(true);
        axios.get("http://localhost:5432/python/run-script")
            .then((response) => {
                alert("Stock data updated successfully!");
                console.log("Python Script Output:", response.data);
                window.location.reload();
            })
            .catch((error) => {
                alert("Failed to update stock data!");
                console.error("Error running the Python script:", error);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    return (
        <div className="sidebar">
            <h2 className="headerSide">Stock Analysis<img src={`${process.env.PUBLIC_URL}/logo.png`} className="logo"></img></h2>
            <ul>
                <li><Link to="/dashboard">Dashboard</Link></li>
                <li><Link to="/historic-data">Historic Data</Link></li>
                <li><Link to="/favorites">Favorites</Link></li>
                <li><Link to="/predictions">Predictions</Link></li>
                <li>
                    <button className="logoutButton" onClick={handleLogout}>
                        Logout
                    </button>
                </li>
                <li>
                    <button
                        className="updateDataButton"
                        onClick={handleUpdateData}
                        disabled={loading}
                    >
                        {loading ? "Updating Stock Data..." : "Update Stock Data"}
                    </button>
                </li>
                {loading && (
                    <div className="loadingIndicator">

                        <span>Updating data... Please wait.</span>
                        <div className="spinner"></div>
                    </div>
                )}
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br />
                <br />
                <br />
                <br />
                <br />
                <br />
                <br />
                <br />
            </ul>
        </div>
    );
};

export default Sidebar;
