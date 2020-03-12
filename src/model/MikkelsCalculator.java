package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MikkelsCalculator implements SalaryCalculator {

	// Normal salary per hour
	private double salaryPrHour = 118.42;

	@Override
	// Day paramater is not used, since there is no use of it on this work place
	// Can not be removed due to strategy pattern and inheritance from interface
	public double calculateSalary(LocalDate day, LocalTime from, LocalTime to, double pause) {
		double hours = 0;
		// Checks if work starts and ends on same day or goes past midnight
		if (from.isBefore(to)) {
			hours = (double) ChronoUnit.MINUTES.between(from, to) / 60;
		} else {
			hours += (double) (ChronoUnit.MINUTES.between(from, LocalTime.of(23, 59)) + 1) / 60;
			hours += (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, to) / 60;
		}
		// Total salary based on salary per hour times hours worked
		double result = (hours - pause) * salaryPrHour;
		// Then we calculate pension by adding 16.5% and subtracting our 4%
		// (collective agreement)
		result -= result * 1.165 * 0.04;
		// Then we subtract ATP and Arbejdsmarkedsbidrag (Danish tax shit)
		result -= result * 0.08;
		// Then we subtract tax (38%)
		result -= result * 0.38;
		return Math.round(result);
	}
}