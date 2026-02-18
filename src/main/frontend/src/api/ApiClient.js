const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

export const apiCall = async (endpoint, options = {}) => {
  const token = localStorage.getItem('authToken');
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers,
    });

    if (!response.ok) {
      throw new Error(`API error: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error('API call failed:', error);
    throw error;
  }
};

export const getCategories = () => apiCall('/categories');
export const getExpenses = () => apiCall('/expenses');
export const getInvestments = () => apiCall('/investments');
export const getAnalytics = () => apiCall('/analytics');
export const getRecurringExpenses = () => apiCall('/recurring-expenses');

export const addExpense = (data) => 
  apiCall('/expenses', {
    method: 'POST',
    body: JSON.stringify(data),
  });

export const addCategory = (data) =>
  apiCall('/categories', {
    method: 'POST',
    body: JSON.stringify(data),
  });

export const deleteExpense = (id) =>
  apiCall(`/expenses/${id}`, {
    method: 'DELETE',
  });
