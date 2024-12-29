import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import 'chartjs-plugin-zoom';
import { Line } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import '../styles/Dashboard.css';
import '../styles/global.css';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);

const Dashboard = () => {
    const [stockData, setStockData] = useState(null);
    const [selectedCode, setSelectedCode] = useState(null);
    const [favoritesCode, setFavoritesCode] = useState(null);
    const [chartData, setChartData] = useState(null);
    const [userData, setUserData] = useState(null);
    const location = useLocation();
    const { name, surname } = location.state || {};
    const UserIcon = () => (
        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="7" r="4" />
            <path d="M12 12c-5 0-8 3-8 6v2h16v-2c0-3-3-6-8-6z" />
        </svg>
    );

    useEffect(() => {


            axios.get(`http://localhost:5432/api/auth/current-user`,{withCredentials: true})
                .then(response => {
                    const userData = response.data;
                    setUserData(userData);
                    localStorage.setItem("user", JSON.stringify(userData));
                    console.log("Fetched and stored in localStorage:", userData); // Debug log
                })
                .catch(error => {
                    console.error("Error fetching user details:", error);
                });
    }, []);


    useEffect(() => {
        // Fetch stock data
        axios.get("http://localhost:5432/api/stock-data",{ withCredentials: true })
            .then(response => {
                console.log("API Response:", response.data);
                setStockData(response.data);
                setSelectedCode(response.data.codes[0]);
                setFavoritesCode(response.data.code);
                console.log(response.data.code);
            })
            .catch(error => {
                console.error("There was an error fetching the stock data:", error);
            });
    }, []);

    useEffect(() => {
        if (selectedCode) {
            axios.get(`http://localhost:5432/api/stock-data/${selectedCode}`)
                .then(response => {
                    console.log("API Response:", response.data);
                    const data = response.data;

                    if (data && data.length > 0) {
                        const maxPrices = data.map(item => parseFloat(item.maxPrice));
                        const dates = data.map(item => item.date);
                        setChartData({
                            labels: dates,
                            datasets: [
                                {
                                    label: "Max price",
                                    data: maxPrices,
                                    borderColor: 'rgba(75,192,192,1)',
                                    fill: false,
                                    borderWidth:1
                                }
                            ],
                            options: {
                                responsive: true,
                                scales: {
                                    x: {
                                        reverse: true,
                                    },
                                },
                            }
                        });
                    } else {
                        console.error("No data found for the given code.");
                    }
                })
                .catch(error => {
                    console.error("There was an error fetching the chart data:", error);
                });
        }
    }, [selectedCode]);

    if (!stockData || !chartData) {
        return <div>Loading...</div>;
    }

    return (
        <div className="dashboardBody">
            <div id="avatar1">
                <UserIcon/>
            </div>
            <span className="username">
                {userData.name} {userData.surname}
            </span>
            <h1 className="dashboardHeader">Dashboard</h1>
            <h2 className="stock-card">Today's top</h2>
            {stockData.stocks ? (
                <ul>
                    {stockData.stocks.map((stock, index) => (
                        <li className="top-stocks" key={index}>
                            <p className="indexCard">{index + 1}</p>
                            <span className="code">
                                {stock.code}
                            </span>

                            <br/>
                            <span className="maxPrice">
                               Max Price: {stock.maxPrice}
                            </span>
                            <span className={`percent ${stock.changePercent < 0 ? 'negative' : 'positive'}`}>
                                {stock.changePercent}%
                            </span>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No stocks data available</p>
            )}

            <br/><br/><br/><br/><br/><br/><br/>
            <section className="favourites-section">
                <h2 className="favoritesHeader">Favorites</h2>
                <h4 className="favoritesAverage">Average Price</h4>
                <ul className="favourites-list">
                    {stockData.favorites.map((fav, index) => (
                        <li key={index} className="favourite-item">
                            <p className="indexCard indexCardFav">{index + 1}</p>
                            <span className="code">{fav.code}</span>
                            <span className="favAveragePrice">{fav.averagePrice}</span>
                        </li>
                    ))}
                </ul>
            </section>

            <section className="chart-section">
                <h2 className="chartHeader">Chart</h2>
                <select
                    className="charts"
                    value={selectedCode}
                    onChange={(e) => setSelectedCode(e.target.value)}
                >
                    {stockData.codes.map((cod, index) => (
                        <option key={index} value={cod}>{cod}</option>
                    ))}
                </select>
                <div className="chart-container">
                    <Line
                        data={chartData}
                        options={{
                            responsive: true,
                            scales: {
                                x: {
                                    reverse: true
                                }
                            },
                            plugins: {
                                legend: {
                                    display: false
                                }
                            }
                        }}
                        width={600}
                        height={600}
                    />
                </div>
            </section>
        </div>
    );
};

export default Dashboard;
