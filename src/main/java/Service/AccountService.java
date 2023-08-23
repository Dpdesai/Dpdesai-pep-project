// import Model.Account;
// import Model.Message;
// import io.javalin.http.Context;
// import DAO.AccountDAO;

// import java.util.List;

// public class AccountService {
//     AccountDAO accountDAO;


//     public void AccountHandler(AccountDAO accountDAO) {
//         this.accountDAO = accountDAO;
//     }

//     public void register(Context ctx) {
//         Account account = ctx.bodyAsClass(Account.class);
//         Account registeredAccount = accountDAO.register(account);
//         if (registeredAccount != null) {
//             ctx.json(registeredAccount);
//         } else {
//             ctx.status(400);
//         }
//     }

//     public void login(Context ctx) {
//         Account account = ctx.bodyAsClass(Account.class);
//         Account loggedInAccount = accountDAO.login(account.getUsername(), account.getPassword());
//         if (loggedInAccount != null) {
//             ctx.json(loggedInAccount);
//         } else {
//             ctx.status(401);
//         }
//     }

//     public void getMessagesByAccount(Context ctx) {
//         long accountId = Long.parseLong(ctx.pathParam("accountId"));
//         List<Message> messages = accountDAO.getMessagesByAccount(accountId);
//         ctx.json(messages);
//     }
    
// }

package Service;

import java.sql.SQLException;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();

    /**
     * @param account
     * @return
     * @throws SQLException
     */
    public Account register(Account account) throws SQLException { 
        Account accountExists = accountDAO.findByUsername(account.getUsername());
        System.out.println("accountExists: " + accountExists);
        if(accountExists != null || account.getPassword().length() < 4) {
            return null;
        }
        return accountDAO.create(account);
    }


    public Account login(Account account) throws SQLException {
        Account accountExists = accountDAO.findByUsername(account.getUsername());
        if(accountExists == null || !accountExists.getPassword().equals(account.getPassword())) {
            return null;
        }
        return accountExists;
    }
}
