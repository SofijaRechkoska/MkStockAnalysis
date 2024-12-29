import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/LoginRegister.css';

const Register = () => {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [name, setName] = useState('');
    const [surname, setSurname] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleGoBackToLogin = () => {
        navigate("/login");
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        if (password !== repeatPassword) {
            setErrorMessage("Passwords do not match");
            return;
        }

        try {
            const response = await fetch('http://localhost:5432/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, email, password, repeatPassword, name, surname }),
            });

            if (response.ok) {
                navigate('/login');
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Failed to register');
            }
        } catch (error) {
            setErrorMessage('An error occurred. Please try again later.');
        }
    };

    return (
        <div className="loginContainer">
            <h2 className="loginPageHeader">Register</h2>
            <p className="login-description">
                Please enter your email, username, and password.
            </p>
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <form className="loginForm" onSubmit={handleSubmit} method="post"
                  action="http://localhost:5432/api/auth/register">
                <input
                    className="emailForm"
                    name="email"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                    required
                />
                <br/>
                <input
                    className="emailForm"
                    name="name"
                    type="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Name"
                    required
                />
                <br/>
                <input
                    className="emailForm"
                    name="surname"
                    type="surname"
                    value={surname}
                    onChange={(e) => setSurname(e.target.value)}
                    placeholder="Surname"
                    required
                />
                <br/>
                <input
                    className="passwordForm"
                    name="username"
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="Username"
                    required
                />
                <br/>
                <input
                    className="passwordForm"
                    name="password"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                />
                <br/>
                <input
                    className="passwordForm"
                    name="repeatPassword"
                    type="password"
                    value={repeatPassword}
                    onChange={(e) => setRepeatPassword(e.target.value)}
                    placeholder="Repeat Password"
                    required
                />
                <br/>
                <button className="loginButton" type="submit">
                    Register
                </button>
                <button className="loginButton" type="submit" onClick={handleGoBackToLogin}>
                    Go back to Login page
                </button>
            </form>
        </div>
    );
};

export default Register;
