package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import managers.BudgetManager;
import model.Transaction;
import model.Expense;
import model.Income;
import model.Category;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TransactionHandler implements HttpHandler {
    private final Gson gson;
    private final BudgetManager budgetManager;
    
    public TransactionHandler() {
        this.budgetManager = Main.getBudgetManager();
        this.gson = Main.getGson();
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
        // Parse request body
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, Object> data = gson.fromJson(requestBody, Map.class);
        
        // Extract transaction data
        String type = ((String) data.get("type")).toLowerCase();
        double amount = Double.parseDouble(data.get("amount").toString());
        String description = (String) data.get("description");
        LocalDate date = LocalDate.parse((String) data.get("date"));
        
        // Create and add transaction
        Transaction transaction;
        if ("expense".equals(type)) {
            Category category = Category.valueOf(((String) data.get("category")).toUpperCase());
            transaction = new Expense(amount, description, date, category);
        } else if ("income".equals(type)) {
            String source = (String) data.get("source");
            transaction = new Income(amount, description, date, source);
        } else {
            sendResponse(exchange, 400, "Invalid transaction type");
            return;
        }
        
        budgetManager.addTransaction(transaction);
        Main.saveData(); // Persist changes
        
        sendJsonResponse(exchange, 201, Map.of("message", "Transaction added successfully", "id", transaction.getId()));
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
            Main.saveData(); // Persist changes
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
        String response = gson.toJson(data);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
