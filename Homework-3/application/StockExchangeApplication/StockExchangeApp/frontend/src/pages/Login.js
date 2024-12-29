import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginRegister.css';
import axios from "axios";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [userDetails, setUserDetails] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        try {
            const response = await axios.post('http://localhost:5432/api/auth/login', {
                username,
                password
            });

            if (response.status === 200) {
                const userData = response.data;
                const { name, surname } = userData;

                setUserDetails({ name, surname });
                navigate('/dashboard', { state: { name, surname } });
            } else {
                setErrorMessage('Invalid credentials');
            }
        } catch (error) {
            setErrorMessage('An error occurred while logging in. Please try again.');
        }
    };

    const handleRegisterRedirect = () => {
        navigate('/register');
    };

    return (
        <div className="loginContainer">
            <h2 className="loginPageHeader">Login</h2>
            <p className="login-description">Please enter your username and password</p>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <form className="loginForm" onSubmit={handleSubmit}>
                <input
                    className="emailForm"
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                    required
                /><br />
                <input
                    className="passwordForm"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                /><br />
                <button className="loginButton" type="submit">Login</button>
            </form>
            <button className="registerButton" onClick={handleRegisterRedirect}>
                Go to Register Page
            </button>
        </div>
    );
};

export default Login;