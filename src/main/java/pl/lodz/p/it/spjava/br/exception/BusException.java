/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.br.model.Bus;

public class BusException extends AppBaseException {

    static final public String KEY_BUS_EXIST = "error.bus.exist.problem";
    static final public String KEY_BUS_WRONG_STATE = "error.bus.wrong.state.problem";
    static final public String KEY_BUS_NOT_FOUND = "error.no.bus.found.problem";
    static final public String ORDERS_ORDERED_BUS = "error.bus.is.in.order.problem";
    static final public String KEY_BUS_ALREADY_ACTIVE = "error.bus.already.active.problem";
    static final public String KEY_BUS_ALREADY_DEACTIVE = "error.bus.already.deactive.problem";
    static final public String KEY_BUS_UNAVAILABLE = "error.bus.unavailable.problem";
    static final public String KEY_BUS_EDITED = "error.bus.edited.problem";
    static final public String KEY_BUS_HAS_ACTIVE_ORDERS = "error.bus.has.active.orders.problem";
    static final public String KEY_BUS_WRONG_SEATS_NUMBER = "page.bus.validator.seats";

    private Bus bus;

    public Bus getBus() {
        return bus;
    }

    private BusException(String message, Bus bus) {
        super(message);
        this.bus = bus;
    }

    private BusException(String message, Throwable cause, Bus bus) {
        super(message, cause);
        this.bus = bus;
    }

    private BusException(String message, Throwable cause) {
        super(message, cause);
    }

    private BusException(String message) {
        super(message);
    }

    static public BusException createExceptionBusAlreadyExists(Throwable cause, Bus bus) {
        return new BusException(KEY_BUS_EXIST, cause, bus);
    }

    static public BusException createExceptionWrongState(Bus bus) {
        return new BusException(KEY_BUS_WRONG_STATE, bus);
    }

    static public BusException createExceptionBusInOrder(Throwable cause, Bus bus) {
        return new BusException(ORDERS_ORDERED_BUS, cause, bus);
    }

    static public BusException createExceptionBusAlreadyActvivated(Bus bus) {
        return new BusException(KEY_BUS_ALREADY_ACTIVE, bus);
    }

    static public BusException createExceptionBusAlreadyDeactvivated(Bus bus) {
        return new BusException(KEY_BUS_ALREADY_DEACTIVE, bus);
    }

    public static BusException createExceptionNoBusFound(NoResultException e) {
        return new BusException(KEY_BUS_NOT_FOUND, e);
    }

    public static BusException createExceptionBusUnavailable(Bus bus) {
        return new BusException(KEY_BUS_UNAVAILABLE, bus);
    }

    public static BusException createExceptionBusDetailsEdited(Bus bus) {
        return new BusException(KEY_BUS_EDITED, bus);
    }

    public static BusException createExceptionBusHasActiveOrders(Bus bus) {
        return new BusException(KEY_BUS_HAS_ACTIVE_ORDERS, bus);
    }

    public static BusException createExceptionBusWrongSeatsNumber() {
        return new BusException(KEY_BUS_WRONG_SEATS_NUMBER);
    }
}
