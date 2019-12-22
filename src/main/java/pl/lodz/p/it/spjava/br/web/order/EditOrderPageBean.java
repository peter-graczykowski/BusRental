/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.order;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.dto.OrderDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.BusEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "editOrderPageBean")
@ViewScoped
public class EditOrderPageBean implements Serializable {

    @EJB
    private BusEndpoint busEndpoint;

    @Inject
    private OrderControllerBean orderControllerBean;

    private OrderDTO orderDTO;

    private BusDTO busDTO;

    private String displayConflictDates;

    public EditOrderPageBean() {
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public BusDTO getBusDTO() {
        return busDTO;
    }

    public void setBusDTO(BusDTO busDTO) {
        this.busDTO = busDTO;
    }

    private List<BusDTO> listActiveBuses;

    public List<BusDTO> getListActiveBuses() {
        return listActiveBuses;
    }

    public void setListActiveBuses(List<BusDTO> listActiveBuses) {
        this.listActiveBuses = listActiveBuses;
    }

    public String getDisplayConflictDates() {
        return displayConflictDates;
    }

    public void setDisplayConflictDates(String ConflictedDatesStringList) {
        this.displayConflictDates = ConflictedDatesStringList;
    }

    @PostConstruct
    public void init() {
        try {
            if (orderControllerBean.getSelectedConflictedDatesStateList() != null) {
                displayConflictDates = orderControllerBean.getSelectedConflictedDatesStateList().toString().replace("[", "").replace("]", "");
            }
            orderControllerBean.setSelectedConflictedDatesStateList(null);

            listActiveBuses = busEndpoint.listActiveBuses();
        } catch (AppBaseException ex) {
            Logger.getLogger(EditOrderPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        orderDTO = orderControllerBean.getSelectedOrderDTO();
        busDTO = new BusDTO();
    }

    public String editOrderAction() {
        if (busDTO.getPlateNumber() != null) {

            Iterator<BusDTO> iterator = listActiveBuses.iterator();
            while (iterator.hasNext()) {
                BusDTO bus = iterator.next();
                if (bus.getPlateNumber().equals(busDTO.getPlateNumber())) {
                    busDTO = bus;
                }
            }

            try {
                orderControllerBean.editOrder(orderDTO, busDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(EditOrderPageBean.class.getName()).log(Level.SEVERE, null, ex);
                init();
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return null;
            }
            ContextUtils.getContext().getFlash().setKeepMessages(true);
            orderControllerBean.setSelectedConflictedDatesStateList(null);
            return "listMyCurrentOrders";
        }
        ContextUtils.emitI18NMessage("EditOrderForm:chooseBus", "choosen.bus.is.empty");
        return null;
    }

}
