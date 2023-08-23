package DAO;

import java.sql.*;
import java.util.*;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    private Connection connection;
    
    public MessageDAO() {
        this.connection = ConnectionUtil.getConnection();
    }

    public Message create(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (connection == null) {
                connection = ConnectionUtil.getConnection();
            }
            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, System.currentTimeMillis()); // Assuming current time as epoch
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    message.setMessage_id(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return message;

    }

    public List<Message> findAll() {
        String sql="SELECT * FROM Message";
        List<Message>messages=new ArrayList<>();
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        try {
            if (connection == null) {
                connection = ConnectionUtil.getConnection();
            }
            pstmt=connection.prepareStatement(sql);
            rs=pstmt.executeQuery();
            while (rs.next()) {
                Message message=new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
        return messages;
    }

    /**
     * @param id
     * @return
     * @throws SQLException
     */
    public Message findById(int id) throws SQLException {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Message message = null;
        try {
            if (connection == null) {
                connection = ConnectionUtil.getConnection();
            }
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    
    }

    public Message deleteById(int id) throws SQLException {
        String sql = "DELETE FROM Message WHERE message_id = ?";
        PreparedStatement pstmt = null;
        Message message = findById(id);  // Retrieve the message first

        try {
            if (connection == null) {
                connection = ConnectionUtil.getConnection();
            }
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return message;
    }
    
    public Message updateMessageTextById(int id, String newText) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        PreparedStatement pstmt = null;
        Message message = null;

        try {
            connection = ConnectionUtil.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newText);
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                message = findById(id);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return message;  
    }

    public List<Message> findMessagesByAccountId(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = ConnectionUtil.getConnection();
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, accountId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        return messages;
    }
    
}