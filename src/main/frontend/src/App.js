import React, { useState, useEffect } from 'react';
import './App.css';
import { AuthProvider } from './context/AuthContext';
import Dashboard from './components/Dashboard';
import LoginPage from './components/LoginPage';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if token exists in localStorage
    const token = localStorage.getItem('authToken');
    if (token) {
      setIsAuthenticated(true);
    }
    setLoading(false);
  }, []);

  if (loading) {
    return <div className="loading-container">Loading...</div>;
  }

  return (
    <AuthProvider>
      <div className="App">
        {isAuthenticated ? (
          <Dashboard onLogout={() => {
            localStorage.removeItem('authToken');
            setIsAuthenticated(false);
          }} />
        ) : (
          <LoginPage onLogin={() => {
            setIsAuthenticated(true);
          }} />
        )}
      </div>
    </AuthProvider>
  );
}

export default App;
