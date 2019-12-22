/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.bus;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.ejb.endpoint.BusEndpoint;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.order.OrderControllerBean;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "ourOfferPageBean")
@RequestScoped
public class OurOfferPageBean {

    @EJB
    private BusEndpoint busEndpoint;

    @Inject
    private BusControllerBean busControllerBean;

    @Inject
    OrderControllerBean orderControllerBean;

    private List<BusDTO> listBusesDTO;

    private DataModel<BusDTO> dataModelBuses;

    public OurOfferPageBean() {
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
            listBusesDTO = busControllerBean.listOfferBuses();
        } catch (AppBaseException ex) {
            Logger.getLogger(OurOfferPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelBuses = new ListDataModel<>(listBusesDTO);
    }

}
