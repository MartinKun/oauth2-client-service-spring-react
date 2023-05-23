import { Navigate, Outlet } from "react-router-dom";
import { useAppSelector } from "../../../redux/hooks";

const LayoutPrivate = () => {
  const { authenticated } = useAppSelector((state) => state.userReducer);

  if (!authenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default LayoutPrivate;
