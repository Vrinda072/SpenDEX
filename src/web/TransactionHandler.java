package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.BudgetManager;
import model.Transaction;
import model.Expense;
import model.Income;
import model.Category;
import app.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TransactionHandler implements HttpHandler {
    private final BudgetManager budgetManager;
    
    public TransactionHandler() {
        this.budgetManager = Main.getBudgetManager();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            // Set CORS headers
            setCorsHeaders(exchange);
            
            // Handle OPTIONS request for CORS preflight
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                handleOptions(exchange);
                return;
            }
            
            // Route to appropriate handler method
            switch (exchange.getRequestMethod().toUpperCase()) {
                case "GET":
                    handleGetTransactions(exchange);
                    break;
                case "POST":
                    handleAddTransaction(exchange);
                    break;
                case "DELETE":
                    handleDeleteTransaction(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }
    
    private void handleGetTransactions(HttpExchange exchange) throws IOException {
        Map<String, String> params = WebUtils.parseQueryParams(exchange.getRequestURI().getQuery());
        
        // Get transactions with optional filters
        List<Transaction> transactions;
        
        if (params.containsKey("month") && params.containsKey("year")) {
            int month = Integer.parseInt(params.get("month"));
            int year = Integer.parseInt(params.get("year"));
            transactions = budgetManager.getTransactionsByMonth(month, year);
        } else if (params.containsKey("category")) {
            Category category = Category.valueOf(params.get("category").toUpperCase());
            transactions = budgetManager.getTransactionsByCategory(category);
        } else if (params.containsKey("type")) {
            String type = params.get("type").toLowerCase();
            if ("expense".equals(type)) {
                transactions = budgetManager.getExpenses();
            } else if ("income".equals(type)) {
                transactions = budgetManager.getIncomes();
            } else {
                sendResponse(exchange, 400, "Invalid transaction type");
                return;
            }
        } else {
            // Return all transactions if no filters
            transactions = budgetManager.getAllTransactions();
        }
        
        sendJsonResponse(exchange, 200, transactions);
    }
    
    private void handleAddTransaction(HttpExchange exchange) throws IOException {
        // Parse simple key=value request body format
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> data = parseFormData(requestBody);
        
        // Validate all required fields are provided by user
        if (!data.containsKey("type") || data.get("type").trim().isEmpty()) {
            sendResponse(exchange, 400, "Error: type is required (expense or income)");
            return;
        }
        
        if (!data.containsKey("amount") || data.get("amount").trim().isEmpty()) {
            sendResponse(exchange, 400, "Error: amount is required");
            return;
        }
        
        if (!data.containsKey("description") || data.get("description").trim().isEmpty()) {
            sendResponse(exchange, 400, "Error: description is required");
            return;
        }
        
        if (!data.containsKey("date") || data.get("date").trim().isEmpty()) {
            sendResponse(exchange, 400, "Error: date is required (format: YYYY-MM-DD)");
            return;
        }
        
        if (!data.containsKey("category") || data.get("category").trim().isEmpty()) {
            sendResponse(exchange, 400, "Error: category is required");
            return;
        }
        
        try {
            // Extract transaction data - all from user input
            String type = data.get("type").toLowerCase();
            double amount = Double.parseDouble(data.get("amount"));
            String description = data.get("description");
            LocalDate date = LocalDate.parse(data.get("date"));
            
            if (amount <= 0) {
                sendResponse(exchange, 400, "Error: amount must be greater than 0");
                return;
            }
            
            // Create and add transaction
            Transaction transaction;
            if ("expense".equals(type)) {
                Category category = Category.valueOf(data.get("category").toUpperCase());
                transaction = new Expense(amount, description, date, category);
            } else if ("income".equals(type)) {
                Category category = Category.valueOf(data.get("category").toUpperCase());
                transaction = new Income(amount, description, date, category);
            } else {
                sendResponse(exchange, 400, "Error: type must be 'expense' or 'income'");
                return;
            }
            
            budgetManager.addTransaction(transaction);
            
            String response = "{\"message\": \"Transaction added\", \"id\": \"" + transaction.getId() + "\"}";
            sendJsonResponse(exchange, 201, response);
        } catch (NumberFormatException e) {
            sendResponse(exchange, 400, "Error: invalid amount (must be a number)");
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, "Error: invalid category or date format");
        }
    }
    
    private void handleDeleteTransaction(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String id = path.substring(path.lastIndexOf('/') + 1);
        
        if (id == null || id.isEmpty()) {
            sendResponse(exchange, 400, "Transaction ID is required");
            return;
        }
        
        boolean removed = budgetManager.removeTransaction(id);
        if (removed) {
            sendResponse(exchange, 200, "Transaction deleted successfully");
        } else {
            sendResponse(exchange, 404, "Transaction not found");
        }
    }
    
    private void handleOptions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type,Authorization");
        exchange.sendResponseHeaders(204, -1);
    }
    
    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type,Authorization");
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        String response;
        if (data instanceof String) {
            response = (String) data;
        } else if (data instanceof List) {
            response = listToJson((List<?>) data);
        } else {
            response = data.toString();
        }
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
    
    private String listToJson(List<?> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(",");
            Object item = list.get(i);
            if (item instanceof Transaction) {
                Transaction t = (Transaction) item;
                sb.append(String.format("{\"id\":\"%s\",\"type\":\"%s\",\"amount\":%.2f,\"title\":\"%s\",\"date\":\"%s\",\"category\":\"%s\"}",
                    t.getId(), t.getType(), t.getAmount(), escape(t.getTitle()), t.getDate(), t.getCategory()));
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    private Map<String, String> parseFormData(String body) {
        Map<String, String> data = new java.util.HashMap<>();
        if (body == null || body.isEmpty()) return data;
        
        for (String pair : body.split("&")) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = pair.substring(0, idx);
                String value = pair.substring(idx + 1);
                try {
                    value = java.net.URLDecoder.decode(value, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    // Keep original value
                }
                data.put(key, value);
            }
        }
        return data;
    }
    
    private String escape(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"").replace("\n", "\\n");
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
