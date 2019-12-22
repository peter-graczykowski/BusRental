/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.bus;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "editBusPageBean")
@RequestScoped
public class EditBusPageBean {

    @Inject
    private BusControllerBean busControllerBean;

    private BusDTO busDTO;

    public EditBusPageBean() {
    }

    public BusDTO getBusDTO() {
        return busDTO;
    }

    public void setBusDTO(BusDTO busDTO) {
        this.busDTO = busDTO;
    }

    @PostConstruct
    public void init() {
        busDTO = busControllerBean.getSelectedBusDTO();
    }

    public String saveEditBusAction() {
        if (busDTO.getSeats() >= 8 && busDTO.getSeats() < 61) {
            try {
                busControllerBean.editBus(busDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(EditBusPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
            }
        } else {
            ContextUtils.emitI18NMessage("NewBusForm:seats", "page.bus.validator.seats");
            return null;
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        return "listBuses";
    }

}
