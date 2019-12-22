/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.PLANIST_KEY)
@NamedQueries({
    @NamedQuery(name = "Planist.findAll", query = "SELECT a FROM Planist a")
    , @NamedQuery(name = "Planist.findByLogin", query = "SELECT a FROM Planist a WHERE a.login = :login")
})
public class Planist extends Account implements Serializable {

    public Planist() {
    }

    public Planist(Account account) {
        super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhoneNumber(), account.isAuthorized(), account.isActive(), account.getAccountCreator(), account.getModifiedBy());
    }

}
