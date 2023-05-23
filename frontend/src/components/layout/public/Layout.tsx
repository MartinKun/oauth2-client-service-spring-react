import { Outlet } from "react-router-dom";
import LoadingIndicator from "../../loading/LoadingIndicator";
import Appheader from "../../header/Appheader";
import { useAppDispatch, useAppSelector } from "../../../redux/hooks";
import Success from "../../success/Success";
import Error from "../../error/Error";
import { useEffect } from "react";
import { loadCurrentUser } from "../../../actions/userActions";

const Layout = () => {
  const dispatch = useAppDispatch();
  const { isLoading, success, error } = useAppSelector(
    (state) => state.userReducer
  );

  useEffect(() => {
    loadCurrentUser()(dispatch);
  }, []);

  if (isLoading) {
    return <LoadingIndicator />;
  }

  if (success) {
    return <Success />;
  }

  if (error) {
    return <Error />;
  }

  return (
    <div className="app">
      <div className="app-top-box">
        <Appheader />
      </div>
      <div className="app-body">
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
