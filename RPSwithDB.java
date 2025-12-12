package raxkpaa;
import java.sql.*;
import java.util.*;

public class RPSwithDB {
	static final String URL = "jdbc:mysql://localhost:3306/game_db";
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

            while (true) {
                System.out.println("\n===== ROCK - PAPER - SCISSORS =====");
                System.out.println("1. Play Game");
                System.out.println("2. View Game History");
                System.out.println("3. Exit");
                System.out.print("Choose: ");
                int choice = sc.nextInt();

                if (choice == 1) {
                    sc.nextLine(); 
                    System.out.print("Enter your choice (rock/paper/scissors): ");
                    String userChoice = sc.nextLine().toLowerCase();

                    String[] options = {"rock", "paper", "scissors"};
                    String compChoice = options[rand.nextInt(3)];

                    System.out.println("Computer: " + compChoice);

                    String result;

                    if (userChoice.equals(compChoice)) {
                        result = "Draw";
                    } else if (
                        userChoice.equals("rock") && compChoice.equals("scissors") ||
                        userChoice.equals("paper") && compChoice.equals("rock") ||
                        userChoice.equals("scissors") && compChoice.equals("paper")
                    ) {
                        result = "User Wins";
                    } else {
                        result = "Computer Wins";
                    }

                    System.out.println("Result: " + result);

                    
                    String insert = "INSERT INTO rps_history(user_choice, computer_choice, result) VALUES (?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(insert);
                    ps.setString(1, userChoice);
                    ps.setString(2, compChoice);
                    ps.setString(3, result);
                    ps.executeUpdate();

                    System.out.println("Game result saved to database!");

                } else if (choice == 2) {
                    
                    System.out.println("\n=== Game History ===");
                    ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM rps_history ORDER BY played_at DESC");

                    while (rs.next()) {
                        System.out.println(
                                rs.getInt("id") + " | "
                                + rs.getString("user_choice") + " | "
                                + rs.getString("computer_choice") + " | "
                                + rs.getString("result") + " | "
                                + rs.getTimestamp("played_at")
                        );
                    }

                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;

                } else {
                    System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


