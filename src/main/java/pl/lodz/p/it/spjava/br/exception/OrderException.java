/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.br.model.Order;

public class OrderException extends AppBaseException {

    static final public String KEY_CLIENT_AND_START_DATE_EXIST = "error.client.and.date.exist.problem";
    static final public String KEY_CLIENT_AND_END_DATE_EXIST = "error.client.and.date.exist.problem";
    static final public String KEY_CLIENT_AND_DATE_CONFLICT = "error.client.and.date.exist.problem";
    static final public String KEY_BUS_AND_START_DATE_EXIST = "error.bus.and.date.exist.problem";
    static final public String KEY_BUS_AND_END_DATE_EXIST = "error.bus.and.date.exist.problem";
    static final public String KEY_BUS_AND_DATE_CONFLICT = "error.bus.and.date.exist.problem";
    static final public String KEY_ORDER_WRONG_STATE = "error.order.wrong.state.problem";
    static final public String KEY_BUS_WRONG_PASSWORD = "error.order.wrong.password.problem";
    static final public String KEY_NO_ORDER_FOUND = "error.no.order.found.problem";
    static final public String KEY_ORDER_OUTDATED_DELETE = "error.order.outdated.delete.problem";
    static final public String KEY_ORDER_OUTDATED_EDIT = "error.order.outdated.edit.problem";
    static final public String KEY_DATE_PAST_DATE = "error.new.order.past.date";
    static final public String KEY_DATE_HOLIDAY = "error.new.order.holiday";
    static final public String KEY_DATE_SIX_MONTHS_RESTRICTION = "error.new.order.six.months.restriction";
    static final public String KEY_DATE_TWO_WEEKS_RESTRICTION = "error.new.order.two.weeks.restriction";
    static final public String KEY_DATE_CLOSED_AFTER_TWENTY = "error.new.order.closed.after.twenty";

    private Order order;

    public Order getOrder() {
        return order;
    }

    private OrderException(String message, Order order) {
        super(message);
        this.order = order;
    }

    private OrderException(String message, Throwable cause, Order order) {
        super(message, cause);
        this.order = order;
    }

    private OrderException(String message, Throwable cause) {
        super(message, cause);
    }

    private OrderException(String message) {
        super(message);
    }

    public static OrderException createExceptionClientAlreadyOrderedOnThatStartDate(Throwable cause, Order order) {
        return new OrderException(KEY_CLIENT_AND_START_DATE_EXIST, cause, order);
    }

    public static OrderException createExceptionClientAlreadyOrderedOnThatEndDate(Throwable cause, Order order) {
        return new OrderException(KEY_CLIENT_AND_END_DATE_EXIST, cause, order);
    }

    public static OrderException createExceptionBusAlreadyOrderedOnThatStartDate(Throwable cause, Order order) {
        return new OrderException(KEY_BUS_AND_START_DATE_EXIST, cause, order);
    }

    public static OrderException createExceptionBusAlreadyOrderedOnThatEndDate(Throwable cause, Order order) {
        return new OrderException(KEY_BUS_AND_END_DATE_EXIST, cause, order);
    }

    public static OrderException createExceptionClientAndDateConflict(Order order) {
        return new OrderException(KEY_CLIENT_AND_DATE_CONFLICT, order);
    }

    public static OrderException createExceptionBusAndDateConflict(Order order) {
        return new OrderException(KEY_BUS_AND_DATE_CONFLICT, order);
    }

    public static OrderException createExceptionOrderOutdatedDelete(Order order) {
        return new OrderException(KEY_ORDER_OUTDATED_DELETE, order);
    }

    public static OrderException createExceptionOrderOutdatedEdit(Order order) {
        return new OrderException(KEY_ORDER_OUTDATED_EDIT, order);
    }

    public static OrderException createExceptionWrongState(Order order) {
        return new OrderException(KEY_ORDER_WRONG_STATE, order);
    }

    public static OrderException createExceptionWrongPassword(Order order) {
        return new OrderException(KEY_BUS_WRONG_PASSWORD, order);
    }

    public static OrderException createExceptionNoOrderFound(NoResultException e) {
        return new OrderException(KEY_NO_ORDER_FOUND, e);
    }

    public static OrderException createExceptionDatePastDate() {
        return new OrderException(KEY_DATE_PAST_DATE);
    }

    public static OrderException createExceptionDateHoliday() {
        return new OrderException(KEY_DATE_HOLIDAY);
    }

    public static OrderException createExceptionDateSixMonthsRestriction() {
        return new OrderException(KEY_DATE_SIX_MONTHS_RESTRICTION);
    }

    public static OrderException createExceptionDateTwoWeeksRestriction() {
        return new OrderException(KEY_DATE_TWO_WEEKS_RESTRICTION);
    }

    public static OrderException createExceptionDateClosedAfterTwenty() {
        return new OrderException(KEY_DATE_CLOSED_AFTER_TWENTY);
    }

}
