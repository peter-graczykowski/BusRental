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
@DiscriminatorValue(AccessLevel.AccessLevelKeys.CLIENT_KEY)
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT a FROM Client a")
    , @NamedQuery(name = "Client.findByLogin", query = "SELECT a FROM Client a WHERE a.login = :login") 
})
public class Client extends Account implements Serializable {

    public Client() {
    }

    public Client(Account account) {
        super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhoneNumber(), account.isAuthorized(), account.isActive(), account.getAccountCreator(), account.getModifiedBy());
    }

}
