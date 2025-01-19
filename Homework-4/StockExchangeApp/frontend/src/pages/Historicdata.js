import React, { useEffect, useState } from 'react';
import DataTable from 'react-data-table-component';
import axios from 'axios';
import '../styles/Historicdata.css';
import '../styles/global.css';


const HistoricData = () => {
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [marketName, setMarketName] = useState('');
    const [marketList, setMarketList] = useState([]);
    const [filteredMarketList, setFilteredMarketList] = useState([]);
    const [userData, setUserData] = useState(null);

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
            })
            .catch(error => {
                console.error("Error fetching user details:", error);
            });
    }, []);

    const handleSearch = async () => {
        try {
            const response = await axios.get('http://localhost:5432/api/markets', {
                params: {
                    marketName
                }
            });

            setMarketList(response.data);
            filterDataByDateAndCode(response.data);
        } catch (error) {
            console.error('Error fetching market data:', error);
        }
    };

    const filterDataByDateAndCode = (data) => {
        const start = startDate ? new Date(startDate) : new Date('1900-01-01');
        const end = endDate ? new Date(endDate) : new Date();


        const filteredData = data.filter(row => {
            const rowDate = new Date(row.date);
            const matchesDate = rowDate >= start && rowDate <= end;
            const matchesCode = marketName ? row.code.toLowerCase().includes(marketName.toLowerCase()) : true; // Проверка дали кодот се совпаѓа

            return matchesDate && matchesCode;
        });

        setFilteredMarketList(filteredData);
    };

    const handleStartDateChange = (e) => {
        setStartDate(e.target.value);
    };

    const handleEndDateChange = (e) => {
        setEndDate(e.target.value);
    };
    const handleAddFavorite = async (row) => {
        try {
            const response = await axios.post(
                'http://localhost:5432/api/favorites/add',
                row,
                { withCredentials: true }
            );
            console.log('Added to favorites:', response.data);
            alert('Market added to favorites!');
        } catch (error) {
            console.error('Error adding to favorites:', error);
            alert('Failed to add market to favorites. Please try again.');
        }
    };

    const columns = [
        { name: 'Code', selector: row => row.code, sortable: true },
        { name: 'Date', selector: row => row.date },
        { name: 'Max Price', selector: row => row.maxPrice },
        { name: 'Min Price', selector: row => row.minPrice },
        { name: 'Average Price', selector: row => row.averagePrice },
        { name: 'Percentage Change', selector: row => row.percentageChange },
        {
            name: 'Action',
            cell: (row) => (
                <button onClick={() => handleAddFavorite(row)} className="add-favorite-button">
                    Add to Favorites
                </button>
            ),
        },
    ];

    return (
        <div className='container mt-5'>
            {userData ? (
                <>
                    <span id="username">{userData.name} {userData.surname}</span>
                    <div id="avatar">
                        <UserIcon/>
                    </div>
                </>
            ) : (
                <p>Loading user data...</p>
            )}
            <h1 className="historicHeader">Historic Data</h1>
            <div className="input-container">
                <div className="input-group">
                    <label>Date from</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={handleStartDateChange}
                        className="input-field"
                    />
                </div>
                <div className="input-group">
                    <label>Date to</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={handleEndDateChange}
                        className="input-field"
                    />
                </div>
                <br/>
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
                data={filteredMarketList}
                selectableRows
                fixedHeader
                pagination
                paginationPerPage={10}
                paginationRowsPerPageOptions={[10, 20, 30]}
                paginationClass="custom-pagination"
            />
        </div>
    );
};

export default HistoricData;
