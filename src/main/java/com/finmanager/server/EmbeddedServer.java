package com.finmanager.server;

import com.finmanager.api.*;
import com.finmanager.config.SwaggerConfig;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EmbeddedServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private final CategoryAPI categoryAPI;
    private final ExpenseAPI expenseAPI;
    private final RecurringExpenseAPI recurringAPI;
    private final InvestmentAPI investmentAPI;
    private final AnalyticsAPI analyticsAPI;
    private final Gson gson;

    public EmbeddedServer() {
        this.categoryAPI = new CategoryAPI();
        this.expenseAPI = new ExpenseAPI();
        this.recurringAPI = new RecurringExpenseAPI();
        this.investmentAPI = new InvestmentAPI();
        this.analyticsAPI = new AnalyticsAPI();
        this.gson = new Gson();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        
        // Category endpoints
        server.createContext("/api/categories", exchange -> handleCategories(exchange));
        server.createContext("/api/category", exchange -> handleCategory(exchange));
        
        // Expense endpoints
        server.createContext("/api/expenses", exchange -> handleExpenses(exchange));
        server.createContext("/api/expense", exchange -> handleExpense(exchange));
        server.createContext("/api/expenses/total", exchange -> handleExpenseTotal(exchange));
        
        // Recurring expense endpoints
        server.createContext("/api/recurring", exchange -> handleRecurring(exchange));
        
        // Investment endpoints
        server.createContext("/api/investments", exchange -> handleInvestments(exchange));
        server.createContext("/api/investment", exchange -> handleInvestment(exchange));
        server.createContext("/api/investments/cagr", exchange -> handleCAGR(exchange));
        server.createContext("/api/investments/projection", exchange -> handleProjection(exchange));
        
        // Analytics endpoints
        server.createContext("/api/analytics/breakdown", exchange -> handleAnalyticsBreakdown(exchange));
        server.createContext("/api/analytics/trend", exchange -> handleAnalyticsTrend(exchange));
        
        // Swagger/OpenAPI endpoints
        server.createContext("/api-docs", exchange -> handleApiDocs(exchange));
        server.createContext("/swagger-ui", exchange -> handleSwaggerUI(exchange));
        
        // Health check
        server.createContext("/api/health", exchange -> handleHealth(exchange));
        
        // Root endpoint
        server.createContext("/", exchange -> handleRoot(exchange));
        
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(10));
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void handleRoot(HttpExchange exchange) throws IOException {
        String response = "{\"message\": \"fin-manager API v1.0\", \"endpoints\": \"/api/categories, /api/expenses, /api/investments, /api/analytics\"}";
        sendResponse(exchange, 200, response);
    }

    private void handleHealth(HttpExchange exchange) throws IOException {
        String response = "{\"status\": \"healthy\"}";
        sendResponse(exchange, 200, response);
    }

    private void handleCategories(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = categoryAPI.getAllCategories();
            sendResponse(exchange, 200, response);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = categoryAPI.createCategory(body);
            sendResponse(exchange, 201, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleCategory(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = categoryAPI.updateCategory(body);
            sendResponse(exchange, 200, response);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Long id = extractId(query);
            String response = categoryAPI.deleteCategory(id);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleExpenses(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.contains("month")) {
                String month = extractParam(query, "month");
                response = expenseAPI.getExpensesByMonth(month);
            } else {
                response = "{\"error\": \"month parameter required\"}";
            }
            sendResponse(exchange, 200, response);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = expenseAPI.createExpense(body);
            sendResponse(exchange, 201, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleExpense(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = expenseAPI.updateExpense(body);
            sendResponse(exchange, 200, response);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Long id = extractId(query);
            String response = expenseAPI.deleteExpense(id);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleExpenseTotal(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String month = extractParam(query, "month");
            String response = expenseAPI.getTotalExpensesByMonth(month);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleRecurring(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = recurringAPI.getAllRecurringExpenses();
            sendResponse(exchange, 200, response);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = recurringAPI.createRecurringExpense(body);
            sendResponse(exchange, 201, response);
        } else if ("PUT".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = recurringAPI.updateRecurringExpense(body);
            sendResponse(exchange, 200, response);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Long id = extractId(query);
            String response = recurringAPI.deleteRecurringExpense(id);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleInvestments(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String response;
            if (query != null && query.contains("year")) {
                int year = Integer.parseInt(extractParam(query, "year"));
                response = investmentAPI.getInvestmentsByYear(year);
            } else {
                response = investmentAPI.getAllInvestments();
            }
            sendResponse(exchange, 200, response);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = investmentAPI.createInvestmentEntry(body);
            sendResponse(exchange, 201, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleInvestment(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = investmentAPI.updateInvestmentEntry(body);
            sendResponse(exchange, 200, response);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            Long id = extractId(query);
            String response = investmentAPI.deleteInvestmentEntry(id);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleCAGR(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = investmentAPI.calculateCAGR(body);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleProjection(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = getRequestBody(exchange);
            String response = investmentAPI.projectInvestmentValue(body);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleAnalyticsBreakdown(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String month = extractParam(query, "month");
            String response = analyticsAPI.getCategoryBreakdown(month);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private void handleAnalyticsTrend(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            int year = Integer.parseInt(extractParam(query, "year"));
            String response = analyticsAPI.getYearlyTrendByCategory(year);
            sendResponse(exchange, 200, response);
        } else {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }

    private String getRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        
        byte[] responseBytes = response.getBytes();
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    private Long extractId(String query) {
        if (query != null && query.contains("id=")) {
            String[] parts = query.split("=");
            if (parts.length > 1) {
                return Long.parseLong(parts[1]);
            }
        }
        return null;
    }

    private String extractParam(String query, String param) {
        if (query == null) return null;
        String[] params = query.split("&");
        for (String p : params) {
            if (p.startsWith(param + "=")) {
                return p.substring((param + "=").length());
            }
        }
        return null;
    }

    private void handleApiDocs(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        String response = SwaggerConfig.generateOpenAPIJson();
        sendResponse(exchange, 200, response);
    }

    private void handleSwaggerUI(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        String html = generateSwaggerUI();
        sendResponse(exchange, 200, html);
    }

    private String generateSwaggerUI() {
        return "<!DOCTYPE html>" +
            "<html>" +
            "<head>" +
            "  <title>Fin-Manager API Documentation</title>" +
            "  <meta charset=\"utf-8\"/>" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
            "  <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/4.15.5/swagger-ui.min.css\">" +
            "</head>" +
            "<body>" +
            "  <div id=\"swagger-ui\"></div>" +
            "  <script src=\"https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/4.15.5/swagger-ui.min.js\"></script>" +
            "  <script>" +
            "    window.onload = function() {" +
            "      SwaggerUIBundle({" +
            "        url: '/api-docs'," +
            "        dom_id: '#swagger-ui'," +
            "        presets: [" +
            "          SwaggerUIBundle.presets.apis," +
            "          SwaggerUIBundle.SwaggerUIStandalonePreset" +
            "        ]," +
            "        layout: 'StandaloneLayout'" +
            "      })" +
            "    }" +
            "  </script>" +
            "</body>" +
            "</html>";
    }
}
