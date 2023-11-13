import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";
import {AiOutlineSortAscending, AiOutlineSortDescending} from "react-icons/ai"
import { Button } from "bootstrap";

export default function Home() {
  const [users, setUsers] = useState([]);
  const [asc, setASC] = useState(true);
  const [sortedColumn, setSortedColumn] = useState();

  const { id } = useParams();

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    const result = await axios.get("http://localhost:8080/users");
    setUsers(result.data);
  };

  const deleteUser = async (id) => {
    await axios.delete(`http://localhost:8080/user/${id}`);
    loadUsers();
  };

  const sortFunction = (column) => {

    const sortedUsers = [...users]
    //console.log(column);
    //console.log(users[0][column]);
    sortedUsers.sort((a, b) => {
      const columnA = a[column].toString().toUpperCase(); // ignore upper and lowercase
      const columnB = b[column].toString().toUpperCase(); // ignore upper and lowercase
      if (columnA < columnB) {
        return !asc ? -1 : 1;
      }
      if (columnA > columnB) {
        return !asc ? 1 : -1;
      }
    
      // columns must be equal
      return 0;
    });
   
    setASC(!asc);
    setSortedColumn(column);

    console.log(sortedUsers);
    setUsers(sortedUsers);
   
  }

  return ( 
    <div className="container">
      <div className="py-4">
        <table className="table border shadow">
          <thead>
            <tr>
              <th style={{cursor:"pointer"}} onClick={() => sortFunction("id")} scope="col">S.N{sortedColumn=="id" ? (asc ? <AiOutlineSortAscending/>: <AiOutlineSortDescending/>) :""}</th>
              <th style={{cursor:"pointer"}} onClick={() => sortFunction("name")} scope="col">Name{sortedColumn=="name" ? (asc ? <AiOutlineSortAscending/>: <AiOutlineSortDescending/>) :""}</th>
              <th style={{cursor:"pointer"}} onClick={() => sortFunction("username")} scope="col">Username{sortedColumn=="username" ? (asc ? <AiOutlineSortAscending/>: <AiOutlineSortDescending/>) :""}</th>
              <th style={{cursor:"pointer"}} onClick={() => sortFunction("email")} scope="col">Email{sortedColumn=="email" ? (asc ? <AiOutlineSortAscending/>: <AiOutlineSortDescending/>) :""}</th>
              <th style={{cursor:"pointer"}} onClick={() => sortFunction("department")} scope="col">Department{sortedColumn=="department" ? (asc ? <AiOutlineSortAscending/>: <AiOutlineSortDescending/>) :""}</th>
              <th scope="col">Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, index) => (
              <tr key={index}>
                <th scope="row">
                  {user.id}
                </th>
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
    </div>)
  ;
}