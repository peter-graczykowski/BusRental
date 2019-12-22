/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.NEWACCOUNT_KEY)
public class NewAccount extends Account implements Serializable {

    public NewAccount() {
    }

}
