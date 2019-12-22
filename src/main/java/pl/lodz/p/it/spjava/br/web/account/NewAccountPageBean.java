/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "newAccountPageBean")
@RequestScoped
public class NewAccountPageBean implements Serializable {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String passwordRepeat;

    public NewAccountPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    @PostConstruct
    public void init() {
        accountDTO = new AccountDTO();
    }

    public String registerAccountAction() {
        if (passwordRepeat.equals(accountDTO.getPassword())) {
            try {
                accountControllerBean.newAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(NewAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(ex.getMessage().equals("error.account.login.exists.problem") ? "RegisterForm:login" : null, ex.getMessage());
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:passwordRepeat", "passwords.not.matching");
            return null;
        }
        return "main";
    }

}
