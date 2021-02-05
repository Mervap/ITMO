package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class ServerManager {
  private final Server server;
  private final ServletContextHandler servletContext;

  public ServerManager(int port) throws Exception {
    server = new Server(port);
    servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContext.setContextPath("/");
    server.setHandler(servletContext);
    server.start();
  }

  public void addServlet(Servlet servlet, String pathSpec) {
    servletContext.addServlet(new ServletHolder(servlet), pathSpec);
  }

  public Server getServer() {
    return server;
  }
}
