/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.ejb.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.spjava.br.model.NewAccount;

@Stateless
public class NewAccountFacade extends AbstractFacade<NewAccount> {

    @PersistenceContext(unitName = "BusRentalPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NewAccountFacade() {
        super(NewAccount.class);
    }

}
