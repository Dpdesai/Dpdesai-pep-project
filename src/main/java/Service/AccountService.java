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
