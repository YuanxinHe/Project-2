package listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Application Lifecycle Listener implementation class MaxPrincipal
 *
 */
@WebListener
public class MaxPrincipal implements HttpSessionAttributeListener {

	/**
	 * Default constructor.
	 */
	public MaxPrincipal() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpSessionAttributeListener#attributeAdded(HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		handleEvent(arg0);
	}

	/**
	 * @see HttpSessionAttributeListener#attributeRemoved(HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		//handleEvent(arg0);
	}

	/**
	 * @see HttpSessionAttributeListener#attributeReplaced(HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		handleEvent(arg0);
	}

	public void handleEvent(HttpSessionBindingEvent event) {
		// Lecture sample
		if (event.getName().equals("principal")) {
			System.out.println("I was notified a principal changed....");
			String p = (String) event.getSession().getAttribute("principal");
			Double currentPrincipal = Double.parseDouble(p);
			String m = (String) event.getSession().getAttribute("maxPrincipal");
			double max;
			if (m == null) {
				max = currentPrincipal;
				event.getSession().setAttribute("maxPrincipal", p);
			} else {
				max = Double.parseDouble(m);
			}
			if (currentPrincipal > max)
				event.getSession().setAttribute("maxPrincipal", p);
		}
	}
	// calculate the MaxPrincipal here and set the attribute to the
	// application
}
