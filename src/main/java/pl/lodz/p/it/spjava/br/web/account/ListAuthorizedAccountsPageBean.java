/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.model.AccessLevel;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "listAuthorizedAccountsPageBean")
@ViewScoped
public class ListAuthorizedAccountsPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private List<AccountDTO> listAccounts;

    private List<AccessLevel> listAccessLevels;

    public List<AccessLevel> getListAccessLevels() {
        return listAccessLevels;
    }

    private DataModel<AccountDTO> dataModelAccounts;

    public ListAuthorizedAccountsPageBean() {
    }

    public DataModel<AccountDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    @PostConstruct
    public void initListAuthorizedAccounts() {
        try {
            listAccounts = accountControllerBean.listAuthorizedAccounts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listAccounts);
        AccessLevel[] listAllAccessLevels = AccessLevel.values();
        for (AccessLevel accessLevel : listAllAccessLevels) {
            accessLevel.setAccessLevelI18NValue(ContextUtils.getI18NMessage(accessLevel.getAccessLevelKey()));
        }
        listAccessLevels = new ArrayList<>(Arrays.asList(listAllAccessLevels));
        listAccessLevels.remove(AccessLevel.ACCOUNT);
        listAccessLevels.remove(AccessLevel.NEWACCOUNT);
    }

    public String changeAccessLevelForSelectedAccountAction(AccountDTO accountDTO) {
        if (accountDTO.getAccessLevel() != null) {
            try {
                accountControllerBean.changeAccessLevelAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
            }
            initListAuthorizedAccounts();
        }
        return null;
    }

    public String activateAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.activateAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListAuthorizedAccounts();
        return null;
    }

    public String deactivateAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.deactivateAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListAuthorizedAccounts();
        return null;
    }

    public String changePasswordAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.selectAccountForPasswordChange(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            initListAuthorizedAccounts();
            return null;
        }
        return "changePassword";
    }

    public String editAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.selectAccountForEdit(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            initListAuthorizedAccounts();
            return null;
        }
        return "editAccount";
    }

}
