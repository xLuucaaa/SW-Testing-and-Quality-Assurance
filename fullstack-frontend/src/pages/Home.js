import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import InputGroup from "react-bootstrap/InputGroup";
import Form from "react-bootstrap/Form";

export default function Home() {
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");
  const [filterCriteria, setFilterCriteria] = useState("name"); 
  const [sortCriteria, setSortCriteria] = useState("id");

  const { id } = useParams();

  useEffect(() => {
    loadSortedUsers();
  }, [sortCriteria]);

  const loadSortedUsers = async () => {
    let url = `http://localhost:8080/users`;

    if (sortCriteria) {
      url += `?sortBy=${sortCriteria}`;
    }

    const result = await axios.get(url);
    setUsers(result.data);
  };

  const handleSortClick = (sortBy) => {
    setSortCriteria(sortBy);
  };

  const deleteUser = async (id) => {
    await axios.delete(`http://localhost:8080/user/${id}`);
    loadSortedUsers();
  };

  return (
    <div className="container">
      <div className="py-4">
        <InputGroup>
          <Form.Control 
            onChange={(e) => setSearch(e.target.value.toLowerCase())} 
            placeholder={`Search for ${filterCriteria === 'department' || 'name' ? 'a' : 'an'} ${filterCriteria}`}
          />
          <Form.Select onChange={(e) => setFilterCriteria(e.target.value)}>
            <option value="name">Name</option>
            <option value="username">Username</option>
            <option value="email">Email</option>
            <option value="department">Department</option>
          </Form.Select>
        </InputGroup>
        <table className="table border shadow">
          <thead>
            <tr>
              <th scope="col">
                <button onClick={() => handleSortClick("id")}>S.N</button>
              </th>
              <th scope="col">
                <button onClick={() => handleSortClick("name")}>Name</button>
              </th>
              <th scope="col">
                <button onClick={() => handleSortClick("username")}>Username</button>
              </th>
              <th scope="col">
                <button onClick={() => handleSortClick("email")}>Email</button>
              </th>
              <th scope="col">
                <button onClick={() => handleSortClick("department")}>Department</button>
              </th>
              <th scope="col">Action</th>
            </tr>
          </thead>
          <tbody>
            {users
              .filter((filteredUser) => {
                const searchTerm = search.trim().toLowerCase();
                const filterValue = filteredUser[filterCriteria].toLowerCase();
                return (
                  searchTerm === "" ||
                  filterValue.includes(searchTerm)
                );
              })
              .map((user, index) => (
                <tr key={index}>
                  <th scope="row">{user.id}</th>
                  <td>{user.name}</td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.department}</td>
                  <td>
                    <Link
                      className="btn btn-primary mx-2"
                      to={`/viewuser/${user.id}`}
                    >
                      View
                    </Link>
                    <Link
                      className="btn btn-outline-primary mx-2"
                      to={`/edituser/${user.id}`}
                    >
                      Edit
                    </Link>
                    <button
                      className="btn btn-danger mx-2"
                      onClick={() => deleteUser(user.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
