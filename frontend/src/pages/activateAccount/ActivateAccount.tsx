import { useLocation } from "react-router-dom";
import { activateAccount } from "../../actions/userActions";
import { useAppDispatch } from "../../redux/hooks";
import { loading } from "../../redux/slices/user.slice";
import { useEffect } from "react";
const ActivateAccount = () => {
  const dispatch = useAppDispatch();
  let location = useLocation();

  const getUrlParameter = (name: string) => {
    const regex = new RegExp("[?&]" + name + "=([^&#]*)");
    const results = regex.exec(location.search);
    return results === null
      ? ""
      : decodeURIComponent(results[1].replace(/\+/g, " "));
  };

  const token = getUrlParameter("token");

  useEffect(() => {
    if (token) {
      dispatch(loading("Loading"));
      activateAccount(token)(dispatch);
    }
  }, []);

  return <div></div>;
};

export default ActivateAccount;
