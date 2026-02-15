package com.finmanager.service;

import com.finmanager.model.InvestmentEntry;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Year;

public class InvestmentServiceTest {
    private InvestmentService investmentService;

    @Before
    public void setUp() {
        investmentService = InvestmentService.getInstance();
    }

    @Test
    public void testCreateInvestmentEntry() {
        InvestmentEntry entry = new InvestmentEntry(LocalDate.now(), 1000.0, "USD", "Initial investment");
        Long id = investmentService.createInvestmentEntry(entry);
        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    public void testGetInvestmentsByYear() {
        Year year = Year.now();
        investmentService.createInvestmentEntry(new InvestmentEntry(LocalDate.now(), 500.0, "USD", "Inv1"));
        investmentService.createInvestmentEntry(new InvestmentEntry(LocalDate.now(), 300.0, "EUR", "Inv2"));
        
        var entries = investmentService.getInvestmentsByYear(year);
        assertTrue(entries.size() >= 2);
    }

    @Test
    public void testGetTotalInvestmentsByYear() {
        Year year = Year.now();
        investmentService.createInvestmentEntry(new InvestmentEntry(LocalDate.now(), 1000.0, "USD", "Inv1"));
        investmentService.createInvestmentEntry(new InvestmentEntry(LocalDate.now(), 500.0, "USD", "Inv2"));
        
        Double total = investmentService.getTotalInvestmentsByYear(year);
        assertTrue(total >= 1500.0);
    }

    @Test
    public void testCalculateCAGR() {
        Double startValue = 10000.0;
        Double endValue = 13382.0;
        int years = 3;
        
        Double cagr = investmentService.calculateCAGR(startValue, endValue, years);
        assertTrue(cagr > 9.0 && cagr < 11.0);
    }

    @Test
    public void testProjectInvestmentValue() {
        Double currentValue = 10000.0;
        Double annualContribution = 1000.0;
        Double expectedReturn = 7.0;
        int years = 5;
        
        Double projected = investmentService.projectInvestmentValue(currentValue, annualContribution, expectedReturn, years);
        assertTrue(projected > currentValue);
    }

    @Test
    public void testMultiCurrencyConversion() {
        InvestmentEntry usd = new InvestmentEntry(LocalDate.now(), 1000.0, "USD", "USD Investment");
        usd.setExchangeRate(1.0);
        
        InvestmentEntry eur = new InvestmentEntry(LocalDate.now(), 850.0, "EUR", "EUR Investment");
        eur.setExchangeRate(1.1);
        
        Double usdBase = usd.getAmountInBaseCurrency();
        Double eurBase = eur.getAmountInBaseCurrency();
        
        assertEquals(1000.0, usdBase, 0.01);
        assertEquals(935.0, eurBase, 0.01);
    }
}
