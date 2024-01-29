import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.less';
import HomePage from './routes/homepage/HomePage';
import reportWebVitals from './reportWebVitals';

import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import ManagementPage from './routes/management/ManagementPage';
import Root from './routes/root/Root';
import RootErrorPage from './routes/root/RootErrorPage';
import ListingPage from './routes/listing/ListingPage';
import KeyGenPage from './routes/keygen/KeyGenPage';

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    errorElement: <RootErrorPage />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: "/keygen",
        element: <KeyGenPage />,
      },
      {
        path: "/management",
        element: <ManagementPage />,
      },
      {
        path: "/listing",
        element: <ListingPage />,
      },
    ]
  }
]);

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
