package model;

import java.security.InvalidParameterException;

public class Loan {

	public Loan() {

	}

	public double computePayment(String p, String per, String i, String g, String gp, String fi) throws Exception {
		double principal, interest, gracePeriod, fixedInterest;
		double period, total_interest;
		double mPayment, gInterest;
		principal = parsePrincipal(p);
		period = parsePeriod(per);
		interest = parseInterest(i);
		gracePeriod = Double.parseDouble(gp);
		fixedInterest = Double.parseDouble(fi);
		total_interest = interest + fixedInterest;
		total_interest = total_interest / 100;
		gInterest = computeGraceInterest(p, gp, i, fi);
		if (g.equals("on")) {
			mPayment = (total_interest / 12) * principal / (1 - Math.pow((1 + (total_interest / 12)), -period))
					+ gInterest / (gracePeriod + period);
		} else {
			mPayment = (total_interest / 12) * principal / (1 - Math.pow((1 + (total_interest / 12)), -period));
		}
		return mPayment;
	}

	public double computeGraceInterest(String p, String gp, String i, String fi) throws Exception {
		double principal, interest, gracePeriod, fixedInterest;
		double total_interest;
		double gInterest;
		principal = parsePrincipal(p);
		interest = parseInterest(i);
		gracePeriod = Double.parseDouble(gp);
		fixedInterest = Double.parseDouble(fi);
		total_interest = interest + fixedInterest;
		total_interest = total_interest / 100;
		gInterest = principal * (total_interest / 12) * (gracePeriod);
		return gInterest;
	}
	// these private functions are used for invoking "parseDouble"
	// and try to throw exception with correspondingly error message.
	private double parsePrincipal(String x) {
		if(x==null) throw new InvalidParameterException("Principal can not be empty.");
		if(!isNumeric(x)) throw new InvalidParameterException("Principal value must be positive number.");
		double result = Double.parseDouble(x);
		if (result <= 0)
			throw new InvalidParameterException("Principal value must be positive.");
		return result;
	}

	private double parseInterest(String x) {
		if(x==null) throw new InvalidParameterException("Interest can not be empty.");
		if(!isNumeric(x)) throw new InvalidParameterException("Interest value must be positive number.");
		double result = Double.parseDouble(x);
		if (result <= 0)
			throw new InvalidParameterException("Interest value must be positive.");
		return result;
	}

	private double parsePeriod(String x) {
		if(x==null) throw new InvalidParameterException("Period can not be empty.");
		if(!isNumeric(x)) throw new InvalidParameterException("Period value must be positive number.");
		double result = Double.parseDouble(x);
		if (result <= 0)
			throw new InvalidParameterException("Period value must be positive.");
		return result;
	}

	private boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
