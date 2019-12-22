/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import pl.lodz.p.it.spjava.br.model.Bus;
import pl.lodz.p.it.spjava.br.model.Client;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

public class OrderDTO implements Comparable<OrderDTO> {

    private Date orderStartDate;
    private String orderStringDate;
    private Date orderEndDate;
    private Bus orderedBus;
    private Client orderCreator;
    private String timeButtonLabel;

    public OrderDTO() {
    }

    public OrderDTO(Date orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public OrderDTO(Date orderStartDate, Date orderEndDate, Bus orderedBus, Client orderCreator) {
        this.orderStartDate = orderStartDate;
        this.orderEndDate = orderEndDate;
        this.orderedBus = orderedBus;
        this.orderCreator = orderCreator;
    }

    public OrderDTO(Date orderStartDate, String orderStringDate, Date orderEndDate, Bus orderedBus, Client orderCreator) {
        this.orderStartDate = orderStartDate;
        this.orderStringDate = orderStringDate;
        this.orderEndDate = orderEndDate;
        this.orderedBus = orderedBus;
        this.orderCreator = orderCreator;
    }

    public Date getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(Date orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public Client getOrderCreator() {
        return orderCreator;
    }

    public void setOrderCreator(Client orderCreator) {
        this.orderCreator = orderCreator;
    }

    public Bus getOrderedBus() {
        return orderedBus;
    }

    public void setOrderedBus(Bus orderedBus) {
        this.orderedBus = orderedBus;
    }

    public Date getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(Date orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public String getOrderStringDate() {
        return orderStringDate;
    }

    public void setOrderStringDate(String orderStringDate) throws ParseException {
        this.orderStartDate = new SimpleDateFormat("yyyy/MM/dd").parse(orderStringDate.substring(0, 10));
        this.orderEndDate = new SimpleDateFormat("yyyy/MM/dd").parse(orderStringDate.substring(13));
        this.orderStringDate = orderStringDate;
    }

    public String getTimeButtonLabel() {
        Date currentDate = new Date(System.currentTimeMillis());
        if (currentDate.after(orderEndDate)) {
            this.timeButtonLabel = ContextUtils.printI18NMessage("page.orders.list.action.delete.past");
        } else {
            this.timeButtonLabel = ContextUtils.printI18NMessage("page.orders.list.action.delete");
        }
        return timeButtonLabel;
    }

    public void setTimeButtonLabel(String timeButtonLabel) {
        this.timeButtonLabel = timeButtonLabel;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.orderStartDate);
        hash = 23 * hash + Objects.hashCode(this.orderStringDate);
        hash = 23 * hash + Objects.hashCode(this.orderEndDate);
        hash = 23 * hash + Objects.hashCode(this.orderedBus);
        hash = 23 * hash + Objects.hashCode(this.orderCreator);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderDTO other = (OrderDTO) obj;
        if (!Objects.equals(this.orderStringDate, other.orderStringDate)) {
            return false;
        }
        if (!Objects.equals(this.orderStartDate, other.orderStartDate)) {
            return false;
        }
        if (!Objects.equals(this.orderEndDate, other.orderEndDate)) {
            return false;
        }
        if (!Objects.equals(this.orderedBus, other.orderedBus)) {
            return false;
        }
        if (!Objects.equals(this.orderCreator, other.orderCreator)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderDTO{" + "orderDate=" + orderStartDate + ", orderCreator=" + orderCreator + ", orderedBus=" + orderedBus + '}';
    }

    @Override
    public int compareTo(OrderDTO o) {
        return this.orderStartDate.compareTo(o.orderStartDate);
    }

}
