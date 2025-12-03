package web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.BudgetManager;
import model.CalendarEvent;
import app.Main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

public class EventHandler implements HttpHandler {
    private final BudgetManager budgetManager;
    
    public EventHandler() {
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
                    handleGetEvents(exchange);
                    break;
                case "POST":
                    handleAddEvent(exchange);
                    break;
                case "DELETE":
                    handleDeleteEvent(exchange);
                    break;
                default:
                    sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Internal Server Error: " + e.getMessage());
        }
    }
    
    private void handleGetEvents(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, 200, "[]");
    }
    
    private void handleAddEvent(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, 201, "{\"message\": \"Event added\"}");
    }
    
    private void handleDeleteEvent(HttpExchange exchange) throws IOException {
        sendResponse(exchange, 200, "Event deleted");
    }
    
    private void handleOptions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }
    
    private void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
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
