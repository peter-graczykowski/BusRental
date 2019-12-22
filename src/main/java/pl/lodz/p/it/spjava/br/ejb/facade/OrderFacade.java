/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.Date;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.br.exception.OrderException;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.exception.BusException;
import pl.lodz.p.it.spjava.br.model.Client;
import pl.lodz.p.it.spjava.br.model.Order;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class OrderFacade extends AbstractFacade<Order> {

    static final public String DB_UNIQUE_CONSTRAINT_CLIENT_AND_START_DATE = "UNIQ_CLIENT_START";
    static final public String DB_UNIQUE_CONSTRAINT_CLIENT_AND_END_DATE = "UNIQ_CLIENT_END";
    static final public String DB_UNIQUE_CONSTRAINT_BUS_AND_START_DATE = "UNIQ_BUS_START";
    static final public String DB_UNIQUE_CONSTRAINT_BUS_AND_END_DATE = "UNIQ_BUS_END";

    @PersistenceContext(unitName = "BusRentalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderFacade() {
        super(Order.class);
    }

    @RolesAllowed({"Client"})
    @Override
    public void create(Order entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_CLIENT_AND_START_DATE)) {
                throw OrderException.createExceptionClientAlreadyOrderedOnThatStartDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_CLIENT_AND_END_DATE)) {
                throw OrderException.createExceptionClientAlreadyOrderedOnThatEndDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_BUS_AND_START_DATE)) {
                throw OrderException.createExceptionBusAlreadyOrderedOnThatStartDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_BUS_AND_END_DATE)) {
                throw OrderException.createExceptionBusAlreadyOrderedOnThatEndDate(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Client"})
    @Override
    public void edit(Order entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_CLIENT_AND_START_DATE)) {
                throw OrderException.createExceptionClientAlreadyOrderedOnThatStartDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_CLIENT_AND_END_DATE)) {
                throw OrderException.createExceptionClientAlreadyOrderedOnThatEndDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_BUS_AND_START_DATE)) {
                throw OrderException.createExceptionBusAlreadyOrderedOnThatStartDate(e, entity);
            }
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_BUS_AND_END_DATE)) {
                throw OrderException.createExceptionBusAlreadyOrderedOnThatEndDate(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Planist", "Client"})
    @Override
    public void remove(Order entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            throw AppBaseException.createExceptionDatabaseQueryProblem(e);
        }
    }

    @RolesAllowed({"Planist"})
    @Override
    public List<Order> findAll() throws AppBaseException {
        try {
            return super.findAll();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Client"})
    public Order findByStartDateAndOrderCreator(Date orderStartDate, Client orderCreator) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findByDateAndOrderCreator", Order.class);
        tq.setParameter("orderStartDate", orderStartDate);
        tq.setParameter("orderCreator", orderCreator);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw OrderException.createExceptionNoOrderFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist", "Client"})
    public Order findByDatesAndOrderCreator(Date orderStartDate, Date orderEndDate, Client orderCreator) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findByDatesAndOrderCreator", Order.class);
        tq.setParameter("orderStartDate", orderStartDate);
        tq.setParameter("orderEndDate", orderEndDate);
        tq.setParameter("orderCreator", orderCreator);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw OrderException.createExceptionNoOrderFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist"})
    public List<Order> findPastOrders(Date currentDate) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findPastOrders", Order.class);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist", "Client"})
    public List<Order> findCurrentClientOrders(String login, Date currentDate) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findCurrentClientOrders", Order.class);
        tq.setParameter("login", login);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist", "Client"})
    public List<Order> findPastClientOrders(String login, Date currentDate) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findPastClientOrders", Order.class);
        tq.setParameter("login", login);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist"})
    public List<Order> findCurrentOrders(Date currentDate) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findCurrentOrders", Order.class);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist", "Client"})
    public List<Order> findByBus(String plateNumber) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findByBus", Order.class);
        tq.setParameter("plateNumber", plateNumber);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist"})
    public List<Order> findCurrentBusOrders(String plateNumber, Date currentDate) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findCurrentBusOrders", Order.class);
        tq.setParameter("plateNumber", plateNumber);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist"})
    public List<Order> findPastBusOrders(String plateNumber, Date currentDate) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findPastBusOrders", Order.class);
        tq.setParameter("plateNumber", plateNumber);
        tq.setParameter("currentDate", currentDate);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist"})
    public List<Order> findBusOrders(String plateNumber) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findByBus", Order.class);
        tq.setParameter("plateNumber", plateNumber);
        try {
            return tq.getResultList();
        } catch (NoResultException e) {
            throw BusException.createExceptionNoBusFound(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Client"})
    public List<Order> findMyOrders(Client orderCreator) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findMyOrders", Order.class);
        tq.setParameter("orderCreator", orderCreator);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @RolesAllowed({"Planist"})
    public List<Order> findClientOrders(String login) throws AppBaseException {
        TypedQuery<Order> tq = em.createNamedQuery("Order.findClientOrders", Order.class);
        tq.setParameter("login", login);
        try {
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

}
