package org.jboss.jca.adapters.jdbc.unit.support.transactionisolation;

import org.jboss.jca.adapters.jdbc.unit.support.TestDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Test JDBC driver
 *
 * @author Tomas Hofman (thofman@redhat.com)
 */
public class TransactionTestDriver extends TestDriver {
   /**
    * {@inheritDoc}
    */
   public Connection connect(String url, Properties info) throws SQLException
   {
      return new TransactionTestConnection(info);
   }
}
