import { useAppSelector } from "../../redux/hooks";
import "./LoadingIndicator.css";

export default function LoadingIndicator() {
  const { loadingMessage } = useAppSelector((state) => state.userReducer);
  return (
    <div className="loader">
      <span>{loadingMessage}</span>
    </div>
  );
}
