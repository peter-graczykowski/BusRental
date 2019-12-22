/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.account;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "resetPasswordPageBean")
@RequestScoped
public class ResetPasswordPageBean implements Serializable {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    public ResetPasswordPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    @PostConstruct
    public void init() {
        accountControllerBean.setPasswordResetAccountDTO(null);
        accountControllerBean.setQuestionCheckAccountDTO(null);
        accountDTO = new AccountDTO();
    }

    public String resetPasswordAction() {

        try {
            accountControllerBean.selectAccountForQuestionCheck(accountDTO);
            if (!accountControllerBean.getQuestionCheckAccountDTO().isActive()) {
                ContextUtils.emitI18NMessage(null, "error.account.not.active.problem");
                return null;
            }
        } catch (AppBaseException ex) {
            Logger.getLogger(ResetPasswordPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(ex.getMessage().equals("error.no.account.found.problem") ? "ResetPasswordForm:login" : null, ex.getMessage());
            ContextUtils.getContext().getFlash().setKeepMessages(true);
        }
        if (accountControllerBean.getQuestionCheckAccountDTO() != null) {
            return "resetPassword2";
        }
        return null;
    }

}
