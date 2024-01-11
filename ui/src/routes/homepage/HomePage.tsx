import React from 'react';
import './HomePage.css';
import bg from '../../assets/hpbackground.jpg';

function HomePage() {
  return (
    <main className="App"
      style={{
        backgroundImage: `url(${bg})`,
        height: '100vh',
        width: '100vw',
        position: 'fixed'
      }}
    >

    </main>
  );
}

export default HomePage;
