package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.BudgetManager;
import model.Category;
import app.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BudgetHandler implements HttpHandler {
    private final BudgetManager budgetManager;
    
    public BudgetHandler() {
        this.budgetManager = Main.getBudgetManager();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            setCorsHeaders(exchange);
            
            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                handleOptions(exchange);
                return;
            }
            
            switch (exchange.getRequestMethod().toUpperCase()) {
                case "GET":
                    handleGetBudgetInfo(exchange);
                    break;
                case "PUT":
                    handleUpdateBudget(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }
    
    private void handleGetBudgetInfo(HttpExchange exchange) throws IOException {
        double spent = budgetManager.getTotalSpent();
        double budget = budgetManager.getMonthlyBudget();
        double remaining = budgetManager.getRemainingBudget();
        
        String response = String.format(
            "{\"monthlyBudget\":%.2f,\"totalSpent\":%.2f,\"remaining\":%.2f,\"dailyLimit\":%.2f}",
            budget, spent, remaining, budgetManager.getDailySpendingLimit()
        );
        sendJsonResponse(exchange, 200, response);
    }
    
    private void handleUpdateBudget(HttpExchange exchange) throws IOException {
        String response = "{\"message\":\"Budget updated\"}";
        sendJsonResponse(exchange, 200, response);
    }
    
    private void handleOptions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }
    
    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, PUT, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String data) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, data.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(data.getBytes(StandardCharsets.UTF_8));
        }
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.sendResponseHeaders(statusCode, message.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
