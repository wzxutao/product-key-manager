import React from 'react';
import './HomePage.css';
import bg from '../../assets/hpbackground.jpg';

function HomePage() {
  return (
    <div className="App"
      style={{
        backgroundImage: `url(${bg})`,
        height: '100vh'
      }}
    >

    </div>
  );
}

export default HomePage;
