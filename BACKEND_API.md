# fin-manager Backend API

## Overview
fin-manager is a personal finance management system with expense tracking, recurring expenses, and investment management capabilities.

## Building

```bash
mvn clean package
```

This creates a fat JAR: `target/fin-manager.jar`

## Running

```bash
java -jar target/fin-manager.jar
```

Server starts on `http://localhost:8080`

## API Endpoints

### Categories
- `GET /api/categories` - Get all categories
- `POST /api/categories` - Create new category
- `PUT /api/category` - Update category
- `DELETE /api/category?id={id}` - Delete category

### Expenses
- `GET /api/expenses?month=2026-02` - Get expenses by month
- `POST /api/expenses` - Create expense
- `PUT /api/expense` - Update expense
- `DELETE /api/expense?id={id}` - Delete expense
- `GET /api/expenses/total?month=2026-02` - Get monthly total

### Recurring Expenses
- `GET /api/recurring` - Get all recurring expenses
- `POST /api/recurring` - Create recurring expense
- `PUT /api/recurring` - Update recurring expense
- `DELETE /api/recurring?id={id}` - Delete recurring expense

### Investments
- `GET /api/investments` - Get all investments
- `GET /api/investments?year=2026` - Get investments by year
- `POST /api/investments` - Create investment
- `PUT /api/investment` - Update investment
- `DELETE /api/investment?id={id}` - Delete investment
- `POST /api/investments/cagr` - Calculate CAGR
- `POST /api/investments/projection` - Project investment value

### Analytics
- `GET /api/analytics/breakdown?month=2026-02` - Get category breakdown
- `GET /api/analytics/trend?year=2026` - Get yearly trend

## Testing

```bash
mvn test
```

Runs all unit and integration tests.

## Project Structure

```
src/main/java/com/finmanager/
├── db/              # Database layer
├── model/           # Data models
├── service/         # Business logic
├── api/             # API layer
├── util/            # Utilities
├── server/          # HTTP server
└── Main.java        # Entry point

src/test/java/com/finmanager/
├── service/         # Service tests
├── util/            # Utility tests
└── integration/     # Integration tests
```

## Database

SQLite database automatically created at `fin-manager.db`

## Configuration

Edit `application.properties` for custom settings:
- `server.port` - HTTP server port (default: 8080)
- `db.path` - Database file path (default: fin-manager.db)
- `data.retention.years` - Data retention period (default: 3)

## Features

✅ Monthly expense tracking
✅ Recurring monthly/yearly expenses
✅ Multi-currency investment tracking
✅ CAGR and investment projections
✅ Category management with colors
✅ Analytics and reporting
✅ Data export to CSV
✅ 3-year data retention policy

## Technologies

- Java 11+
- SQLite
- Gson (JSON)
- Maven
- JUnit 4
