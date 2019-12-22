/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.br.model.Account;

public class AccountException extends AppBaseException {

    static final public String KEY_ACCOUNT_LOGIN_EXIST = "error.account.login.exists.problem";
    static final public String KEY_ACCOUNT_WRONG_STATE = "error.account.wrong.state.problem";
    static final public String KEY_ACCOUNT_WRONG_PASSWORD = "error.account.wrong.password.problem";
    static final public String KEY_NO_ACCOUNT_FOUND = "error.no.account.found.problem";
    static final public String KEY_ACCOUNT_ALREADY_ACTIVATED = "error.account.already.active.problem";
    static final public String KEY_ACCOUNT_ALREADY_DEACTIVATED = "error.account.already.deactive.problem";
    static final public String KEY_ACCOUNT_YOU_DEACTIVATED_YOURSELF = "easter.egg.i.hate.my.job";
    static final public String KEY_ACCOUNT_ALREADY_CHANGED = "error.account.already.changed.problem";
    static final public String KEY_ACCOUNT_USED_IN_ACCOUNTS = "error.unique.account.used.in.accounts.problem";
    static final public String KEY_ACCOUNT_USED_IN_BUSES = "error.unique.account.used.in.buses.problem";
    static final public String KEY_ACCOUNT_USED_IN_ORDERS = "error.unique.account.used.in.orders.problem";
    static final public String KEY_ACCOUNT_NOT_ACTIVE = "error.account.not.active.problem";

    private Account account;

    public Account getAccount() {
        return account;
    }

    private AccountException(String message, Account account) {
        super(message);
        this.account = account;
    }

    private AccountException(String message, Throwable cause, Account account) {
        super(message, cause);
        this.account = account;
    }

    private AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    private AccountException(String message) {
        super(message);
    }

    static public AccountException createExceptionLoginAlreadyExists(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_LOGIN_EXIST, cause, account);
    }

    static public AccountException createExceptionAccountInUseInBus(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_BUSES, cause, account);
    }

    static public AccountException createExceptionAccountInUseInAccount(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_ACCOUNTS, cause, account);
    }

    static public AccountException createExceptionAccountInUseInOrder(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_ORDERS, cause, account);
    }

    static public AccountException createExceptionWrongState(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_STATE, account);
    }

    static public AccountException createExceptionWrongPassword(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_PASSWORD, account);
    }

    public static AccountException createExceptionAccountNotFound(NoResultException e) {
        return new AccountException(KEY_NO_ACCOUNT_FOUND, e);
    }

    static public AccountException createExceptionAccountAlreadyActvivated(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_ACTIVATED, account);
    }

    static public AccountException createExceptionAccountAlreadyDeactvivated(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_DEACTIVATED, account);
    }

    static public AccountException createExceptionYouDeactivatedYourOwnAccountGenius(Account account) {
        return new AccountException(KEY_ACCOUNT_YOU_DEACTIVATED_YOURSELF, account);
    }

    static public AccountException ceateExceptionAccountChangedByAnotherAdmin(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_CHANGED, account);
    }

    static public AccountException ceateExceptionAccountAlreadyHaveThisAccessLevel(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_CHANGED, account);
    }

    public static AccountException createExceptionAccountNotActive(Account account) {
        return new AccountException(KEY_ACCOUNT_NOT_ACTIVE);
    }

}
