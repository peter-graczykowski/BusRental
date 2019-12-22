/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.web.order;

import pl.lodz.p.it.spjava.br.web.bus.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.br.dto.AccountDTO;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.dto.OrderDTO;
import pl.lodz.p.it.spjava.br.ejb.endpoint.BusEndpoint;
import pl.lodz.p.it.spjava.br.ejb.endpoint.OrderEndpoint;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

@Named(value = "newOrderPageBean")
@ViewScoped
public class NewOrderPageBean implements Serializable {

    @EJB
    private BusEndpoint busEndpoint;

    @EJB
    private OrderEndpoint orderEndpoint;

    @Inject
    private OrderControllerBean orderControllerBean;

    private List<BusDTO> listActiveBuses;

    private AccountDTO accountDTO;

    private OrderDTO orderDTO;

    private BusDTO busDTO;

    private String displayConflictDates;

    public NewOrderPageBean() {

    }

    public List<BusDTO> getListActiveBuses() {
        return listActiveBuses;
    }

    public void setListActiveBuses(List<BusDTO> listActiveBuses) {
        this.listActiveBuses = listActiveBuses;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
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

    public OrderControllerBean getOrderControllerBean() {
        return orderControllerBean;
    }

    public void setOrderControllerBean(OrderControllerBean orderControllerBean) {
        this.orderControllerBean = orderControllerBean;
    }

    public String getDisplayConflictDates() {
        return displayConflictDates;
    }

    public void setDisplayConflictDates(String ConflictedDatesStringList) {
        this.displayConflictDates = ConflictedDatesStringList;
    }

    @PostConstruct
    public void initOrderDetails() {
        try {
            if (orderControllerBean.getSelectedConflictedDatesStateList() != null) {
                displayConflictDates = orderControllerBean.getSelectedConflictedDatesStateList().toString().replace("[", "").replace("]", "");
            }
            orderControllerBean.setSelectedConflictedDatesStateList(null);

            listActiveBuses = busEndpoint.listActiveBuses();
        } catch (AppBaseException ex) {
            Logger.getLogger(NewBusPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        orderDTO = new OrderDTO();
        busDTO = new BusDTO();
    }

    public String newOrderAction() throws AppBaseException {
        if (busDTO.getPlateNumber() != null) {

            Iterator<BusDTO> iterator = listActiveBuses.iterator();
            while (iterator.hasNext()) {
                BusDTO bus = iterator.next();
                if (bus.getPlateNumber().equals(busDTO.getPlateNumber())) {
                    busDTO = bus;
                }
            }

            LocalDate selectedStartDate = (orderDTO.getOrderStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate selectedEndDate = (orderDTO.getOrderEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDateTime selectedStartDateTime = (orderDTO.getOrderStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (selectedStartDate.isBefore(LocalDate.now())) {
                ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.past.date");
                return null;
            }
            // Święta stałe ustawowo wolne od pracy 
            List<LocalDate> permanentHolidaysList = new ArrayList<>();
            permanentHolidaysList.add(LocalDate.of(2020, Month.JANUARY, 1)); // Nowy Rok
            permanentHolidaysList.add(LocalDate.of(2020, Month.JANUARY, 6)); // Trzech Króli
            permanentHolidaysList.add(LocalDate.of(2020, Month.MAY, 1)); // Święto Pracy
            permanentHolidaysList.add(LocalDate.of(2020, Month.MAY, 3)); // Święto Konstytucji 3 Maja
            permanentHolidaysList.add(LocalDate.of(2020, Month.AUGUST, 15)); // Wniebowzięcie Najświętszej Maryi Panny
            permanentHolidaysList.add(LocalDate.of(2020, Month.NOVEMBER, 1)); // Wszystkich Świętych
            permanentHolidaysList.add(LocalDate.of(2020, Month.NOVEMBER, 11)); // Narodowe Święto Niepodległości
            permanentHolidaysList.add(LocalDate.of(2020, Month.DECEMBER, 25)); // Boże Narodzenie
            permanentHolidaysList.add(LocalDate.of(2020, Month.DECEMBER, 26)); // Drugi dzień Bożego Narodzenia

            // Święta ruchome ustawowo wolne od pracy 
            List<LocalDate> moveableHolidaysList = new ArrayList<>();
            moveableHolidaysList.add(LocalDate.of(2020, Month.APRIL, 12)); // Wielkanoc
            moveableHolidaysList.add(LocalDate.of(2020, Month.APRIL, 13)); // Poniedziałek Wielkanocny
            moveableHolidaysList.add(LocalDate.of(2020, Month.MAY, 31)); // Zielone Świątki
            moveableHolidaysList.add(LocalDate.of(2020, Month.JUNE, 11)); // Boże Ciało

            for (LocalDate permanentHoliday : permanentHolidaysList) {
                if (((permanentHoliday.getMonth().equals(selectedStartDate.getMonth())) && (permanentHoliday.getDayOfMonth() == selectedStartDate.getDayOfMonth()))
                        || ((permanentHoliday.getMonth().equals(selectedEndDate.getMonth())) && (permanentHoliday.getDayOfMonth() == selectedEndDate.getDayOfMonth()))
                        || moveableHolidaysList.contains(selectedStartDate)
                        || moveableHolidaysList.contains(selectedEndDate)) {
                    ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.holiday");
                    return null;
                }
            }
            if (selectedEndDate.isAfter(LocalDate.now().plusMonths(6))) {
                ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.six.months.restriction");
                return null;
            }
            if (selectedStartDate.plusWeeks(2).isBefore(selectedEndDate)) {
                ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.two.weeks.restriction");
                return null;
            }
            if (selectedStartDate.equals(LocalDate.now()) && selectedStartDateTime.getHour() > 19) {
                ContextUtils.emitI18NMessage("OrderForm:orderDate", "error.new.order.closed.after.twenty");
                return null;
            }

            try {
                orderControllerBean.newBusOrder(orderDTO, busDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(NewBusPageBean.class.getName()).log(Level.SEVERE, null, ex);
                initOrderDetails();
                String messageLocation;
                switch (ex.getMessage()) {
                    case "error.bus.unavailable.problem":
                    case "error.bus.edited.problem":
                        messageLocation = "OrderForm:chosenBus";
                        break;
                    case "error.new.order.past.date":
                    case "error.new.order.holiday":
                    case "error.new.order.six.months.restriction":
                    case "error.new.order.two.weeks.restriction":
                    case "error.new.order.closed.after.twenty":
                        messageLocation = "OrderForm:orderDate";
                        break;
                    default:
                        messageLocation = null;
                        break;
                }
                ContextUtils.emitI18NMessage(messageLocation, ex.getMessage());
                return null;
            }
            orderControllerBean.setSelectedConflictedDatesStateList(null);
            return "listMyCurrentOrders";
        }
        ContextUtils.emitI18NMessage("OrderForm:chosenBus", "choosen.bus.is.empty");
        return null;

    }
}
