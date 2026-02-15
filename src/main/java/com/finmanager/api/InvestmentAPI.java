package com.finmanager.api;

import com.finmanager.model.InvestmentEntry;
import com.finmanager.service.InvestmentService;
import com.google.gson.Gson;

import java.time.Year;
import java.util.List;

public class InvestmentAPI {
    private final InvestmentService investmentService;
    private final Gson gson;

    public InvestmentAPI() {
        this.investmentService = InvestmentService.getInstance();
        this.gson = new Gson();
    }

    public String getAllInvestments() {
        List<InvestmentEntry> entries = investmentService.getAllInvestments();
        return gson.toJson(entries);
    }

    public String getInvestmentsByYear(int year) {
        List<InvestmentEntry> entries = investmentService.getInvestmentsByYear(Year.of(year));
        return gson.toJson(entries);
    }

    public String createInvestmentEntry(String json) {
        try {
            InvestmentEntry entry = gson.fromJson(json, InvestmentEntry.class);
            Long id = investmentService.createInvestmentEntry(entry);
            entry.setId(id);
            return gson.toJson(entry);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to create investment: " + e.getMessage()));
        }
    }

    public String updateInvestmentEntry(String json) {
        try {
            InvestmentEntry entry = gson.fromJson(json, InvestmentEntry.class);
            investmentService.updateInvestmentEntry(entry);
            return gson.toJson(entry);
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to update investment: " + e.getMessage()));
        }
    }

    public String deleteInvestmentEntry(Long id) {
        try {
            investmentService.deleteInvestmentEntry(id);
            return gson.toJson(new CategoryAPI.ApiResponse("Investment deleted successfully"));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to delete investment: " + e.getMessage()));
        }
    }

    public String getTotalInvestmentsByYear(int year) {
        Double total = investmentService.getTotalInvestmentsByYear(Year.of(year));
        return gson.toJson(new ExpenseAPI.TotalResponse(total));
    }

    public String calculateCAGR(String json) {
        try {
            CAGRRequest request = gson.fromJson(json, CAGRRequest.class);
            Double cagr = investmentService.calculateCAGR(request.startValue, request.endValue, request.years);
            return gson.toJson(new CAGRResponse(cagr));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to calculate CAGR: " + e.getMessage()));
        }
    }

    public String projectInvestmentValue(String json) {
        try {
            ProjectionRequest request = gson.fromJson(json, ProjectionRequest.class);
            Double projected = investmentService.projectInvestmentValue(
                request.currentValue, request.annualContribution, request.expectedReturn, request.years
            );
            return gson.toJson(new ProjectionResponse(projected));
        } catch (Exception e) {
            return gson.toJson(new CategoryAPI.ApiError("Failed to project investment: " + e.getMessage()));
        }
    }

    public static class CAGRRequest {
        public Double startValue;
        public Double endValue;
        public int years;
    }

    public static class CAGRResponse {
        public Double cagr;
        public CAGRResponse(Double cagr) {
            this.cagr = cagr;
        }
    }

    public static class ProjectionRequest {
        public Double currentValue;
        public Double annualContribution;
        public Double expectedReturn;
        public int years;
    }

    public static class ProjectionResponse {
        public Double projectedValue;
        public ProjectionResponse(Double projectedValue) {
            this.projectedValue = projectedValue;
        }
    }
}
