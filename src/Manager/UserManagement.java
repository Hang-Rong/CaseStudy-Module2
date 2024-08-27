package Manager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManagement {
    private Map<String, String> users; // Stores username and password
    private File userFile;

    public UserManagement(String userFilePath) {
        this.userFile = new File(userFilePath);
        this.users = new HashMap<>();
        loadUsers();
    }

    // Load users from the file
    private void loadUsers() {
        if (userFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        users.put(parts[0], parts[1]);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading user data: " + e.getMessage());
            }
        }
    }

    // Save users to the file
    public void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    // Register a new user
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    // Authenticate a user
    public boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}

