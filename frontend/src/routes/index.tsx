import { createBrowserRouter } from "react-router-dom";
import Home from "../pages/home/Home";
import Login from "../pages/login/Login";
import Signup from "../pages/signup/Signup";
import Profile from "../pages/profile/Profile";
import LayoutPrivate from "../components/layout/private/LayoutPrivate";
import Layout from "../components/layout/public/Layout";
import OAuth2RedirectHandler from "../pages/oauth2/OAuth2RedirectHandler";
import ActivateAccount from "../pages/activateAccount/ActivateAccount";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    children: [
      { index: true, element: <Home /> },
      { path: "/login", element: <Login /> },
      { path: "/signup", element: <Signup /> },
      { path: "/oauth2/redirect", element: <OAuth2RedirectHandler /> },
      { path: "/activate-account", element: <ActivateAccount /> },
      {
        path: "/user",
        element: <LayoutPrivate />,
        children: [{ path: "/user/profile", element: <Profile /> }],
      },
    ],
  },
]);
