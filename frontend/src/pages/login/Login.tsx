import SocialLogin from "./SocialLogin";
import "./Login.css";
import { Link, Navigate } from "react-router-dom";
import LoginForm from "./LoginForm";
import { useAppSelector } from "../../redux/hooks";

const Login = () => {
  const { authenticated } = useAppSelector((state) => state.userReducer);

  if (authenticated) {
    return <Navigate to="/user/profile" />;
  }

  return (
    <div className="login-container">
      <div className="login-content">
        <h1 className="login-title">Login</h1>
        <LoginForm />
        <div className="or-separator">
          <span className="or-text">OR</span>
        </div>
        <SocialLogin />
        <span className="signup-link">
          New user? <Link to="/signup">Sign up!</Link>
        </span>
      </div>
    </div>
  );
};

export default Login;
