import java.sql.*;
import java.util.*;
import java.io.*;

public class Airsystem {

   // Set JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost:3306/Airsystem?useSSL=false";

   // Database credentials
   static final String USER = "root";// add your user
   static final String PASSWORD = "admin";// add password

   public static void main(String[] args) {
      Connection conn = null;
      Statement stmt = null;

      Scanner create = null;
      Scanner insert = null;

      Scanner scan = new Scanner(System.in);

      boolean loginSuccessful = false;
      for (int i = 0; i < 3; i++) {
         System.out.println("Welcome to the Flight Management System!");
         System.out.print("Please enter your username: ");
         String username = scan.nextLine();
         System.out.print("Please enter your password: ");
         String password = scan.nextLine();

         if (log_in(username, password)) {
            loginSuccessful = true;
            break;
         } else {
            System.out.println("Invalid username or password. Please try again.");
         }
      }

      if (!loginSuccessful) {
         System.out.println("Too many failed login attempts. Exiting...");
         scan.close();
         return;
      }

      // STEP 2. Connecting to the Database
      try {
         // STEP 2a: Register JDBC driver
         Class.forName(JDBC_DRIVER);
         // STEP 2b: Open a connection
         System.out.println("Connecting to database...");
         conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         // STEP 2c: Execute a query
         System.out.println("Creating statement...");
         stmt = conn.createStatement();

         runScript("/home/uttam/Documents/IIITB/sem4/DBMS project/IMT2022524_JDBC_Project/src/create.sql", conn);
         runScript("/home/uttam/Documents/IIITB/sem4/DBMS project/IMT2022524_JDBC_Project/src/insert.sql", conn);

         clearScreen();
         System.out.println("\n *********MENU*******\n");
         main_menu(stmt, scan, conn);

         scan.close();
         stmt.close();
         conn.close();
      } catch (SQLException se) {
         // se.printStackTrace();
      } catch (Exception e) {
         // e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            // se.printStackTrace();
         }
      }
   }

   static boolean log_in(String username, String password) {
      return username.equals("user") && password.equals("user");
   }

   static void main_menu(Statement stmt, Scanner scan, Connection conn) {

      System.out.println("Please select any option-\n");
      System.out.println("1.  Add a Passenger ");
      System.out.println("2.  Add a Flight ");
      System.out.println("3.  List down all the passengers ");
      System.out.println("4.  List down all the Flights ");
      System.out.println("5.  Book a flight for a Passenger "); // This query inserts booking of a passenger and update seats left in flight
      System.out.println("6.  List down all the Bookings ");
      System.out.println("7.  Seats available in flight ");
      System.out.println("8.  Details of a Passenger "); // This performs join operation between bookings,passenger and flight tables
      System.out.println("9.  Remove a Passenger  ");
      System.out.println("10. Remove a Flight ");
      System.out.println("11. Flight with maximum duration ");
      System.out.println("12. Rate the service ");
      System.out.println("0.  Exit");

      System.out.print("\nENTER YOUR CHOICE : ");
      int choice = Integer.parseInt(scan.nextLine());
      clearScreen();

      switch (choice) {
         case 0:
            return;
         case 1:
            add_passenger(stmt, scan, conn);
            break;
         case 2:
            add_flight(stmt, scan, conn);
            break;
         case 3:
            list_passengers(stmt, scan);
            break;
         case 4:
            list_flights(stmt, scan);
            break;
         case 5:
            book_flight(stmt, scan, conn);
            break;
         case 6:
            list_bookings(stmt, scan);
            break;

         case 7:
            seats_left(stmt, scan);
            break;

         case 8:
            passenger_details(stmt, scan);
            break;

         case 9:
            remove_passenger(stmt, scan,conn);
            break;

         case 10:
            remove_flight(stmt, scan,conn);
            break;

         case 11:
            duration_max(stmt, scan);
            break;

         case 12:
            service_feedback(stmt, scan,conn);
            break;

         default:
            clearScreen();
            System.out.println("Please Enter a Valid Choice!!\n");
            break;
      }
      main_menu(stmt, scan, conn);
   }

   static void add_passenger(Statement stmt, Scanner scan, Connection conn) {
      try {
         conn.setAutoCommit(false); 

         System.out.print("\nEnter Passenger ID : ");
         String id = scan.nextLine();
         System.out.print("\nEnter Passenger name : ");
         String name = scan.nextLine();
         System.out.print("\nEnter Phone number of Passenger : ");
         String phone = scan.nextLine();

         String sql = String.format(
               "INSERT INTO passenger VALUES('%s', '%s', '%s');",
               id, name, phone);
         PreparedStatement pstmt = conn.prepareStatement(sql);
         int result = pstmt.executeUpdate();

         conn.commit();

         if (result != 0)
            System.out.println("The Passenger has been added successfully!!\n");
         else
            System.out.println("Error! Try adding again\n");
      } catch (Exception e) {
         try {
            conn.rollback();
         } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
         }
         e.printStackTrace();
      } finally {
         try {
            conn.setAutoCommit(true); // Restore auto-commit mode
         } catch (SQLException setAutoCommitException) {
            setAutoCommitException.printStackTrace();
         }
      }
   }

   static void add_flight(Statement stmt, Scanner scan, Connection conn) {
      try {
         conn.setAutoCommit(false);

         System.out.print("\nEnter Flight ID : ");
         String id = scan.nextLine();
         System.out.print("\nEnter Flight name : ");
         String name = scan.nextLine();
         System.out.print("\nEnter the duration of flight (in hrs) : ");
         String time = scan.nextLine();
         System.out.print("\nEnter the destination of flight : ");
         String dest = scan.nextLine();

         clearScreen();

         String sql = String.format(
               "INSERT INTO flight VALUES('%s', '%s', '%s','%s','%d');",
               id, name, time, dest, 100);
         // System.out.println(sql);
         PreparedStatement pstmt = conn.prepareStatement(sql);
         int result = pstmt.executeUpdate();
         if (result != 0) {
            System.out.println("The Flight has been added successfully!!\n");
            conn.commit(); 
         } else {
            System.out.println("Error! Try adding again\n");
         }
      } catch (Exception e) {
         try {
            conn.rollback();
            System.out.println("Transaction rolled back!.");
         } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
         }
         e.printStackTrace();
      } finally {
         try {
            conn.setAutoCommit(true); 
         } catch (SQLException setAutoCommitException) {
            setAutoCommitException.printStackTrace();
         }
      }
   }

   static void list_passengers(Statement stmt, Scanner scan) {
      String sql = "select * from passenger";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try {
         System.out.println("List of all the Passengers :\n");
         while (rs.next()) {
            String id = rs.getString("passenger_id");
            String name = rs.getString("passenger_name");
            String phone = rs.getString("passenger_phone");

            System.out.println("Passenger ID is  : " + id);
            System.out.println("Passenger Name is  " + name);
            System.out.println("Passenger's Phone Number is  " + phone);

            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void list_flights(Statement stmt, Scanner scan) {
      String sql = "select * from flight";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try {
         System.out.println("List of all the Flights :\n");
         while (rs.next()) {
            String id = rs.getString("flight_id");
            String name = rs.getString("flight_name");
            String time = rs.getString("flight_time");
            String dest = rs.getString("flight_destination");

            System.out.println("Flight ID is  : " + id);
            System.out.println("Flight Name is  " + name);
            System.out.println("Duration of Flight is : " + time);
            System.out.println("Destination of Flight is : " + dest);

            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
      }
   }

   static void remove_passenger(Statement stmt, Scanner scan, Connection conn) {
      try {
         System.out.print("Enter id of Passenger that you want to delete: ");
         String id = scan.nextLine();

         clearScreen();

         String sql = String.format("DELETE FROM passenger where passenger_id = '%s'", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0) {
            System.out.println("The Passenger has been deleted successfully!!\n");
            conn.commit(); // Commit
         } else {
            System.out.println("Error! Try adding again\n");
         }
      } catch (Exception e) {
         try {
            conn.rollback(); // Rollback 
            System.out.println("Transaction rolled back!.");
         } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
         }
         e.printStackTrace();
      }
   }

   static void remove_flight(Statement stmt, Scanner scan, Connection conn) {
      try {
         System.out.print("Enter id of Flight that you want to delete: ");
         String id = scan.nextLine();

         clearScreen();

         String sql = String.format("DELETE FROM flight where flight_id = '%s'", id);
         int result = updateSqlStmt(stmt, sql);

         if (result != 0) {
            System.out.println("The Flight has been deleted successfully!!\n");
            conn.commit(); 
         } else {
            System.out.println("Error! Try adding again\n");
         }
      } catch (Exception e) {
         try {
            conn.rollback(); 
            System.out.println("Transaction rolled back!.");
         } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
         }
         e.printStackTrace();
      }
   }

   static void seats_left(Statement stmt, Scanner scan) {
      try {
         System.out.print("Enter Flight ID: ");
         String f_id = scan.nextLine();

         String sql = "SELECT seats_left FROM flight WHERE flight_id = '" + f_id + "'";
         ResultSet rs = executeSqlStmt(stmt, sql);

         clearScreen();

         if (rs.next()) {
            int seatsLeft = rs.getInt("seats_left");
            System.out.println("Seats left for Flight " + f_id + ": " + seatsLeft);
         } else {
            System.out.println("No information found for Flight " + f_id);
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void book_flight(Statement stmt, Scanner scan, Connection conn) {
      try {
         System.out.print("\nEnter Passenger ID: ");
         String p_id = scan.nextLine();

         System.out.print("Enter Flight ID: ");
         String f_id = scan.nextLine();

         String sqlFlight = "SELECT flight_name, seats_left FROM flight WHERE flight_id = '" + f_id + "'";
         ResultSet rsFlight = executeSqlStmt(stmt, sqlFlight);
         String flightName = "";
         int availableSeats = 0;
         if (rsFlight.next()) {
            flightName = rsFlight.getString("flight_name");
            availableSeats = rsFlight.getInt("seats_left");
         }
         rsFlight.close();

         clearScreen();

         if (availableSeats <= 0) {
            System.out.println("Sorry, no seats available for this flight.");
            return;
         }

         conn.setAutoCommit(false);

         String sqlBooking = String.format(
               "INSERT INTO bookings (passenger_id, flight_id, flight_name) VALUES ('%s', '%s', '%s');",
               p_id, f_id, flightName);
         int result = updateSqlStmt(stmt, sqlBooking);

         if (result != 0) {
            conn.commit();

         if (result != 0) {
            String sqlUpdateSeats = String
                  .format("UPDATE flight SET seats_left = seats_left - 1 WHERE flight_id = '%s'", f_id);
            int resultUpdateSeats = updateSqlStmt(stmt, sqlUpdateSeats);
            if (resultUpdateSeats != 0) {
               conn.commit();
               System.out.println("Flight has been booked successfully!\n");
            } else {
               conn.rollback();
               System.out.println("Error updating seats left. Transaction rolled back.\n");
            }
         } else {
            conn.rollback();
            System.out.println("Error! Try booking again. Transaction rolled back.\n");
         }

         conn.setAutoCommit(true);}
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void list_bookings(Statement stmt, Scanner scan) {
      String sql = "select * from bookings";
      ResultSet rs = executeSqlStmt(stmt, sql);

      try {
         System.out.println("List of all the Bookings :\n");
         while (rs.next()) {
            String p_id = rs.getString("passenger_id");
            String f_id = rs.getString("flight_id");
            String f_name = rs.getString("flight_name");

            System.out.println("Passenger ID is  : " + p_id);
            System.out.println("Flight ID  :" + f_id);
            System.out.println("Flight name is :  " + f_name);
            System.out.println("*********************************");

            System.out.println("\n");
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void passenger_details(Statement stmt, Scanner scan) {
      try {
         System.out.print("Enter Passenger ID: ");
         String p_id = scan.nextLine();

         String sql = "SELECT p.passenger_id, p.passenger_name, p.passenger_phone, " +
               "f.flight_id, f.flight_destination " +
               "FROM passenger p " +
               "JOIN bookings b ON p.passenger_id = b.passenger_id " +
               "JOIN flight f ON b.flight_id = f.flight_id " +
               "WHERE p.passenger_id = '" + p_id + "'";

         ResultSet rs = executeSqlStmt(stmt, sql);

         clearScreen();

         System.out.println("Passenger Details and Destination for Passenger " + p_id + ":\n");
         if (rs.next()) {
            String passengerId = rs.getString("passenger_id");
            String passengerName = rs.getString("passenger_name");
            String passengerPhone = rs.getString("passenger_phone");
            String flightId = rs.getString("flight_id");
            String destination = rs.getString("flight_destination");

            System.out.println("Passenger ID: " + passengerId);
            System.out.println("Passenger Name: " + passengerName);
            System.out.println("Passenger Phone: " + passengerPhone);
            System.out.println("Flight ID: " + flightId);
            System.out.println("Destination: " + destination);
            System.out.println("--------------------------------------");
         } else {
            System.out.println("No booking found for Passenger " + p_id);
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void service_feedback(Statement stmt, Scanner scan, Connection conn) {
      try {
         System.out.println("Enter Passenger ID: ");
         String passengerId = scan.nextLine();

         System.out.println("Enter Flight ID: ");
         String flightId = scan.nextLine();

         System.out.println("Enter Rating (1-5): ");
         int rating = Integer.parseInt(scan.nextLine());

         System.out.println("Enter Review: ");
         String review = scan.nextLine();

         String sql = String.format("INSERT INTO service_feedback (passenger_id, flight_id, rating, review_text) " +
               "VALUES ('%s', '%s', %d, '%s');", passengerId, flightId, rating, review);

         int result = updateSqlStmt(stmt, sql);

         if (result != 0) {
            System.out.println("Feedback submitted successfully!\n");
            conn.commit(); // Commit the transaction if successful
         } else {
            System.out.println("Error! Try again.\n");
         }
      } catch (Exception e) {
         try {
            conn.rollback(); // Rollback the transaction if an exception occurs
            System.out.println("Transaction rolled back!.");
         } catch (SQLException rollbackException) {
            rollbackException.printStackTrace();
         }
         e.printStackTrace();
      }
   }

   static void duration_max(Statement stmt, Scanner scan) {
      try {
         String sql = "SELECT flight_id FROM flight ORDER BY flight_time DESC LIMIT 1";
         ResultSet rs = executeSqlStmt(stmt, sql);

         clearScreen();

         if (rs.next()) {
            String maxFlightID = rs.getString("flight_id");
            System.out.println("Flight with maximum duration has ID: " + maxFlightID);
         } else {
            System.out.println("No flights found.");
         }

         rs.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   static void runScript(String filePath, Connection connection) {
      try {
         // Read the SQL script file
         File file = new File(filePath);
         StringBuilder scriptContent = new StringBuilder();
         try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
               scriptContent.append(line);
               scriptContent.append("\n");
            }
         }

         String[] sqlStatements = scriptContent.toString().split(";");

         try (Statement statement = connection.createStatement()) {
            for (String sqlStatement : sqlStatements) {
               String trimmedSqlStatement = sqlStatement.trim();
               if (!trimmedSqlStatement.isEmpty()) {
                  statement.execute(trimmedSqlStatement);
               }
            }
         }

         System.out.println("Script executed successfully: " + filePath);

      } catch (IOException | SQLException e) {
         if (e.getMessage().contains("Table") && e.getMessage().contains("already exists")) {
            System.out.println("Table already exists: " + filePath);
         } else {
            e.printStackTrace();
         }
      }
   }

   static ResultSet executeSqlStmt(Statement stmt, String sql) {
      try {
         ResultSet rs = stmt.executeQuery(sql);
         return rs;
      } catch (SQLException se) {
      } catch (Exception e) {
      }
      return null;
   }

   static int updateSqlStmt(Statement stmt, String sql) {
      try {
         int rs = stmt.executeUpdate(sql);
         return rs;
      } catch (SQLException se) {
      } catch (Exception e) {
      }
      return 0;
   }

   static void clearScreen() {
      System.out.println("*********************************");
      System.out.flush();
   }
}

// COMMANDS TO RUN

// export CLASSPATH='/home/uttam/Documents/IIITB/sem4/DBMS project/JDBC-tutorial
// (4)/JDBC-tutorial/mysql-connector-j-8.3.0.jar:.'
// javac Airsystem.java
// java Airsystem
