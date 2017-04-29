import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Maia on 4/21/2017.
 */
public class CubesDB {


    //Set up for SQL Database
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cubes";     //Connection string – where's the database?
    static final String USER = "maia";   //TODO replace with your username
    static final String PASSWORD = System.getenv("MYSQL_PW");   //TODO remember to set the environment variable
    // static final String PASSWORD = "password";   // If on lab PC, uncomment this line and replace "password" with your own password

    public static ResultSet rs;

    public List<String> solvers;
    public List<Double> all_times;

    public ArrayList <String>[][] rows;

    public static void main(String[] args) throws SQLException {

        CubesForm form = new CubesForm();

        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drivers and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }

        //Try with resources to open the connection and create a statement. Make sure your language level is 1.7+
        try (Connection con = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
             Statement statement = con.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS cubes (SOLVED_BY varchar(50), DURATION DOUBLE)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Created Cubes table");

            statement.execute("INSERT INTO cubes VALUES ('Ronald Brinkmann (Germany)', 19)");      //Add some test data
            statement.execute("INSERT INTO cubes VALUES ('Robert Pergl (Czechoslovakia)', 17.02)");
            statement.execute("INSERT INTO cubes VALUES ('Dan Knights (USA)', 16.71)");
            statement.execute("INSERT INTO cubes VALUES ('Jess Bonde (Denmark)', 16.53)");
            statement.execute("INSERT INTO cubes VALUES ('Shotaro Makisumi (Japan)', 12.11)");
            statement.execute("INSERT INTO cubes VALUES ('Jean Pons (France)', 11.57)");
            statement.execute("INSERT INTO cubes VALUES ('Leyan Lo (USA)', 11.13)");
            statement.execute("INSERT INTO cubes VALUES ('Toby Mao (USA)', 10.48)");
            statement.execute("INSERT INTO cubes VALUES ('Edouard Chambon (France)', 9.18)");
            statement.execute("INSERT INTO cubes VALUES ('Thibaut Jacquinot (France)', 9.86)");
            statement.execute("INSERT INTO cubes VALUES ('Erik Akkersdijk (Netherlands)', 7.08)");
            statement.execute("INSERT INTO cubes VALUES ('Ron van Bruchem (Netherlands)', 9.55)");
            statement.execute("INSERT INTO cubes VALUES ('Yu Nakajima (Japan)', 8.72)");
            statement.execute("INSERT INTO cubes VALUES ('Feliks Zemdegs (Australia)', 4.73)");
            statement.execute("INSERT INTO cubes VALUES ('Mats Valk (Netherlands)', 4.74)");
            statement.execute("INSERT INTO cubes VALUES ('Collin Burns (USA)', 5.25)");
            statement.execute("INSERT INTO cubes VALUES ('Keaton Ellis (USA)', 5.09)");
            statement.execute("INSERT INTO cubes VALUES ('Lucas Etter (USA)', 4.90)");
            statement.execute("INSERT INTO cubes VALUES ('Kaijun Lin - blindfold', 18.50)");
            statement.execute("INSERT INTO cubes VALUES ('Feliks Zemdegs - one handed', 6.88)");
            statement.execute("INSERT INTO cubes VALUES ('Jakub Kipa - with feet', 20.57)");

            rs = statement.executeQuery("SELECT * FROM cubes");   //Fetch all data; data is returned in a ResultSet

            //Add to lists.
            while (rs.next()) { //Loop over ResultSet, and print data
                System.out.println("Solved_By: " + rs.getString(1));



                System.out.println("Time_To_Solve: " + rs.getInt(2));

                System.out.println("*****");
            }


            System.out.printf("1. Add New Solver\n");
            System.out.printf("2. Add New Time\n");
            System.out.printf("3. Exit Program\n");

            Scanner scanner = new Scanner(System.in);

            if (scanner.nextLine().equals("2")){

                System.out.printf("Enter cube solver you would like to edit the time for:\n");
                String searchName = scanner.nextLine();
                System.out.printf("Enter new time\n");
                Double durationEntered = Double.valueOf(scanner.nextLine());
                String updateDuration = "UPDATE cubes SET DURATION = ? WHERE SOLVED_BY = ?";
                PreparedStatement updatePS = con.prepareStatement(updateDuration);
                updatePS.setDouble(1, durationEntered);
                updatePS.setString(2, searchName);
                updatePS.execute();
                rs = statement.executeQuery("SELECT * FROM cubes");


                while (rs.next()) {                                //Loop over ResultSet, and print data
                    System.out.println("Solved_By: " + rs.getString(1));
                    System.out.println("Time_To_Solve: " + rs.getDouble(2));
                    System.out.println("*****");
                }
            }
            if(scanner.nextLine().equals("1")){
                System.out.printf("Enter new cube solver:\n");
                String newName = scanner.nextLine();
                System.out.printf("Enter new time\n");
                Double durationEntered = Double.valueOf(scanner.nextLine());

                String addNewSolver = "INSERT INTO cubes VALUES ('?', 1?)";

                PreparedStatement updatePS = con.prepareStatement(addNewSolver);
                updatePS.setDouble(1, durationEntered);
                updatePS.setString(2, newName);
                updatePS.execute();

                rs = statement.executeQuery("SELECT * FROM cubes");
                while (rs.next()) {                                //Loop over ResultSet, and print data
                    System.out.println("Solved_By: " + rs.getString(1));
                    System.out.println("Time_To_Solve: " + rs.getDouble(2));
                    System.out.println("*****");
                }
            }
            if(scanner.nextLine().equals("q")){
                rs.close();                        //Close the result set, statement and connect
                statement.close();
                con.close();

            }

            //statement.execute("DROP TABLE cubes");      //Used for Testing (otherwise duplicates

        }
        catch (SQLException se) {
            se.printStackTrace();
        }
        System.out.println("End of program");
    }

}
