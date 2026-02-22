import React, { useState, useEffect } from 'react';
import './RecurringExpenseList.css';

const RecurringExpenseList = () => {
  const [recurringExpenses, setRecurringExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [statusMessage, setStatusMessage] = useState(null);
  const [statusType, setStatusType] = useState('info'); // 'info', 'success', 'error'
  const [generatingId, setGeneratingId] = useState(null); // Track which recurring is generating

  useEffect(() => {
    fetchRecurringExpenses();
  }, []);

  const fetchRecurringExpenses = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/recurring');
      if (response.ok) {
        const data = await response.json();
        setRecurringExpenses(Array.isArray(data) ? data : []);
        setError(null);
      } else {
        setError('Failed to fetch recurring expenses');
      }
    } catch (err) {
      console.error('Error fetching recurring expenses:', err);
      setError('Error connecting to backend: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const showStatus = (message, type = 'info') => {
    setStatusMessage(message);
    setStatusType(type);
    setTimeout(() => setStatusMessage(null), 5000); // Auto-clear after 5 seconds
  };

  const handleGenerateForRecurring = async (recurringId) => {
    try {
      setGeneratingId(recurringId);
      const response = await fetch(`/api/recurring/${recurringId}/generate`, {
        method: 'POST',
      });

      const data = await response.json();

      if (response.ok && data.success) {
        showStatus(`Generated ${data.count} instance(s) for recurring expense`, 'success');
        // Refresh the list to show updated lastGeneratedDate
        fetchRecurringExpenses();
      } else {
        showStatus(data.message || 'Failed to generate instances', 'error');
      }
    } catch (err) {
      console.error('Error generating instances:', err);
      showStatus('Error: ' + err.message, 'error');
    } finally {
      setGeneratingId(null);
    }
  };

  const handleGenerateAll = async () => {
    try {
      setGeneratingId('all');
      const response = await fetch('/api/recurring/generate-all', {
        method: 'POST',
      });

      const data = await response.json();

      if (response.ok && data.success) {
        showStatus(`Generated ${data.count} total instance(s) across all recurring expenses`, 'success');
        // Refresh the list to show updated lastGeneratedDate
        fetchRecurringExpenses();
      } else {
        showStatus(data.message || 'Failed to generate instances', 'error');
      }
    } catch (err) {
      console.error('Error generating all instances:', err);
      showStatus('Error: ' + err.message, 'error');
    } finally {
      setGeneratingId(null);
    }
  };

  const formatDate = (date) => {
    if (!date) return 'Never';
    try {
      return new Date(date).toLocaleDateString('en-IN');
    } catch {
      return date;
    }
  };

  const getGenerationStatusColor = (lastGeneratedDate) => {
    if (!lastGeneratedDate) return 'danger'; // red - never generated
    
    const lastGen = new Date(lastGeneratedDate);
    const now = new Date();
    const daysAgo = Math.floor((now - lastGen) / (1000 * 60 * 60 * 24));
    
    if (daysAgo > 30) return 'warning'; // yellow - old generation
    return 'success'; // green - recent
  };

  if (loading) {
    return <div className="alert alert-info">Loading recurring expenses...</div>;
  }

  return (
    <div className="recurring-expense-container">
      <div className="recurring-header">
        <h4>Recurring Expenses</h4>
        <button
          className="btn btn-success"
          onClick={handleGenerateAll}
          disabled={generatingId !== null || recurringExpenses.length === 0}
        >
          {generatingId === 'all' ? 'Generating...' : '⚡ Generate All'}
        </button>
      </div>

      {statusMessage && (
        <div className={`alert alert-${statusType} alert-dismissible fade show`}>
          {statusMessage}
          <button
            type="button"
            className="btn-close"
            onClick={() => setStatusMessage(null)}
          ></button>
        </div>
      )}

      {error && (
        <div className="alert alert-danger">
          {error}
          <button
            className="btn btn-sm btn-outline-danger ms-2"
            onClick={fetchRecurringExpenses}
          >
            Retry
          </button>
        </div>
      )}

      {recurringExpenses.length === 0 ? (
        <p className="text-muted">No recurring expenses configured</p>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover">
            <thead className="table-light">
              <tr>
                <th>Description</th>
                <th>Amount</th>
                <th>Frequency</th>
                <th>Last Generated</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {recurringExpenses.map((recurring) => (
                <tr key={recurring.id}>
                  <td>{recurring.description || '-'}</td>
                  <td>₹{recurring.amount?.toFixed(2) || '0.00'}</td>
                  <td>
                    <span className="badge bg-info">
                      {recurring.frequency || '-'}
                    </span>
                  </td>
                  <td>{formatDate(recurring.lastGeneratedDate)}</td>
                  <td>
                    <span
                      className={`badge bg-${getGenerationStatusColor(recurring.lastGeneratedDate)}`}
                    >
                      {!recurring.lastGeneratedDate
                        ? 'Never'
                        : Math.floor(
                            (new Date() - new Date(recurring.lastGeneratedDate)) /
                              (1000 * 60 * 60 * 24)
                          ) + ' days ago'}
                    </span>
                  </td>
                  <td>
                    <button
                      className="btn btn-sm btn-primary"
                      onClick={() => handleGenerateForRecurring(recurring.id)}
                      disabled={generatingId !== null}
                    >
                      {generatingId === recurring.id ? (
                        <>
                          <span
                            className="spinner-border spinner-border-sm me-2"
                            role="status"
                            aria-hidden="true"
                          ></span>
                          Generating...
                        </>
                      ) : (
                        '⚡ Generate'
                      )}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default RecurringExpenseList;
