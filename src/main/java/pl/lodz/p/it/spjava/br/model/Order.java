/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ORDERS", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQ_CLIENT_START", columnNames = {"ORDER_CREATOR", "ORDER_START_DATE"})
    , @UniqueConstraint(name = "UNIQ_CLIENT_END", columnNames = {"ORDER_CREATOR", "ORDER_END_DATE"})
    , @UniqueConstraint(name = "UNIQ_BUS_START", columnNames = {"ORDERED_BUS", "ORDER_START_DATE"})
    , @UniqueConstraint(name = "UNIQ_BUS_END", columnNames = {"ORDERED_BUS", "ORDER_END_DATE"})
})
@TableGenerator(name = "OrderGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "OrderGen")
@NamedQueries({
    @NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
    , @NamedQuery(name = "Order.findCurrentOrders", query = "SELECT o FROM Order o WHERE o.orderEndDate >= :currentDate")
    , @NamedQuery(name = "Order.findPastOrders", query = "SELECT o FROM Order o WHERE o.orderEndDate < :currentDate")
    , @NamedQuery(name = "Order.findCurrentBusOrders", query = "SELECT o FROM Order o WHERE o.orderedBus.plateNumber = :plateNumber AND o.orderEndDate >= :currentDate")
    , @NamedQuery(name = "Order.findPastBusOrders", query = "SELECT o FROM Order o WHERE o.orderedBus.plateNumber = :plateNumber AND o.orderEndDate < :currentDate")
    , @NamedQuery(name = "Order.findCurrentClientOrders", query = "SELECT o FROM Order o WHERE o.orderCreator.login = :login AND o.orderEndDate >= :currentDate")
    , @NamedQuery(name = "Order.findPastClientOrders", query = "SELECT o FROM Order o WHERE o.orderCreator.login = :login AND o.orderEndDate < :currentDate")
    , @NamedQuery(name = "Order.findByBus", query = "SELECT o FROM Order o WHERE o.orderedBus.plateNumber = :plateNumber")
    , @NamedQuery(name = "Order.findByBusAndDate", query = "SELECT o FROM Order o WHERE o.orderedBus = :orderedBus AND o.orderStartDate = :orderStartDate")
    , @NamedQuery(name = "Order.findByDateAndOrderCreator", query = "SELECT o FROM Order o WHERE o.orderStartDate = :orderStartDate AND o.orderCreator = :orderCreator")
    , @NamedQuery(name = "Order.findByDatesAndOrderCreator", query = "SELECT o FROM Order o WHERE o.orderStartDate = :orderStartDate AND o.orderEndDate = :orderEndDate AND o.orderCreator = :orderCreator")
    , @NamedQuery(name = "Order.findMyOrders", query = "SELECT o FROM Order o WHERE o.orderCreator = :orderCreator")
    , @NamedQuery(name = "Order.findClientOrders", query = "SELECT o FROM Order o WHERE o.orderCreator.login = :login")
})
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrderGenerator")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDER_START_DATE", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date orderStartDate;

    @JoinColumn(name = "ORDER_CREATOR", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Client orderCreator;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDER_END_DATE", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date orderEndDate;

    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true) //toDo7 dodać przy przebudowie bazy!
    @ManyToOne(optional = false)
    private Client modifiedBy;

    @JoinColumn(name = "ORDERED_BUS", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Bus orderedBus;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long id, Date orderStartDate) {
        this.id = id;
        this.orderStartDate = orderStartDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Client getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Client modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(Date orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public Date getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(Date orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.orderStartDate);
        hash = 79 * hash + Objects.hashCode(this.orderCreator);
        hash = 79 * hash + Objects.hashCode(this.orderEndDate);
        hash = 79 * hash + Objects.hashCode(this.modifiedBy);
        hash = 79 * hash + Objects.hashCode(this.orderedBus);
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.orderStartDate, other.orderStartDate)) {
            return false;
        }
        if (!Objects.equals(this.orderCreator, other.orderCreator)) {
            return false;
        }
        if (!Objects.equals(this.orderEndDate, other.orderEndDate)) {
            return false;
        }
        if (!Objects.equals(this.modifiedBy, other.modifiedBy)) {
            return false;
        }
        if (!Objects.equals(this.orderedBus, other.orderedBus)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", orderStartDate=" + orderStartDate + ", orderCreator=" + orderCreator + ", modifiedBy=" + modifiedBy + ", orderedBus=" + orderedBus + '}';
    }

}
