/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.OrderDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.account.AccountControllerBean;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "listClientOrdersPageBean")
@ViewScoped
public class ListClientOrdersPageBean implements Serializable {

    @Inject
    private OrderControllerBean orderControllerBean;

    @Inject
    private AccountControllerBean accountControllerBean;

    private List<OrderDTO> listClientOrders;

    private DataModel<OrderDTO> dataModelOrderes;

    public ListClientOrdersPageBean() {
    }

    public DataModel<OrderDTO> getDataModelOrderes() {
        return dataModelOrderes;
    }

    @PostConstruct
    public void initListAccountOrders() {
        try {
            listClientOrders = orderControllerBean.listClientOrders(accountControllerBean.getSelectedAccountDTO());
        } catch (AppBaseException ex) {
            Logger.getLogger(ListClientOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelOrderes = new ListDataModel<>(listClientOrders);
    }

    public String deleteSelectedOrderAction(OrderDTO orderDTO) {
        try {
            orderControllerBean.deleteOrder(orderDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListClientOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListAccountOrders();
        return null;
    }

    public String deleteCurrentClientOrders() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            orderControllerBean.deleteCurrentClientOrders(accountControllerBean.getSelectedAccountDTO(), currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListClientOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListAccountOrders();
        return null;
    }

    public String deletePastClientOrders() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            orderControllerBean.deletePastClientOrders(accountControllerBean.getSelectedAccountDTO(), currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListClientOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListAccountOrders();
        return null;
    }

}
