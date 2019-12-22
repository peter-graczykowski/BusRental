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

@Named(value = "listMyCurrentOrdersPageBean")
@ViewScoped
public class ListMyCurrentOrdersPageBean implements Serializable {

    @Inject
    private OrderControllerBean orderControllerBean;

    private List<OrderDTO> listMyCurrentOrders;

    private DataModel<OrderDTO> dataModelOrderes;

    public ListMyCurrentOrdersPageBean() {
    }

    public DataModel<OrderDTO> getDataModelOrderes() {
        return dataModelOrderes;
    }

    @PostConstruct
    public void initListMyCurrentOrderes() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            listMyCurrentOrders = orderControllerBean.listMyCurrentOrders(currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListMyCurrentOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelOrderes = new ListDataModel<>(listMyCurrentOrders);
    }

    public String deleteMySelectedOrderAction(OrderDTO orderDTO) {
        try {
            orderControllerBean.deleteMyOrder(orderDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListMyCurrentOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListMyCurrentOrderes();
        return null;
    }

    public String editMySelectedOrderAction(OrderDTO orderDTO) {
        try {
            orderControllerBean.selectOrderForChange(orderDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListMyCurrentOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            initListMyCurrentOrderes();
            return null;
        }
        return "editOrder";
    }
}
