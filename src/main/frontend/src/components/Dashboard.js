import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import AddExpenseForm from './AddExpenseForm';
import MonthSelector from './MonthSelector';
import RecurringExpenseList from './RecurringExpenseList';
import CategoryManagement from './CategoryManagement';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddExpenseForm, setShowAddExpenseForm] = useState(false);
  const [selectedMonth, setSelectedMonth] = useState(
    () => {
      const now = new Date();
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
    }
  );

  const fetchExpenses = async (month) => {
    try {
      console.log('[Dashboard] Fetching expenses for month:', month);
      setLoading(true);
      
      const response = await fetch(`/api/expenses?month=${month}`);
      console.log('[Dashboard] Response status:', response.status);
      
      if (response.ok) {
        const data = await response.json();
        console.log('[Dashboard] Expenses data:', data);
        setExpenses(Array.isArray(data) ? data : []);
        setError(null);
      } else {
        const text = await response.text();
        console.error('[Dashboard] Error response:', text);
        setError('Failed to fetch expenses');
      }
    } catch (err) {
      console.error('[Dashboard] Error:', err);
      setError('Error connecting to backend: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchExpenses(selectedMonth);
  }, [selectedMonth]);

  const totalExpenses = expenses.reduce((sum, exp) => sum + (exp.amount || 0), 0);

  return (
    <div className="dashboard-container">
      <nav className="navbar navbar-dark bg-dark">
        <div className="container-fluid">
          <span className="navbar-brand mb-0 h1">Financial Manager</span>
          <div className="d-flex">
            <span className="text-light me-3">Welcome, {user?.email || 'User'}</span>
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
          <div className="d-flex justify-content-between align-items-center mb-4">
            <h2>Dashboard</h2>
            <button
              className="btn btn-primary"
              onClick={() => setShowAddExpenseForm(true)}
            >
              + Add Expense
            </button>
          </div>

          <MonthSelector 
            selectedMonth={selectedMonth} 
            onMonthChange={setSelectedMonth} 
          />
          
          {error && <div className="alert alert-danger">{error}</div>}
          {loading && <div className="alert alert-info">Loading...</div>}
          
          <div className="row mt-4">
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Total Expenses</h5>
                  <p className="card-text">₹{totalExpenses.toFixed(2)}</p>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Total Income</h5>
                  <p className="card-text">₹0.00</p>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Categories</h5>
                  <p className="card-text">{expenses.length}</p>
                </div>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card">
                <div className="card-body">
                  <h5 className="card-title">Transactions</h5>
                  <p className="card-text">{expenses.length}</p>
                </div>
              </div>
            </div>
          </div>

          <div className="row mt-4">
            <div className="col-md-12">
              <div className="card">
                <div className="card-header">
                  <h5>Recent Expenses</h5>
                </div>
                <div className="card-body">
                  {expenses.length === 0 ? (
                    <p className="text-muted">No expenses recorded</p>
                  ) : (
                    <table className="table">
                      <thead>
                        <tr>
                          <th>Description</th>
                          <th>Category</th>
                          <th>Amount</th>
                          <th>Date</th>
                        </tr>
                      </thead>
                      <tbody>
                        {expenses.map((exp, idx) => (
                          <tr key={idx}>
                            <td>{exp.description || '-'}</td>
                            <td>{exp.category || '-'}</td>
                            <td>₹{exp.amount?.toFixed(2) || '0.00'}</td>
                            <td>{exp.date || '-'}</td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  )}
                </div>
              </div>
            </div>
          </div>

          <div className="row mt-4">
            <div className="col-md-12">
              <CategoryManagement />
            </div>
          </div>

          <RecurringExpenseList />
        </div>
      </div>

      {showAddExpenseForm && (
        <AddExpenseForm
          onExpenseAdded={() => {
            setShowAddExpenseForm(false);
            fetchExpenses();
          }}
          onCancel={() => setShowAddExpenseForm(false)}
        />
      )}
    </div>
  );
};

export default Dashboard;
