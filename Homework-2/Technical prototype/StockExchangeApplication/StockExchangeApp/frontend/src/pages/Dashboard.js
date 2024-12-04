import React, { useEffect, useState } from 'react';
import axios from 'axios';
import grafik from './grafik.png'
import grafik2 from './Vector_10_1.jpg'
import avatar from './avatar.png'
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

    useEffect(() => {
        // Make a GET request to Spring Boot backend
        axios.get("http://localhost:5432/api/stock-data")
            .then(response => {
                console.log("API Response:", response.data); // Log the entire response
                setStockData(response.data); // Assuming response contains stocks and favorites
            })
            .catch(error => {
                console.error("There was an error fetching the stock data:", error);
            });
    }, []);

    if (!stockData) {
        return <div>Loading...</div>; // Show a loading message while data is being fetched
    }

    // Render stock and favorite data
    return (
        <div className="dashboardBody">
            <input className="searchInput" type="text" value="Search ..."/>
            <img className="avatar" src={avatar}/>
            <span className="avatar username">Courtney Henry</span>
            <h1 className="dashboardHeader">Dashboard</h1>
            <h2 className="stock-card">Today's top</h2>
            {stockData.stocks ? (
                <ul>
                    {stockData.stocks.map((stock, index) => (

                        <li className="top-stocks" key={index}>
                            <p className="indexCard">{index + 1}</p>
                            <img className="graphImage" src={grafik2} alt=""/>
                            {stock.symbol}
                            <span className={`percent ${stock.changePercent < 0 ? 'negative' : 'positive'}`}>
                                {stock.changePercent}%
                             </span>

                        </li>
                    ))}
                </ul>
            ) : (
                <p>No stocks data available</p>
            )}

            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <section className="favourites-section">
                <h2 className="favoritesHeader">Favorites</h2>
                <h4 className="favoritesAverage">Average Price</h4>
                <ul className="favourites-list">
                    {stockData.favorites.map((fav, index) => (
                        <li key={index} className="favourite-item">
                            <p className="indexCard indexCardFav">{index + 1}</p>
                            <span className="symbol">{fav.symbol}</span>
                            <span>{fav.averagePrice}</span>
                        </li>
                    ))}
                </ul>
            </section>
            <img className="graphImage2" src={grafik}/>
            <section className="chart-section">
                <h2 className="chartHeader">Chart</h2>
                <select className="charts">

                    {stockData.codes.map((cod, index) => (
                        <option className="optionCode">{cod}</option>
                    ))}

                </select>
            </section>


        </div>
    );
};

export default Dashboard;
