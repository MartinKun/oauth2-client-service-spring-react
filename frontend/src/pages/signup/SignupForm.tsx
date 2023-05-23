import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signUserUp } from "../../actions/userActions";
import { useAppDispatch } from "../../redux/hooks";

const SignupForm = () => {
  const [state, setState] = useState({
    name: "",
    email: "",
    password: "",
  });
  let navigate = useNavigate();
  const dispatch = useAppDispatch();

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const target = event.target;
    const inputName = target.name;
    const inputValue = target.value;

    setState({
      ...state,
      [inputName]: inputValue,
    });
  };

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const signUpRequest = Object.assign({}, state);

    signUserUp(signUpRequest, navigate)(dispatch);
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="form-item">
        <input
          type="text"
          name="name"
          className="form-control"
          placeholder="Name"
          value={state.name}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-item">
        <input
          type="email"
          name="email"
          className="form-control"
          placeholder="Email"
          value={state.email}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-item">
        <input
          type="password"
          name="password"
          className="form-control"
          placeholder="Password"
          value={state.password}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-item">
        <button type="submit" className="btn btn-block btn-primary">
          Sign Up
        </button>
      </div>
    </form>
  );
};

export default SignupForm;
