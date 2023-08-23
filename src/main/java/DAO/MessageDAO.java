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

    // public List<Message> findAll() throws SQLException {
    //     List<Message> messages = new ArrayList<>();
    //     String sql = "SELECT * FROM Message";
    //     PreparedStatement stmt = connection.prepareStatement(sql);
    //     ResultSet rs = stmt.executeQuery();
    //     while (rs.next()) {
    //         messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
    //     }
    //     return messages;
    // }

    // public Message findById(int id) throws SQLException {
    //     String sql = "SELECT * FROM Message WHERE message_id = ?";
    //     PreparedStatement stmt = connection.prepareStatement(sql);
    //     stmt.setInt(1, id);
    //     ResultSet rs = stmt.executeQuery();
    //     if (rs.next()) {
    //         return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
    //     }
    //     return null;
    // }
    
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

    public Message findById(int id) throws SQLException {
        String FIND_BY_ID_QUERY = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement pstmt = connection.prepareStatement(FIND_BY_ID_QUERY);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
        }
        return null;
    }
    
    
}