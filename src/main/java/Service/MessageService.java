// package Service;

// import Model.Message;
// import DAO.MessageDAO;
// import io.javalin.http.Context;

// import java.util.List;


// public class MessageService {
//     MessageDAO messageDAO;

//     public MessageService(){
//         messageDAO = new MessageDAO();
//     }

//     public MessageService(MessageDAO messageDAO){
//         this.messageDAO = messageDAO;
//     }


//     public void createMessage(Context ctx) {
//         Message message = ctx.bodyAsClass(Message.class);
//         Message createdMessage = messageDAO.createMessage(message);
//         if (createdMessage != null) {
//             ctx.json(createdMessage);
//         } else {
//             ctx.status(400);
//         }
//     }

//     public void getAllMessages(Context ctx) {
//         List<Message> messages = messageDAO.getAllMessages();
//         ctx.json(messages);
//     }

//     public void getMessageById(Context ctx) {
//         long messageId = Long.parseLong(ctx.pathParam("messageId"));
//         Message message = messageDAO.getMessageById(messageId);
//         if (message != null) {
//             ctx.json(message);
//         } else {
//             ctx.status(404);
//         }
//     }

//     public void deleteMessage(Context ctx) {
//         long messageId = Long.parseLong(ctx.pathParam("messageId"));
//         boolean isDeleted = messageDAO.deleteMessage(messageId);
//         if (isDeleted) {
//             ctx.result("Message Deleted");
//         } else {
//             ctx.status(404).result("Message not found");
//         }
//     }

//     public void updateMessage(Context ctx) {
//         long messageId = Long.parseLong(ctx.pathParam("messageId"));
//         Message messageUpdate = ctx.bodyAsClass(Message.class);
//         Message updatedMessage = messageDAO.updateMessage(messageId, messageUpdate.getMessageText());
//         if (updatedMessage != null) {
//             ctx.json(updatedMessage);
//         } else {
//             ctx.status(400);
//         }
//     }

// }

package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;
import io.javalin.http.Context;

public class MessageService {

    private MessageDAO messageDAO = new MessageDAO();
    private AccountDAO accountDAO = new AccountDAO();

    public Message postMessage(Message message) {
        Boolean isValid = isValidMessage(message);
        System.out.println("isValid: " + isValid);
        System.out.println("Message Length: " + message.getMessage_text().length());
        if (isValidMessage(message)) {
            return messageDAO.create(message);
        }
        return null;
    }

     private boolean isValidMessage(Message message) {
        if (message.getMessage_text() == null || 
            message.getMessage_text().isEmpty() || 
            message.getMessage_text().length() >= 255 || 
            // Assuming accountDAO.exists checks if an account with the given ID exists
            !accountDAO.exists(message.getPosted_by())) {
            return false;
        }
        return true;
    }

    public List<Message> getAllMessages() {
        return messageDAO.findAll();
    }

    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.findById(messageId);

        // return messageDAO.findById(message_id);

    }

    public Message deleteMessageById(String messageId) throws SQLException {
        Message message = messageDAO.findById(Integer.parseInt(messageId));
        System.out.println("Looking for a message" + message);
        return message;
    }
    
    // Similarly, add methods for creating, deleting, updating messages, and retrieving messages by user_id.
}
