package com.finmanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class SwaggerConfig {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String generateOpenAPIJson() {
        ObjectNode root = objectMapper.createObjectNode();

        // OpenAPI version
        root.put("openapi", "3.0.0");

        // Info object
        ObjectNode info = objectMapper.createObjectNode();
        info.put("title", "Fin-Manager API");
        info.put("description", "Personal Finance Management API");
        info.put("version", "1.0.0");
        root.set("info", info);

        // Servers
        ArrayNode servers = objectMapper.createArrayNode();
        ObjectNode server = objectMapper.createObjectNode();
        server.put("url", "http://localhost:8080");
        server.put("description", "Local Development Server");
        servers.add(server);
        root.set("servers", servers);

        // Paths
        ObjectNode paths = objectMapper.createObjectNode();

        // Category endpoints
        paths.set("/api/categories", createCategoryListEndpoint());
        paths.set("/api/category", createCategoryEndpoint());

        // Expense endpoints
        paths.set("/api/expenses", createExpenseListEndpoint());
        paths.set("/api/expense", createExpenseEndpoint());
        paths.set("/api/expenses/total", createExpenseTotalEndpoint());

        // Recurring expense endpoints
        paths.set("/api/recurring", createRecurringExpenseEndpoint());

        // Investment endpoints
        paths.set("/api/investments", createInvestmentListEndpoint());
        paths.set("/api/investment", createInvestmentEndpoint());
        paths.set("/api/investments/cagr", createCAGREndpoint());
        paths.set("/api/investments/projection", createProjectionEndpoint());

        // Analytics endpoints
        paths.set("/api/analytics/breakdown", createBreakdownEndpoint());
        paths.set("/api/analytics/monthly-total", createMonthlyTotalEndpoint());
        paths.set("/api/analytics/yearly-total", createYearlyTotalEndpoint());
        paths.set("/api/analytics/expense-trend", createExpenseTrendEndpoint());

        root.set("paths", paths);

        // Components (schemas)
        ObjectNode components = objectMapper.createObjectNode();
        ObjectNode schemas = objectMapper.createObjectNode();

        // Category schema
        schemas.set("Category", createCategorySchema());

        // Expense schema
        schemas.set("Expense", createExpenseSchema());

        // RecurringExpense schema
        schemas.set("RecurringExpense", createRecurringExpenseSchema());

        // Investment schema
        schemas.set("Investment", createInvestmentSchema());

        components.set("schemas", schemas);
        root.set("components", components);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        } catch (Exception e) {
            return "{}";
        }
    }

    private static ObjectNode createCategoryListEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get all categories");
        get.put("description", "Retrieve all expense categories");
        ObjectNode responses = objectMapper.createObjectNode();
        ObjectNode response200 = objectMapper.createObjectNode();
        response200.put("description", "Successful response");
        responses.set("200", response200);
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createCategoryEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        
        ObjectNode post = objectMapper.createObjectNode();
        post.put("summary", "Create category");
        post.put("description", "Create a new expense category");
        ObjectNode postResponses = objectMapper.createObjectNode();
        postResponses.set("201", objectMapper.createObjectNode().put("description", "Category created"));
        post.set("responses", postResponses);
        endpoint.set("post", post);

        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get category by ID");
        ObjectNode getResponses = objectMapper.createObjectNode();
        getResponses.set("200", objectMapper.createObjectNode().put("description", "Successful response"));
        get.set("responses", getResponses);
        endpoint.set("get", get);

        ObjectNode put = objectMapper.createObjectNode();
        put.put("summary", "Update category");
        ObjectNode putResponses = objectMapper.createObjectNode();
        putResponses.set("200", objectMapper.createObjectNode().put("description", "Category updated"));
        put.set("responses", putResponses);
        endpoint.set("put", put);

        ObjectNode delete = objectMapper.createObjectNode();
        delete.put("summary", "Delete category");
        ObjectNode deleteResponses = objectMapper.createObjectNode();
        deleteResponses.set("204", objectMapper.createObjectNode().put("description", "Category deleted"));
        delete.set("responses", deleteResponses);
        endpoint.set("delete", delete);

        return endpoint;
    }

    private static ObjectNode createExpenseListEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get all expenses");
        get.put("description", "Retrieve all expenses with optional pagination");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Successful response"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createExpenseEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        
        ObjectNode post = objectMapper.createObjectNode();
        post.put("summary", "Create expense");
        ObjectNode postResponses = objectMapper.createObjectNode();
        postResponses.set("201", objectMapper.createObjectNode().put("description", "Expense created"));
        post.set("responses", postResponses);
        endpoint.set("post", post);

        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get expense by ID");
        ObjectNode getResponses = objectMapper.createObjectNode();
        getResponses.set("200", objectMapper.createObjectNode().put("description", "Successful response"));
        get.set("responses", getResponses);
        endpoint.set("get", get);

        ObjectNode put = objectMapper.createObjectNode();
        put.put("summary", "Update expense");
        ObjectNode putResponses = objectMapper.createObjectNode();
        putResponses.set("200", objectMapper.createObjectNode().put("description", "Expense updated"));
        put.set("responses", putResponses);
        endpoint.set("put", put);

        ObjectNode delete = objectMapper.createObjectNode();
        delete.put("summary", "Delete expense");
        ObjectNode deleteResponses = objectMapper.createObjectNode();
        deleteResponses.set("204", objectMapper.createObjectNode().put("description", "Expense deleted"));
        delete.set("responses", deleteResponses);
        endpoint.set("delete", delete);

        return endpoint;
    }

    private static ObjectNode createExpenseTotalEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get total expenses");
        get.put("description", "Calculate total expenses for a given month");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Total calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createRecurringExpenseEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        
        ObjectNode post = objectMapper.createObjectNode();
        post.put("summary", "Create recurring expense");
        ObjectNode postResponses = objectMapper.createObjectNode();
        postResponses.set("201", objectMapper.createObjectNode().put("description", "Recurring expense created"));
        post.set("responses", postResponses);
        endpoint.set("post", post);

        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get recurring expenses");
        ObjectNode getResponses = objectMapper.createObjectNode();
        getResponses.set("200", objectMapper.createObjectNode().put("description", "Successful response"));
        get.set("responses", getResponses);
        endpoint.set("get", get);

        return endpoint;
    }

    private static ObjectNode createInvestmentListEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get all investments");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Successful response"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createInvestmentEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        
        ObjectNode post = objectMapper.createObjectNode();
        post.put("summary", "Create investment");
        ObjectNode postResponses = objectMapper.createObjectNode();
        postResponses.set("201", objectMapper.createObjectNode().put("description", "Investment created"));
        post.set("responses", postResponses);
        endpoint.set("post", post);

        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get investment by ID");
        ObjectNode getResponses = objectMapper.createObjectNode();
        getResponses.set("200", objectMapper.createObjectNode().put("description", "Successful response"));
        get.set("responses", getResponses);
        endpoint.set("get", get);

        return endpoint;
    }

    private static ObjectNode createCAGREndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Calculate CAGR");
        get.put("description", "Calculate Compound Annual Growth Rate for an investment");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "CAGR calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createProjectionEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get investment projection");
        get.put("description", "Project future investment value");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Projection calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createBreakdownEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get category breakdown");
        get.put("description", "Get expense breakdown by category");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Breakdown calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createMonthlyTotalEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get monthly total");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Total calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createYearlyTotalEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get yearly total");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Total calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createExpenseTrendEndpoint() {
        ObjectNode endpoint = objectMapper.createObjectNode();
        ObjectNode get = objectMapper.createObjectNode();
        get.put("summary", "Get expense trend");
        get.put("description", "Get expense trend over time");
        ObjectNode responses = objectMapper.createObjectNode();
        responses.set("200", objectMapper.createObjectNode().put("description", "Trend calculated"));
        get.set("responses", responses);
        endpoint.set("get", get);
        return endpoint;
    }

    private static ObjectNode createCategorySchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode properties = objectMapper.createObjectNode();
        properties.set("id", objectMapper.createObjectNode().put("type", "integer").put("format", "int64"));
        properties.set("name", objectMapper.createObjectNode().put("type", "string"));
        properties.set("type", objectMapper.createObjectNode().put("type", "string"));
        properties.set("color", objectMapper.createObjectNode().put("type", "string"));
        properties.set("active", objectMapper.createObjectNode().put("type", "boolean"));
        schema.set("properties", properties);
        return schema;
    }

    private static ObjectNode createExpenseSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode properties = objectMapper.createObjectNode();
        properties.set("id", objectMapper.createObjectNode().put("type", "integer").put("format", "int64"));
        properties.set("date", objectMapper.createObjectNode().put("type", "string").put("format", "date"));
        properties.set("amount", objectMapper.createObjectNode().put("type", "number").put("format", "double"));
        properties.set("categoryId", objectMapper.createObjectNode().put("type", "integer").put("format", "int64"));
        properties.set("description", objectMapper.createObjectNode().put("type", "string"));
        schema.set("properties", properties);
        return schema;
    }

    private static ObjectNode createRecurringExpenseSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode properties = objectMapper.createObjectNode();
        properties.set("id", objectMapper.createObjectNode().put("type", "integer").put("format", "int64"));
        properties.set("categoryId", objectMapper.createObjectNode().put("type", "integer").put("format", "int64"));
        properties.set("amount", objectMapper.createObjectNode().put("type", "number").put("format", "double"));
        properties.set("description", objectMapper.createObjectNode().put("type", "string"));
        properties.set("frequency", objectMapper.createObjectNode().put("type", "string"));
        properties.set("startDate", objectMapper.createObjectNode().put("type", "string").put("format", "date"));
        properties.set("endDate", objectMapper.createObjectNode().put("type", "string").put("format", "date"));
        schema.set("properties", properties);
        return schema;
    }

    private static ObjectNode createInvestmentSchema() {
        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "object");
        ObjectNode properties = objectMapper.createObjectNode();
        properties.set("id", objectMapper.createObjectNode().put("type", "integer").put("format", "int64"));
        properties.set("name", objectMapper.createObjectNode().put("type", "string"));
        properties.set("type", objectMapper.createObjectNode().put("type", "string"));
        properties.set("amount", objectMapper.createObjectNode().put("type", "number").put("format", "double"));
        properties.set("currentValue", objectMapper.createObjectNode().put("type", "number").put("format", "double"));
        properties.set("purchaseDate", objectMapper.createObjectNode().put("type", "string").put("format", "date"));
        properties.set("description", objectMapper.createObjectNode().put("type", "string"));
        schema.set("properties", properties);
        return schema;
    }
}
