/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.bus;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.BusEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "busControllerBean")
@SessionScoped
public class BusControllerBean implements Serializable {

    @EJB
    private BusEndpoint busEndpoint;

    private int lastActionMethod = 0;

    private BusDTO selectedBusDTO;

    private List<BusDTO> selectedBussesListDTO;

    private List<BusDTO> offerBussesListDTO;

    private DataModel<BusDTO> dataModelBuses;

    private ListDataModel<BusDTO> listDataModelBuses;

    public BusControllerBean() {
    }

    public BusDTO getSelectedBusDTO() {
        return selectedBusDTO;
    }

    public void setSelectedBusDTO(BusDTO selectedBusDTO) {
        this.selectedBusDTO = selectedBusDTO;
    }

    public List<BusDTO> getSelectedBussesListDTO() {
        return selectedBussesListDTO;
    }

    public void setSelectedBussesListDTO(List<BusDTO> selectedBussesListDTO) {
        this.selectedBussesListDTO = selectedBussesListDTO;
    }

    public DataModel<BusDTO> getDataModelBuses() {
        return dataModelBuses;
    }

    public void setDataModelBuses(DataModel<BusDTO> dataModelBuses) {
        this.dataModelBuses = dataModelBuses;
    }

    public ListDataModel<BusDTO> getListDataModelBuses() {
        return listDataModelBuses;
    }

    public void setListDataModelBuses(ListDataModel<BusDTO> listDataModelBuses) {
        this.listDataModelBuses = listDataModelBuses;
    }

    public void newBus(final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                busEndpoint.newBus(busDTO);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("BussesForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteBus(final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                busEndpoint.deleteBus(busDTO);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("BussesForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<BusDTO> listAllBuses() throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedBussesListDTO = busEndpoint.listAllBuses();
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedBussesListDTO;
    }

    public List<BusDTO> listOfferBuses() throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            offerBussesListDTO = busEndpoint.listOfferBuses();
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return offerBussesListDTO;
    }

    public void editBus(final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                busEndpoint.editBus(busDTO);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("BussesForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void activateBus(final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                busEndpoint.activateBus(busDTO);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("BussesForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deactivateBus(final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 6;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                busEndpoint.deactivateBus(busDTO);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("BussesForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectBusForChange(BusDTO busDTO) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedBusDTO = busEndpoint.rememberSelectedBusInState(busDTO.getPlateNumber());
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

}
