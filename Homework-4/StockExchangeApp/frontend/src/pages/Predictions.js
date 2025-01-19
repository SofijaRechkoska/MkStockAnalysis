import React, { useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
import axios from "axios";
import '../styles/predictions.css';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const Predictions = () => {
    const [stocks, setStocks] = useState([]);
    const [selectedStock, setSelectedStock] = useState('');
    const [timeFrame, setTimeFrame] = useState('');
    const [chartData, setChartData] = useState(null);
    const [riseOrFall, setRiseOrFall] = useState(null);

    useEffect(() => {
        axios.get(`http://localhost:5432/api/stock-codes`, { withCredentials: true })
            .then(response => {
                const codesData = response.data.code;
                if (Array.isArray(codesData)) {
                    setStocks(codesData);
                }
            })
            .catch(error => {
                console.error("Error fetching stock codes:", error);
            });
    }, []);

    const handleFetchChartData = () => {
        if (!selectedStock || !timeFrame) return;

        fetch(`http://localhost:5432/api/predictions/signal-percentages?stockCode=${selectedStock}&timeFrame=${timeFrame}`)
            .then(response => response.json())
            .then(data => {
                setChartData({
                    labels: ['Buy', 'Sell', 'Hold'],
                    datasets: [{
                        label: 'Prediction Percentages',
                        data: [data.buy, data.sell, data.hold],
                        backgroundColor: ['rgba(0,255,105,0.6)', 'rgba(250,27,69,0.6)', 'rgba(73,228,255,0.6)'],
                    }],
                });
            })
            .catch(error => console.error('Error fetching chart data:', error));
    };

    const handleFetchRiseOrFall = () => {
        if (!selectedStock) return;

        fetch(`http://localhost:5432/api/predictions/news-prediction?stockCode=${selectedStock}`)
            .then(response => response.json())
            .then(data => {
                setRiseOrFall(data.recommendation);
            })
            .catch(error => console.error('Error fetching rise or fall prediction:', error));
    };

    return (
        <div className="predictions-container">
            <select className="stock-select" onChange={(e) => setSelectedStock(e.target.value)} value={selectedStock}>
                <option value="">Select Stock</option>
                {stocks.map(stock => (
                    <option key={stock} value={stock}>{stock}</option>
                ))}
            </select>

            <div className="timeframe-buttons">
                {['daily', 'weekly', 'monthly'].map((frame) => (
                    <button
                        key={frame}
                        className={`timeframe-button ${timeFrame === frame ? 'selected' : ''}`}
                        onClick={() => setTimeFrame(frame)}
                    >
                        {frame.charAt(0).toUpperCase() + frame.slice(1)}
                    </button>
                ))}
            </div>

            <div className="action-buttons">
                <button className="fetch-button" onClick={handleFetchChartData}>
                    Get Signal Percentages
                </button>
                <button className="fetch-button2" onClick={handleFetchRiseOrFall}>
                    Get Rise or Fall Prediction
                </button>
            </div>

            {chartData && (
                <div className="chart-container2">
                    <Bar data={chartData} />
                </div>
            )}

            {riseOrFall && (
                <div
                    className={`prediction-result ${
                        riseOrFall.includes('rise')
                            ? 'rise'
                            : riseOrFall.includes('fall')
                                ? 'fall'
                                : 'neutral'
                    }`}
                >
                    <div className="icon-container">
                        {riseOrFall.includes('rise')
                            ? '📈'
                            : riseOrFall.includes('fall')
                                ? '📉'
                                : '⚖️'}
                    </div>
                    <h3>
                        {riseOrFall.includes('rise')
                            ? 'Stock Will Rise'
                            : riseOrFall.includes('fall')
                                ? 'Stock Will Fall'
                                : 'Stock Is Neutral'}
                    </h3>
                    <p>
                        {riseOrFall.includes('rise')
                            ? 'Positive momentum detected. Expect an upward trend.'
                            : riseOrFall.includes('fall')
                                ? 'Negative signals detected. Expect a downward trend.'
                                : 'Balanced sentiment detected. No clear trend identified.'}
                    </p>
                </div>
            )}



        </div>
    );
};

export default Predictions;
