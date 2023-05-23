import { Link, NavLink } from "react-router-dom";
import "./Appheader.css";
import { logout } from "../../actions/userActions";
import { useAppDispatch } from "../../redux/hooks";

const Appheader = () => {
  const dispatch = useAppDispatch();
  const handleLogout = () => {
    logout()(dispatch);
  };

  return (
    <header className="app-header">
      <div className="container">
        <div className="app-branding">
          <Link to="/" className="app-title">
            My Application
          </Link>
        </div>
        <div className="app-options">
          <nav className="app-nav">
            <ul>
              {
                <li>
                  <a href="#" onClick={handleLogout}>
                    Logout
                  </a>
                </li>
              }
              <li>
                <NavLink to="/login">Login</NavLink>
              </li>
              <li>
                <NavLink to="/signup">Signup</NavLink>
              </li>
            </ul>
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Appheader;
