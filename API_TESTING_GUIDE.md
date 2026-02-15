# fin-manager API Testing Guide

Quick curl examples for testing all endpoints.

## Start the Server

```bash
java -jar target/fin-manager.jar
```

Server runs on `http://localhost:8080`

## Health Check

```bash
curl http://localhost:8080/api/health
```

## Categories

### Get all categories
```bash
curl http://localhost:8080/api/categories
```

### Create category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Groceries",
    "type": "UTILITIES",
    "color": "#0000FF"
  }'
```

### Update category
```bash
curl -X PUT http://localhost:8080/api/category \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "Food",
    "type": "UTILITIES",
    "color": "#0000FF",
    "active": true
  }'
```

### Delete category
```bash
curl -X DELETE "http://localhost:8080/api/category?id=1"
```

## Expenses

### Get expenses for month
```bash
curl "http://localhost:8080/api/expenses?month=2026-02"
```

### Create expense
```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2026-02-15",
    "amount": 50.00,
    "categoryId": 1,
    "description": "Groceries"
  }'
```

### Get monthly total
```bash
curl "http://localhost:8080/api/expenses/total?month=2026-02"
```

### Update expense
```bash
curl -X PUT http://localhost:8080/api/expense \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "date": "2026-02-20",
    "amount": 75.00,
    "categoryId": 1,
    "description": "Updated groceries"
  }'
```

### Delete expense
```bash
curl -X DELETE "http://localhost:8080/api/expense?id=1"
```

## Recurring Expenses

### Get all recurring
```bash
curl http://localhost:8080/api/recurring
```

### Create recurring
```bash
curl -X POST http://localhost:8080/api/recurring \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "amount": 150.00,
    "description": "Monthly subscription",
    "frequency": "MONTHLY",
    "startDate": "2026-01-01"
  }'
```

### Update recurring
```bash
curl -X PUT http://localhost:8080/api/recurring \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "categoryId": 1,
    "amount": 200.00,
    "description": "Updated subscription",
    "frequency": "MONTHLY",
    "startDate": "2026-01-01",
    "active": true
  }'
```

### Delete recurring
```bash
curl -X DELETE "http://localhost:8080/api/recurring?id=1"
```

## Investments

### Get all investments
```bash
curl http://localhost:8080/api/investments
```

### Get investments by year
```bash
curl "http://localhost:8080/api/investments?year=2026"
```

### Create investment
```bash
curl -X POST http://localhost:8080/api/investments \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2026-02-15",
    "amount": 1000.00,
    "currency": "USD",
    "exchangeRate": 1.0,
    "description": "Stock purchase"
  }'
```

### Calculate CAGR
```bash
curl -X POST http://localhost:8080/api/investments/cagr \
  -H "Content-Type: application/json" \
  -d '{
    "startValue": 10000.00,
    "endValue": 13382.00,
    "years": 3
  }'
```

### Project investment value
```bash
curl -X POST http://localhost:8080/api/investments/projection \
  -H "Content-Type: application/json" \
  -d '{
    "currentValue": 10000.00,
    "annualContribution": 1000.00,
    "expectedReturn": 7.0,
    "years": 5
  }'
```

## Analytics

### Get category breakdown
```bash
curl "http://localhost:8080/api/analytics/breakdown?month=2026-02"
```

### Get yearly trend
```bash
curl "http://localhost:8080/api/analytics/trend?year=2026"
```

## Testing with SoapUI or Postman

1. Import the base URL: `http://localhost:8080`
2. Create GET/POST/PUT/DELETE requests for each endpoint
3. Use the JSON payloads above
4. Check responses for status codes (200, 201, 404, 400, etc.)

## Batch Testing

```bash
#!/bin/bash

echo "Testing fin-manager API"

# Health check
echo "1. Health check"
curl -s http://localhost:8080/api/health | jq .

# Get categories
echo "2. Get categories"
curl -s http://localhost:8080/api/categories | jq .

# Get expenses
echo "3. Get expenses"
curl -s "http://localhost:8080/api/expenses?month=2026-02" | jq .

# Get investments
echo "4. Get investments"
curl -s http://localhost:8080/api/investments | jq .

echo "✅ All tests completed"
```

---

**Note**: Requires `curl` and optionally `jq` for JSON formatting.
