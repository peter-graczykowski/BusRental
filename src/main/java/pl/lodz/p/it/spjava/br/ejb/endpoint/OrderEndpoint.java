/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.ejb.endpoint;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.dto.OrderDTO;
import pl.lodz.p.it.spjava.br.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.BusFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.ClientFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.OrderFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.PlanistFacade;
import pl.lodz.p.it.spjava.br.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.br.exception.AccountException;
import pl.lodz.p.it.spjava.br.exception.BusException;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.exception.OrderException;
import pl.lodz.p.it.spjava.br.model.Bus;
import pl.lodz.p.it.spjava.br.model.Client;
import pl.lodz.p.it.spjava.br.model.Order;
import pl.lodz.p.it.spjava.br.web.order.OrderControllerBean;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class OrderEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private OrderFacade orderFacade;

    @EJB
    private PlanistFacade planistFacade;

    @EJB
    private ClientFacade clientFacade;

    @EJB
    private AccountFacade accountFacade;

    @EJB
    private BusFacade busFacade;

    @Resource
    private SessionContext sessionContext;

    private Order orderState;

    private List<Order> savedOrderStateList;

    private List<LocalDate> conflictedDatesStateList;

    @Inject
    private OrderControllerBean orderControllerBean;

    public Order getOrderState() {
        return orderState;
    }

    public void setOrderState(Order orderState) {
        this.orderState = orderState;
    }

    public List<LocalDate> getConflictedDatesStateList() {
        return conflictedDatesStateList;
    }

    public void setConflictedDatesStateList(List<LocalDate> conflictedDatesStateList) {
        this.conflictedDatesStateList = conflictedDatesStateList;
    }

    @RolesAllowed({"Client"})
    private Client loadCurrentClient() throws AppBaseException {
        String clientLogin = sessionContext.getCallerPrincipal().getName();
        Client clientAccount = clientFacade.findByLogin(clientLogin);
        if (clientAccount == null) {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
        if (!clientAccount.isActive()) {
            throw AccountException.createExceptionAccountNotActive(clientAccount);
        }
        return clientAccount;
    }

    public Order selectOrderWithIterator(Date startDate, Client orderCreator, List<Order> orders) {
        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
            Order order = iterator.next();
            if (order.getOrderStartDate().equals(startDate) && order.getOrderCreator().equals(orderCreator)) {
                return order;
            }
        }
        return null;
    }

    public List<LocalDate> listAllDatesInRange(Date firstListDate, Date lastListDate) {
        LocalDate start = firstListDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = lastListDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }

    public boolean selectedDtoAndDatabaseDateComparisonMechanism(OrderDTO orderDTO, BusDTO busDTO, Date startDate, Date endDate) {
        return ((orderDTO.getOrderStartDate().after(startDate) && orderDTO.getOrderStartDate().before(endDate))
                || (orderDTO.getOrderEndDate().after(startDate) && orderDTO.getOrderEndDate().before(endDate))
                || (startDate.after(orderDTO.getOrderStartDate()) && startDate.before(orderDTO.getOrderEndDate()))
                || (endDate.after(orderDTO.getOrderStartDate()) && endDate.before(orderDTO.getOrderEndDate()))
                || startDate.equals(orderDTO.getOrderStartDate())
                || endDate.equals(orderDTO.getOrderEndDate())
                || startDate.equals(orderDTO.getOrderEndDate())
                || endDate.equals(orderDTO.getOrderStartDate()));
    }

    public List<LocalDate> listConflictedDates(OrderDTO orderDTO, BusDTO busDTO) throws AppBaseException {
        List<LocalDate> conflictedDatesList = new ArrayList<>();

        List<Order> listAllSelectedClientOrders = orderFacade.findMyOrders(loadCurrentClient());
        for (Order selectedOrder : listAllSelectedClientOrders) {
            Date startDate = selectedOrder.getOrderStartDate();
            Date endDate = selectedOrder.getOrderEndDate();
            if ((selectedDtoAndDatabaseDateComparisonMechanism(orderDTO, busDTO, startDate, endDate))) {

                List<LocalDate> selectedOrderDatesList = listAllDatesInRange(startDate, endDate);
                List<LocalDate> clientOrderDatesList = listAllDatesInRange(orderDTO.getOrderStartDate(), orderDTO.getOrderEndDate());
                for (LocalDate selectedOrderDate : selectedOrderDatesList) {
                    for (LocalDate clientOrderDate : clientOrderDatesList) {
                        if (selectedOrderDate.equals(clientOrderDate)) {
                            conflictedDatesList.add(clientOrderDate);
                        }
                    }
                }
            }
        }

        List<Order> listAllSelectedBusOrders = orderFacade.findByBus(busDTO.getPlateNumber());
        for (Order selectedOrder : listAllSelectedBusOrders) {
            Date startDate = selectedOrder.getOrderStartDate();
            Date endDate = selectedOrder.getOrderEndDate();
            if ((selectedDtoAndDatabaseDateComparisonMechanism(orderDTO, busDTO, startDate, endDate))) {

                List<LocalDate> selectedOrderDatesList = listAllDatesInRange(startDate, endDate);
                List<LocalDate> clientOrderDatesList = listAllDatesInRange(orderDTO.getOrderStartDate(), orderDTO.getOrderEndDate());
                for (LocalDate selectedOrderDate : selectedOrderDatesList) {
                    for (LocalDate clientOrderDate : clientOrderDatesList) {
                        if (selectedOrderDate.equals(clientOrderDate)) {
                            conflictedDatesList.add(clientOrderDate);
                        }
                    }
                }
            }
        }
        List<LocalDate> conflictListWithoutDuplicates = new ArrayList<>(new HashSet<>(conflictedDatesList));
        Collections.reverse(conflictListWithoutDuplicates);
        return conflictListWithoutDuplicates;
    }

    @RolesAllowed({"Client"})
    public void addNewOrder(OrderDTO orderDTO, BusDTO busDTO) throws AppBaseException {

        LocalDate selectedStartDate = (orderDTO.getOrderStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate selectedEndDate = (orderDTO.getOrderEndDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime selectedStartDateTime = (orderDTO.getOrderStartDate()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (selectedStartDate.isBefore(LocalDate.now())) {
            throw OrderException.createExceptionDatePastDate();
        }
        // Święta stałe ustawowo wolne od pracy 
       List<LocalDate> permanentHolidaysList = new ArrayList<>();
        permanentHolidaysList.add(LocalDate.of(2020, Month.JANUARY, 1));
        permanentHolidaysList.add(LocalDate.of(2020, Month.JANUARY, 6));
        permanentHolidaysList.add(LocalDate.of(2020, Month.MAY, 1));
        permanentHolidaysList.add(LocalDate.of(2020, Month.MAY, 3));
        permanentHolidaysList.add(LocalDate.of(2020, Month.AUGUST, 15));
        permanentHolidaysList.add(LocalDate.of(2020, Month.NOVEMBER, 1));
        permanentHolidaysList.add(LocalDate.of(2020, Month.NOVEMBER, 11));
        permanentHolidaysList.add(LocalDate.of(2020, Month.DECEMBER, 25));
        permanentHolidaysList.add(LocalDate.of(2020, Month.DECEMBER, 26));

        // Święta ruchome ustawowo wolne od pracy 
        List<LocalDate> moveableHolidaysList = new ArrayList<>();
        moveableHolidaysList.add(LocalDate.of(2020, Month.APRIL, 12));
        moveableHolidaysList.add(LocalDate.of(2020, Month.APRIL, 13));
        moveableHolidaysList.add(LocalDate.of(2020, Month.MAY, 31));
        moveableHolidaysList.add(LocalDate.of(2020, Month.JUNE, 11));

        for (LocalDate permanentHoliday : permanentHolidaysList) {
            if (((permanentHoliday.getMonth().equals(selectedStartDate.getMonth())) && (permanentHoliday.getDayOfMonth() == selectedStartDate.getDayOfMonth()))
                    || ((permanentHoliday.getMonth().equals(selectedEndDate.getMonth())) && (permanentHoliday.getDayOfMonth() == selectedEndDate.getDayOfMonth()))
                    || moveableHolidaysList.contains(selectedStartDate)
                    || moveableHolidaysList.contains(selectedEndDate)) {
                throw OrderException.createExceptionDateHoliday();
            }
        }
        if (selectedEndDate.isAfter(LocalDate.now().plusMonths(6))) {
            throw OrderException.createExceptionDateSixMonthsRestriction();
        }
        if (selectedStartDate.plusWeeks(2).isBefore(selectedEndDate)) {
            throw OrderException.createExceptionDateTwoWeeksRestriction();
        }
        if (selectedStartDate.equals(LocalDate.now()) && selectedStartDateTime.getHour() > 19) {
            throw OrderException.createExceptionDateClosedAfterTwenty();
        }

        Bus bus = busFacade.findByPlateNumber(busDTO.getPlateNumber());
        if (!bus.isActive()) {
            orderControllerBean.setSelectedConflictedDatesStateList(null);
            throw BusException.createExceptionBusUnavailable(bus);
        }
        if (!bus.getBusName().equals(busDTO.getBusName()) || bus.getSeats() != busDTO.getSeats()) {
            orderControllerBean.setSelectedConflictedDatesStateList(null);
            throw BusException.createExceptionBusDetailsEdited(bus);
        }
        List<Order> listAllSelectedClientOrders = orderFacade.findMyOrders(loadCurrentClient());
        for (Order selectedOrder : listAllSelectedClientOrders) {
            Date startDate = selectedOrder.getOrderStartDate();
            Date endDate = selectedOrder.getOrderEndDate();
            if ((selectedDtoAndDatabaseDateComparisonMechanism(orderDTO, busDTO, startDate, endDate))) {
                throw OrderException.createExceptionClientAndDateConflict(selectedOrder);
            }
        }

        List<Order> listAllSelectedBusOrders = orderFacade.findByBus(busDTO.getPlateNumber());
        for (Order selectedOrder : listAllSelectedBusOrders) {
            Date startDate = selectedOrder.getOrderStartDate();
            Date endDate = selectedOrder.getOrderEndDate();
            if ((selectedDtoAndDatabaseDateComparisonMechanism(orderDTO, busDTO, startDate, endDate))) {
                throw OrderException.createExceptionBusAndDateConflict(selectedOrder);
            }
        }

        Order order = new Order();
        order.setOrderStartDate(orderDTO.getOrderStartDate());
        order.setOrderEndDate(orderDTO.getOrderEndDate());
        order.setOrderedBus(bus);
        order.setOrderCreator(loadCurrentClient());
        orderFacade.create(order);
    }

    @RolesAllowed({"Planist"})
    public List<OrderDTO> listCurrentOrders(Date currentDate) throws AppBaseException {
        List<Order> listCurrentOrders = orderFacade.findCurrentOrders(currentDate);
        savedOrderStateList = listCurrentOrders;
        List<OrderDTO> listCurrentOrdersDTO = new ArrayList<>();
        for (Order order : listCurrentOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderStartDate(),
                    order.getOrderEndDate(),
                    order.getOrderedBus(),
                    order.getOrderCreator()
            );
            listCurrentOrdersDTO.add(orderDTO);
        }
        Collections.sort(listCurrentOrdersDTO);
        return listCurrentOrdersDTO;
    }

    @RolesAllowed({"Planist"})
    public List<OrderDTO> listPastOrders(Date currentDate) throws AppBaseException {
        List<Order> listPastOrders = orderFacade.findPastOrders(currentDate);
        savedOrderStateList = listPastOrders;
        List<OrderDTO> listPastOrdersDTO = new ArrayList<>();
        for (Order order : listPastOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderStartDate(),
                    order.getOrderEndDate(),
                    order.getOrderedBus(),
                    order.getOrderCreator()
            );
            listPastOrdersDTO.add(orderDTO);
        }
        Collections.sort(listPastOrdersDTO);
        return listPastOrdersDTO;
    }

    @RolesAllowed({"Client"})
    public List<OrderDTO> listMyCurrentOrders(Date currentDate) throws AppBaseException {
        List<Order> listMyCurrentOrders = orderFacade.findCurrentClientOrders(loadCurrentClient().getLogin(), currentDate);
        savedOrderStateList = listMyCurrentOrders;
        List<OrderDTO> listMyCurrentOrdersDTO = new ArrayList<>();
        for (Order order : listMyCurrentOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderStartDate(),
                    order.getOrderStartDate().toString() + order.getOrderEndDate().toString(),
                    order.getOrderEndDate(),
                    order.getOrderedBus(),
                    order.getOrderCreator()
            );
            listMyCurrentOrdersDTO.add(orderDTO);
        }
        Collections.sort(listMyCurrentOrdersDTO);
        return listMyCurrentOrdersDTO;
    }

    @RolesAllowed({"Client"})
    public List<OrderDTO> listMyPastOrders(Date currentDate) throws AppBaseException {
        List<Order> listMyPastOrders = orderFacade.findPastClientOrders(loadCurrentClient().getLogin(), currentDate);
        savedOrderStateList = listMyPastOrders;
        List<OrderDTO> listMyPastOrdersDTO = new ArrayList<>();
        for (Order order : listMyPastOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderStartDate(),
                    order.getOrderStartDate().toString() + order.getOrderEndDate().toString(),
                    order.getOrderEndDate(),
                    order.getOrderedBus(),
                    order.getOrderCreator()
            );
            listMyPastOrdersDTO.add(orderDTO);
        }
        Collections.sort(listMyPastOrdersDTO);
        return listMyPastOrdersDTO;
    }

    @RolesAllowed({"Planist"})
    public List<OrderDTO> listBusOrders(BusDTO busDTO) throws AppBaseException {
        List<Order> listBusOrders = orderFacade.findBusOrders(busDTO.getPlateNumber());
        savedOrderStateList = listBusOrders;
        List<OrderDTO> listBusOrdersDTO = new ArrayList<>();
        for (Order order : listBusOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderStartDate(),
                    order.getOrderEndDate(),
                    order.getOrderedBus(),
                    order.getOrderCreator()
            );
            listBusOrdersDTO.add(orderDTO);
        }
        Collections.sort(listBusOrdersDTO);
        return listBusOrdersDTO;
    }

    @RolesAllowed({"Planist"})
    public void deleteCurrentBusOrders(String plateNumber, Date currentDate) throws AppBaseException {
        List<Order> listCurrentBusOrders = orderFacade.findCurrentBusOrders(plateNumber, currentDate);
        for (Order order : listCurrentBusOrders) {
            orderFacade.remove(order);
        }
    }

    @RolesAllowed({"Planist"})
    public void deletePastBusOrders(String plateNumber, Date currentDate) throws AppBaseException {
        List<Order> listPastBusOrders = orderFacade.findPastBusOrders(plateNumber, currentDate);
        for (Order order : listPastBusOrders) {
            orderFacade.remove(order);
        }
    }

    @RolesAllowed({"Planist"})
    public List<OrderDTO> listClintOrders(String login) throws AppBaseException {
        List<Order> listClintOrders = orderFacade.findClientOrders(login);
        savedOrderStateList = listClintOrders;
        List<OrderDTO> listClintOrdersDTO = new ArrayList<>();
        for (Order order : listClintOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderStartDate(),
                    order.getOrderEndDate(),
                    order.getOrderedBus(),
                    order.getOrderCreator()
            );
            listClintOrdersDTO.add(orderDTO);
        }
        Collections.sort(listClintOrdersDTO);
        return listClintOrdersDTO;
    }

    @RolesAllowed({"Planist"})
    public void deleteOrder(OrderDTO orderDTO) throws AppBaseException {
        Order order = selectOrderWithIterator(orderDTO.getOrderStartDate(), orderDTO.getOrderCreator(), savedOrderStateList);
        orderFacade.findByDatesAndOrderCreator(orderDTO.getOrderStartDate(), orderDTO.getOrderEndDate(), orderDTO.getOrderCreator());
        orderFacade.remove(order);
    }

    @RolesAllowed({"Client"})
    public void deleteMyOrder(OrderDTO orderDTO) throws AppBaseException {
        Order order = selectOrderWithIterator(orderDTO.getOrderStartDate(), orderDTO.getOrderCreator(), savedOrderStateList);
        Date currentDate = new Date(System.currentTimeMillis());
        if (orderDTO.getOrderEndDate().before(currentDate)) {
            throw OrderException.createExceptionOrderOutdatedDelete(order);
        }
        orderFacade.findByDatesAndOrderCreator(orderDTO.getOrderStartDate(), orderDTO.getOrderEndDate(), orderDTO.getOrderCreator());
        orderFacade.remove(order);
    }

    @RolesAllowed({"Client"})
    public void editOrder(OrderDTO orderDTO, BusDTO busDTO) throws AppBaseException {
        if (orderState.getOrderStartDate().equals(orderDTO.getOrderStartDate()) && orderState.getOrderCreator().equals(orderDTO.getOrderCreator())) {
            orderFacade.findByStartDateAndOrderCreator(orderDTO.getOrderStartDate(), orderDTO.getOrderCreator());

            Bus bus = busFacade.findByPlateNumber(busDTO.getPlateNumber());
            if (!bus.isActive()) {
                throw BusException.createExceptionBusUnavailable(bus);
            }
            if (!bus.getBusName().equals(busDTO.getBusName()) || bus.getSeats() != busDTO.getSeats()) {
                throw BusException.createExceptionBusDetailsEdited(bus);
            }

            List<Order> listAllSelectedBusOrders = orderFacade.findByBus(busDTO.getPlateNumber());
            for (Order selectedOrder : listAllSelectedBusOrders) {
                Date startDate = selectedOrder.getOrderStartDate();
                Date endDate = selectedOrder.getOrderEndDate();
                if ((selectedDtoAndDatabaseDateComparisonMechanism(orderDTO, busDTO, startDate, endDate))) {
                    throw OrderException.createExceptionBusAndDateConflict(selectedOrder);
                }
            }

            orderState.setOrderedBus(bus);
            orderState.setModifiedBy(loadCurrentClient());
            orderFacade.edit(orderState);
        } else {
            throw OrderException.createExceptionWrongState(orderState);
        }
    }

    @RolesAllowed({"Client"})
    public OrderDTO rememberSelectedOrderInState(OrderDTO orderDTO) throws AppBaseException {
        orderState = orderFacade.findByStartDateAndOrderCreator(orderDTO.getOrderStartDate(), orderDTO.getOrderCreator());
        Date currentDate = new Date(System.currentTimeMillis());
        if (orderDTO.getOrderEndDate().before(currentDate)) {
            throw OrderException.createExceptionOrderOutdatedDelete(orderState);
        }
        return new OrderDTO(
                orderState.getOrderStartDate(),
                new SimpleDateFormat("dd/MM/yyyy").format(orderState.getOrderStartDate()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(orderState.getOrderEndDate()),
                orderState.getOrderEndDate(),
                orderState.getOrderedBus(),
                orderState.getOrderCreator()
        );
    }

    @RolesAllowed({"Planist"})
    public void deleteAllPastOrders(Date currentDate) throws AppBaseException {
        List<Order> listAllPastOrders = orderFacade.findPastOrders(currentDate);
        if (listAllPastOrders.isEmpty()) {
            throw OrderException.createExceptionDateClosedAfterTwenty();
        }
        for (Order order : savedOrderStateList) {
            orderFacade.remove(order);
        }
    }

    @RolesAllowed({"Planist"})
    public void deleteCurrentClientOrders(String login, Date currentDate) throws AppBaseException {
        List<Order> listCurrentClientOrders = orderFacade.findCurrentClientOrders(login, currentDate);
        if (listCurrentClientOrders.isEmpty()) {
            throw OrderException.createExceptionDateClosedAfterTwenty();
        }
        for (Order order : savedOrderStateList) {
            orderFacade.remove(order);
        }
    }

    @RolesAllowed({"Planist"})
    public void deletePastClientOrders(String login, Date currentDate) throws AppBaseException {
        List<Order> listPastClientOrders = orderFacade.findPastClientOrders(login, currentDate);
        if (listPastClientOrders.isEmpty()) {
            throw OrderException.createExceptionDateClosedAfterTwenty();
        }
        for (Order order : savedOrderStateList) {
            orderFacade.remove(order);
        }
    }

}
