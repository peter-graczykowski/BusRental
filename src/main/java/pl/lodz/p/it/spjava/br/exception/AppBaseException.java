/*
 * Final project of postgraduate studies:
 * "Nowoczesne aplikacje biznesowe Java EE" edition 8
 */
package pl.lodz.p.it.spjava.br.exception;

import javax.ejb.ApplicationException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;

@ApplicationException(rollback = true)
public class AppBaseException extends Exception {

    static final public String KEY_OPTIMISTIC_LOCK = "error.optimistic.lock.problem";
    static final public String KEY_REPEATED_TRANSACTION_ROLLBACK = "error.repeated.transaction.rollback.problem";
    static final public String KEY_DATABASE_QUERY_PROBLEM = "error.database.query.problem";
    static final public String KEY_DATABASE_CONNECTION_PROBLEM = "error.database.connection.problem";
    static final public String KEY_NOT_AUTHORIZED_ACTION = "error.not.authorized.account.problem";
    static final public String KEY_NO_RESULT = "error.no.result.problem";

    public AppBaseException() {
    }

    protected AppBaseException(String message) {
        super(message);
    }

    protected AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    static public AppBaseException creatExceptionForRepeatedTransactionRollback() {
        return new AppBaseException(KEY_REPEATED_TRANSACTION_ROLLBACK);
    }

    public static AppBaseException createExceptionDatabaseQueryProblem(Throwable e) {
        return new AppBaseException(KEY_DATABASE_QUERY_PROBLEM);
    }

    public static AppBaseException createExceptionDatabaseConnectionProblem(Throwable e) {
        return new AppBaseException(KEY_DATABASE_CONNECTION_PROBLEM);
    }

    public static AppBaseException createExceptionOptimisticLock(OptimisticLockException e) {
        return new AppBaseException(KEY_OPTIMISTIC_LOCK);
    }

    public static AppBaseException createExceptionNotAuthorizedAction() {
        return new AppBaseException(KEY_NOT_AUTHORIZED_ACTION);
    }

    public static AppBaseException createExceptionNoResult(NoResultException e) {
        return new AppBaseException(KEY_NO_RESULT, e);
    }

}
