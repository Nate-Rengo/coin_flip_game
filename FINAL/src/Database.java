import java.sql.*;

public class Database {
    Connection databaseConnection;
    public Database()
    {
        try {
            databaseConnection = DriverManager.getConnection("jdbc:sqlite:PlayerDatabase.db");
            System.out.println("Connected to Player Database");
            String create = "CREATE TABLE IF NOT EXISTS players ("
                    + "id INTEGER PRIMARY KEY,"
                    + "username STRING,"
                    + "password STRING,"
                    + "currency INTEGER);";
            Statement statement = databaseConnection.createStatement();
            statement.execute(create);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void createAccount(String username, String password)
    {
        try {
            String insert = "INSERT INTO players(username, password, currency) VALUES(?, ?, ?);";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(insert);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setInt(3,0);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Boolean login(String username, String password) throws SQLException {
        String query = "SELECT * FROM players WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet bool = preparedStatement.executeQuery();
        return bool.next();

    }
    public Boolean checkUniqueUsername(String Username) throws SQLException {
        String query = "SELECT * FROM players WHERE username = ?";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
        preparedStatement.setString(1, Username);
        ResultSet bool = preparedStatement.executeQuery();
        if(bool.next()){
            return true;
        }
        else{
            return false;
        }

    }

    public void addCurrency(String username,String password,Integer change){
        try {
            String updateQuery = "UPDATE players SET currency =currency+ ? WHERE username = ? AND password=?";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, change);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3,password);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public String getScoreboard(){
        String scoreboardComplete="";
        try{
            Statement statement = databaseConnection.createStatement();
            ResultSet scoreboard = statement.executeQuery("SELECT username, currency FROM players ORDER by currency DESC LIMIT 3");
            int count=1;
            while(scoreboard.next()){
                String username= scoreboard.getString("username");
                int currency= scoreboard.getInt("currency");
                scoreboardComplete+=" "+count+" "+username+" "+currency+",";
                count+=1;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return scoreboardComplete;
    }
    public String getCurrency(String username, String password){
        String currency="";
        try{
            String query = "SELECT currency FROM players WHERE username=? AND password=?";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int value = resultSet.getInt("currency");
                currency = Integer.toString(value);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return currency;
    }
}
