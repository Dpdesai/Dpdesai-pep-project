package DAO;

import java.sql.*;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    private Connection connection;
    
    public AccountDAO() {
        this.connection = ConnectionUtil.getConnection();
    }

    public Account create(Account account) throws SQLException {
        String sql = "INSERT INTO Account(username, password) VALUES (?, ?)";
        // Use ConnectionUtil.getConnection() without try-with-resources
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
        try {
            if (connection == null) {
                connection = ConnectionUtil.getConnection();
            }
            pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.executeUpdate();
    
            // Retrieve the generated ID
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                account.setAccount_id(id);

                return account;
            } else {
                // Handle error: ID not generated
                throw new RuntimeException("Failed to retrieve the generated account ID");
            }
        } catch (SQLException e) {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (connection != null) connection.close();

            // Handle error: could be because the username already exists or other database issues
            throw new RuntimeException("Failed to create the account", e);
        }
    }

    public Account findByUsername(String username) throws SQLException {
        String sql = "SELECT account_id, username, password FROM Account WHERE username = ?";
        if (connection == null) {
            connection = ConnectionUtil.getConnection();
        }
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("AccountId: " + rs.getInt("account_id"));
            System.out.println("username: " + rs.getString("username"));
            System.out.println("Password: " + rs.getString("password") );
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }

    public Account findById(int id) throws SQLException {
        String FIND_BY_ID_QUERY = "SELECT * FROM Account WHERE account_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(FIND_BY_ID_QUERY);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }

    public boolean exists(int id) {
        try {
            return this.findById(id) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
