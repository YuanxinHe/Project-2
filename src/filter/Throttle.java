package filter;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class Throttle
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/Start, /Startup" }, servletNames = {
		"Start" })
public class Throttle implements Filter {
	private static final long LIMIT_TIME_PERIOD = 5000;// unit is ms. i.e. 5 seconds.
	/**
	 * Default constructor.
	 */
	public Throttle() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		final String LAST_ACCESS = "last_access_time";
		
//		System.out.println("I am filetring... before chaining");
//		HttpServletRequest req = (HttpServletRequest) request;
//		long time = req.getSession().getLastAccessedTime();
//		System.out.println("Last time: " + time);
//		long current = System.currentTimeMillis();
//		System.out.println("current time: " + current);
//		if (time + 5000 > current) {
//			request.getRequestDispatcher("/Throttle.jspx").forward(request, response);
//			System.out.println("Request too frequently!");
//			return;
//		} else {
//			// pass the request along the filter chain
//			chain.doFilter(request, response);
//		}
		HttpServletRequest casted_request = (HttpServletRequest) request;
		HttpSession session = casted_request.getSession();
		//if the session is NEW
		if (!session.isNew() && session.getAttribute(LAST_ACCESS) != null) {
			long access_time_difference = System.currentTimeMillis() - (long) session.getAttribute(LAST_ACCESS);
			if (access_time_difference < LIMIT_TIME_PERIOD) {
				//Set view context
				request.setAttribute("timeToWait", (LIMIT_TIME_PERIOD - access_time_difference) + " ms."); 
				request.getRequestDispatcher("/Throttle.jspx").forward(request, response);
				return; //Cancel the chain
			} 
		} 
		session.setAttribute(LAST_ACCESS, session.getLastAccessedTime());
		chain.doFilter(request, response);	
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
