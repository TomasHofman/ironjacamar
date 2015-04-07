/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2014, Red Hat Inc, and individual contributors
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

package org.jboss.jca.as.tracer;

import org.jboss.jca.Version;
import org.jboss.jca.core.tracer.TraceEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * HTML report generator for a tracer log
 */
public class HTMLReport
{
   /**
    * Write string
    * @param fw The file writer
    * @param s The string
    * @exception Exception If an error occurs
    */
   static void writeString(FileWriter fw, String s) throws Exception
   {
      for (int i = 0; i < s.length(); i++)
      {
         fw.write((int)s.charAt(i));
      }
   }

   /**
    * Write EOL
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   static void writeEOL(FileWriter fw) throws Exception
   {
      fw.write((int)'\n');
   }

   /**
    * Write top-level index.html
    * @param poolNames The pool names
    * @param statuses The overall status of each pool
    * @param ccmStatus The status of the CCM
    * @param ccmPoolStatuses The CCM status of the pools
    * @param version The version information
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateTopLevelIndexHTML(Set<String> poolNames,
                                                 Map<String, TraceEventStatus> statuses,
                                                 TraceEventStatus ccmStatus,
                                                 Map<String, TraceEventStatus> ccmPoolStatuses,
                                                 TraceEvent version,
                                                 FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>IronJacamar tracer report</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>IronJacamar tracer report</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Generated:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + new Date() + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Data:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + Version.PROJECT + " " + (version != null ? version.getPool() : "Unknown") + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>By:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + Version.FULL_VERSION + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<h2>Pool</h2>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      for (String name : poolNames)
      {
         TraceEventStatus status = statuses.get(name);

         writeString(fw, "<li>");

         writeString(fw, "<a href=\"" + name + "/index.html\"><div style=\"color: ");
         if (status != null)
         {
            writeString(fw, status.getColor());
         }
         else
         {
            writeString(fw, TraceEventStatus.GREEN.getColor());
         }
         writeString(fw, ";\">");

         writeString(fw, name);

         writeString(fw, "</div></a>");
         writeEOL(fw);

         writeString(fw, "</li>");
         writeEOL(fw);
      }

      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "<h2>Lifecycle</h2>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      for (String name : poolNames)
      {
         writeString(fw, "<li>");

         writeString(fw, "<a href=\"" + name + "/lifecycle.html\">");

         writeString(fw, name);

         writeString(fw, "</a>");
         writeEOL(fw);

         writeString(fw, "</li>");
         writeEOL(fw);
      }

      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "<h2>Cached connection manager</h2>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      writeString(fw, "<li><a href=\"CachedConnectionManager/ccm.html\"><div style=\"color: ");
      writeString(fw, ccmStatus.getColor());
      writeString(fw, ";\">Main report</div></a></li>");

      writeEOL(fw);

      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      for (String name : poolNames)
      {
         writeString(fw, "<li>");

         writeString(fw, "<a href=\"" + name + "/ccm.html\"><div style=\"color: ");
         TraceEventStatus ps = ccmPoolStatuses.get(name);
         if (ps != null)
         {
            writeString(fw, ps.getColor());
         }
         else
         {
            writeString(fw, TraceEventStatus.GREEN.getColor());
         }
         writeString(fw, ";\">");

         writeString(fw, name);

         writeString(fw, "</div></a>");
         writeEOL(fw);

         writeString(fw, "</li>");
         writeEOL(fw);
      }

      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "<h2>Reference</h2>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      writeString(fw, "<li><a href=\"toc-c.html\">Connection</div></a></li>");
      writeEOL(fw);
      
      writeString(fw, "<li><a href=\"toc-cl.html\">ConnectionListener</div></a></li>");
      writeEOL(fw);
      
      writeString(fw, "<li><a href=\"toc-mcp.html\">Managed Connection Pool</div></a></li>");
      writeEOL(fw);
      
      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write pool index.html
    * @param poolName The name of the pool
    * @param overallStatus The overall status of the pool
    * @param mcps The managed connection pools
    * @param statuses The overall status of each connection listener
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generatePoolIndexHTML(String poolName, TraceEventStatus overallStatus, Set<String> mcps,
                                             Map<String, TraceEventStatus> statuses, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>Pool: " + poolName + "</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>Pool: " + poolName + "</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Status:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><div style=\"color: " + overallStatus.getColor() + ";\">");
      writeString(fw, overallStatus.getDescription());
      writeString(fw, "</div></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>");
      Iterator<String> mcpIt = mcps.iterator();
      while (mcpIt.hasNext())
      {
         String mcp = mcpIt.next();
         writeString(fw, mcp);
         if (mcpIt.hasNext())
            writeString(fw, "<br/>");
      }
      writeString(fw, "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);
      
      writeString(fw, "<h2>ConnectionListeners</h2>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      Iterator<Map.Entry<String, TraceEventStatus>> it = statuses.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry<String, TraceEventStatus> entry = it.next();

         String directory = entry.getKey();

         writeString(fw, "<li>");

         writeString(fw, "<a href=\"" + directory + "/index.html\"><div style=\"color: ");
         writeString(fw, entry.getValue().getColor());
         writeString(fw, ";\">");

         writeString(fw, directory);

         writeString(fw, "</div></a>");
         writeEOL(fw);

         writeString(fw, "</li>");
         writeEOL(fw);
      }

      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<h2>Lifecycle</h2>");
      writeEOL(fw);

      writeString(fw, "<a href=\"lifecycle.html\">Report</a>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<h2>Cached connection manager</h2>");
      writeEOL(fw);

      writeString(fw, "<a href=\"ccm.html\">Report</a>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"../index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write ConnectionListener index.html
    * @param identifier The identifier
    * @param data The data
    * @param ignoreDelist Should DELIST be ignored
    * @param ignoreTracking Should TRACKING be ignored
    * @param root The root directory
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateConnectionListenerIndexHTML(String identifier, List<TraceEvent> data,
                                                           boolean ignoreDelist, boolean ignoreTracking,
                                                           String root, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>ConnectionListener: " + identifier + "</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>ConnectionListener: " + identifier + "</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Pool:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + data.get(0).getPool() + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + data.get(0).getManagedConnectionPool() + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Status:</b></td>");
      writeEOL(fw);

      TraceEventStatus status = TraceEventHelper.getStatus(data, ignoreDelist, ignoreTracking);
      writeString(fw, "<td><div style=\"color: " + status.getColor() + ";\">");
      writeString(fw, status.getDescription());
      writeString(fw, "</div></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<h2>Reports</h2>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      Map<String, List<TraceEvent>> m = TraceEventHelper.split(data);

      Iterator<Map.Entry<String, List<TraceEvent>>> it = m.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry<String, List<TraceEvent>> entry = it.next();

         writeString(fw, "<li>");

         writeString(fw, "<a href=\"" + entry.getKey() + "/index.html\"><div style=\"color: ");

         status = TraceEventHelper.getStatus(entry.getValue(), ignoreDelist, ignoreTracking);
         writeString(fw, status.getColor());

         writeString(fw, ";\">");

         writeString(fw, entry.getKey());

         writeString(fw, "</div></a>");
         writeEOL(fw);

         writeString(fw, "</li>");
         writeEOL(fw);

         FileWriter cl = null;
         try
         {
            File f = new File(root + "/" + entry.getKey());
            f.mkdirs();

            cl = new FileWriter(f.getAbsolutePath() + "/" + "index.html");
            generateConnectionListenerReportHTML(f.getCanonicalPath(), identifier, entry.getValue(),
                                                 ignoreDelist, ignoreTracking, cl);
         }
         finally
         {
            if (cl != null)
            {
               try
               {
                  cl.flush();
                  cl.close();
               }
               catch (Exception e)
               {
                  // Ignore
               }
            }
         }

         if (status == TraceEventStatus.GREEN)
         {
            FileWriter sdedit = null;
            try
            {
               File f = new File(root + "/" + entry.getKey());
               f.mkdirs();

               sdedit = new FileWriter(f.getAbsolutePath() + "/" + identifier + ".sdx");

               SDeditGenerator.generateSDedit(entry.getValue(), sdedit);
            }
            finally
            {
               if (sdedit != null)
               {
                  try
                  {
                     sdedit.flush();
                     sdedit.close();
                  }
                  catch (Exception e)
                  {
                     // Ignore
                  }
               }
            }
         }
      }

      writeString(fw, "</ul>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"../index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write ConnectionListener report
    * @param root The root directory
    * @param identifier The identifier
    * @param data The data
    * @param ignoreDelist Should DELIST be ignored
    * @param ignoreTracking Should TRACKING be ignored
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateConnectionListenerReportHTML(String root, String identifier, List<TraceEvent> data,
                                                            boolean ignoreDelist, boolean ignoreTracking,
                                                            FileWriter fw)
      throws Exception
   {
      long startTime = data.get(0).getTimestamp();
      long endTime = data.get(data.size() - 1).getTimestamp();

      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>ConnectionListener: " + identifier + " (" + startTime + "-" + endTime + ")</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>ConnectionListener: " + identifier + "</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Pool:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + data.get(0).getPool() + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + data.get(0).getManagedConnectionPool() + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>From:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + startTime + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>To:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + endTime + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Thread:</b></td>");
      writeEOL(fw);

      writeString(fw, "<td>" + data.get(0).getThreadId() + "</td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Status:</b></td>");
      writeEOL(fw);

      TraceEventStatus status = TraceEventHelper.getStatus(data, ignoreDelist, ignoreTracking);
      writeString(fw, "<td><div style=\"color: " + status.getColor() + ";\">");
      writeString(fw, status.getDescription());
      writeString(fw, "</div></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<h2>Sequence diagram</h2>");
      writeEOL(fw);

      if (status == TraceEventStatus.GREEN)
      {
         writeString(fw, "<image src=\"");
         writeString(fw, identifier);
         writeString(fw, ".png\" alt=\"SDedit image\"/>");
         writeEOL(fw);

         writeString(fw, "<p/>");
         writeEOL(fw);

         writeString(fw, "Generate the image by: <i>sdedit -t png -o ");
         writeString(fw, root);
         writeString(fw, "/");
         writeString(fw, identifier);
         writeString(fw, ".png ");
         writeString(fw, root);
         writeString(fw, "/");
         writeString(fw, identifier);
         writeString(fw, ".sdx ");
         writeString(fw, "</i>");
         writeEOL(fw);
      }
      else
      {
         writeString(fw, "See Description or Data for recorded data");
         writeEOL(fw);
      }

      writeString(fw, "<h2>Description</h2>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<thead align=\"left\">");
      writeEOL(fw);

      writeString(fw, "<th>Timestamp</th>");
      writeEOL(fw);

      writeString(fw, "<th>Description</th>");
      writeEOL(fw);

      writeString(fw, "</thead>");
      writeEOL(fw);

      writeString(fw, "<tbody>");
      writeEOL(fw);

      for (TraceEvent te : data)
      {
         writeString(fw, "<tr>");
         writeEOL(fw);

         // Timestamp
         writeString(fw, "<td>");

         if (TraceEventHelper.isRed(te))
         {
            writeString(fw, "<div style=\"color: red;\">");
         }
         else if (TraceEventHelper.isYellow(te))
         {
            writeString(fw, "<div style=\"color: yellow;\">");
         }

         writeString(fw, Long.toString(te.getTimestamp()));

         if (TraceEventHelper.isRed(te) || TraceEventHelper.isYellow(te))
            writeString(fw, "</div>");

         writeString(fw, "</td>");
         writeEOL(fw);

         // Text
         writeString(fw, "<td>");

         if (TraceEventHelper.isRed(te))
         {
            writeString(fw, "<div style=\"color: red;\">");
         }
         else if (TraceEventHelper.isYellow(te))
         {
            writeString(fw, "<div style=\"color: yellow;\">");
         }

         writeString(fw, TraceEvent.asText(te));

         if (TraceEventHelper.isRed(te) || TraceEventHelper.isYellow(te))
            writeString(fw, "</div>");

         writeString(fw, "</td>");
         writeEOL(fw);

         writeString(fw, "</tr>");
         writeEOL(fw);
      }

      writeString(fw, "</tbody>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);

      if (TraceEventHelper.hasException(data))
      {
         writeString(fw, "<h2>Exception</h2>");
         writeEOL(fw);

         for (TraceEvent te : data)
         {
            if (te.getType() == TraceEvent.EXCEPTION)
            {
               writeString(fw, "<pre>");
               writeEOL(fw);

               writeString(fw, TraceEventHelper.exceptionDescription(te.getPayload1()));
               writeEOL(fw);

               writeString(fw, "</pre>");
               writeEOL(fw);

               writeString(fw, "<p/>");
               writeEOL(fw);
            }
         }
      }

      writeString(fw, "<h2>Data</h2>");
      writeEOL(fw);

      writeString(fw, "<pre>");
      writeEOL(fw);

      for (TraceEvent te : data)
      {
         writeString(fw, TraceEventHelper.prettyPrint(te));
         writeEOL(fw);
      }

      writeString(fw, "</pre>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"../index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write lifecycle.html
    * @param poolName The name of the pool
    * @param events The events
    * @param activeCLs The active connection listeners
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateLifecycleHTML(String poolName, List<TraceEvent> events,
                                             Set<String> activeCLs, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>Lifecycle: " + poolName + "</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>Lifecycle: " + poolName + "</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Timestamp</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Event</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>ConnectionListener</b></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      for (TraceEvent te : events)
      {
         writeString(fw, "<tr>");
         writeEOL(fw);

         writeString(fw, "<td>" + te.getTimestamp() + "</td>");
         writeEOL(fw);
      
         writeString(fw, "<td>" + te.getManagedConnectionPool() + "</td>");
         writeEOL(fw);
      
         writeString(fw, "<td>" + TraceEvent.asText(te) + "</td>");
         writeEOL(fw);
      
         if (!"NONE".equals(te.getConnectionListener()))
         {
            if (activeCLs.contains(te.getConnectionListener()))
            {
               writeString(fw, "<td><a href=\"" + te.getConnectionListener() + "/index.html\">" +
                           te.getConnectionListener() + "</a></td>");
            }
            else
            {
               writeString(fw, "<td>" + te.getConnectionListener() + "</td>");
            }
         }
         else
         {
            writeString(fw, "<td></td>");
         }
         writeEOL(fw);
      
         writeString(fw, "</tr>");
         writeEOL(fw);
      }

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<h2>Pool</h2>");
      writeEOL(fw);

      writeString(fw, "<a href=\"index.html\">Report</a>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<h2>Cached connection manager</h2>");
      writeEOL(fw);

      writeString(fw, "<a href=\"ccm.html\">Report</a>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<h2>Data</h2>");
      writeEOL(fw);

      writeString(fw, "<pre>");
      writeEOL(fw);

      for (TraceEvent te : events)
      {
         writeString(fw, TraceEventHelper.prettyPrint(te));
         writeEOL(fw);
      }

      writeString(fw, "</pre>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"../index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write ccm.html for the CCM
    * @param events The events
    * @param status The status
    * @param path The root path
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateCCMHTML(List<TraceEvent> events, TraceEventStatus status, String path, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>CachedConnectionManager</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>CachedConnectionManager</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Status:</b></td><td><div style=\"color: ");
      writeString(fw, status.getColor());
      writeString(fw, ";\">");

      writeString(fw, status.getDescription());

      writeString(fw, "</div></td>");
      writeEOL(fw);
      
      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Timestamp</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Event</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Key</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Call stack</b></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      for (TraceEvent te : events)
      {
         writeString(fw, "<tr>");
         writeEOL(fw);

         writeString(fw, "<td>" + te.getTimestamp() + "</td>");
         writeEOL(fw);
      
         writeString(fw, "<td>" + TraceEvent.asText(te) + "</td>");
         writeEOL(fw);
      
         writeString(fw, "<td>" + te.getPayload1() + "</td>");
         writeEOL(fw);

         String callstack = te.getPayload1();
         if (te.getType() == TraceEvent.PUSH_CCM_CONTEXT)
         {
            callstack += "-push";
         }
         else
         {
            callstack += "-pop";
         }
         callstack += ".txt";
         
         writeString(fw, "<td><a href=\"" + callstack + "\">Link</a></td>");
         writeEOL(fw);

         FileWriter report = null;
         try
         {
            report = new FileWriter(path + "/" + callstack);
            writeString(report, TraceEventHelper.exceptionDescription(te.getPayload2()));
            writeEOL(report);
         }
         finally
         {
            if (report != null)
            {
               try
               {
                  report.flush();
                  report.close();
               }
               catch (Exception e)
               {
                  // Ignore
               }
            }
         }
         
         writeString(fw, "</tr>");
         writeEOL(fw);
      }

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"../index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write ccm.html for pools
    * @param poolName The name of the pool
    * @param events The events
    * @param status The status
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateCCMPoolHTML(String poolName, List<TraceEvent> events,
                                           TraceEventStatus status, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>CCM: " + poolName + "</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>CCM: " + poolName + "</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Status:</b></td><td><div style=\"color: ");

      writeString(fw, status.getColor());
      writeString(fw, ";\">");

      writeString(fw, status.getDescription());
      
      writeString(fw, "</div></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);

      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>Timestamp</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Event</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>ConnectionListener</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Connection</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Key</b></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      for (TraceEvent te : events)
      {
         writeString(fw, "<tr>");
         writeEOL(fw);

         writeString(fw, "<td>" + te.getTimestamp() + "</td>");
         writeEOL(fw);
      
         if (!"NONE".equals(te.getManagedConnectionPool()))
         {
            writeString(fw, "<td>" + te.getManagedConnectionPool() + "</td>");
         }
         else
         {
            writeString(fw, "<td></td>");
         }
         writeEOL(fw);
      
         writeString(fw, "<td>" + TraceEvent.asText(te) + "</td>");
         writeEOL(fw);
      
         if (!"NONE".equals(te.getConnectionListener()))
         {
            writeString(fw, "<td><a href=\"" + te.getConnectionListener() + "/index.html\">" +
                        te.getConnectionListener() + "</a></td>");
         }
         else
         {
            writeString(fw, "<td></td>");
         }
         writeEOL(fw);
      
         if (!"NONE".equals(te.getPayload1()))
         {
            writeString(fw, "<td>" + te.getPayload1() + "</td>");
         }
         else
         {
            writeString(fw, "<td></td>");
         }
         writeEOL(fw);
      
         writeString(fw, "<td>" + te.getPayload2() + "</td>");
         writeEOL(fw);
      
         writeString(fw, "</tr>");
         writeEOL(fw);
      }

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<h2>Pool</h2>");
      writeEOL(fw);

      writeString(fw, "<a href=\"index.html\">Report</a>");
      writeEOL(fw);

      writeString(fw, "<h2>Lifecycle</h2>");
      writeEOL(fw);

      writeString(fw, "<a href=\"lifecycle.html\">Report</a>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<h2>Data</h2>");
      writeEOL(fw);

      writeString(fw, "<pre>");
      writeEOL(fw);

      for (TraceEvent te : events)
      {
         writeString(fw, TraceEventHelper.prettyPrint(te));
         writeEOL(fw);
      }

      writeString(fw, "</pre>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"../index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write toc-c.html for connections
    * @param events The events
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateToCConnection(Map<String, List<TraceEvent>> events, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>Reference: Connection</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>Reference: Connection</h1>");
      writeEOL(fw);

      writeString(fw, "<ul>");
      writeEOL(fw);

      for (String id : events.keySet())
      {
         writeString(fw, "<li><a href=\"#" + id + "\">" + id + "</a></li>");
         writeEOL(fw);
      }

      writeString(fw, "</ul>");
      writeEOL(fw);

      for (Map.Entry<String, List<TraceEvent>> entry : events.entrySet())
      {
         writeString(fw, "<a name=\"" + entry.getKey() + "\"><h2>" + entry.getKey() + "</h2></a>");
         writeEOL(fw);

         writeString(fw, "<table>");
         writeEOL(fw);
      
         writeString(fw, "<tr>");
         writeEOL(fw);

         writeString(fw, "<td><b>Timestamp</b></td>");
         writeEOL(fw);

         writeString(fw, "<td><b>ConnectionListener</b></td>");
         writeEOL(fw);

         writeString(fw, "<td><b>ManagedConnectionPool</b></td>");
         writeEOL(fw);

         writeString(fw, "<td><b>Pool</b></td>");
         writeEOL(fw);

         writeString(fw, "</tr>");
         writeEOL(fw);

         for (TraceEvent te : entry.getValue())
         {
            writeString(fw, "<tr>");
            writeEOL(fw);

            writeString(fw, "<td>" + te.getTimestamp() + "</td>");
            writeEOL(fw);
      
            writeString(fw, "<td><a href=\"" + te.getPool() + "/" + te.getConnectionListener() + "/index.html\">" +
                        te.getConnectionListener() + "</a></td>");
            writeEOL(fw);
         
            writeString(fw, "<td>" + te.getManagedConnectionPool() + "</td>");
            writeEOL(fw);
      
            writeString(fw, "<td><a href=\"" + te.getPool() + "/index.html\">" + te.getPool() + "</a></td>");
            writeEOL(fw);
      
            writeString(fw, "</tr>");
            writeEOL(fw);
         }
         
         writeString(fw, "</table>");
         writeEOL(fw);

         writeString(fw, "<p/>");
         writeEOL(fw);
      }

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write toc-cl.html for connection listeners
    * @param events The events
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateToCConnectionListener(Map<String, List<TraceEvent>> events, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>Reference: ConnectionListener</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>Reference: ConnectionListener</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);
      
      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>ConnectionListener</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Pool</b></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      for (Map.Entry<String, List<TraceEvent>> entry : events.entrySet())
      {
         TraceEvent te = entry.getValue().get(0);

         writeString(fw, "<tr>");
         writeEOL(fw);

         writeString(fw, "<td><a href=\"" + te.getPool() + "/" + te.getConnectionListener() + "/index.html\">" +
                     te.getConnectionListener() + "</a></td>");
         writeEOL(fw);
         
         writeString(fw, "<td>" + te.getManagedConnectionPool() + "</td>");
         writeEOL(fw);
      
         writeString(fw, "<td><a href=\"" + te.getPool() + "/index.html\">" + te.getPool() + "</a></td>");
         writeEOL(fw);
      
         writeString(fw, "</tr>");
         writeEOL(fw);
      }

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }

   /**
    * Write toc-mcp.html for managed connection pool
    * @param events The events
    * @param fw The file writer
    * @exception Exception If an error occurs
    */
   private static void generateToCManagedConnectionPool(Map<String, List<TraceEvent>> events, FileWriter fw)
      throws Exception
   {
      writeString(fw, "<html>");
      writeEOL(fw);

      writeString(fw, "<head>");
      writeEOL(fw);

      writeString(fw, "<title>Reference: Managed Connection Pool</title>");
      writeEOL(fw);

      writeString(fw, "</head>");
      writeEOL(fw);

      writeString(fw, "<body style=\"background: #D7D7D7;\">");
      writeEOL(fw);

      writeString(fw, "<h1>Reference: Managed Connection Pool</h1>");
      writeEOL(fw);

      writeString(fw, "<table>");
      writeEOL(fw);
      
      writeString(fw, "<tr>");
      writeEOL(fw);

      writeString(fw, "<td><b>ManagedConnectionPool</b></td>");
      writeEOL(fw);

      writeString(fw, "<td><b>Pool</b></td>");
      writeEOL(fw);

      writeString(fw, "</tr>");
      writeEOL(fw);

      for (Map.Entry<String, List<TraceEvent>> entry : events.entrySet())
      {
         TraceEvent te = entry.getValue().get(0);

         writeString(fw, "<tr>");
         writeEOL(fw);

         writeString(fw, "<td>" + te.getManagedConnectionPool() + "</td>");
         writeEOL(fw);
      
         writeString(fw, "<td><a href=\"" + te.getPool() + "/index.html\">" + te.getPool() + "</a></td>");
         writeEOL(fw);
      
         writeString(fw, "</tr>");
         writeEOL(fw);
      }

      writeString(fw, "</table>");
      writeEOL(fw);

      writeString(fw, "<p/>");
      writeEOL(fw);

      writeString(fw, "<a href=\"index.html\">Back</a>");
      writeEOL(fw);

      writeString(fw, "</body>");
      writeEOL(fw);

      writeString(fw, "</html>");
      writeEOL(fw);
   }
   
   /**
    * Main
    * @param args The arguments
    */
   public static void main(String[] args)
   {
      if (args == null || args.length < 1)
      {
         System.out.println("Usage: HTMLReport [-ignore-delist] [-ignore-tracking] <file> [<output>]");
         return;
      }

      boolean ignoreDelist = false;
      boolean ignoreTracking = false;
      int argCount = 0;

      if ("-ignore-delist".equals(args[argCount]))
      {
         ignoreDelist = true;
         argCount++;
      }
      if ("-ignore-tracking".equals(args[argCount]))
      {
         ignoreTracking = true;
         argCount++;
      }

      File logFile = new File(args[argCount]);
      FileReader logReader = null;

      String rootDirectory = "report";
      if (args.length > argCount + 1)
         rootDirectory = args[argCount + 1];

      File root = new File(rootDirectory);

      try
      {
         logReader = new FileReader(logFile);
         root.mkdirs();

         List<TraceEvent> events = TraceEventHelper.getEvents(logReader, root);
         Map<String, Map<String, List<TraceEvent>>> filteredPool = TraceEventHelper.filterPoolEvents(events);
         Map<String, List<TraceEvent>> filteredLifecycle = TraceEventHelper.filterLifecycleEvents(events);
         List<TraceEvent> filteredCCM = TraceEventHelper.filterCCMEvents(events);
         Map<String, List<TraceEvent>> filteredCCMPool = TraceEventHelper.filterCCMPoolEvents(events);
         Map<String, Set<String>> poolMCPs = TraceEventHelper.poolManagedConnectionPools(events);
         Map<String, List<TraceEvent>> tocConnections = TraceEventHelper.tocConnections(events);
         Map<String, List<TraceEvent>> tocConnectionListeners = TraceEventHelper.tocConnectionListeners(events);
         Map<String, List<TraceEvent>> tocMCPs = TraceEventHelper.tocManagedConnectionPools(events);

         // Status calculation
         TraceEventStatus ccmStatus = TraceEventHelper.getCCMStatus(filteredCCM);
         Map<String, TraceEventStatus> ccmPoolStatus = new TreeMap<String, TraceEventStatus>();
         for (Map.Entry<String, List<TraceEvent>> entry : filteredCCMPool.entrySet())
         {
            ccmPoolStatus.put(entry.getKey(), TraceEventHelper.getCCMPoolStatus(entry.getValue()));
         }
         
         Map<String, TraceEventStatus> topLevelStatus = new TreeMap<String, TraceEventStatus>();

         Iterator<Map.Entry<String, Map<String, List<TraceEvent>>>> it = filteredPool.entrySet().iterator();
         while (it.hasNext())
         {
            Map.Entry<String, Map<String, List<TraceEvent>>> entry = it.next();
            Collection<List<TraceEvent>> values = entry.getValue().values();

            List<TraceEventStatus> status = new ArrayList<TraceEventStatus>();

            for (List<TraceEvent> l : values)
            {
               status.add(TraceEventHelper.getStatus(l, ignoreDelist, ignoreTracking));
            }

            topLevelStatus.put(entry.getKey(), TraceEventHelper.mergeStatus(status));
         }

         FileWriter topLevel = null;
         try
         {
            topLevel = new FileWriter(root.getAbsolutePath() + "/" + "index.html");
            generateTopLevelIndexHTML(filteredLifecycle.keySet(), topLevelStatus,
                                      ccmStatus, ccmPoolStatus,
                                      TraceEventHelper.getVersion(events), topLevel);
         }
         finally
         {
            if (topLevel != null)
            {
               try
               {
                  topLevel.flush();
                  topLevel.close();
               }
               catch (Exception e)
               {
                  // Ignore
               }
            }
         }


         for (String poolName : filteredLifecycle.keySet())
         {
            Map<String, List<TraceEvent>> data = filteredPool.get(poolName);

            FileWriter pool = null;
            try
            {
               String path = root.getAbsolutePath() + "/" + poolName;
               File f = new File(path);
               f.mkdirs();

               Map<String, TraceEventStatus> status = new TreeMap<String, TraceEventStatus>();
               if (data != null)
               {
                  Iterator<Map.Entry<String, List<TraceEvent>>> dataIt = data.entrySet().iterator();
                  while (dataIt.hasNext())
                  {
                     Map.Entry<String, List<TraceEvent>> dataEntry = dataIt.next();

                     status.put(dataEntry.getKey(), TraceEventHelper.getStatus(dataEntry.getValue(),
                                                                               ignoreDelist, ignoreTracking));

                     String identifier = dataEntry.getKey();
                     FileWriter cl = null;
                     try
                     {
                        String clPath = path + "/" + identifier;
                        File clF = new File(clPath);
                        clF.mkdirs();

                        cl = new FileWriter(clF.getAbsolutePath() + "/" + "index.html");
                        generateConnectionListenerIndexHTML(identifier, dataEntry.getValue(),
                                                            ignoreDelist, ignoreTracking, clPath, cl);
                     }
                     finally
                     {
                        if (cl != null)
                        {
                           try
                           {
                              cl.flush();
                              cl.close();
                           }
                           catch (Exception e)
                           {
                              // Ignore
                           }
                        }
                     }
                  }
               }

               pool = new FileWriter(f.getAbsolutePath() + "/" + "index.html");
               generatePoolIndexHTML(poolName, topLevelStatus.get(poolName), poolMCPs.get(poolName), status, pool);
            }
            finally
            {
               if (pool != null)
               {
                  try
                  {
                     pool.flush();
                     pool.close();
                  }
                  catch (Exception e)
                  {
                     // Ignore
                  }
               }
            }
         }

         Set<String> activeCLs = new HashSet<String>();
         for (Map<String, List<TraceEvent>> m : filteredPool.values())
         {
            activeCLs.addAll(m.keySet());
         }

         Iterator<Map.Entry<String, List<TraceEvent>>> lifeIt = filteredLifecycle.entrySet().iterator();
         while (lifeIt.hasNext())
         {
            Map.Entry<String, List<TraceEvent>> entry = lifeIt.next();

            FileWriter lifecycle = null;
            try
            {
               String path = root.getAbsolutePath() + "/" + entry.getKey();
               File f = new File(path);
               f.mkdirs();

               lifecycle = new FileWriter(path + "/" + "lifecycle.html");
               generateLifecycleHTML(entry.getKey(), entry.getValue(), activeCLs, lifecycle);
            }
            finally
            {
               if (lifecycle != null)
               {
                  try
                  {
                     lifecycle.flush();
                     lifecycle.close();
                  }
                  catch (Exception e)
                  {
                     // Ignore
                  }
               }
            }
         }

         if (filteredCCM.size() > 0)
         {
            FileWriter ccm = null;
            try
            {
               String path = root.getAbsolutePath() + "/" + filteredCCM.get(0).getPool();
               File f = new File(path);
               f.mkdirs();

               ccm = new FileWriter(path + "/" + "ccm.html");
               generateCCMHTML(filteredCCM, ccmStatus, path, ccm);
            }
            finally
            {
               if (ccm != null)
               {
                  try
                  {
                     ccm.flush();
                     ccm.close();
                  }
                  catch (Exception e)
                  {
                     // Ignore
                  }
               }
            }
         }

         Iterator<Map.Entry<String, List<TraceEvent>>> ccmIt = filteredCCMPool.entrySet().iterator();
         while (ccmIt.hasNext())
         {
            Map.Entry<String, List<TraceEvent>> entry = ccmIt.next();

            FileWriter ccm = null;
            try
            {
               String path = root.getAbsolutePath() + "/" + entry.getKey();
               File f = new File(path);
               f.mkdirs();

               ccm = new FileWriter(path + "/" + "ccm.html");
               generateCCMPoolHTML(entry.getKey(), entry.getValue(), ccmPoolStatus.get(entry.getKey()), ccm);
            }
            finally
            {
               if (ccm != null)
               {
                  try
                  {
                     ccm.flush();
                     ccm.close();
                  }
                  catch (Exception e)
                  {
                     // Ignore
                  }
               }
            }
         }

         // Reference
         FileWriter tocC = null;
         try
         {
            String path = root.getAbsolutePath();

            tocC = new FileWriter(path + "/" + "toc-c.html");
            generateToCConnection(tocConnections, tocC);
         }
         finally
         {
            if (tocC != null)
            {
               try
               {
                  tocC.flush();
                  tocC.close();
               }
               catch (Exception e)
               {
                  // Ignore
               }
            }
         }
         FileWriter tocCL = null;
         try
         {
            String path = root.getAbsolutePath();

            tocCL = new FileWriter(path + "/" + "toc-cl.html");
            generateToCConnectionListener(tocConnectionListeners, tocCL);
         }
         finally
         {
            if (tocCL != null)
            {
               try
               {
                  tocCL.flush();
                  tocCL.close();
               }
               catch (Exception e)
               {
                  // Ignore
               }
            }
         }
         FileWriter tocMCP = null;
         try
         {
            String path = root.getAbsolutePath();

            tocMCP = new FileWriter(path + "/" + "toc-mcp.html");
            generateToCManagedConnectionPool(tocMCPs, tocMCP);
         }
         finally
         {
            if (tocMCP != null)
            {
               try
               {
                  tocMCP.flush();
                  tocMCP.close();
               }
               catch (Exception e)
               {
                  // Ignore
               }
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      finally
      {
         if (logReader != null)
         {
            try
            {
               logReader.close();
            }
            catch (Exception e)
            {
               // Ignore
            }
         }
      }
   }
}