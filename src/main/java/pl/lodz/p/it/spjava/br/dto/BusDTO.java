/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.dto;

import java.util.Date;
import java.util.Objects;
import pl.lodz.p.it.spjava.br.model.Planist;
import pl.lodz.p.it.spjava.br.web.utils.ContextUtils;

public class BusDTO implements Comparable<BusDTO> {

    private String busName;
    private String plateNumber;
    private int seats;
    private boolean active;
    private Planist busCreator;
    private boolean activeReservations;
    private Date createdAt;

    public BusDTO() {
    }

    public BusDTO(String busName, String plateNumber, int seats) {
        this.busName = busName;
        this.plateNumber = plateNumber;
        this.seats = seats;
    }

    public BusDTO(String busName, String plateNumber, int seats, boolean active) {
        this.busName = busName;
        this.plateNumber = plateNumber;
        this.seats = seats;
        this.active = active;
    }

    public BusDTO(String busName, String plateNumber, int seats, boolean active, Planist busCreator, boolean activeReservations, Date createdAt) {
        this.busName = busName;
        this.plateNumber = plateNumber;
        this.seats = seats;
        this.active = active;
        this.busCreator = busCreator;
        this.activeReservations = activeReservations;
        this.createdAt = createdAt;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
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

    public Planist getBusCreator() {
        return busCreator;
    }

    public boolean isActiveReservations() {
        return activeReservations;
    }

    public void setActiveReservations(boolean activeReservations) {
        this.activeReservations = activeReservations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.busName);
        hash = 61 * hash + Objects.hashCode(this.plateNumber);
        hash = 61 * hash + this.seats;
        hash = 61 * hash + (this.active ? 1 : 0);
        hash = 61 * hash + Objects.hashCode(this.busCreator);
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
        final BusDTO other = (BusDTO) obj;
        if (this.seats != other.seats) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.busName, other.busName)) {
            return false;
        }
        if (!Objects.equals(this.plateNumber, other.plateNumber)) {
            return false;
        }
        if (!Objects.equals(this.busCreator, other.busCreator)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return busName + ", " + ContextUtils.printI18NMessage("bus.dto.seats") + ": " + seats;
    }

    @Override
    public int compareTo(BusDTO o) {
        return this.createdAt.compareTo(o.createdAt);
    }

}
