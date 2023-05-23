import { Link, Navigate } from "react-router-dom";
import "./Signup.css";
import SignupForm from "./SignupForm";
import { useAppSelector } from "../../redux/hooks";

const Signup = () => {
  const { authenticated } = useAppSelector((state) => state.userReducer);

  if (authenticated) {
    return <Navigate to="/user/profile" replace />;
  }

  return (
    <div className="signup-container">
      <div className="signup-content">
        <h1 className="signup-title">Signup</h1>
        <SignupForm />
        <span className="login-link">
          Already have an account? <Link to="/login">Login!</Link>
        </span>
      </div>
    </div>
  );
};

export default Signup;
