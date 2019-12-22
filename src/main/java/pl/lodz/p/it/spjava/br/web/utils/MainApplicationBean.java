/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.account.ViewMyAccountPageBean;

@Named(value = "mainApplicationPageBean")
@ApplicationScoped
public class MainApplicationBean {

    @EJB
    private AccountEndpoint accountEndpoint;

    public MainApplicationBean() {
    }

    AccountDTO accountDTO = new AccountDTO();

    /**
     * Wylogowanie
     */
    public String logOutAction() {
        ContextUtils.invalidateSession();
        return "main";
    }

    /**
     * Wyświetla login obecnie zalogowanego użytkownika
     */
    public String getMyLogin() {
        return ContextUtils.getUserName();
    }

    /**
     * Wyświetla zinternacjonalizowany poziom dostępu obecnie zalogowanego
     * użytkownika
     */
    public String myAccessLevel() {
        try {
            return accountEndpoint.getI18nAccountForAccessLevelDisplay();
        } catch (AppBaseException ex) {
            Logger.getLogger(ViewMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        return null;
    }

}
