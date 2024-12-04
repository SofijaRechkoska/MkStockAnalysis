// src/pages/HistoricData.js
import React, { useState } from 'react';
import DataTable from 'react-data-table-component';
import axios from 'axios';
import '../styles/Historicdata.css'; // Import the CSS for styling
import '../styles/global.css'
import avatar from "./avatar.png";


const HistoricData = () => {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [marketName, setMarketName] = useState('');
    const [marketList, setMarketList] = useState([]);

    const handleSearch = async () => {
        try {
            const response = await axios.get(
                `http://localhost:5432/api/markets`, // Adjust the API URL as needed
                {
                    params: {
                        startDate,
                        endDate,
                        marketName
                    }
                }
            );
            console.log(response)
            setMarketList(response.data);
        } catch (error) {
            console.error('Error fetching historic data:', error);
        }
    };
    return(
        <div className='container mt-5'>
            <input className="searchInput" type="text" value="Search ..."/>
            <img className="avatar" src={avatar} alt="Avatar"/>
            <span className="avatar username">Courtney Henry</span>
            <h1>Historic Data</h1>
            <div className="input-container">
                <div className="input-group">
                    <label>Date from</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        className="input-field"
                    />
                </div>
                <div className="input-group">
                    <label>Date to</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        className="input-field"
                    />
                </div>
                <div className="input-group">
                    <label>Code name</label>
                    <input
                        type="text"
                        value={marketName}
                        onChange={(e) => setMarketName(e.target.value)}
                        placeholder="Enter name"
                        className="input-field"
                    />
                </div>
                <button onClick={handleSearch} className="search-button">
                    Search
                </button>
            </div>
            <DataTable
                className="dataTable"
                columns={columns}
                data={data}
                selectableRows
                fixedHeader
                pagination
                paginationClass="custom-pagination"  // Add your custom class here
            >
            </DataTable>

        </div>
    )
};

export default HistoricData;
