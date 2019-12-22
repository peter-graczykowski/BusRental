/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.order;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.dto.OrderDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.BusEndpoint;
import pl.lodz.p.it.spjava.br.ejb.endpoint.OrderEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "orderControllerBean")
@SessionScoped
public class OrderControllerBean implements Serializable {

    @EJB
    private OrderEndpoint orderEndpoint;

    @EJB
    private BusEndpoint busEndpoint;

    private int lastActionMethod = 0;

    private BusDTO selectedBusDTO;

    private OrderDTO selectedOrderDTO;

    private List<OrderDTO> selectedOrdersListDTO;

    private List<LocalDate> selectedConflictedDatesStateList;

    public OrderControllerBean() {
    }

    public OrderDTO getSelectedOrderDTO() {
        return selectedOrderDTO;
    }

    public void setSelectedOrderDTO(OrderDTO selectedOrderDTO) {
        this.selectedOrderDTO = selectedOrderDTO;
    }

    public List<LocalDate> getSelectedConflictedDatesStateList() {
        return selectedConflictedDatesStateList;
    }

    public void setSelectedConflictedDatesStateList(List<LocalDate> selectedConflictedDatesStateList) {
        this.selectedConflictedDatesStateList = selectedConflictedDatesStateList;
    }

    public void newBusOrder(final OrderDTO orderDTO, final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + busDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedConflictedDatesStateList = orderEndpoint.listConflictedDates(orderDTO, busDTO);
                orderEndpoint.addNewOrder(orderDTO, busDTO);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteOrder(final OrderDTO orderDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteOrder(orderDTO);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteMyOrder(final OrderDTO orderDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteMyOrder(orderDTO);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<OrderDTO> listCurrentOrders(final Date currentDate) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listCurrentOrders(currentDate);
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedOrdersListDTO;
    }

    public List<OrderDTO> listPastOrders(final Date currentDate) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listPastOrders(currentDate);
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedOrdersListDTO;
    }

    public List<OrderDTO> listMyCurrentOrders(final Date currentDate) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listMyCurrentOrders(currentDate);
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedOrdersListDTO;
    }

    public List<OrderDTO> listMyPastOrders(final Date currentDate) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listMyPastOrders(currentDate);
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedOrdersListDTO;
    }

    public List<OrderDTO> listBusOrders(BusDTO busDTO) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listBusOrders(busDTO);
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedOrdersListDTO;
    }

    public void deleteCurrentBusOrders(final BusDTO busDTO, final Date currentDate) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteCurrentBusOrders(busDTO.getPlateNumber(), currentDate);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deletePastBusOrders(final BusDTO busDTO, final Date currentDate) throws AppBaseException {
        final int UNIQ_METHOD_ID = busDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deletePastBusOrders(busDTO.getPlateNumber(), currentDate);
                endpointCallCounter--;
            } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<OrderDTO> listClientOrders(AccountDTO accountDTO) throws AppBaseException {
        int endpointCallCounter = busEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listClintOrders(accountDTO.getLogin());
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
        return selectedOrdersListDTO;
    }

    public void editOrder(final OrderDTO orderDTO, final BusDTO busDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + 6;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedConflictedDatesStateList = orderEndpoint.listConflictedDates(orderDTO, busDTO);
                orderEndpoint.editOrder(orderDTO, busDTO);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteAllPastOrders(Date currentDate) throws AppBaseException {
        final int UNIQ_METHOD_ID = currentDate.hashCode() + 7;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteAllPastOrders(currentDate);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteCurrentClientOrders(final AccountDTO accountDTO, final Date currentDate) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 8;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteCurrentClientOrders(accountDTO.getLogin(), currentDate);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deletePastClientOrders(final AccountDTO accountDTO, final Date currentDate) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 9;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deletePastClientOrders(accountDTO.getLogin(), currentDate);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
            }
            ContextUtils.emitI18NMessage("OrdersForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void selectOrderForChange(OrderDTO orderDTO) throws AppBaseException {
        int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrderDTO = orderEndpoint.rememberSelectedOrderInState(orderDTO);
            endpointCallCounter--;
        } while (busEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.creatExceptionForRepeatedTransactionRollback();
        }
    }

}
