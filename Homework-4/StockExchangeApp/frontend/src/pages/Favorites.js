import React, { useEffect, useState } from 'react';
import axios from 'axios';

import '../styles/global.css'
import '../styles/Favorites.css'

const Favorites = () => {
    const [favoritesData, setFavoritesData] = useState(null);
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
                console.log(response.data+"dasdda")
                setUserData(userData);
                localStorage.setItem("user", JSON.stringify(userData));
                console.log("Fetched and stored in localStorage:", userData);
            })
            .catch(error => {
                console.error("Error fetching user details:", error);
            });
    }, []);

    useEffect(() => {
        axios.get("http://localhost:5432/api/favorites/get", { withCredentials: true })
            .then(response => {
                setFavoritesData(response.data.favorites);
            })
            .catch(error => {
                console.error("There was an error fetching the favorites data:", error);
            });

    }, []);

    if (!favoritesData) {
        return <div>Loading...</div>;
    }
    const handleRemoveFavorite = async (favoriteId) => {
        try {
            await axios.post(
                'http://localhost:5432/api/favorites/delete',
                { id: favoriteId },
                { withCredentials: true }
            );
            alert('Favorite removed successfully!');

            setFavoritesData(prevFavorites =>
                prevFavorites.filter(fav => fav.id !== favoriteId)
            );
        } catch (error) {
            console.error('Error removing favorite:', error);
            alert('Failed to remove favorite. Please try again.');
        }
    };



    return (
        <div className="favoritesBody">
            <span id="username1">
            {userData ? `${userData.name} ${userData.surname}` : "Loading..."}
        </span>
            <div id="avatar">
                <UserIcon/>
            </div>
            <h1 className="favoritesHeader">Favorites</h1>
            <ul className="favoritesList">
                <section className="sectionFavorites">
                    {favoritesData.map((fav, index) => (
                        <li className="favoriteItem" key={index}>
                            <p className="indexCard indexCardFav">{index + 1}</p>

                            <span className="code">{fav.code}</span>
                            <span className="favoritesSectionAvg">
                                Average price: {fav.averagePrice}
                                <br/><br/> Max price: {fav.maxPrice}
                                <br/><br/> Min price: {fav.minPrice}
                                <br/><br/> Percentage:
                                <span
                                    className={`percent1 ${fav.changePercent.includes("-") ? 'negative' : 'positive'}`}>
                                 {fav.changePercent}
                                </span>
                            </span>
                            <button id="deleteFav" onClick={() => handleRemoveFavorite(fav.id)}>
                                Delete
                            </button>

                        </li>

                    ))}
                </section>

            </ul>
        </div>
    );
};

export default Favorites;
