/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.model;

import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "BUS", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQUE_PLATE_NR", columnNames = "PLATE_NUMBER")
})
@TableGenerator(name = "BusGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "BusGen")
@NamedQueries({
    @NamedQuery(name = "Bus.findAll", query = "SELECT b FROM Bus b")
    , @NamedQuery(name = "Bus.findByPlateNumber", query = "SELECT b FROM Bus b WHERE b.plateNumber = :plateNumber")
    , @NamedQuery(name = "Bus.findActiveBus", query = "SELECT b FROM Bus b WHERE b.active = true")
})
public class Bus extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BusGenerator")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "BUS_NAME", nullable = false)
    private String busName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 6, max = 9)
    @Column(name = "PLATE_NUMBER", nullable = false)
    private String plateNumber;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "SEATS", nullable = false)
    private int seats;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @JoinColumn(name = "BUS_CREATOR", referencedColumnName = "ID", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Planist busCreator;

    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = false)
    private Planist modifiedBy;

    public Bus() {
    }

    public Bus(Long id) {
        this.id = id;
    }

    public Bus(Long id, String busName, String plateNumber, int seats, boolean active) {
        this.id = id;
        this.busName = busName;
        this.plateNumber = plateNumber;
        this.seats = seats;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Planist getBusCreator() {
        return busCreator;
    }

    public void setBusCreator(Planist busCreator) {
        this.busCreator = busCreator;
    }

    public Planist getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Planist modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return busName + " " + plateNumber;
    }

}
