package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import model.Loan;

/**
 * Servlet implementation class Start
 */
@WebServlet({ "/Start", "/Startup", "/Startup/*" })
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String GPI = "gInterest";// Grace Period Interest
	private static final String MP = "mPayment"; // Monthly Payment
	private static final String PRINCIPAL = "principal";
	private static final String PERIOD = "period";
	private static final String INTEREST = "interest";
	private static final String GRACE = "grace";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Start() {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("Hello world From Servlet.");
	}

	public void init() throws ServletException {
		super.init();
		Loan mloan = new Loan();
		this.getServletContext().setAttribute("loan", mloan);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Loan myLoan = (Loan) this.getServletContext().getAttribute("loan");
		HttpSession session = request.getSession();
		if (request.getParameter("submit") == null || request.getParameter("submit").equals("Restart")) {
			String target = "/UI.jspx";
			request.getRequestDispatcher(target).forward(request, response);
		} else {
			System.out.println("USING GET METHOD.");
			response.setContentType("text/jsp");
			String target;

			// Writer resOut = response.getWriter();
			String gPeriod = request.getParameter("gPeriod");
			boolean grace;
			// this new parameter indicates
			// whether grace period is accounted
			// for in the monthly payments.
			if (gPeriod != null) {
				grace = true;
			} else {
				grace = false;
			}
			// ******END********

			String principal = request.getParameter(PRINCIPAL);
			String interest = request.getParameter(INTEREST);
			String period = request.getParameter(PERIOD);
			String gp = this.getServletContext().getInitParameter("gracePeriod");
			String fi = this.getServletContext().getInitParameter("fixedInterest");
			if (principal == null | principal.length() < 1) {
				principal = (String) session.getAttribute(PRINCIPAL);
				if (principal == null) {
					principal = this.getServletContext().getInitParameter(PRINCIPAL);
				}
			}

			if (interest == null || interest.length() < 1) {
				interest = (String) session.getAttribute(INTEREST);
				if (interest == null) {
					interest = this.getServletContext().getInitParameter(INTEREST);
				}
			}

			if (period == null || period.length() < 1) {
				period = (String) session.getAttribute(PERIOD);
				if (period == null) {
					period = this.getServletContext().getInitParameter(PERIOD);
				}
			}
			// prepare for pre-fill
			session.setAttribute(PRINCIPAL, principal);
			session.setAttribute(INTEREST, interest);
			session.setAttribute(PERIOD, period);
			session.setAttribute(GRACE, grace);
			double monthly, gInterest;
			try {
				gInterest = myLoan.computeGraceInterest(principal, gp, interest, fi);
				if (grace) {
					monthly = myLoan.computePayment(principal, period, interest, "on", gp, fi);
					request.setAttribute(GPI, gInterest);// persisted in the request
				} else {
					monthly = myLoan.computePayment(principal, period, interest, "off", gp, fi);
					request.setAttribute(GPI, 0);// persisted in the request
				}
				request.setAttribute(MP, monthly);// persisted in the request
				target = "/Result.jspx";
				// these value DO NOT have to be converted to strings.
				// request.getSession().setAttribute(GPI, gInterest);
				// request.getSession().setAttribute(MP, monthly);
				// this.getServletContext().setAttribute(GPI, gInterest);
				// this.getServletContext().setAttribute(MP, monthly);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMsg", e.getMessage());
				target = "/UI.jspx";
			}
			request.getRequestDispatcher(target).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Loan myLoan = (Loan) this.getServletContext().getAttribute("loan");
		System.out.println("USING POST METHOD.");
		response.setContentType("text/jsp");
		String target;
		HttpSession session = request.getSession();
		// Writer resOut = response.getWriter();
		String gPeriod = request.getParameter("gPeriod");
		boolean grace;
		// this new parameter indicates
		// whether grace period is accounted
		// for in the monthly payments.
		if (gPeriod != null) {
			grace = true;
		} else {
			grace = false;
		}
		// ******END********

		String principal = request.getParameter(PRINCIPAL);
		String interest = request.getParameter(INTEREST);
		String period = request.getParameter(PERIOD);
		String gp = this.getServletContext().getInitParameter("gracePeriod");
		String fi = this.getServletContext().getInitParameter("fixedInterest");
		if (principal == null || principal.length() < 1) { //user did not provide principal
			principal = (String) session.getAttribute(PRINCIPAL); // try to get whatever user entered before.
			if (principal == null) { // if user never entered anything
				principal = this.getServletContext().getInitParameter(PRINCIPAL); //get default value that stored in context.
			}
		}
	
		if (interest == null || interest.length() < 1) {
			interest = (String) session.getAttribute(INTEREST);
			if (interest == null) {
				interest = this.getServletContext().getInitParameter(INTEREST);
			}
		}

		if (period == null || period.length() < 1) {
			period = (String) session.getAttribute(PERIOD);
			if (period == null) {
				period = this.getServletContext().getInitParameter(PERIOD);
			}
		}
		// store whatever user entered.
		session.setAttribute(PRINCIPAL, principal);
		session.setAttribute(INTEREST, interest);
		session.setAttribute(PERIOD, period);
		session.setAttribute(GRACE, grace);

		double monthly, gInterest;
		try {
			gInterest = myLoan.computeGraceInterest(principal, gp, interest, fi);//invoke LOAN function for calculation.
			if (grace) {// test whether "Grace Period" check box is checked.
				monthly = myLoan.computePayment(principal, period, interest, "on", gp, fi); //if check box is checked
				request.setAttribute(GPI, gInterest);// persisted in the request
			} else {
				monthly = myLoan.computePayment(principal, period, interest, "off", gp, fi);//if check box is unchecked
				request.setAttribute(GPI, 0);// persisted in the request
			}
			request.setAttribute(MP, monthly);// persisted in the request
			// these value DO NOT have to be converted to strings.
			// request.getSession().setAttribute(GPI, gInterest);
			// request.getSession().setAttribute(MP, monthly);
			// this.getServletContext().setAttribute(GPI, gInterest);
			// this.getServletContext().setAttribute(MP, monthly);
			target = "/Result.jspx";
			
		} catch (Exception e) {
			// if any exception occurred, 
			// it will re-direct to UI page and
			// set the error message into "errorMsg".
			e.printStackTrace();
			request.setAttribute("errorMsg", e.getMessage());
			target = "/UI.jspx";
		}
		request.getRequestDispatcher(target).forward(request, response);
	}

}
