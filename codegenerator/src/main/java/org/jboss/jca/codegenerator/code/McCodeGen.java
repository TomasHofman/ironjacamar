/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jca.codegenerator.code;

import org.jboss.jca.codegenerator.Definition;

import java.io.IOException;
import java.io.Writer;

/**
 * A managed connection CodeGen.
 * 
 * @author Jeff Zhang
 * @version $Revision: $
 */
public class McCodeGen extends AbstractCodeGen
{

   /**
    * Output class
    * @param def definition
    * @param out Writer
    * @throws IOException ioException
    */
   @Override
   public void writeClassBody(Definition def, Writer out) throws IOException
   {
      int indent = 1;
      out.write("public class " + getClassName(def) + " implements ManagedConnection");
      writeLeftCurlyBracket(out, 0);
      writeEol(out);

      writeIndent(out, indent);
      out.write("/** The logger */");
      writeEol(out);
      writeIndent(out, indent);
      out.write("private static Logger log = Logger.getLogger(\"" + getClassName(def) + "\");");
      writeEol(out);
      writeEol(out);

      writeIndent(out, indent);
      out.write("/** The logwriter */");
      writeEol(out);
      writeIndent(out, indent);
      out.write("private PrintWriter logwriter;");
      writeEol(out);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/** ManagedConnectionFactory */");
      writeEol(out);
      writeIndent(out, indent);
      out.write("private " + def.getMcfClass() + " mcf;");
      writeEol(out);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/** Listeners */");
      writeEol(out);
      writeIndent(out, indent);
      out.write("private List<ConnectionEventListener> listeners;");
      writeEol(out);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/** Connection */");
      writeEol(out);
      writeIndent(out, indent);
      out.write("private Object connection;");
      writeEol(out);
      writeEol(out);
      
      //constructor
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Default constructor");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param mcf mcf");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public " + getClassName(def) + "(" + def.getMcfClass() + " mcf)");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("this.mcf = mcf;");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("this.logwriter = null;");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("this.listeners = new ArrayList<ConnectionEventListener>(1);");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("this.connection = null;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
      
      writeConnection(def, out, indent);
      writeLifecycle(def, out, indent);
      writeConnectionEventListener(def, out, indent);
      writeLogWriter(def, out, indent);
      writeTransaction(def, out, indent);
      writeMetaData(def, out, indent);
      
      writeRightCurlyBracket(out, 0);
   }
   
   /**
    * Output class import
    * @param def definition
    * @param out Writer
    * @throws IOException ioException
    */
   @Override
   public void writeImport(Definition def, Writer out) throws IOException
   {
      out.write("package " + def.getRaPackage() + ";");
      writeEol(out);
      writeEol(out);
      out.write("import java.io.PrintWriter;");
      writeEol(out);
      writeEol(out);
      out.write("import java.util.ArrayList;");
      writeEol(out);
      out.write("import java.util.List;");
      writeEol(out);
      out.write("import java.util.logging.Logger;");
      writeEol(out);
      writeEol(out);
      out.write("import javax.resource.ResourceException;");
      writeEol(out);
      out.write("import javax.resource.spi.ConnectionEvent;");
      writeEol(out);
      out.write("import javax.resource.spi.ConnectionEventListener;");
      writeEol(out);
      out.write("import javax.resource.spi.ConnectionRequestInfo;");
      writeEol(out);
      out.write("import javax.resource.spi.LocalTransaction;");
      writeEol(out);
      out.write("import javax.resource.spi.ManagedConnection;");
      writeEol(out);
      out.write("import javax.resource.spi.ManagedConnectionMetaData;");
      writeEol(out);
      writeEol(out);
      out.write("import javax.security.auth.Subject;");
      writeEol(out);
      out.write("import javax.transaction.xa.XAResource;");
      writeEol(out);
      writeEol(out);
   }
   
   /**
    * get this class name
    * @param def definition
    * @return String class name
    */
   @Override
   public String getClassName(Definition def)
   {
      return def.getMcClass();
   }
   
   /**
    * Output Connection method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeConnection(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Creates a new connection handle for the underlying physical connection ");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * represented by the ManagedConnection instance. ");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param subject Security context as JAAS subject");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param cxRequestInfo ConnectionRequestInfo instance");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @return generic Object instance representing the connection handle. ");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public Object getConnection(Subject subject,");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("ConnectionRequestInfo cxRequestInfo) throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"getConnection()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("connection = new " + def.getConnImplClass() + "(this, mcf);");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("return connection;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);

      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Used by the container to change the association of an ");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * application-level connection handle with a ManagedConneciton instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param connection Application-level connection handle");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public void associateConnection(Object connection) throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"associateConnection()\");");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   
   /**
    * Output Lifecycle method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeLifecycle(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Application server calls this method to force any cleanup on the ManagedConnection instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public void cleanup() throws ResourceException");
      writeEol(out);
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"cleanup()\");");
      writeRightCurlyBracket(out, indent);
      writeEol(out);

      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Destroys the physical connection to the underlying resource manager.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public void destroy() throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"destroy()\");");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   
   /**
    * Output ConnectionEventListener method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeConnectionEventListener(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Adds a connection event listener to the ManagedConnection instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param listener A new ConnectionEventListener to be registered");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public void addConnectionEventListener(ConnectionEventListener listener)");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"addConnectionEventListener()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("if (listener == null)");
      writeEol(out);
      writeIndent(out, indent + 2);
      out.write("throw new IllegalArgumentException(\"Listener is null\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("listeners.add(listener);");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Removes an already registered connection event listener from the ManagedConnection instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param listener already registered connection event listener to be removed");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public void removeConnectionEventListener(ConnectionEventListener listener)");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"removeConnectionEventListener()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("if (listener == null)");
      writeEol(out);
      writeIndent(out, indent + 2);
      out.write("throw new IllegalArgumentException(\"Listener is null\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("listeners.remove(listener);");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Close handle");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param handle The handle");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public void closeHandle(" + def.getConnInterfaceClass() + " handle)");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("event.setConnectionHandle(handle);");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("for (ConnectionEventListener cel : listeners)");
      writeLeftCurlyBracket(out, indent + 1);
      writeIndent(out, indent + 2);
      out.write("cel.connectionClosed(event);");
      writeRightCurlyBracket(out, indent + 1);

      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   /**
    * Output LogWriter method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeLogWriter(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Gets the log writer for this ManagedConnection instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @return Character ourput stream associated with this Managed-Connection instance");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public PrintWriter getLogWriter() throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"getLogWriter()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("return logwriter;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Sets the log writer for this ManagedConnection instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @param out Character Output stream to be associated");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException  generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public void setLogWriter(PrintWriter out) throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"setLogWriter()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("logwriter = out;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   
   /**
    * Output Transaction method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeTransaction(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Returns an <code>javax.resource.spi.LocalTransaction</code> instance.");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @return LocalTransaction instance");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
            
      writeIndent(out, indent);
      out.write("public LocalTransaction getLocalTransaction() throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"getLocalTransaction()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("return null;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Returns an <code>javax.transaction.xa.XAresource</code> instance. ");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @return XAResource instance");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
            
      writeIndent(out, indent);
      out.write("public XAResource getXAResource() throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"getXAResource()\");");
      writeEol(out);
      writeIndent(out, indent + 1);
      out.write("return null;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   
   /**
    * Output MetaData method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeMetaData(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("/**");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * Gets the metadata information for this connection's underlying EIS resource manager instance. ");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" *");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @return ManagedConnectionMetaData instance");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" * @throws ResourceException generic exception if operation fails");
      writeEol(out);
      writeIndent(out, indent);
      out.write(" */");
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("public ManagedConnectionMetaData getMetaData() throws ResourceException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("log.finest(\"getMetaData()\");");
      writeEol(out);
      
      writeIndent(out, indent + 1);
      out.write("return new " + def.getMcMetaClass() + "();");

      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
}
