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
import pl.lodz.p.it.spjava.br.web.bus.BusControllerBean;
import pl.lodz.p.it.spjava.br.web.bus.ListBusesPageBean;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "listBusOrdersPageBean")
@ViewScoped
public class ListBusOrdersPageBean implements Serializable {

    @Inject
    private OrderControllerBean orderControllerBean;

    @Inject
    private BusControllerBean busControllerBean;

    private List<OrderDTO> listBusOrders;

    private DataModel<OrderDTO> dataModelOrderes;

    public ListBusOrdersPageBean() {
    }

    public DataModel<OrderDTO> getDataModelOrderes() {
        return dataModelOrderes;
    }

    @PostConstruct
    public void initListBusOrders() {
        try {
            listBusOrders = orderControllerBean.listBusOrders(busControllerBean.getSelectedBusDTO());
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelOrderes = new ListDataModel<>(listBusOrders);
    }

    public String deleteSelectedOrderAction(OrderDTO orderDTO) {
        try {
            orderControllerBean.deleteOrder(orderDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusOrdersPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListBusOrders();
        return null;
    }

    public String deleteCurrentBusOrders() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            orderControllerBean.deleteCurrentBusOrders(busControllerBean.getSelectedBusDTO(), currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListBusOrders();
        return null;
    }

    public String deletePastBusOrders() {
        Date currentDate = new Date(System.currentTimeMillis());
        try {
            orderControllerBean.deletePastBusOrders(busControllerBean.getSelectedBusDTO(), currentDate);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListBusOrders();
        return null;
    }

}
