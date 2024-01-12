import React from 'react';
import './HomePage.less';
import { Grid } from '@mui/material';
import LoginSection from './LoginSection';
import LogoutSection from './LogoutSection';
import { useUsername } from '../../common/hooks';


function HomePage() {
  const username = useUsername();

  return (
    <Grid id="HomePage" component="main" container spacing={0}>
      <Grid item xs={0} lg={7}>

      </Grid>
      {username === null && <LoginSection />}
      {username !== null && <LogoutSection username={username} />}
    </Grid>
  );
}

export default HomePage;
