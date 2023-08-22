// package DAO;

// import Model.Message;
// import Util.ConnectionUtil;

// import java.sql.*;
// import java.util.List;

// public class MessageDAO {

//     public Message createMessage(Message message) {
//         return null;
//     }

//     public List<Message> getAllMessages() {
//         return null;
//     }

//     public Message getMessageById(long messageId) {
//         return null;
//     }

//     public boolean deleteMessage(long messageId) {
//         return false;
//     }

//     public Message updateMessage(long messageId, Object messageText) {
//         return null;
//     }

// }

package DAO;

import java.sql.*;
import java.util.*;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    
    // private final Connection connection;
    
    // public MessageDAO(Connection connection) {
    //     this.connection = connection;
    // }

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
        String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // pstmt.setInt(1, message.getPostedBy());
            // pstmt.setString(2, message.getMessageText());
            // pstmt.setLong(3, message.getTimePostedEpoch());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;  // Similarly, return the created message, potentially with its ID.
    }

    public List<Message> findAll() {
        String sql = "SELECT * FROM Message";
        List<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
    
}