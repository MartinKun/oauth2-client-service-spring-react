import { useAppSelector } from "../../redux/hooks";
import { Navigate } from "react-router-dom";

const Home = () => {
  const { authenticated } = useAppSelector((state) => state.userReducer);

  if (authenticated) {
    return <Navigate to="/user/profile" />;
  }

  return <div>Home</div>;
};

export default Home;
