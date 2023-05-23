import { ACCESS_TOKEN, REFRESH_TOKEN } from "../../constants";
import { Navigate, useLocation } from "react-router-dom";

const OAuth2RedirectHandler = () => {
  let location = useLocation();
  const getUrlParameter = (name: string) => {
    name = name.replace(/\[/, "\\[").replace(/\]/, "\\]");
    const regex = new RegExp("[\\?&]" + name + "=([^&#]*)");

    const results = regex.exec(location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  };

  const refreshToken = getUrlParameter("refresh");
  const accessToken = getUrlParameter("access");
  const error = getUrlParameter("error");

  if (refreshToken && accessToken) {
    localStorage.setItem(REFRESH_TOKEN, refreshToken);
    localStorage.setItem(ACCESS_TOKEN, accessToken);
    return <Navigate to="/user/profile" replace state={{ from: location }} />;
  } else {
    return (
      <Navigate
        to="/login"
        replace
        state={{
          from: location,
          error: error,
        }}
      />
    );
  }
};

export default OAuth2RedirectHandler;
