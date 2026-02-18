import React, { createContext, useContext, useState, useCallback } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const login = useCallback(async (email, password) => {
    setLoading(true);
    setError(null);
    console.log('[Auth] Logging in:', email);
    try {
      // Dummy login - in real app, call backend API at /api/auth/login
      if (email && password) {
        const userData = { email, id: Math.random() };
        console.log('[Auth] Login successful:', userData);
        setUser(userData);
        setIsAuthenticated(true);
        localStorage.setItem('userEmail', email);
      } else {
        setError('Please provide email and password');
      }
    } catch (err) {
      console.error('[Auth] Login error:', err);
      setError(err.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  }, []);

  const logout = useCallback(() => {
    console.log('[Auth] Logging out');
    setUser(null);
    setIsAuthenticated(false);
    localStorage.removeItem('userEmail');
  }, []);

  const value = {
    isAuthenticated,
    user,
    loading,
    error,
    login,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
