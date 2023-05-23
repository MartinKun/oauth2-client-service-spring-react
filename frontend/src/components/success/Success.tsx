import { useEffect } from "react";
import { useAppSelector } from "../../redux/hooks";
import "./Success.css";
import { useNavigate } from "react-router-dom";

const Success = () => {
  let navigate = useNavigate();
  const { successMessage } = useAppSelector((state) => state.userReducer);

  useEffect(() => {
    const timer = setTimeout(() => {
      window.location.replace("/login");
    }, 4000);

    return () => {
      clearTimeout(timer);
    };
  }, [navigate]);

  return (
    <div className="success-container">
      <div className="row">
        <div className="modalbox success col-sm-8 col-md-6 col-lg-5 center animate">
          <div className="icon">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="90"
              height="90"
              viewBox="0 0 24 24"
              style={{ fill: "#ffffff" }}
            >
              <path d="m10 15.586-3.293-3.293-1.414 1.414L10 18.414l9.707-9.707-1.414-1.414z"></path>
            </svg>
          </div>
          <h1>Success!</h1>
          <p>{successMessage}</p>
        </div>
      </div>
    </div>
  );
};

export default Success;
