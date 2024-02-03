import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import { useIsAdmin, useUsername } from '../../../common/hooks';
import { deepPurple, grey } from '@mui/material/colors';
import { useCallback, useMemo } from 'react';
import { useCookies } from 'react-cookie';
import { COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME } from '../../../common/constants';
import { Link as RouterLink } from "react-router-dom";
import { useNavigate } from "react-router-dom";

interface PageAndURL {
  page: string;
  url: string;
  adminOnly?: boolean;
}

const pages: PageAndURL[] = [
  { page: '生成序列号', url: '/keygen' },
  { page: '今日生成的序列号', url: '/today-listing' },
  { page: '补录', url: '/', adminOnly: true },
  { page: '查询', url: '/listing', adminOnly: true },
  { page: '管理', url: '/management', adminOnly: true },
];

function TheAppBar() {
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);
  const username = useUsername();
  const [, , removeCookie] = useCookies([COOKIE_KEY_NORMAL_AUTH, COOKIE_KEY_USERNAME])
  const navigate = useNavigate();
  const isAdmin = useIsAdmin();

  const pagesFiltered = useMemo(() =>
    username === null
      ? [{
        page: 'RiQiang',
        url: '/'
      }]
      : pages
        .filter(page => isAdmin || !page.adminOnly)
    ,
    [isAdmin, username])

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = useCallback(() => {
    setAnchorElNav(null);
  }, []);

  const handleLogout = useCallback(() => {
    handleCloseUserMenu();
    removeCookie(COOKIE_KEY_NORMAL_AUTH)
    removeCookie(COOKIE_KEY_USERNAME)
    navigate('/')
  }, []);

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  return (
    <AppBar id="app-bar" position="sticky">
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          {/* xs */}
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
            >
              {pagesFiltered.map((page) => (
                <MenuItem
                  key={page.page}
                  component={RouterLink}
                  to={page.url}
                  onClick={handleCloseNavMenu}>
                  <Typography textAlign="center">{page.page}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
          <Typography
            variant="h5"
            noWrap
            component={RouterLink}
            to="/"
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            日强
          </Typography>

          {/* md */}
          <Typography
            variant="h6"
            noWrap
            component={RouterLink}
            to="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            日强
          </Typography>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            {pagesFiltered.map((page) => (
              <Button
                component={RouterLink}
                key={page.page}
                to={page.url}
                sx={{ my: 2, color: 'white', display: 'block', textTransform: 'none' }}
              >
                {page.page}
              </Button>
            ))}
          </Box>

          {/* user */}
          <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="用户">
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar sx={{ bgcolor: username != null ? deepPurple[500] : grey[500] }}>
                  {username ? username.charAt(0) : ''}</Avatar>
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              <MenuItem onClick={handleLogout} disabled={username === null}>
                <Typography textAlign="center">注销</Typography>
              </MenuItem>
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
export default TheAppBar;