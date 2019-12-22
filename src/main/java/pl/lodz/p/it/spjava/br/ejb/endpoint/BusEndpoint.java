/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.ejb.endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.br.dto.BusDTO;
import pl.lodz.p.it.spjava.br.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.BusFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.ClientFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.OrderFacade;
import pl.lodz.p.it.spjava.br.ejb.facade.PlanistFacade;
import pl.lodz.p.it.spjava.br.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.br.exception.AccountException;
import pl.lodz.p.it.spjava.br.exception.BusException;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.model.Bus;
import pl.lodz.p.it.spjava.br.model.Planist;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class BusEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private PlanistFacade planistFacade;

    @EJB
    private ClientFacade clientFacade;

    @EJB
    private AccountFacade accountFacade;

    @EJB
    private BusFacade busFacade;

    @EJB
    private OrderFacade orderFacade;

    @Resource
    private SessionContext sessionContext;

    private Bus busState;

    private List<Bus> savedBusStateList;

    public Bus getBusState() {
        return busState;
    }

    public void setBusState(Bus busState) {
        this.busState = busState;
    }

    @RolesAllowed({"Planist"})
    private Planist loadCurrentPlanist() throws AppBaseException {
        String planistLogin = sessionContext.getCallerPrincipal().getName();
        Planist planistAccount = planistFacade.findByLogin(planistLogin);
        if (planistAccount == null) {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
        if (!planistAccount.isActive()) {
            throw AccountException.createExceptionAccountNotActive(planistAccount);
        }
        return planistAccount;
    }

    public Bus selectBusWithIterator(String plateNumber, List<Bus> buses) {
        Iterator<Bus> iterator = buses.iterator();
        while (iterator.hasNext()) {
            Bus bus = iterator.next();
            if (bus.getPlateNumber().equals(plateNumber)) {
                return bus;
            }
        }
        return null;
    }

    @RolesAllowed({"Planist"})
    public void newBus(BusDTO busDTO) throws AppBaseException {
        if (busDTO.getSeats() >= 8 && busDTO.getSeats() < 61) {
            Bus bus = new Bus();
            bus.setBusName(busDTO.getBusName());
            bus.setPlateNumber(busDTO.getPlateNumber());
            bus.setSeats(busDTO.getSeats());
            bus.setActive(true);
            bus.setBusCreator(loadCurrentPlanist());
            busFacade.create(bus);
        } else {
            throw BusException.createExceptionBusWrongSeatsNumber();
        }
    }

    @RolesAllowed({"Client"})
    public List<BusDTO> listActiveBuses() throws AppBaseException {
        List<Bus> listBuses = busFacade.findActiveBus();
        savedBusStateList = listBuses;
        List<BusDTO> listActiveBuses = new ArrayList<>();
        for (Bus bus : listBuses) {
            BusDTO busDTO = new BusDTO(
                    bus.getBusName(),
                    bus.getPlateNumber(),
                    bus.getSeats(),
                    bus.isActive()
            );
            listActiveBuses.add(busDTO);
        }
        return listActiveBuses;
    }

    @RolesAllowed({"Planist"})
    public List<BusDTO> listAllBuses() throws AppBaseException {
        List<Bus> listBuses = busFacade.findAll();
        savedBusStateList = listBuses;
        List<BusDTO> listAllBuses = new ArrayList<>();
        for (Bus bus : listBuses) {
            BusDTO busDTO = new BusDTO(
                    bus.getBusName(),
                    bus.getPlateNumber(),
                    bus.getSeats(),
                    bus.isActive(),
                    bus.getBusCreator(),
                    orderFacade.findByBus(bus.getPlateNumber()).isEmpty(),
                    bus.getCreatedAt()
            );
            listAllBuses.add(busDTO);
        }
        Collections.sort(listAllBuses);
        return listAllBuses;
    }

    @PermitAll
    public List<BusDTO> listOfferBuses() throws AppBaseException {
        List<Bus> listAllOfferBuses = busFacade.findAll();
        savedBusStateList = listAllOfferBuses;
        List<BusDTO> listOfferBuses = new ArrayList<>();
        for (Bus bus : listAllOfferBuses) {
            BusDTO busDTO = new BusDTO(
                    bus.getBusName(),
                    bus.getPlateNumber(),
                    bus.getSeats()
            );
            listOfferBuses.add(busDTO);
        }
        return listOfferBuses;
    }

    @RolesAllowed({"Planist"})
    public void deleteBus(BusDTO busDTO) throws AppBaseException {
        Bus bus = selectBusWithIterator(busDTO.getPlateNumber(), savedBusStateList);
        busFacade.findByPlateNumber(busDTO.getPlateNumber());
        busFacade.remove(bus);
    }

    @RolesAllowed({"Planist"})
    public void activateBus(BusDTO busDTO) throws AppBaseException {
        Bus bus = selectBusWithIterator(busDTO.getPlateNumber(), savedBusStateList);
        busFacade.findByPlateNumber(busDTO.getPlateNumber());
        if (!busDTO.isActive()) {
            bus.setActive(true);
            bus.setModifiedBy(loadCurrentPlanist());
            busFacade.edit(bus);
        } else {
            throw BusException.createExceptionBusAlreadyActvivated(bus);
        }
    }

    @RolesAllowed({"Planist"})
    public void deactivateBus(BusDTO busDTO) throws AppBaseException {
        Bus bus = selectBusWithIterator(busDTO.getPlateNumber(), savedBusStateList);
        busFacade.findByPlateNumber(busDTO.getPlateNumber());
        if (busDTO.isActive()) {
            bus.setActive(false);
            bus.setModifiedBy(loadCurrentPlanist());
            busFacade.edit(bus);
        } else {
            throw BusException.createExceptionBusAlreadyDeactvivated(bus);
        }
    }

    @RolesAllowed({"Planist"})
    public void editBus(BusDTO busDTO) throws AppBaseException {
        Date currentDate = new Date(System.currentTimeMillis());
        if (!orderFacade.findCurrentBusOrders(busDTO.getPlateNumber(), currentDate).isEmpty()) {
            throw BusException.createExceptionBusHasActiveOrders(busState);
        }
        if (busState.getPlateNumber().equals(busDTO.getPlateNumber())) {
            busFacade.findByPlateNumber(busDTO.getPlateNumber());
            busState.setBusName(busDTO.getBusName());
            busState.setSeats(busDTO.getSeats());
            busState.setModifiedBy(loadCurrentPlanist());
            busFacade.edit(busState);
        } else {
            throw BusException.createExceptionWrongState(busState);
        }
    }

    @RolesAllowed({"Planist"})
    public BusDTO rememberSelectedBusInState(String plateNumber) throws AppBaseException {
        busState = busFacade.findByPlateNumber(plateNumber);
        return new BusDTO(
                busState.getBusName(),
                busState.getPlateNumber(),
                busState.getSeats());
    }

}
