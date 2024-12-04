// src/pages/LoginRegister.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginRegister.css'
// import '../styles/global.css'

const LoginRegister = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLogin, setIsLogin] = useState(true); // Toggle between login and register

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        // Perform the login/register API call here
        // For now, simulate successful login
        navigate('/dashboard');
    };

    return (
        <div className="loginContainer">
            <h2 className="loginPageHeader">{isLogin ? 'Login' : 'Register'}</h2>
            <p className="login-description">
                Please enter your email and password
            </p>
            <form className="loginForm" onSubmit={handleSubmit}>
                <input className="emailForm"
                       type="email"
                       value={email}
                       onChange={(e) => setEmail(e.target.value)}
                       placeholder="Email"
                       required
                /><br/>
                <input className="passwordForm"
                       type="password"
                       value={password}
                       onChange={(e) => setPassword(e.target.value)}
                       placeholder="Password"
                       required
                /><br/>
                <button className="loginButton" type="submit">{isLogin ? 'Login' : 'Register'}</button>
            </form>
            <button className="registerButton" onClick={() => setIsLogin(!isLogin)}>
                {isLogin ? 'Register' : 'Switch to Login'}
            </button>
        </div>
    );
};

export default LoginRegister;
