package com.finmanager.integration;

import com.finmanager.model.Category;
import com.finmanager.model.Expense;
import com.finmanager.model.RecurringExpense;
import com.finmanager.model.InvestmentEntry;
import com.finmanager.service.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.Year;

public class FinManagerIntegrationTest {
    private CategoryService categoryService;
    private ExpenseService expenseService;
    private RecurringExpenseService recurringService;
    private InvestmentService investmentService;
    private AnalyticsService analyticsService;

    @Before
    public void setUp() {
        categoryService = CategoryService.getInstance();
        expenseService = ExpenseService.getInstance();
        recurringService = RecurringExpenseService.getInstance();
        investmentService = InvestmentService.getInstance();
        analyticsService = AnalyticsService.getInstance();
    }

    @Test
    public void testCompleteWorkflow() {
        Category mandatory = new Category("Rent", "MANDATORY", "#FF0000");
        Long mandatoryId = categoryService.createCategory(mandatory);
        assertNotNull(mandatoryId);

        Category leisure = new Category("Entertainment", "LEISURE", "#00FF00");
        Long leisureId = categoryService.createCategory(leisure);
        assertNotNull(leisureId);

        Category investments = new Category("Savings", "INVESTMENTS", "#0000FF");
        Long investmentsId = categoryService.createCategory(investments);
        assertNotNull(investmentsId);

        YearMonth currentMonth = YearMonth.now();
        expenseService.createExpense(new Expense(
            currentMonth.atDay(1), 1200.0, mandatoryId, "Monthly Rent"
        ));
        expenseService.createExpense(new Expense(
            currentMonth.atDay(15), 100.0, leisureId, "Movie Tickets"
        ));

        RecurringExpense monthlyBill = new RecurringExpense(
            mandatoryId, 150.0, "Internet Bill", 
            RecurringExpense.Frequency.MONTHLY, LocalDate.now()
        );
        recurringService.createRecurringExpense(monthlyBill);

        Double monthlyTotal = analyticsService.getMonthlyTotal(currentMonth);
        assertTrue(monthlyTotal >= 1300.0);

        var breakdown = analyticsService.getCategoryBreakdown(currentMonth);
        assertTrue(breakdown.size() > 0);

        InvestmentEntry investment = new InvestmentEntry(
            LocalDate.now(), 5000.0, "USD", "Monthly Contribution"
        );
        investmentService.createInvestmentEntry(investment);

        Double yearlyInvestments = investmentService.getTotalInvestmentsByYear(Year.now());
        assertTrue(yearlyInvestments >= 5000.0);
    }
}
