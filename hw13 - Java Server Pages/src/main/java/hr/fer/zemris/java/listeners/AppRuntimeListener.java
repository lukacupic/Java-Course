package hr.fer.zemris.java.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class represents a listener which stores the
 * starting time of the application into the servlet
 * context attribute map. This attribute is then use.
 * to calculate the total runtime of the application.
 *
 * @author Luka Čupić
 */
@WebListener
public class AppRuntimeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("principium", System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}