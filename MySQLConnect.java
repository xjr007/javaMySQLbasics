package MySQLConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.time.LocalDate;;

public class MySQLConnect {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  public void readDataBase() throws Exception {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connect = DriverManager
        .getConnection("jdbc:mysql:localhost/feedback?" + "user=sqluser&password=sqlUserPassword@1");

      statement = connect.createStatement();
      resultSet = statement
        .executeQuery("SELECT * FROM feedback.comments");
      writeResultSet(resultSet);

      preparedStatement = connect
        .prepareStatement("INSERT INTO feedback.comments values (default, ?, ?, ?, ?, ?, ?)");

      preparedStatement.setString(1, "Test");
      preparedStatement.setString(2, "TestEmail");
      preparedStatement.setString(3, "TestWebpage");
      preparedStatement.setDate(4, LocalDate.now());
      preparedStatement.setString(5, "testSummary");
      preparedStatement.setString(6, "TestComment");
      preparedStatement.executeUpdate();

      preparedStatement = connect
        .prepareStatement("SELECT myuser, webpage, datum, summary, comments from feedback.comments");

      resultSet = preparedStatement.executeQuery();
      writeResultSet(resultSet);

      preparedStatement = connect
        .prepareStatement("DELETE FROM feedback.comments WHERE myuser=?;");
        preparedStatement.setString(1, "Test");
        preparedStatement.executeUpdate();

    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
  }

  private void writeMetaData(ResultSet resultSet) throws SQLException {
    System.out.println("The columns in the table are: ");
    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));

    for (int i = 1; i <= resultSet.getMetaData().getColumnCount();i++) {
      System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
    }
  }

  private void writeResultSet(ResultSet resultSet) throws Exception {
    while (resultSet.next()) {
      String user = resultSet.getString("myuser");
      String website = resultSet.getString("webpage");
      String summary = resultSet.getString("summary");
      Date date = resultSet.getDate("datum");
      String comment = resultSet.getString("comments");
      System.out.println("User: " + user);
      System.out.println("Website: " + website);
      System.out.println("Summary: " + summary);
      System.out.println("Date: " + date);
      System.out.println("Comment: " + comment);
    }
  }

  private void close() {
    try {
      if(resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }

}
