import React from 'react';
import './MonthSelector.css';

const MonthSelector = ({ selectedMonth, onMonthChange }) => {
  const handlePreviousMonth = () => {
    const [year, month] = selectedMonth.split('-');
    let newMonth = parseInt(month) - 1;
    let newYear = parseInt(year);
    
    if (newMonth === 0) {
      newMonth = 12;
      newYear -= 1;
    }
    
    const newMonthStr = String(newMonth).padStart(2, '0');
    onMonthChange(`${newYear}-${newMonthStr}`);
  };

  const handleNextMonth = () => {
    const [year, month] = selectedMonth.split('-');
    let newMonth = parseInt(month) + 1;
    let newYear = parseInt(year);
    
    if (newMonth === 13) {
      newMonth = 1;
      newYear += 1;
    }
    
    const newMonthStr = String(newMonth).padStart(2, '0');
    onMonthChange(`${newYear}-${newMonthStr}`);
  };

  const handleToday = () => {
    const now = new Date();
    const month = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
    onMonthChange(month);
  };

  const getMonthName = (monthString) => {
    const [year, month] = monthString.split('-');
    const monthIndex = parseInt(month) - 1;
    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
                        'July', 'August', 'September', 'October', 'November', 'December'];
    return `${monthNames[monthIndex]} ${year}`;
  };

  return (
    <div className="month-selector">
      <button className="btn-nav" onClick={handlePreviousMonth} title="Previous month">
        ◀
      </button>
      <div className="month-display">{getMonthName(selectedMonth)}</div>
      <button className="btn-nav" onClick={handleNextMonth} title="Next month">
        ▶
      </button>
      <button className="btn-today" onClick={handleToday}>
        Today
      </button>
    </div>
  );
};

export default MonthSelector;
