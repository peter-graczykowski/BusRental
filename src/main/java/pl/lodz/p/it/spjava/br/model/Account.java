/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "LEVEL_OF_ACCESS", discriminatorType = DiscriminatorType.STRING)
@Table(name = "ACCOUNT", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQUE_LOGIN", columnNames = "LOGIN")
})
@TableGenerator(name = "AccountGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "AccountGen")
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login")
    , @NamedQuery(name = "Account.findNewAccount", query = "SELECT a FROM NewAccount a")
    , @NamedQuery(name = "Account.findClientAccounts", query = "SELECT a FROM Client a")
    , @NamedQuery(name = "Account.findAuthorizedAccount", query = "SELECT a FROM Account a WHERE a.authorized = true")
})
public class Account extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AccountGenerator")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", updatable = false)
    protected Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 6, max = 20)
    @Column(name = "LOGIN", nullable = false, updatable = false)
    private String login;

    @Basic(optional = false)
    @NotNull
    @Size(min = 64, max = 64)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Basic(optional = false)
    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "QUESTION", nullable = false)
    private String question;

    @Basic(optional = false)
    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "ANSWER", nullable = false)
    private String answer;

    @Basic(optional = false)
    @NotNull
    @Size(min = 2, max = 60)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "SURNAME", nullable = false)
    private String surname;

    @Basic(optional = false)
    @NotNull
    @Size(min = 9, max = 13)
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Basic(optional = false)
    @NotNull
    @Column(name = "AUTHORIZED", nullable = false)
    private boolean authorized;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @JoinColumn(name = "ACCOUNT_CREATOR", referencedColumnName = "ID", nullable = true, updatable = false)
    @ManyToOne(optional = false)
    private Admin accountCreator;

    @JoinColumn(name = "MODIFIED_BY", referencedColumnName = "ID", nullable = true)
    @ManyToOne(optional = false)
    private Admin modifiedBy;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, String login, String password, String question, String answer, String name, String surname, String phoneNumber, boolean authorized, boolean active, Admin accountCreator, Admin modifiedBy) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.question = question;
        this.answer = answer;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.authorized = authorized;
        this.active = active;
        this.accountCreator = accountCreator;
        this.modifiedBy = modifiedBy;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < encodedhash.length; i++) {
                stringBuffer.append(Integer.toString((encodedhash[i] & 0xff) + 0x100, 16).substring(1));
            }
            this.password = stringBuffer.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Admin getAccountCreator() {
        return accountCreator;
    }

    public void setAccountCreator(Admin accountCreator) {
        this.accountCreator = accountCreator;
    }

    public Admin getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Admin modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
