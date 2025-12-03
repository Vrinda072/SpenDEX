package app;

import com.sun.net.httpserver.HttpServer;
import managers.BudgetManager;
import model.Transaction;
import model.CalendarEvent;
import model.Category;
import web.StaticFileHandler;
import web.TransactionHandler;
import web.EventHandler;
import web.BudgetHandler;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 8000;
    private static final String DATA_DIR = "data";
    private static final String CURRENCY = "AED"; // Default currency: UAE Dirham
    private static final double DEFAULT_BUDGET = 5000.0; // Default monthly budget
    
    private static BudgetManager budgetManager;

    public static void main(String[] args) {
        try {
            // Create data directory if it doesn't exist
            new File(DATA_DIR).mkdirs();
            
            // Initialize BudgetManager with default budget (3000 AED)
            budgetManager = new BudgetManager(DEFAULT_BUDGET);
            
            // Load existing data (in-memory only, no persistence)
            loadData();
            
            // Create HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Set up context handlers
            server.createContext("/", new StaticFileHandler("web"));
            server.createContext("/api/transactions", new TransactionHandler());
            server.createContext("/api/events", new EventHandler());
            server.createContext("/api/budget", new BudgetHandler());
            
            // Set executor and start server
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║  SpenDEX Server Started            ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  Port: " + PORT + "                        ║");
            System.out.println("║  URL: http://localhost:" + PORT + "       ║");
            System.out.println("║  Currency: " + CURRENCY + "                     ║");
            System.out.println("║  Budget: " + DEFAULT_BUDGET + " " + CURRENCY + "            ║");
            System.out.println("║  Status: Ready                     ║");
            System.out.println("╚════════════════════════════════════╝");
            
            // Add shutdown hook to save data
            Runtime.getRuntime().addShutdownHook(new Thread(Main::saveData));
            
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void loadData() {
        // In-memory only - no file persistence needed
        // BudgetManager starts empty and uses in-memory ArrayList
    }
    
    private static void saveData() {
        // In-memory only - data is lost on shutdown
        // This is sufficient for the application use case
    }
    
    // Getters for handlers
    public static BudgetManager getBudgetManager() {
        return budgetManager;
    }
    
    public static String getCurrency() {
        return CURRENCY;
    }
}
