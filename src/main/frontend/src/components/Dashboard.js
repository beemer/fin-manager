import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchExpenses = async () => {
      try {
        // Fetch from backend API
        const response = await fetch('/api/expenses');
        if (response.ok) {
          const data = await response.json();
          setExpenses(data);
        } else {
          setError('Failed to fetch expenses');
        }
      } catch (err) {
        console.error('Error fetching expenses:', err);
        setError('Error connecting to backend');
      } finally {
        setLoading(false);
      }
    };

    fetchExpenses();
  }, []);

  return (
    <div className="dashboard-container">
      <nav className="navbar navbar-dark bg-dark">
        <div className="container-fluid">
          <span className="navbar-brand mb-0 h1">Financial Manager</span>
          <div className="d-flex">
            <span className="text-light me-3">Welcome, {user?.email}</span>
            <button
              className="btn btn-outline-light"
              onClick={logout}
            >
              Logout
            </button>
          </div>
        </div>
      </nav>

      <div className="dashboard-content">
        <div className="container-fluid mt-4">
          <h2>Dashboard</h2>
          
          <div className="row mt-4">
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Total Expenses</h5>
                  <p className="card-text">$0.00</p>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Total Income</h5>
                  <p className="card-text">$0.00</p>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Categories</h5>
                  <p className="card-text">0</p>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Investments</h5>
                  <p className="card-text">$0.00</p>
                </div>
              </div>
            </div>
          </div>

          <div className="row mt-4">
            <div className="col-md-12">
              <h3>Recent Expenses</h3>
              {loading ? (
                <div className="alert alert-info">Loading expenses...</div>
              ) : error ? (
                <div className="alert alert-danger">{error}</div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>Date</th>
                        <th>Category</th>
                        <th>Description</th>
                        <th>Amount</th>
                      </tr>
                    </thead>
                    <tbody>
                      {expenses.length === 0 ? (
                        <tr>
                          <td colSpan="4" className="text-center">No expenses yet</td>
                        </tr>
                      ) : (
                        expenses.map((expense) => (
                          <tr key={expense.id}>
                            <td>{expense.date}</td>
                            <td>{expense.categoryName}</td>
                            <td>{expense.description}</td>
                            <td>${expense.amount.toFixed(2)}</td>
                          </tr>
                        ))
                      )}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
