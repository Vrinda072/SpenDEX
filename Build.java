import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * SpenDEX Build Runner - Pure Java compilation and execution
 * No shell scripts needed!
 */
public class Build {
    public static void main(String[] args) throws Exception {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║    SpenDEX - Pure Java Builder     ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();

        // Step 1: Create bin directory
        Path binDir = Paths.get("bin");
        Files.createDirectories(binDir);
        System.out.println("[1/2] Created bin directory");

        // Step 2: Find all Java files
        List<String> javaFiles = new ArrayList<>();
        Files.walk(Paths.get("src"))
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> javaFiles.add(p.toString()));
        
        System.out.println("[2/3] Found " + javaFiles.size() + " Java files");

        // Step 3: Compile
        System.out.println("[3/4] Compiling...");
        ProcessBuilder pb = new ProcessBuilder("javac", "-d", "bin");
        pb.command().addAll(javaFiles);
        
        Process process = pb.start();
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            System.err.println("✗ Compilation failed!");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            System.exit(1);
        }
        
        System.out.println("✓ Compilation successful!");
        System.out.println();
        System.out.println("✓ Build complete!");
        System.out.println();
        System.out.println("To start the server, run:");
        System.out.println("  java -cp bin app.Main");
    }
}
