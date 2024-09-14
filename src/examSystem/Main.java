package examSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;



public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Online Exam System!");
        System.out.println("1. Register");
        System.out.println("2. Login");

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice == 1) {
            registerUser(sc);
        } else if (choice == 2) {
            loginUser(sc);
        }
    }

    // Function to register a new user
    public static void registerUser(Scanner sc) {
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        System.out.print("Enter Role (admin/student): ");
        String role = sc.nextLine();

        // Hash the password
        String hashedPassword = HashUtils.hashPassword(password);

        // Create a new user
        User newUser = new User(username, password, role);

        // Save user data to a file
        try (FileWriter fw = new FileWriter("users.txt", true)) {
            fw.write(newUser.getUsername() + "," + newUser.getPassword() + "," + newUser.getRole() + "\n");
            System.out.println("User registered successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Function to log in an existing user
    public static void loginUser(Scanner sc) {
        System.out.print("Enter Username: ");
        String username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        // Hash the entered password
        String hashedPassword = HashUtils.hashPassword(password);

        // Check user credentials from the file
        boolean loggedIn = false;
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                String storedUsername = userDetails[0];
                String storedPassword = userDetails[1];
                String role = userDetails[2];

                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    System.out.println("Login successful! Welcome, " + role);
                    loggedIn = true;
                    if (role.equalsIgnoreCase("admin")) {
                        adminDashboard(sc);
                    } else if (role.equalsIgnoreCase("student")) {
                        studentDashboard(sc);
                    }
                    break;
                }

            }

            if (!loggedIn) {
                System.out.println("Invalid username or password!");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

    }

    // Admin dashboard to add questions and view results
    public static void adminDashboard(Scanner sc) {
        boolean keepAdding = true;

        while (keepAdding) {
            System.out.println("Admin Dashboard");
            System.out.println("1. Add a question");
            System.out.println("2. View student scores");
            System.out.println("3. Logout");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (choice == 1) {
                addQuestion(sc);
            } else if (choice == 2) {
                viewStudentScores();
            } else if (choice == 3) {
                System.out.println("Logging out...");
                keepAdding = false;
            }
        }
    }

    // Function to view all student scores
    public static void viewStudentScores() {
        System.out.println("Student Scores:");

        try (BufferedReader br = new BufferedReader(new FileReader("scores.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading scores: " + e.getMessage());
        }
    }

    // Function to add a question to the exam
    public static void addQuestion(Scanner sc) {
        System.out.print("Enter the question: ");
        String question = sc.nextLine();

        System.out.print("Enter option 1: ");
        String option1 = sc.nextLine();

        System.out.print("Enter option 2: ");
        String option2 = sc.nextLine();

        System.out.print("Enter option 3: ");
        String option3 = sc.nextLine();

        System.out.print("Enter option 4: ");
        String option4 = sc.nextLine();

        System.out.print("Enter the correct option number (1-4): ");
        int correctOption = sc.nextInt();
        sc.nextLine(); // Consume newline

        // Save the question to a file
        try (FileWriter fw = new FileWriter("questions.txt", true)) {
            fw.write(question + "," + option1 + "," + option2 + "," + option3 + "," + option4 + "," + correctOption + "\n");
            System.out.println("Question added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Student dashboard to take the exam
    public static void studentDashboard(Scanner sc) {
        System.out.println("Student Dashboard");
        System.out.println("1. Take the exam");
        System.out.println("2. Logout");

        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice == 1) {
            takeExam(sc);
        } else if (choice == 2) {
            System.out.println("Logging out...");
        }
    }

    // Function for students to take the exam
    // Function for students to take the exam
    // Function for students to take the exam
    public static void takeExam(Scanner sc) {
        try (BufferedReader br = new BufferedReader(new FileReader("questions.txt"))) {
            String line;
            ArrayList<String> questionsList = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                questionsList.add(line);
            }

            // Shuffle the questions randomly
            Collections.shuffle(questionsList);

            int score = 0;
            int questionCount = 0;

            for (String questionDetailsLine : questionsList) {
                String[] questionDetails = questionDetailsLine.split(",");
                String question = questionDetails[0];
                String option1 = questionDetails[1];
                String option2 = questionDetails[2];
                String option3 = questionDetails[3];
                String option4 = questionDetails[4];
                int correctOption = Integer.parseInt(questionDetails[5]);

                // Display the question and options
                System.out.println(question);
                System.out.println("1. " + option1);
                System.out.println("2. " + option2);
                System.out.println("3. " + option3);
                System.out.println("4. " + option4);

                System.out.print("Enter your answer (1-4): ");
                int studentAnswer = sc.nextInt();
                sc.nextLine(); // Consume newline

                if (studentAnswer == correctOption) {
                    score++;
                }
                questionCount++;
            }

            System.out.println("Exam finished!");
            System.out.println("Your score: " + score + "/" + questionCount);

            // Save the student’s score
            saveStudentScore(sc, score, questionCount);

        } catch (IOException e) {
            System.out.println("Error reading questions: " + e.getMessage());
        }
    }


    // Function to save student score
    public static void saveStudentScore(Scanner sc, int score, int totalQuestions) {
        System.out.print("Enter your username to save your score: ");
        String username = sc.nextLine();

        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(username + "," + score + "/" + totalQuestions + "\n");
            System.out.println("Score saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing score to file: " + e.getMessage());
        }
    }
}