// src/pages/Favorites.js
import React, { useState, useEffect } from 'react';
import axios from 'axios';
import avatar from './avatar.png';
import grafik from './grafik.png';
import grafik2 from './Vector_10_1.jpg';
import '../styles/global.css'
import '../styles/Favorites.css'

const Favorites = () => {
    const [favoritesData, setFavoritesData] = useState(null);

    useEffect(() => {
        // Make a GET request to Spring Boot backend for favorites data
        axios.get("http://localhost:5432/favorites")
            .then(response => {
                console.log("API Response:", response.data); // Log the entire response
                setFavoritesData(response.data.favorites); // Assuming response contains favorites
            })
            .catch(error => {
                console.error("There was an error fetching the favorites data:", error);
            });
    }, []);

    if (!favoritesData) {
        return <div>Loading...</div>; // Show a loading message while data is being fetched
    }

    // Render favorite markets
    return (
        <div className="favoritesBody">
            <input className="searchInput" type="text" value="Search ..." />
            <img className="avatar" src={avatar} alt="Avatar" />
            <span className="avatar username">Courtney Henry</span>
            <h1 className="favoritesHeader">Favorites</h1>
            <ul className="favoritesList">
                <section className="sectionFavorites">
                    {favoritesData.map((fav, index) => (
                        <li className="favoriteItem" key={index}>
                            <p className="indexCard indexCardFav">{index + 1}</p>
                            <span className="symbol">{fav.symbol}</span>
                            <span className="favoritesSectionAvg">
                                Average price: {fav.averagePrice}
                                <br/><br/> Max price: {fav.maxPrice}
                                <br/><br/> Min price: {fav.minPrice}
                                <br/><br/> Percentage:
                                <span className={`percent1 ${fav.changePercent < 0 ? 'negative' : 'positive'}`}>
                                 {fav.changePercent}
                                </span>
                            </span>
                        </li>
                    ))}
                </section>

            </ul>
        </div>
    );
};

export default Favorites;
