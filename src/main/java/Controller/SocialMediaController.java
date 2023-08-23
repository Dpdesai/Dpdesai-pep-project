package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();

    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

     /**
     The registration will be successful if and only if the username is not blank, 
     the password is at least 4 characters long, and an Account with that username does not already exist. 
     If all these conditions are met, the response body should contain a JSON of the Account, 
     including its account_id. The response status should be 200 OK, which is the default. 
     The new account should be persisted to the database.
     If the registration is not successful, the response status should be 400. (Client error)
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void registerHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if (account.username == null || account.username.length() == 0 || account.password.length() <= 4) {
            ctx.status(400);
        } else {
            try {
                Account registeredAccount = accountService.register(account);
                if (registeredAccount == null) {
                    ctx.status(400);
                } else {
                    ctx.status(200);
                    ctx.json(mapper.writeValueAsString(registeredAccount));
                }      
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account authorizedAccount = accountService.login(account);
        if (authorizedAccount != null) {
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(authorizedAccount));
        } else {
            ctx.status(401);
        }

    }

    private void messageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        System.out.println("Received message: " + message);
        Message messagePosted = messageService.postMessage(message);
        if (messagePosted != null) {
            ctx.status(200).json(messagePosted);
        } else {
            ctx.status(400).result("");
        }

    }

    private void getAllMessageHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200).json(messages);
    }

    private void getMessageByIdHandler(Context ctx) throws SQLException, JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200);
        } 
    }

    private void deleteMessageByIdHandler(Context ctx) throws SQLException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessageById(messageId);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200); 
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message messageToBeUpdated = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessageText(messageId, messageToBeUpdated.getMessage_text());
        if (updatedMessage != null) {
            ctx.status(200).json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesByAccountHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        ctx.json(messages).status(200);

    }

}