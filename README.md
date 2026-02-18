# fin-manager
**Avant's Personal Finance Tool**

A full-stack personal finance management application with Java backend and React frontend.

---

## Quick Start

### Prerequisites
- **Java 21 JDK** - [Download](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi)
- **Git** - [Download](https://git-scm.com/)
- ✅ **Node.js & npm** - Automatically installed by Maven (you don't need to install manually!)

### Run in 30 Seconds

```bash
# Clone/navigate to project
cd c:\work\IdeaProjects\fin-manager

# Build and start server
mvn clean package
java -jar target/fin-manager.jar
```

Server runs at: **http://localhost:8080**

---

## Features

✅ **Expense Tracking** - Record and categorize your spending  
✅ **Income Management** - Track multiple income sources  
✅ **Recurring Expenses** - Auto-track bills and subscriptions  
✅ **Investment Tracking** - Monitor your portfolio  
✅ **Analytics** - Generate financial reports and insights  
✅ **Currency Conversion** - Multi-currency support  
✅ **Data Export** - Export financial data  
✅ **REST API** - Full API with Swagger documentation  

---

## Installation & Setup

### 1. Verify Prerequisites

```bash
# Check Java version (should be 21+)
java -version

# Check Maven version (should be 3.8+)
mvn --version

# Check Git version
git --version
```

### 2. Build the Project

```bash
mvn clean package
```

This will automatically:
- Install Node.js v20.10.0 (Maven-managed - not global)
- Install npm 10.2.3 (Maven-managed - not global)
- Install React dependencies via `npm install`
- Build the React frontend
- Compile Java backend
- Run all unit and integration tests
- Package everything into `target/fin-manager.jar`

### 3. Run the Application

```bash
java -jar target/fin-manager.jar
```

Expected output:
```
fin-manager starting...
Database initialized
fin-manager API server running on http://localhost:8080
Press Ctrl+C to stop
```

---

## How to Use

### Web Application
Navigate to **http://localhost:8080** in your browser

### API Testing
Swagger API documentation: **http://localhost:8080/swagger-ui.html**

Example API calls:
```bash
# Health check
curl http://localhost:8080/api/health

# Get all categories
curl http://localhost:8080/api/categories

# Get all expenses
curl http://localhost:8080/api/expenses
```

---

## Development

### Run Development Server (Auto-reload)

For backend development:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.finmanager.Main"
```

For frontend development (hot reload):
```bash
cd src/main/frontend
npm start
```
Runs on **http://localhost:3000** (requires backend on port 8080)

### Run Tests

```bash
mvn test
```

### Making Changes

**Backend:**
- Edit files in `src/main/java/com/finmanager/`
- Run `mvn compile` to verify
- Run `mvn test` to test

**Frontend:**
- Edit files in `src/main/frontend/src/`
- Rebuild with `mvn clean package`

---

## Project Structure

```
fin-manager/
├── src/main/
│   ├── java/com/finmanager/        # Java backend
│   │   ├── Main.java               # Entry point
│   │   ├── api/                    # REST API endpoints
│   │   ├── service/                # Business logic
│   │   ├── db/                     # Database layer (SQLite)
│   │   ├── model/                  # Data models
│   │   ├── util/                   # Utilities
│   │   ├── config/                 # API documentation config
│   │   └── server/                 # Embedded HTTP server
│   │
│   └── frontend/                   # React frontend
│       ├── src/
│       │   ├── components/         # React components
│       │   ├── context/            # State management
│       │   ├── api/                # API client
│       │   └── index.js            # React entry point
│       └── package.json
│
├── src/test/java/                  # Unit & integration tests
├── pom.xml                         # Maven configuration
├── build.bat                       # Windows build script
└── README.md                       # This file
```

---

## Technology Stack

### Backend
- **Language:** Java 21
- **Database:** SQLite 3
- **JSON Processing:** Gson 2.10.1
- **API Documentation:** Swagger/OpenAPI 3.0
- **Server:** Embedded (Jetty/equivalent)
- **Testing:** JUnit 4

### Frontend
- **Framework:** React 18
- **CSS:** Bootstrap 5
- **Build Tool:** react-scripts

### Build & Deployment
- **Build System:** Apache Maven
- **Node.js:** v20.10.0 (auto-installed)
- **npm:** 10.2.3 (auto-installed)

---

## Database

- **Type:** SQLite (file-based, no setup required)
- **Auto-initialization:** Database schema is created automatically on first run
- **No external setup needed** - Everything is managed automatically

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `mvn: command not found` | Install Maven and add to PATH |
| Port 8080 already in use | Close other applications on that port or modify `EmbeddedServer.java` |
| Tests fail | Run `mvn clean test` to ensure fresh build |
| Frontend build fails | Delete `src/main/frontend/package-lock.json` and retry |
| Changes not taking effect | Restart the server after modifications |

---

## API Documentation

Complete API documentation available at: **http://localhost:8080/swagger-ui.html**

All endpoints (except health check) require authentication via Authorization header.

---

## Build Scripts

### Windows
```bash
./build.bat
```

### Linux/Mac
```bash
./build.sh
```

---

## Contributing

1. Create a feature branch
2. Make changes and run tests (`mvn test`)
3. Submit pull request

---

## Project Documentation

For detailed architecture and setup information, see [.PROJECT_CONTEXT.md](.PROJECT_CONTEXT.md)

---

## Performance

- **Startup Time:** ~5-10 seconds
- **Memory Usage:** ~256MB (JVM + application)
- **Typical Response Time:** <100ms for API calls

---

## License

[Add your license information here]

---

## Support

For issues or questions, please check:
1. [.PROJECT_CONTEXT.md](.PROJECT_CONTEXT.md) - Detailed architecture & troubleshooting
2. **http://localhost:8080/swagger-ui.html** - API documentation
3. GitHub Issues - For bug reports

---

**Last Updated:** February 18, 2026
