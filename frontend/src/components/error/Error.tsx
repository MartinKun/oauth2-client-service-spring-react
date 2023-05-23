import { useAppSelector } from "../../redux/hooks";
import "./Error.css";

const Error = () => {
  const { errorMessage } = useAppSelector((state) => state.userReducer);

  return (
    <div className="error-container">
      <div className="row">
        <div className="modalbox error col-sm-8 col-md-6 col-lg-5 center animate">
          <div className="icon">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="90"
              height="90"
              viewBox="0 0 24 24"
              style={{ fill: "#ffffff" }}
            >
              <path d="m16.192 6.344-4.243 4.242-4.242-4.242-1.414 1.414L10.535 12l-4.242 4.242 1.414 1.414 4.242-4.242 4.243 4.242 1.414-1.414L13.364 12l4.242-4.242z"></path>
            </svg>
          </div>
          <h1>Error!</h1>
          <p>{errorMessage}</p>
        </div>
      </div>
    </div>
  );
};

export default Error;
