import com.mysql.cj.mysqlx.protobuf.MysqlxCrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Maia on 4/27/2017.
 */
public class CubesForm extends JFrame {


    //Set up for SQL Database
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cubesolver";     //Connection string – where's the database?
    static final String USER = "maia";
    static final String PASSWORD = System.getenv("MYSQL_PW");   //TODO remember to set the environment variable
    // static final String PASSWORD = "password";   // If on lab PC, uncomment this line and replace "password" with your own password


    public static ResultSet rs;

    public String[] columns = new String[]{
            "Solved By", "Time"
    };

    public JPanel rootPane;
    public JRadioButton solverRadioButton;
    public JRadioButton timeRadioButton;
    private JTextField solverTxt;
    private JTextField timeTxt;
    private JLabel solverLabel;
    public JButton addBtn;
    private JLabel timeLabel;
    private JScrollPane spScollPane;
    private JTable cubesTable;

    protected CubesForm() {

        try {
            Class.forName(JDBC_DRIVER);
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Can't instantiate driver class; check you have drivers and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }

        //Try with resources to open the connection and create a statement. Make sure your language level is 1.7+
        try(Connection con = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
                 Statement statement = con.createStatement())
        {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS cubesolver (SOLVED_BY varchar(50), DURATION DOUBLE)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Created Cubes table");

            statement.execute("INSERT INTO cubesolver VALUES ('Ronald Brinkmann (Germany)', 19)");      //Add some test data
            statement.execute("INSERT INTO cubesolver VALUES ('Robert Pergl (Czechoslovakia)', 17.02)");
            statement.execute("INSERT INTO cubesolver VALUES ('Dan Knights (USA)', 16.71)");
            statement.execute("INSERT INTO cubesolver VALUES ('Jess Bonde (Denmark)', 16.53)");
            statement.execute("INSERT INTO cubesolver VALUES ('Shotaro Makisumi (Japan)', 12.11)");
            statement.execute("INSERT INTO cubesolver VALUES ('Jean Pons (France)', 11.57)");
            statement.execute("INSERT INTO cubesolver VALUES ('Leyan Lo (USA)', 11.13)");
            statement.execute("INSERT INTO cubesolver VALUES ('Toby Mao (USA)', 10.48)");
            statement.execute("INSERT INTO cubesolver VALUES ('Edouard Chambon (France)', 9.18)");
            statement.execute("INSERT INTO cubesolver VALUES ('Thibaut Jacquinot (France)', 9.86)");
            statement.execute("INSERT INTO cubesolver VALUES ('Erik Akkersdijk (Netherlands)', 7.08)");
            statement.execute("INSERT INTO cubesolver VALUES ('Ron van Bruchem (Netherlands)', 9.55)");
            statement.execute("INSERT INTO cubesolver VALUES ('Yu Nakajima (Japan)', 8.72)");
            statement.execute("INSERT INTO cubesolver VALUES ('Feliks Zemdegs (Australia)', 4.73)");
            statement.execute("INSERT INTO cubesolver VALUES ('Mats Valk (Netherlands)', 4.74)");
            statement.execute("INSERT INTO cubesolver VALUES ('Collin Burns (USA)', 5.25)");
            statement.execute("INSERT INTO cubesolver VALUES ('Keaton Ellis (USA)', 5.09)");
            statement.execute("INSERT INTO cubesolver VALUES ('Lucas Etter (USA)', 4.90)");
            statement.execute("INSERT INTO cubesolver VALUES ('Kaijun Lin - blindfold', 18.50)");
            statement.execute("INSERT INTO cubesolver VALUES ('Feliks Zemdegs - one handed', 6.88)");
            statement.execute("INSERT INTO cubesolver VALUES ('Jakub Kipa - with feet', 20.57)");

            rs = statement.executeQuery("SELECT * FROM cubesolver");   //Fetch all data; data is returned in a ResultSet

            setContentPane(rootPane);

            //Finish creating GUI
            pack();
            setVisible(true);

            //Because the JTable is created automatically normal JTable coding that uses
            //new JTable will override the table object that is created by Intellij and
            //display  nothing.
            //Have Intellij create the table and use dataModel to provide the table with
            //data.
            DefaultTableModel dataModel = new DefaultTableModel(null, columns);
            cubesTable.setModel(dataModel);
            //Add to lists.
            while (rs.next()) { //Loop over ResultSet, and print data
                Object [] row = {rs.getString(1), rs.getDouble(2)};
                dataModel.addRow(row);
            }
            //Must comment out to run and show in command line.
            //statement.execute("DROP TABLE cubesolver");      //Used for Testing (otherwise duplicates

            rs.close();                        //Close the result set, statement and connect
            statement.close();
            con.close();
        }
        catch (SQLException se) {
            se.printStackTrace();
        }


            //Decides which radio button is selected and changes
            //the labels accordingly
        ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (solverRadioButton.isSelected()) {
                        solverLabel.setText("New Solver");
                    } else {
                        solverLabel.setText("Current Solver");
                    }
                }
            };
            solverRadioButton.addActionListener(listener);
            timeRadioButton.addActionListener(listener);


        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName(JDBC_DRIVER);
                }
                catch (ClassNotFoundException cnfe)
                {
                    System.out.println("Can't instantiate driver class; check you have drivers and classpath configured correctly?");
                    cnfe.printStackTrace();
                    System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
                }
                try(Connection con = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
                        Statement statement = con.createStatement()) {
                    String s = solverTxt.getText();
                    Double t = Double.valueOf(timeTxt.getText());
                    PreparedStatement updatePS = con.prepareStatement("INSERT INTO cubsolver VALUES (?, ?)");
                    updatePS.setString(1, s);
                    updatePS.setDouble(2, t);
                    updatePS.execute();


                    rs = statement.executeQuery("SELECT * FROM cubesolver");
                    while (rs.next()) {                                //Loop over ResultSet, and print data
                        System.out.println("Solved_By: " + rs.getString(1));
                        System.out.println("Time_To_Solve: " + rs.getDouble(2));
                        System.out.println("*****");
                    }

                    System.out.println("Added");

                    //Must comment out to run and show in command line.
                    //statement.execute("DROP TABLE cubesolver");      //Used for Testing (otherwise duplicates

                    rs.close();                        //Close the result set, statement and connect
                    statement.close();
                    con.close();
                }
                catch (SQLException es){
                    System.out.println(es);
                }
            }
        });
    }

    public static void main(String[] args) throws SQLException {
        CubesForm form = new CubesForm();
    }
}