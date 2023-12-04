import axios from "axios";
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { VALID_EMAIL_REGEX, INVALID_EMAIL_ERR_MSG } from "../constants/Constants.js";

export default function AddUser() {
  let navigate = useNavigate();
  
  const [emailError, setEmailError] = useState("");
  const [user, setUser] = useState({
    name: "",
    username: "",
    email: "",
    department: "",
  });

  const { name, username, email, department } = user;

  const onInputChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };


  useEffect(() => {
    if (user.email) {
      validateEmail();
    }
  }, [user.email]);

  const validateEmail = () => {
    setEmailError(VALID_EMAIL_REGEX.test(email) ? "" : INVALID_EMAIL_ERR_MSG);
  };

  const onSubmit = async (e) => {
    e.preventDefault();

    validateEmail();
    if (emailError) {
      return;
    }

    await axios.post("http://localhost:8080/user", user);
    navigate("/");
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
          <h2 className="text-center m-4">Register User</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className="mb-3">
              <label htmlFor="Name" className="form-label">
                Name
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your name"
                name="name"
                required
                value={name}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Username" className="form-label">
                Username
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your username"
                name="username"
                required
                value={username}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Email" className="form-label">
                E-mail
              </label>
              <input
                type={"text"}
                className={`form-control ${
                  !emailError && email !== ""
                    ? "is-valid"
                    : emailError
                    ? "is-invalid"
                    : ""
                }`}
                placeholder="Enter your e-mail address"
                name="email"
                required
                value={email}
                onChange={(e) => onInputChange(e)}
              />
              {emailError && (
                <div className="invalid-feedback">{emailError}</div>
              )}
            </div>
            <div className="mb-3">
              <label htmlFor="Department" className="form-label">
                Department
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your Department"
                name="department"
                required
                value={department}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <button type="submit" className="btn btn-outline-primary">
              Submit
            </button>
            <Link className="btn btn-outline-danger mx-2" to="/">
              Cancel
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}
