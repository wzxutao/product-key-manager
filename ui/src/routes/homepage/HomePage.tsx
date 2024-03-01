import React from 'react';
import './HomePage.less';
import { Grid } from '@mui/material';
import LoginSection from './LoginSection';
import LogoutSection from './LogoutSection';
import { useUsername } from '../../common/hooks';
import { API_URL } from '../../common/constants';


function HomePage() {
  const username = useUsername();

  return (
    <Grid id="HomePage" component="main" container spacing={0}
      style={{
        backgroundImage: `url(${API_URL}/static/background-image)`,
      }}
    >
      <Grid item xs={0} lg={7}>

      </Grid>
      {username === null && <LoginSection />}
      {username !== null && <LogoutSection username={username} />}
    </Grid>
  );
}

export default HomePage;
