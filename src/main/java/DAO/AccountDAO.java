// import Model.Account;
// import Model.Message;
// import Util.ConnectionUtil;

// import java.sql.*;
// import java.util.List;

// public class AccountDAO {

//     public List<Message> getMessagesByAccount(long accountId) {
//         return null;
//     }

//     public Account login(String username, String password) {
//         return null;
//     }

//     public Account register(Account account) {
//         return null;
//     } 

// }

package DAO;

import java.sql.*;
import java.util.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    // private final Connection connection;
    
    // public AccountDAO(Connection connection) {
    //     this.connection = connection;
    // }

    // public Account findByUsername(String username) throws SQLException {
    //     String sql = "SELECT * FROM Account WHERE username = ?";
    //     PreparedStatement stmt = connection.prepareStatement(sql);
    //     stmt.setString(1, username);
    //     ResultSet rs = stmt.executeQuery();
        
    //     if (rs.next()) {
    //         return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
    //     }
    //     return null;
    // }

    // public Account create(Account account) throws SQLException {
    //     String sql = "INSERT INTO Account(username, password) VALUES(?, ?)";
    //     PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    //     stmt.setString(1, account.getUsername());
    //     stmt.setString(2, account.getPassword());
    //     stmt.executeUpdate();
        
    //     ResultSet generatedKeys = stmt.getGeneratedKeys();
    //     if (generatedKeys.next()) {
    //         account.setAccountId(generatedKeys.getInt(1));
    //     }
    //     return account;
    // }

    public Account create(Account account) {
        String sql = "INSERT INTO Account(username, password) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;  // Normally, you might want to return the created account with its ID.
    }

    public Account findByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Account account = null;
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }
}
