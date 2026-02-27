# Financial Manager - Phase Planning & Context

**Last Updated:** February 27, 2026
**Current Status:** Phase 2 Complete - BDD tests verified

---

## Completed Work

### Phase 1: Month Selection & Recurring Expenses ✅
**Status:** Complete, PR #12 merged
**Branch:** feature/phase-1-month-selection
**All BDD Tests:** Passing

**Features:**
- MonthSelector React component (Previous/Next/Today buttons)
- Month-based expense filtering (YYYY-MM format)
- Recurring expense toggle in AddExpenseForm
- Conditional frequency and end-date fields
- Custom GsonUtil for Java 21 LocalDate serialization
- H2 in-memory database for test isolation
- Comprehensive Cucumber BDD tests (13 scenarios across 3 features)

**Key Files:**
- Frontend: `MonthSelector.js`, modified `Dashboard.js`, modified `AddExpenseForm.js`
- Backend: `GsonUtil.java`, modified `Expense.java`, modified `RecurringExpenseAPI.java`
- Tests: `Phase1BDDRunner.java`, `Phase1StepDefinitions.java`, 3 feature files
- Database: H2 isolation configured in `BaseTest.java`

---

### Phase 2: Auto-Generation of Recurring Instances ✅
**Status:** Complete, verified with BDD tests
**Branch:** feature/phase-2-recurring-generation
**All BDD Tests:** Passing (17 scenarios)

**Features:**
- RecurringExpenseGenerator service (Monthly/Yearly generation)
- Manual and bulk generation API endpoints
- Last generated date tracking in database
- RecurringExpenseList React component for UI management
- Idempotency checks to avoid duplicate expenses
- Phase 2 BDD test suite

**Key Files:**
- Backend: `RecurringExpenseGenerator.java`, `RecurringExpenseAPI.java`, `RecurringExpenseService.java` (updated)
- Frontend: `RecurringExpenseList.js`, `AddExpenseForm.js`
- Tests: `Phase2BDDRunner.java`, `Phase2StepDefinitions.java`, `RecurringGeneration.feature`

---

## Remaining Phases (3-7)

### Phase 3: Category Management UI
**Objective:** Full CRUD UI for expense categories

**Features:**
- List categories with color indicators and type (MANDATORY/LEISURE/INVESTMENTS)
- Create new category form
- Edit category (name, color, type, active status)
- Delete category with validation (check if expenses exist)
- Color picker component
- Category filtering/search

**Technical:**
- React component: `CategoryManagement.js`
- Modal forms for create/edit
- API integration with existing CategoryAPI
- BDD tests for all scenarios

---

### Phase 4: Analytics & Visualization
**Objective:** Charts and insights for expense trends

**Features:**
- Monthly expense breakdown chart (bar chart)
- Category-wise pie chart
- Spending trends (last 6 months)
- Top categories by spending
- Budget vs actual comparison
- Year-over-year comparison

**Technical:**
- Use React library: Chart.js or Recharts
- Backend: Enhanced AnalyticsAPI with aggregation queries
- BDD tests for analytics calculations

---

### Phase 5: Trend Analysis
**Objective:** Smart analysis of spending patterns

**Features:**
- Average spending by category
- Month-over-month change
- Unusual spending detection
- Savings/surplus tracking
- Projected annual spending
- Category trends

**Technical:**
- Analytics service enhancements
- Statistical calculations (mean, median, deviation)
- Alerts for unusual patterns

---

### Phase 6: Investment Tracking
**Objective:** Track investments separately from expenses

**Features:**
- Investment entry form (currency, exchange rate support)
- Portfolio view with holdings
- Return on investment (ROI) calculation
- Investment history/timeline
- Currency conversion support

**Technical:**
- Uses existing InvestmentEntry model
- Enhanced InvestmentAPI with calculations
- BDD tests

---

### Phase 7: UI Polish & Documentation
**Objective:** Final refinements and complete documentation

**Features:**
- Responsive design improvements
- Dark mode support (optional)
- Accessibility (ARIA labels, keyboard navigation)
- Error handling and user feedback
- Loading states and animations
- Mobile optimization

**Documentation:**
- API documentation (Swagger)
- User guide
- Architecture documentation
- Setup/deployment guide

---

## Key Context for Implementation

### Technology Stack
- **Backend:** Java 21, embedded HTTP server
- **Frontend:** React 18, Bootstrap 5.3
- **Database:** SQLite (production), H2 (testing)
- **Testing:** JUnit 4, Cucumber 7.14.0
- **Logging:** SLF4J 2.0.13 + Logback 1.5.6
- **JSON:** Gson 2.10.1 (custom LocalDate adapter via GsonUtil)
- **Build:** Maven 3.9+

### Development Workflow
1. **Branching:** `feature/phase-X-description`
2. **PR Process:** Create PR with `gh pr create`, include detailed description
3. **Testing:** All features require BDD tests before merge
4. **Commits:** Clear, descriptive commit messages with feature list
5. **Main Protection:** PRs required, branch protected

### Database Considerations
- SQLite for production (fin-manager.db)
- H2 in-memory for tests (isolated per test run)
- BaseTest handles setup/teardown with H2
- All existing tables: categories, expenses, recurring_expenses, investment_entries
- Foreign key constraints active

### API Endpoints (Current)
- `/api/categories` - Category CRUD
- `/api/expenses` - Expense CRUD (month-filtered via ?month=YYYY-MM)
- `/api/recurring` - Recurring expense CRUD
- `/api/investments` - Investment CRUD
- `/api/analytics` - Analytics/summaries

### Frontend Structure
- `App.js` - Main component, auth routing
- `components/` - LoginPage, Dashboard, AddExpenseForm, MonthSelector, etc.
- `context/AuthContext.js` - Auth state management (dummy implementation)
- `api/ApiClient.js` - HTTP communication
- Static files served from `src/main/frontend/public/`

### Important Notes
1. **LocalDate Serialization:** Use GsonUtil.getInstance() - custom adapter handles Java 21 module system
2. **Month Parameter:** Always YYYY-MM format for month-based filtering
3. **Test Database:** H2 is per-test, SQLite is persistent - tests don't pollute main DB
4. **Recurring Expense API:** POST/PUT routes to /api/recurring, GET supports filtering
5. **Server Port:** 8080 (hardcoded in EmbeddedServer.java)
6. **Logging:** SLF4J with class names via Logger utility class

### Build & Run Commands
```powershell
# Build
mvn clean package -DskipTests

# Run tests (all)
mvn test

# Run BDD tests only
mvn test -Dtest=Phase1BDDRunner

# Run application
java -jar target/fin-manager.jar

# Access application
http://localhost:8080
```

### GitHub Commands
```powershell
# Create PR with details
gh pr create --title "Title" --body "Description" --base main

# Merge PR
gh pr merge <PR_NUMBER> --admin

# View PR status
gh pr view <PR_NUMBER>
```

---

## Tomorrow's Checklist

- [ ] PR #12 review and merge to main
- [ ] Rebase from main to start Phase 2
- [ ] Create feature/phase-2-recurring-generation branch
- [ ] Implement RecurringExpenseGenerator service
- [ ] Create Phase 2 BDD feature files
- [ ] Write step definitions
- [ ] Test and commit
- [ ] Create PR #13

---

## Quick Reference: File Locations

**Backend Java:**
- Models: `src/main/java/com/finmanager/model/`
- Services: `src/main/java/com/finmanager/service/`
- APIs: `src/main/java/com/finmanager/api/`
- Utilities: `src/main/java/com/finmanager/util/`
- Database: `src/main/java/com/finmanager/db/`

**Frontend React:**
- Components: `src/main/frontend/src/components/`
- Context: `src/main/frontend/src/context/`
- API Client: `src/main/frontend/src/api/`

**Tests:**
- Unit/Integration: `src/test/java/com/finmanager/`
- BDD: `src/test/java/com/finmanager/bdd/`
- Feature Files: `src/test/resources/features/`

---

## Notes

- PC is slow: Always give commands time to complete (2-5 minutes for builds)
- Only use PowerShell cmdlets (Get-ChildItem, Get-Content, Start-Sleep, etc.)
- Application runs successfully at http://localhost:8080
- Database file: `fin-manager.db` (SQLite, persistent)
- Never skip the full build process - always `mvn clean package` for releases
