/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.utils;

import java.security.Principal;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class ContextUtils {

    /**
     * Tworzy nową instancję AttributesUtils
     */
    public ContextUtils() {
    }

    /**
     * Zwraca obiekt FacesContext - kontekst serwletu FacesServlet
     */
    public static ExternalContext getContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    /**
     * Wyszukuje atrybut o zadanej nazwie w kontekście aplikacji
     */
    public static Object getApplicationAttribute(String attributeName) {
        return getContext().getApplicationMap().get(attributeName);
    }

    /**
     * Wyszukuje atrybut o zadanej nazwie w kontekście sesji
     */
    public static Object getSessionAttribute(String attributeName) {
        return getContext().getSessionMap().get(attributeName);
    }

    /**
     * Wyszukuje atrybut o zadanej nazwie w kontekście żądania
     */
    public static Object getRequestAttribute(String attributeName) {
        return getContext().getRequestMap().get(attributeName);
    }

    /**
     * Wyszukuje parametr inicjalizacyjny o zadanej nazwie
     */
    public static String getContextParameter(String paramName) {
        return getContext().getInitParameter(paramName);
    }

    /**
     * Dokonuje zamknięcia bieżącej sesji
     */
    public static void invalidateSession() {
        ((HttpSession) getContext().getSession(true)).invalidate();
    }

    /**
     * Zwraca identyfikator bieżącej sesji
     */
    public static String getSessionID() {
        HttpSession session = (HttpSession) getContext().getSession(true);
        return session.getId();
    }

    /**
     * Zwraca nazwę zalogowanego użytkownika
     */
    public static String getUserName() {
        Principal principal = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        return (null == principal ? ContextUtils.getI18NMessage("application.no.authentication") : principal.getName());
    }

    /**
     * Zwraca zasób (ResourceBundle) o ścieżce wskazywanej przez parametr
     * resourceBundle.path i języku dostosowanym do preferncji przeglądarki
     * internetowej
     */
    public static ResourceBundle getDefaultBundle() {
        String bundlePath = getContextParameter("resourceBundle.path");

        if (null == bundlePath) {
            return null;
        } else {
            return ResourceBundle.getBundle(bundlePath, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        }
    }

    public static boolean isI18NKeyExist(final String key) {
        try {
            return ContextUtils.getDefaultBundle().getString(key) != null && !"".equals(ContextUtils.getDefaultBundle().getString(key));
        } catch (MissingResourceException e) {
            return false;
        }
    }

    public static String getI18NMessage(final String key) {
        if (isI18NKeyExist(key)) {
            return ContextUtils.getDefaultBundle().getString(key);
        } else {
            return key;
        }
    }

    public static void emitI18NMessage(final String id, final String key) {
        FacesMessage msg = new FacesMessage(getI18NMessage(key));
        FacesContext.getCurrentInstance().addMessage(id, msg);
    }

    /**
     * Zwraca internacjonalizowany String w warstwie logiki biznesowej
     */
    public static String printI18NMessage(final String key) {
        String msg = getI18NMessage(key);
        return msg;
    }

}
