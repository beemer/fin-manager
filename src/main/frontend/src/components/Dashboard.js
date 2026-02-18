import React, { useState, useEffect } from 'react';
import './Dashboard.css';
import { getExpenses, getCategories, addExpense } from '../api/ApiClient';

const Dashboard = ({ onLogout }) => {
  const [expenses, setExpenses] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    description: '',
    amount: '',
    category: '',
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [expensesData, categoriesData] = await Promise.all([
        getExpenses(),
        getCategories(),
      ]);
      setExpenses(Array.isArray(expensesData) ? expensesData : []);
      setCategories(Array.isArray(categoriesData) ? categoriesData : []);
    } catch (err) {
      console.error('Error loading data:', err);
      setError('Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleAddExpense = async (e) => {
    e.preventDefault();
    if (!formData.description || !formData.amount || !formData.category) {
      setError('Please fill all fields');
      return;
    }

    try {
      await addExpense({
        description: formData.description,
        amount: parseFloat(formData.amount),
        category: formData.category,
      });
      setFormData({ description: '', amount: '', category: '' });
      await loadData();
    } catch (err) {
      setError('Failed to add expense');
    }
  };

  const totalExpenses = expenses.reduce((sum, exp) => sum + (exp.amount || 0), 0);

  return (
    <div className="dashboard">
      <nav className="navbar">
        <h1>fin-manager Dashboard</h1>
        <button onClick={onLogout} className="logout-button">Logout</button>
      </nav>

      <div className="container">
        {error && <div className="error-banner">{error}</div>}

        {loading ? (
          <div className="loading">Loading...</div>
        ) : (
          <>
            <div className="summary-card">
              <h2>Total Expenses</h2>
              <p className="amount">₹{totalExpenses.toFixed(2)}</p>
            </div>

            <div className="content-grid">
              <div className="add-expense-section">
                <h3>Add New Expense</h3>
                <form onSubmit={handleAddExpense}>
                  <div className="form-group">
                    <label>Description</label>
                    <input
                      type="text"
                      value={formData.description}
                      onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                      placeholder="Expense description"
                    />
                  </div>
                  <div className="form-group">
                    <label>Amount</label>
                    <input
                      type="number"
                      step="0.01"
                      value={formData.amount}
                      onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                      placeholder="0.00"
                    />
                  </div>
                  <div className="form-group">
                    <label>Category</label>
                    <select
                      value={formData.category}
                      onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                    >
                      <option value="">Select category</option>
                      {categories.map(cat => (
                        <option key={cat.id} value={cat.name}>{cat.name}</option>
                      ))}
                    </select>
                  </div>
                  <button type="submit" className="add-button">Add Expense</button>
                </form>
              </div>

              <div className="expenses-section">
                <h3>Recent Expenses</h3>
                {expenses.length === 0 ? (
                  <p className="no-data">No expenses yet</p>
                ) : (
                  <table className="expenses-table">
                    <thead>
                      <tr>
                        <th>Description</th>
                        <th>Category</th>
                        <th>Amount</th>
                        <th>Date</th>
                      </tr>
                    </thead>
                    <tbody>
                      {expenses.map(exp => (
                        <tr key={exp.id}>
                          <td>{exp.description}</td>
                          <td>{exp.category}</td>
                          <td>₹{exp.amount?.toFixed(2)}</td>
                          <td>{exp.date ? new Date(exp.date).toLocaleDateString() : '-'}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
