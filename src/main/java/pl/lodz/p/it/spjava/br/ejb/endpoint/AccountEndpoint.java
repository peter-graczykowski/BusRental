/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.ejb.endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.AdminFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.ClientFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.OrderFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.PlanistFacade;
import pl.lodz.p.it.spjava.br.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.br.exception.AccountException;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.model.AccessLevel;
import pl.lodz.p.it.spjava.br.model.Account;
import pl.lodz.p.it.spjava.br.model.Admin;
import pl.lodz.p.it.spjava.br.model.NewAccount;
import pl.lodz.p.it.spjava.br.model.Client;
import pl.lodz.p.it.spjava.br.model.Planist;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class AccountEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private AdminFacade adminFacade;

    @EJB
    private ClientFacade clientFacade;

    @EJB
    private PlanistFacade planistFacade;

    @EJB
    private AccountFacade accountFacade;

    @EJB
    private OrderFacade orderFacade;

    @Resource
    private SessionContext sessionContext;

    private Account accountState;

    private Account myAccountState;

    private Account questionCheckAccountState;

    private Account passwordResetAccountState;

    private List<Account> savedAccountStateList;

    @RolesAllowed({"Admin"})
    public Account getAccountState() {
        return accountState;
    }

    @RolesAllowed({"Admin"})
    public void setAccountState(Account accountState) {
        this.accountState = accountState;
    }

    @RolesAllowed({"Admin", "Planist", "Client"})
    public Account getMyAccountState() {
        return myAccountState;
    }
   

    @RolesAllowed({"Admin", "Planist", "Client"})
    public void setMyAccountState(Account myAccountState) {
        this.myAccountState = myAccountState;
    }

    public Account getQuestionCheckAccountState() {
        return questionCheckAccountState;
    }

    public void setQuestionCheckAccountState(Account questionCheckAccountState) {
        this.questionCheckAccountState = questionCheckAccountState;
    }

    public Account getPasswordResetAccountState() throws AccountException {
        if (!passwordResetAccountState.getLogin().equals(questionCheckAccountState.getLogin())) {
            throw AccountException.createExceptionWrongState(passwordResetAccountState);
        } else {
            return passwordResetAccountState;
        }
    }

    public void setPasswordResetAccountState(Account passwordResetAccountState) throws AccountException {
        if (!passwordResetAccountState.getLogin().equals(questionCheckAccountState.getLogin())) {
            throw AccountException.createExceptionWrongState(passwordResetAccountState);
        } else {
            this.passwordResetAccountState = passwordResetAccountState;
        }
    }

    @RolesAllowed({"Admin"})
    private Admin loadCurrentAdmin() throws AppBaseException {
        String adminLogin = sessionContext.getCallerPrincipal().getName();
        Admin adminAccount = adminFacade.findByLogin(adminLogin);
        if (adminAccount == null) {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
        if (!adminAccount.isActive()) {
            throw AccountException.createExceptionAccountNotActive(adminAccount);
        }
        return adminAccount;
    }

    public Account selectAccountWithIterator(String login, List<Account> accounts) {
        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            Account tempAccount = iterator.next();
            if (tempAccount.getLogin().equals(login)) {
                return tempAccount;
            }
        }
        return null;
    }

    @PermitAll
    public void newAccount(AccountDTO accountDTO) throws AppBaseException {
        Account newAccount = new NewAccount();
        newAccount.setLogin(accountDTO.getLogin());
        newAccount.setPassword(accountDTO.getPassword());
        newAccount.setQuestion(accountDTO.getQuestion());
        newAccount.setAnswer(accountDTO.getAnswer());
        newAccount.setName(accountDTO.getName());
        newAccount.setSurname(accountDTO.getSurname());
        newAccount.setPhoneNumber(accountDTO.getPhoneNumber());

        newAccount.setActive(false);
        newAccount.setAuthorized(false);

        accountFacade.create(newAccount);
    }

    @RolesAllowed({"Admin"})
    public void deleteAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = accountFacade.findByLogin(accountDTO.getLogin());
        if (account instanceof NewAccount) {
            accountFacade.remove(account);
        } else {
            throw AccountException.ceateExceptionAccountChangedByAnotherAdmin(account);
        }
    }

    @RolesAllowed({"Admin"})
    public List<AccountDTO> listAuthorizedAccounts() throws AppBaseException {
        List<Account> listAuthorizedAccounts = accountFacade.findAuthorizedAccount();
        savedAccountStateList = listAuthorizedAccounts;

        List<AccountDTO> listAccounts = new ArrayList<>();
        for (Account account : listAuthorizedAccounts) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhoneNumber(),
                    account.isActive(),
                    account.getCreatedAt()
            );

            Admin adminAccount = adminFacade.findByLogin(account.getLogin());
            Client clientAccount = clientFacade.findByLogin(account.getLogin());
            Planist planistAccount = planistFacade.findByLogin(account.getLogin());

            if (adminAccount instanceof Admin) {
                accountDTO.setAccessLevel(AccessLevel.ADMIN);
            }
            if (clientAccount instanceof Client) {
                accountDTO.setAccessLevel(AccessLevel.CLIENT);
            }
            if (planistAccount instanceof Planist) {
                accountDTO.setAccessLevel(AccessLevel.PLANIST);
            }
            listAccounts.add(accountDTO);
        }
        Collections.sort(listAccounts);
        return listAccounts;
    }

    @RolesAllowed({"Admin"})
    public void changeAccessLevelAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = selectAccountWithIterator(accountDTO.getLogin(), savedAccountStateList);
        if (accountDTO.getCreatedAt() == null || accountDTO.getCreatedAt().equals(account.getCreatedAt())) {
            Account newAccount = null;
            switch (accountDTO.getAccessLevel()) {
                case PLANIST:
                    newAccount = new Planist(account);
                    break;
                case ADMIN:
                    newAccount = new Admin(account);
                    break;
                case CLIENT:
                    newAccount = new Client(account);
                    break;
            }

            if (newAccount != null) {
                accountFacade.remove(account);
                if (!newAccount.isAuthorized()) {
                    newAccount.setAuthorized(true);
                    newAccount.setActive(true);
                }
                newAccount.setAccountCreator(loadCurrentAdmin());
                accountFacade.create(newAccount);
            }
        } else {
            try {
                throw new OptimisticLockException("Wyjątek wywołany podczas zmiany poziomu dostępu użytkownika na liście autoryzowanych kont, w sytuacji gdy konto po wyświetleniu listy zostało już edytowane przez innego administratora");
            } catch (OptimisticLockException e) {
                throw AppBaseException.createExceptionOptimisticLock(e);
            }
        }
    }

    @RolesAllowed({"Admin"})
    public List<AccountDTO> listNewAccounts() throws AppBaseException {
        List<Account> listNewAccount = accountFacade.findNewAccount();
        savedAccountStateList = listNewAccount;
        List<AccountDTO> listNewRegisteredAccount = new ArrayList<>();
        for (Account account : listNewAccount) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhoneNumber(),
                    account.getCreatedAt()
            );
            listNewRegisteredAccount.add(accountDTO);
        }
        Collections.sort(listNewRegisteredAccount);
        return listNewRegisteredAccount;
    }

    @RolesAllowed({"Planist"})
    public List<AccountDTO> listClientAccounts() throws AppBaseException {
        List<Account> listClientAccount = accountFacade.findAllClientAccounts();
        savedAccountStateList = listClientAccount;
        List<AccountDTO> listClientAccountsDTO = new ArrayList<>();
        for (Account account : listClientAccount) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhoneNumber(),
                    account.isActive(),
                    account.getCreatedAt(),
                    orderFacade.findClientOrders(account.getLogin()).isEmpty()
            );
            listClientAccountsDTO.add(accountDTO);
        }
        Collections.sort(listClientAccountsDTO);
        return listClientAccountsDTO;
    }

    @RolesAllowed({"Admin"})
    public void activateAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = selectAccountWithIterator(accountDTO.getLogin(), savedAccountStateList);
        if (!adminFacade.findByLogin(sessionContext.getCallerPrincipal().getName()).isActive()) {
            throw AccountException.createExceptionAccountNotActive(account);
        }
        if (!account.isActive()) {
            account.setActive(true);
            account.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(account);
        } else {
            throw AccountException.createExceptionAccountAlreadyActvivated(account);
        }
    }

    @RolesAllowed({"Admin"})
    public void deactivateAccount(AccountDTO accountDTO) throws AppBaseException {
        Account account = selectAccountWithIterator(accountDTO.getLogin(), savedAccountStateList);
        if (!account.isActive()) {
            throw AccountException.createExceptionAccountAlreadyDeactvivated(account);
        } else {
            account.setActive(false);
            if (!account.getLogin().equals(loadCurrentAdmin().getLogin())) {
                account.setModifiedBy(loadCurrentAdmin());
                accountFacade.edit(account);
            } else {
                throw AccountException.createExceptionYouDeactivatedYourOwnAccountGenius(account);
            }
        }
    }

    @RolesAllowed({"Admin"})
    public void changeAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        if (accountState.getLogin().equals(accountDTO.getLogin())) {
            accountState.setPassword(accountDTO.getPassword());
            accountState.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(accountState);
        } else {
            throw AccountException.createExceptionWrongState(accountState);
        }
    }

    @PermitAll
    public void resetAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        if (passwordResetAccountState.getLogin().equals(accountDTO.getLogin())) {
            passwordResetAccountState.setPassword(accountDTO.getPassword());
            passwordResetAccountState.setModifiedBy(passwordResetAccountState.getClass().getSimpleName().equals("Admin") ? adminFacade.findByLogin(accountDTO.getLogin()) : null);
            accountFacade.edit(passwordResetAccountState);
        } else {
            throw AccountException.createExceptionWrongState(passwordResetAccountState);
        }
    }

    @RolesAllowed({"Admin"})
    public void editAccount(AccountDTO accountDTO) throws AppBaseException {
        if (accountState.getLogin().equals(accountDTO.getLogin())) {
            accountState.setName(accountDTO.getName());
            accountState.setSurname(accountDTO.getSurname());
            accountState.setPhoneNumber(accountDTO.getPhoneNumber());
            accountState.setModifiedBy(loadCurrentAdmin());
            accountFacade.edit(accountState);
        } else {
            throw AccountException.createExceptionWrongState(accountState);
        }
    }

    @RolesAllowed({"Admin", "Planist", "Client"})
    public void editMyAccount(AccountDTO accountDTO) throws AppBaseException {
        Account tempAccountForPasswordHash = new Account();
        tempAccountForPasswordHash.setPassword(accountDTO.getPassword());

        if (myAccountState.getLogin().equals(accountDTO.getLogin())) {
            if (myAccountState.getPassword().equals(tempAccountForPasswordHash.getPassword())) {
                myAccountState.setName(accountDTO.getName());
                myAccountState.setSurname(accountDTO.getSurname());
                myAccountState.setPhoneNumber(accountDTO.getPhoneNumber());
                myAccountState.setModifiedBy(myAccountState.getClass().getSimpleName().equals("Admin") ? loadCurrentAdmin() : null);
                accountFacade.edit(myAccountState);
            } else {
                throw AccountException.createExceptionWrongPassword(myAccountState);
            }
        } else {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
    }

    @RolesAllowed({"Admin", "Planist", "Client"})
    public void changeMyPassword(AccountDTO accountDTO) throws AppBaseException {

        if (myAccountState.getLogin().equals(accountDTO.getLogin())) {
            myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());

            Account account = new Account();
            account.setPassword(accountDTO.getOldPassword());
            if ((myAccountState.getPassword().equals(account.getPassword()))) {
                myAccountState.setPassword(accountDTO.getPassword());
                myAccountState.setModifiedBy(myAccountState.getClass().getSimpleName().equals("Admin") ? loadCurrentAdmin() : null);
                accountFacade.edit(myAccountState);
            } else {
                throw AccountException.createExceptionWrongPassword(myAccountState);
            }
        } else {
            throw AccountException.createExceptionWrongState(myAccountState);
        }
    }

    @RolesAllowed({"Admin"})
    public AccountDTO rememberSelectedAccountForPasswordChange(String login) throws AppBaseException {
        accountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                accountState.getLogin()
        );
    }

    @PermitAll
    public AccountDTO rememberSelectedAccountForPasswordResetAndQuestionCheck(String login) throws AppBaseException {
        passwordResetAccountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                passwordResetAccountState.getLogin(),
                passwordResetAccountState.getQuestion(),
                passwordResetAccountState.getAnswer(),
                passwordResetAccountState.isActive()
        );
    }

    @RolesAllowed({"Planist"})
    public AccountDTO rememberSelectAccountForClientsOrdersList(AccountDTO accountDTO) throws AppBaseException {
        accountState = accountFacade.findByLogin(accountDTO.getLogin());
        return new AccountDTO(
                accountState.getLogin(),
                accountState.getName(),
                accountState.getSurname(),
                accountState.getPhoneNumber(),
                accountState.getCreatedAt()
        );
    }

    @RolesAllowed({"Admin"})
    public AccountDTO rememberSelectedAccountForEdit(AccountDTO accountDTO) throws AppBaseException {
        accountState = accountFacade.findByLogin(accountDTO.getLogin());
        return new AccountDTO(
                accountState.getLogin(),
                accountState.getName(),
                accountState.getSurname(),
                accountState.getPhoneNumber(),
                accountState.getClass().getSimpleName()
        );
    }

    @RolesAllowed({"Admin", "Planist", "Client"})
    public AccountDTO rememberMyAccountForDisplayAndEdit() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword(),
                myAccountState.getQuestion(),
                myAccountState.getAnswer(),
                myAccountState.getName(),
                myAccountState.getSurname(),
                myAccountState.getPhoneNumber(),
                myAccountState.isActive()
        );
    }

    @RolesAllowed({"Admin", "Planist", "Client"})
    public AccountDTO rememberMyAccountForPasswordChange() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword()
        );
    }

    @RolesAllowed({"Admin", "Planist", "Client"})
    public String getI18nAccountForAccessLevelDisplay() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        String accessLevel;
        switch (myAccountState.getClass().getSimpleName()) {
            case "Admin":
                accessLevel = "access.level.admin";
                break;
            case "Planist":
                accessLevel = "access.level.planist";
                break;
            case "Client":
                accessLevel = "access.level.client";
                break;
            default:
                accessLevel = "application.no.authentication";
        }
        return ContextUtils.printI18NMessage(accessLevel);
    }

}
