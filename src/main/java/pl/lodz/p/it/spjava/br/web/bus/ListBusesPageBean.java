/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.bus;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.ejb.endpoint.BusEndpoint;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.order.OrderControllerBean;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "listBusesPageBean")
@ViewScoped
public class ListBusesPageBean implements Serializable {

    @EJB
    private BusEndpoint busEndpoint;

    @Inject
    private BusControllerBean busControllerBean;

    @Inject
    OrderControllerBean orderControllerBean;

    private List<BusDTO> listBusesDTO;

    private DataModel<BusDTO> dataModelBuses;

    public ListBusesPageBean() {
    }

    public DataModel<BusDTO> getDataModelBuses() {
        return dataModelBuses;
    }

    public void setDataModelBuses(DataModel<BusDTO> dataModelBuses) {
        this.dataModelBuses = dataModelBuses;
    }

    public BusControllerBean getBusControllerBean() {
        return busControllerBean;
    }

    public void setBusControllerBean(BusControllerBean busControllerBean) {
        this.busControllerBean = busControllerBean;
    }

    @PostConstruct
    public void listAllBuses() {
        try {
            listBusesDTO = busControllerBean.listAllBuses();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelBuses = new ListDataModel<>(listBusesDTO);
    }

    public String deleteSelectedBusAction(BusDTO busDTO) {
        try {
            busControllerBean.deleteBus(busDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllBuses();
        return null;
    }

    public String activateBusAction(BusDTO busDTO) {
        try {
            busControllerBean.activateBus(busDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllBuses();
        return null;
    }

    public String deactivateBusAction(BusDTO busDTO) {
        try {
            busControllerBean.deactivateBus(busDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllBuses();
        return null;
    }

    public String editBusAction(BusDTO busDTO) {
        try {
            busControllerBean.setSelectedBussesListDTO(null);
            busControllerBean.selectBusForChange(busDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            listAllBuses();
            return null;
        }
        return "editBus";
    }

    public String listBusOrders(BusDTO busDTO) {
        try {
            busControllerBean.setSelectedBussesListDTO(null);
            busControllerBean.selectBusForChange(busDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListBusesPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            listAllBuses();
            return null;
        }
        return "listBusOrders";
    }

}
