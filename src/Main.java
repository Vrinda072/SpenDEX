import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.BudgetManager;
import model.Transaction;
import model.CalendarEvent;
import model.Category;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class Main {
    private static final int PORT = 8000;
    private static final String DATA_DIR = "data";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.json";
    private static final String EVENTS_FILE = DATA_DIR + "/events.json";
    
    private static BudgetManager budgetManager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    public static void main(String[] args) {
        try {
            // Create data directory if it doesn't exist
            new File(DATA_DIR).mkdirs();
            
            // Initialize BudgetManager
            budgetManager = new BudgetManager(3000); // Default monthly budget
            
            // Load existing data
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
            
            System.out.println("Server started on port " + PORT);
            System.out.println("Open http://localhost:" + PORT + " in your browser");
            
            // Add shutdown hook to save data
            Runtime.getRuntime().addShutdownHook(new Thread(Main::saveData));
            
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void loadData() {
        try {
            // Load transactions
            File transactionsFile = new File(TRANSACTIONS_FILE);
            if (transactionsFile.exists()) {
                try (Reader reader = new FileReader(transactionsFile, StandardCharsets.UTF_8)) {
                    Transaction[] transactions = gson.fromJson(reader, Transaction[].class);
                    if (transactions != null) {
                        for (Transaction t : transactions) {
                            budgetManager.addTransaction(t);
                        }
                    }
                }
            }
            
            // Load events
            File eventsFile = new File(EVENTS_FILE);
            if (eventsFile.exists()) {
                try (Reader reader = new FileReader(eventsFile, StandardCharsets.UTF_8)) {
                    CalendarEvent[] events = gson.fromJson(reader, CalendarEvent[].class);
                    if (events != null) {
                        for (CalendarEvent e : events) {
                            budgetManager.addEvent(e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    private static void saveData() {
        try {
            // Save transactions
            try (Writer writer = new FileWriter(TRANSACTIONS_FILE, StandardCharsets.UTF_8)) {
                gson.toJson(budgetManager.getAllTransactions(), writer);
            }
            
            // Save events
            try (Writer writer = new FileWriter(EVENTS_FILE, StandardCharsets.UTF_8)) {
                gson.toJson(budgetManager.getUpcomingEvents(365), writer); // Save events for next year
            }
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    // Getters for handlers
    public static BudgetManager getBudgetManager() {
        return budgetManager;
    }
    
    public static Gson getGson() {
        return gson;
    }
}
