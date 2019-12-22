/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.br.exception.AppBaseException;
import pl.lodz.p.it.spjava.br.model.Planist;

@Stateless
public class PlanistFacade extends AbstractFacade<Planist> {

    @PersistenceContext(unitName = "BusRentalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanistFacade() {
        super(Planist.class);
    }

    @RolesAllowed({"Admin", "Planist"})
    public Planist findByLogin(String login) throws AppBaseException {
        TypedQuery<Planist> tq = em.createNamedQuery("Planist.findByLogin", Planist.class);
        tq.setParameter("login", login);
        try {
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
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
