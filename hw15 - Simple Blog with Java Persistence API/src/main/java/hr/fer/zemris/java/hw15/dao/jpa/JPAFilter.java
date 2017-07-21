package hr.fer.zemris.java.hw15.dao.jpa;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * This class represents a filter which is started whenever a servlet
 * mapped to "/servleti/*" has been triggered. The filter causes the
 * next filter in the chain to be invoked.
 *
 * @author Luka Čupić
 */
@WebFilter("/servleti/*")
public class JPAFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} finally {
			JPAEMProvider.close();
		}
	}

	@Override
	public void destroy() {
	}
}