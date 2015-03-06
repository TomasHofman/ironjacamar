package org.jboss.jca.adapters.jdbc.unit.support.transactionisolation;

import org.jboss.jca.adapters.jdbc.unit.support.TestConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Test JDBC connection that doesn't allow to change transaction isolation
 * if transaction was already started (simulating PostgreSQL behaviour).
 *
 * @author Tomas Hofman (thofman@redhat.com)
 */
public class TransactionTestConnection extends TestConnection {

   private boolean activeTransaction = false;
   private boolean autocommit = true;
   private int isolationLevel = TRANSACTION_READ_COMMITTED;

   /**
    * Constructor
    *
    * @param info The properties
    */
   public TransactionTestConnection(Properties info) {
      super(info);
   }

   @Override
   public Statement createStatement() throws SQLException {
      return new TransactionTestStatement(this);
   }

   @Override
   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      return new TransactionTestStatement(this);
   }

   public void setActiveTransaction(boolean activeTransaction) {
      this.activeTransaction = activeTransaction;
   }

   @Override
   public void setAutoCommit(boolean autocommit) throws SQLException {
      if (this.autocommit != autocommit) {
         if (!this.autocommit) { // see org.postgresql.jdbc2.AbstractJdbc2Connection#setAutoCommit(boolean)
            commit();
         }
         this.autocommit = autocommit;
      }
   }

   @Override
   public boolean getAutoCommit() throws SQLException {
      return autocommit;
   }

   @Override
   public void setTransactionIsolation(int isolationLevel) throws SQLException {
      if (activeTransaction) {
         throw new SQLException("Cannot set isolation level in the middle of a transaction.");
      }
      this.isolationLevel = isolationLevel;
   }

   @Override
   public int getTransactionIsolation() throws SQLException {
      return isolationLevel;
   }

   @Override
   public void commit() throws SQLException {
      activeTransaction = false;
   }

   @Override
   public void rollback() throws SQLException {
      activeTransaction = false;
   }
}
