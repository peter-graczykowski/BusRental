/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.account;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "accountControllerBean")
@SessionScoped
public class AccountControllerBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    private int lastActionMethod = 0;

    private AccountDTO selectedAccountDTO;

    private AccountDTO myAccountDTO;

    private AccountDTO questionCheckAccountDTO;

    private AccountDTO passwordResetAccountDTO;

    private List<AccountDTO> selectedAccountsListsDTO;

    public AccountControllerBean() {
    }

    public AccountDTO getSelectedAccountDTO() {
        return selectedAccountDTO;
    }

    public void setSelectedAccountDTO(AccountDTO selectedAccountDTO) {
        this.selectedAccountDTO = selectedAccountDTO;
    }

    public AccountDTO getMyAccountDTO() {
        return myAccountDTO;
    }

    public void setMyAccountDTO(AccountDTO myAccountDTO) {
        this.myAccountDTO = myAccountDTO;
    }

    public AccountDTO getQuestionCheckAccountDTO() {
        return questionCheckAccountDTO;
    }

    public void setQuestionCheckAccountDTO(AccountDTO questionCheckAccountDTO) {
        this.questionCheckAccountDTO = questionCheckAccountDTO;
    }

    public AccountDTO getPasswordResetAccountDTO() {
        return passwordResetAccountDTO;
    }

    public void setPasswordResetAccountDTO(AccountDTO passwordResetAccountDTO) {
        this.passwordResetAccountDTO = passwordResetAccountDTO;
    }

    public List<AccountDTO> getSelectedAccountsListsDTO() {
        return selectedAccountsListsDTO;
    }

    public void newAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.newAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.deleteAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("AccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void changeAccessLevelAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.changeAccessLevelAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("AccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void editAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.editAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("AccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void activateAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.activateAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("AccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deactivateAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 6;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.deactivateAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("AccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void changeAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 7;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.changeAccountPassword(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("AccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void resetAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 8;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.resetAccountPassword(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void changeMyPassword(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 9;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.changeMyPassword(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void editMyAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.editMyAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("ViewAccountForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void selectAccountForPasswordChange(AccountDTO accountDTO) throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedAccountDTO = accountEndpoint.rememberSelectedAccountForPasswordChange(accountDTO.getLogin());
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

    public void selectAccountForQuestionCheck(AccountDTO accountDTO) throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            questionCheckAccountDTO = accountEndpoint.rememberSelectedAccountForPasswordResetAndQuestionCheck(accountDTO.getLogin());
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

    public void selectAccountForClientsOrdersList(AccountDTO accountDTO) throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedAccountDTO = accountEndpoint.rememberSelectAccountForClientsOrdersList(accountDTO);
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

    public void selectAccountForPasswordReset(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 10;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                passwordResetAccountDTO = accountEndpoint.rememberSelectedAccountForPasswordResetAndQuestionCheck(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void selectAccountForEdit(AccountDTO accountDTO) throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedAccountDTO = accountEndpoint.rememberSelectedAccountForEdit(accountDTO);
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

    public List<AccountDTO> listNewAccounts() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedAccountsListsDTO = accountEndpoint.listNewAccounts();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedAccountsListsDTO;
    }

    public List<AccountDTO> listClientAccounts() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedAccountsListsDTO = accountEndpoint.listClientAccounts();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedAccountsListsDTO;
    }

    public List<AccountDTO> listAuthorizedAccounts() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedAccountsListsDTO = accountEndpoint.listAuthorizedAccounts();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedAccountsListsDTO;
    }

    public AccountDTO getMyAccountDTOForDisplay() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            myAccountDTO = accountEndpoint.rememberMyAccountForDisplayAndEdit();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return myAccountDTO;
    }

    public AccountDTO getMyAccountDTOForEdit() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            myAccountDTO = accountEndpoint.rememberMyAccountForDisplayAndEdit();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return myAccountDTO;
    }

    public AccountDTO getMyAccountDTOForPasswordChange() throws AppBaseException {
        final int UNIQ_METHOD_ID = accountEndpoint.hashCode() + 12;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                myAccountDTO = accountEndpoint.rememberMyAccountForPasswordChange();
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
        return myAccountDTO;
    }

}
