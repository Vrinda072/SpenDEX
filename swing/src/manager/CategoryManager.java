package manager;

import java.util.ArrayList;
import java.util.List;

/**
 * SpendEX — CategoryManager
 * Manages expense categories for SpendEX
 */
public class CategoryManager {
    private List<String> categories;

    // Constructor
    public CategoryManager() {
        this.categories = new ArrayList<>();
        initializeDefaultCategories();
    }

    // Initialize default categories
    private void initializeDefaultCategories() {
        categories.add("Food");
        categories.add("Travel");
        categories.add("Bills");
        categories.add("Entertainment");
        categories.add("Other");
    }

    // Get all categories
    public List<String> getAllCategories() {
        return new ArrayList<>(categories);
    }

    // Add custom category
    public void addCategory(String category) {
        if (category != null && !category.isEmpty() && !categories.contains(category)) {
            categories.add(category);
        }
    }

    // Remove category
    public void removeCategory(String category) {
        categories.remove(category);
    }

    // Check if category exists
    public boolean hasCategory(String category) {
        return categories.contains(category);
    }

    // Get default categories
    public String[] getDefaultCategories() {
        return categories.toArray(new String[0]);
    }
}
