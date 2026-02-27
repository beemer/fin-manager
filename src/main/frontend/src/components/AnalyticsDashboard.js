import React, { useState, useEffect } from 'react';
import { 
  PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip,
  BarChart, Bar, XAxis, YAxis, CartesianGrid
} from 'recharts';
import './AnalyticsDashboard.css';

const AnalyticsDashboard = ({ selectedMonth }) => {
  const [breakdownData, setBreakdownData] = useState([]);
  const [trendData, setTrendData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedMonth]);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);

      // Fetch Category Breakdown
      const breakdownRes = await fetch(`/api/analytics/breakdown?month=${selectedMonth}`);
      if (!breakdownRes.ok) throw new Error('Failed to fetch breakdown');
      const breakdownJson = await breakdownRes.json();
      
      if (breakdownJson.breakdown) {
        const formattedPieData = Object.keys(breakdownJson.breakdown).map(name => ({
          name,
          value: breakdownJson.breakdown[name],
          color: breakdownJson.colors[name] || '#8884d8'
        }));
        setBreakdownData(formattedPieData);
      } else {
        setBreakdownData([]);
      }

      // Fetch Monthly Trend for the current year
      const year = selectedMonth.split('-')[0];
      const trendRes = await fetch(`/api/analytics/trend?year=${year}`);
      if (!trendRes.ok) throw new Error('Failed to fetch trend');
      const trendJson = await trendRes.json();
      
      const monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
      const formattedTrendData = Object.keys(trendJson).map(monthKey => ({
        name: monthNames[parseInt(monthKey) - 1],
        amount: trendJson[monthKey]
      }));
      setTrendData(formattedTrendData);

    } catch (err) {
      console.error('[Analytics] Error:', err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="alert alert-info mt-4">Loading analytics...</div>;
  if (error) return <div className="alert alert-danger mt-4">Error: {error}</div>;

  return (
    <div className="analytics-dashboard mt-4">
      <div className="row">
        {/* Category Breakdown (Pie Chart) */}
        <div className="col-md-6 mb-4">
          <div className="card h-100">
            <div className="card-header">
              <h5>Category Breakdown ({selectedMonth})</h5>
            </div>
            <div className="card-body d-flex flex-column align-items-center justify-content-center">
              {breakdownData.length > 0 ? (
                <div style={{ width: '100%', height: '300px' }}>
                  <ResponsiveContainer width="100%" height="100%">
                    <PieChart>
                      <Pie
                        data={breakdownData}
                        cx="50%"
                        cy="50%"
                        innerRadius={60}
                        outerRadius={80}
                        paddingAngle={5}
                        dataKey="value"
                        label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                      >
                        {breakdownData.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={entry.color} />
                        ))}
                      </Pie>
                      <Tooltip formatter={(value) => `₹${value.toFixed(2)}`} />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                </div>
              ) : (
                <p className="text-muted">No data for this month</p>
              )}
            </div>
          </div>
        </div>

        {/* Monthly Trend (Bar Chart) */}
        <div className="col-md-6 mb-4">
          <div className="card h-100">
            <div className="card-header">
              <h5>Monthly Spending Trend ({selectedMonth.split('-')[0]})</h5>
            </div>
            <div className="card-body">
              <div style={{ width: '100%', height: '300px' }}>
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={trendData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip formatter={(value) => `₹${value.toFixed(2)}`} />
                    <Bar dataKey="amount" fill="#0d6efd" radius={[4, 4, 0, 0]} />
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnalyticsDashboard;
