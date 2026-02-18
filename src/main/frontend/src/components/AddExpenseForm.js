import React, { useState, useEffect } from 'react';
import './AddExpenseForm.css';

const AddExpenseForm = ({ onExpenseAdded, onCancel }) => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const [formData, setFormData] = useState({
    date: new Date().toISOString().split('T')[0],
    amount: '',
    categoryId: '',
    description: '',
    isRecurring: false,
    frequency: 'MONTHLY',
    endDate: '',
  });

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        console.log('[AddExpenseForm] Fetching categories...');
        const response = await fetch('/api/categories');
        if (response.ok) {
          const data = await response.json();
          console.log('[AddExpenseForm] Categories:', data);
          setCategories(Array.isArray(data) ? data : []);
        } else {
          setError('Failed to load categories');
        }
      } catch (err) {
        console.error('[AddExpenseForm] Error fetching categories:', err);
        setError('Error connecting to backend: ' + err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchCategories();
  }, []);

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : (name === 'categoryId' ? parseInt(value) : value),
    }));
    setError(null);
    setSuccess(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation
    if (!formData.date || !formData.amount || !formData.categoryId || !formData.description) {
      setError('Please fill in all fields');
      return;
    }

    if (isNaN(parseFloat(formData.amount)) || parseFloat(formData.amount) <= 0) {
      setError('Amount must be a positive number');
      return;
    }

    setSubmitting(true);
    setError(null);

    try {
      if (formData.isRecurring) {
        // Submit as recurring expense
        console.log('[AddExpenseForm] Submitting recurring expense:', formData);
        const payload = {
          categoryId: formData.categoryId,
          amount: parseFloat(formData.amount),
          description: formData.description,
          frequency: formData.frequency,
          startDate: formData.date,
          endDate: formData.endDate || null,
        };
        
        const response = await fetch('/api/recurring', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(payload),
        });

        if (response.ok) {
          const result = await response.json();
          console.log('[AddExpenseForm] Recurring expense created:', result);
          setSuccess('Recurring expense created successfully!');
          
          // Reset form
          setFormData({
            date: new Date().toISOString().split('T')[0],
            amount: '',
            categoryId: '',
            description: '',
            isRecurring: false,
            frequency: 'MONTHLY',
            endDate: '',
          });

          setTimeout(() => {
            onExpenseAdded();
          }, 500);
        } else {
          const text = await response.text();
          console.error('[AddExpenseForm] Error response:', text);
          setError('Failed to create recurring expense: ' + text);
        }
      } else {
        // Submit as one-time expense
        console.log('[AddExpenseForm] Submitting one-time expense:', formData);
        const response = await fetch('/api/expense', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            date: formData.date,
            amount: parseFloat(formData.amount),
            categoryId: formData.categoryId,
            description: formData.description,
          }),
        });

        if (response.ok) {
          const result = await response.json();
          console.log('[AddExpenseForm] Expense created:', result);
          setSuccess('Expense added successfully!');
          
          // Reset form
          setFormData({
            date: new Date().toISOString().split('T')[0],
            amount: '',
            categoryId: '',
            description: '',
            isRecurring: false,
            frequency: 'MONTHLY',
            endDate: '',
          });

          setTimeout(() => {
            onExpenseAdded();
          }, 500);
        } else {
          const text = await response.text();
          console.error('[AddExpenseForm] Error response:', text);
          setError('Failed to create expense: ' + text);
        }
      }
    } catch (err) {
      console.error('[AddExpenseForm] Error:', err);
      setError('Error: ' + err.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay" onClick={onCancel}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Add New Expense</h3>
          <button type="button" className="close-btn" onClick={onCancel}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="expense-form">
          {error && <div className="alert alert-danger">{error}</div>}
          {success && <div className="alert alert-success">{success}</div>}

          <div className="form-group">
            <label htmlFor="date">Date</label>
            <input
              type="date"
              id="date"
              name="date"
              value={formData.date}
              onChange={handleInputChange}
              className="form-control"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="amount">Amount (₹)</label>
            <input
              type="number"
              id="amount"
              name="amount"
              value={formData.amount}
              onChange={handleInputChange}
              placeholder="Enter amount"
              step="0.01"
              min="0"
              className="form-control"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="categoryId">Category</label>
            {loading ? (
              <p className="text-muted">Loading categories...</p>
            ) : (
              <select
                id="categoryId"
                name="categoryId"
                value={formData.categoryId}
                onChange={handleInputChange}
                className="form-control"
                required
              >
                <option value="">Select a category</option>
                {categories.map(category => (
                  <option key={category.id} value={category.id}>
                    {category.name}
                  </option>
                ))}
              </select>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleInputChange}
              placeholder="Enter expense description"
              rows="4"
              className="form-control"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="isRecurring" className="checkbox-label">
              <input
                type="checkbox"
                id="isRecurring"
                name="isRecurring"
                checked={formData.isRecurring}
                onChange={handleInputChange}
                className="form-check-input"
              />
              <span>Make this a recurring expense</span>
            </label>
          </div>

          {formData.isRecurring && (
            <>
              <div className="form-group">
                <label htmlFor="frequency">Recurrence Frequency</label>
                <select
                  id="frequency"
                  name="frequency"
                  value={formData.frequency}
                  onChange={handleInputChange}
                  className="form-control"
                >
                  <option value="MONTHLY">Monthly</option>
                  <option value="YEARLY">Yearly (Jan 1st)</option>
                </select>
              </div>

              <div className="form-group">
                <label htmlFor="endDate">End Date (Optional)</label>
                <input
                  type="date"
                  id="endDate"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleInputChange}
                  className="form-control"
                />
                <small className="text-muted">Leave blank for indefinite recurrence</small>
              </div>
            </>
          )}

          <div className="form-actions">
            <button
              type="button"
              className="btn btn-secondary"
              onClick={onCancel}
              disabled={submitting}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={submitting || loading}
            >
              {submitting ? 'Adding...' : 'Add Expense'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddExpenseForm;
