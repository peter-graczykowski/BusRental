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
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "listPastOrdersPageBean")
@ViewScoped
public class ListPastOrdersPageBean implements Serializable {

    @Inject
    private OrderControllerBean orderControllerBean;

    private List<OrderDTO> listPastOrders;

    private DataModel<OrderDTO> dataModelOrderes;

    private OrderDTO orderDTO;

    public ListPastOrdersPageBean() {
    }

    public DataModel<OrderDTO> getDataModelOrderes() {
        return dataModelOrderes;
    }

    @PostConstruct
    public void initListNewOrderes() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            listPastOrders = orderControllerBean.listPastOrders(currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListPastOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelOrderes = new ListDataModel<>(listPastOrders);
        orderDTO = new OrderDTO();
    }

    public String deleteSelectedOrderAction(OrderDTO orderDTO) {
        try {
            orderControllerBean.deleteOrder(orderDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListPastOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListNewOrderes();
        return null;
    }

    public String deleteAllPastOrders() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            orderControllerBean.deleteAllPastOrders(currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListPastOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
//        return null; 
        }
        initListNewOrderes();
        return null;
    }

}
