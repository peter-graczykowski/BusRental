/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.bus;

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
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "newBusPageBean")
@RequestScoped
public class NewBusPageBean implements Serializable {

    @Inject
    private BusControllerBean busControllerBean;

    private BusDTO busDTO;

    public NewBusPageBean() {
    }

    public BusDTO getBusDTO() {
        return busDTO;
    }

    public void setBusDTO(BusDTO busDTO) {
        this.busDTO = busDTO;
    }

    @PostConstruct
    public void init() {
        busDTO = new BusDTO();
    }

    public String newBusAction() {
        if (busDTO.getSeats() >= 8 && busDTO.getSeats() < 61) {
            try {
                busControllerBean.newBus(busDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(NewBusPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(ex.getMessage().equals("page.bus.validator.seats") ? "NewBusForm:seats" : null, ex.getMessage());
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("NewBusForm:seats", "page.bus.validator.seats");
            return null;
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        return "listBuses";
    }

}
