package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SarasCalculator implements Salary {

	// Normal salary per hour
	private double salaryPrHour = 118.42 + 3.65;
	// From 18:00 - 23:00 on weekdays
	private double weekdayEveningExtra = 26.45;
	// From 23:00 - 6:00 weekdays and Saturday
	private double nightExtra = 37.38;
	// From 15:00 - 24:00 on Saturdays
	private double saturdayExtra = 46.55;
	// All of Sunday
	private double sundayExtra = 51.80;

	// Some specific times used a lot
	private LocalTime sixInMorning = LocalTime.of(6, 0);
	private LocalTime sixInEvening = LocalTime.of(18, 0);
	private LocalTime elevenInEvening = LocalTime.of(23, 0);
	private LocalTime threeInEvening = LocalTime.of(15, 0);

	@Override
	public double calculate(LocalDate day, LocalTime from, LocalTime to, double pause) {
		double hours = 0;
		// Checks if work starts and ends on same day or goes past midnight
		if (from.isBefore(to)) {
			hours = (double) ChronoUnit.MINUTES.between(from, to) / 60;
		} else {
			hours += (double) (ChronoUnit.MINUTES.between(from, LocalTime.of(23, 59)) + 1) / 60;
			hours += (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, to) / 60;
		}
		// Total salary based on salary per hour times hours worked without extra
		// payment
		double result = (hours - (pause / 60)) * salaryPrHour;
		// Then we find the extra payment based on the time of the day and the weekday
		if (hours - (pause / 60) > 0) {
			DayOfWeek dayOfWeek = day.getDayOfWeek();
			switch (dayOfWeek) {
			case MONDAY:
			case TUESDAY:
			case WEDNESDAY:
			case THURSDAY:
			case FRIDAY:
				result += weekdayExtra(from, to);
				break;
			case SATURDAY:
				result += saturdayExtra(from, to);
				break;
			case SUNDAY:
				result += sundayExtra(from, to);
				break;
			}

			// Then we subtract pension
			result -= result * 0.038;
			// Then we subtract ATP and Arbejdsmarkedsbidrag (Danish tax shit)
			result -= result * 0.08;
			// Then we subtract tax (39%)
			result -= result * 0.39;
		}
		return Math.round(result);
	}

	private double weekdayExtra(LocalTime from, LocalTime to) {
		double result = 0;

		// Checks if worktime ends after 18:00 and makes it 5 hours max
		if (to.isAfter(sixInEvening)) {
			double eveningPeriod = (double) ChronoUnit.MINUTES.between(sixInEvening, to) / 60;
			if (eveningPeriod > 5)
				eveningPeriod = 5;
			result += eveningPeriod * weekdayEveningExtra;
		}

		// Checks if worktime passes 23:00 and is before 23:59 since it would change to
		// night extra
		if (to.isAfter(elevenInEvening)) {
			double nightPeriod = (double) ChronoUnit.MINUTES.between(elevenInEvening, to) / 60;
			result += nightPeriod * nightExtra;
			// Checks if worktime passes 0:00 and ends before or at 6:00 next day since it
			// would be night extra
		} else if (to.isBefore(sixInMorning) && from.isAfter(to)) {
			// Checks how much extra weekday evening should be added
			if (from.isBefore(sixInEvening)) {
				double eveningPeriod = (double) ChronoUnit.MINUTES.between(sixInEvening, elevenInEvening) / 60;
				result += eveningPeriod * weekdayEveningExtra;
			} else {
				double eveningPeriod = (double) ChronoUnit.MINUTES.between(from, elevenInEvening) / 60;
				result += eveningPeriod * weekdayEveningExtra;
			}
			// Adds 1 since worktime would have passed 23:00 and thereby earned 1 hour of
			// night extra
			double nightPeriod = (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, to) / 60 + 1;
			result += nightPeriod * nightExtra;
			// Checks if end of worktime passes 0:00
		} else if (from.isAfter(to)) {
			// Checks how much extra weekday evening should be added
			if (from.isBefore(sixInEvening)) {
				double eveningPeriod = (double) ChronoUnit.MINUTES.between(sixInEvening, elevenInEvening) / 60;
				result += eveningPeriod * weekdayEveningExtra;
			} else {
				double eveningPeriod = (double) ChronoUnit.MINUTES.between(from, elevenInEvening) / 60;
				result += eveningPeriod * weekdayEveningExtra;
			}
			// Checks if worktime ends before 6:00 due to night extra ending there
			if (to.isBefore(sixInMorning)) {
				double nightPeriod = (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, to) / 60 + 1;
				result += nightPeriod * nightExtra;
			} else {
				double nightPeriod = (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, sixInMorning) / 60 + 1;
				result += nightPeriod * nightExtra;
			}
		}

		// Checks if worktime starts before 6:00 since it would be night extra
		if (from.isBefore(sixInMorning)) {
			// Checks if worktime ends before 6:00 due to night extra ending there
			if (to.isAfter(sixInMorning)) {
				double morningPeriod = (double) ChronoUnit.MINUTES.between(from, sixInMorning) / 60;
				result += morningPeriod * nightExtra;
			} else {
				double morningPeriod = (double) ChronoUnit.MINUTES.between(from, to) / 60;
				result += morningPeriod * nightExtra;
			}
		}
		return result;
	}

	private double saturdayExtra(LocalTime from, LocalTime to) {
		double result = 0;

		// Checks if time has passed 15:00 since it would be Saturday extra
		if (to.isAfter(threeInEvening)) {
			if (from.isBefore(threeInEvening)) {
				double saturdayPeriod = (double) ChronoUnit.MINUTES.between(threeInEvening, to) / 60;
				result += saturdayPeriod * saturdayExtra;
			} else {
				double saturdayPeriod = (double) ChronoUnit.MINUTES.between(from, to) / 60;
				result += saturdayPeriod * saturdayExtra;
			}
		}

		// Checks if worktime passes 0:00 since it would change to Sunday extra
		if (from.isAfter(to)) {
			// Checks how much Saturday extra should be added
			if (from.isBefore(threeInEvening)) {
				double saturdayPeriod = (double) (ChronoUnit.MINUTES.between(threeInEvening, LocalTime.of(23, 59))
						+ 1) / 60;
				result += saturdayPeriod * saturdayExtra;
			} else {
				double saturdayPeriod = (double) (ChronoUnit.MINUTES.between(from, LocalTime.of(23, 59))
						+ 1) / 60;
				result += saturdayPeriod * saturdayExtra;
			}
			// Adds the Sunday extra
			double sundayPeriod = (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, to) / 60;
			result += sundayPeriod * sundayExtra;
		}
		// Checks if worktime starts before 6:00 since it would be night extra
		if (from.isBefore(sixInMorning)) {
			// Checks if worktime ends before 6:00 due to night extra ending there
			if (to.isAfter(sixInMorning)) {
				double morningPeriod = (double) ChronoUnit.MINUTES.between(from, sixInMorning) / 60;
				result += morningPeriod * nightExtra;
			} else {
				double morningPeriod = (double) ChronoUnit.MINUTES.between(from, to) / 60;
				result += morningPeriod * nightExtra;
			}
		}
		return result;
	}

	private double sundayExtra(LocalTime from, LocalTime to) {
		double result = 0;

		// Adds Sunday extra if work time starts and ends on the same day
		if (to.isAfter(from) && to.isBefore(LocalTime.MAX)) {
			double sundayPeriod = (double) ChronoUnit.MINUTES.between(from, to) / 60;
			result += sundayPeriod * sundayExtra;
		}

		// Checks if work time has passed 0:00 and ends before 6:00 since it would then
		// be normal night extra
		if (to.isBefore(sixInMorning) && from.isAfter(to)) {
			// Adds the Sunday extra
			double sundayPeriod = (double) (ChronoUnit.MINUTES.between(from, LocalTime.of(23, 59)) + 1) / 60;
			result += sundayPeriod * sundayExtra;
			// Adds the night extra
			double nightPeriod = (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, to) / 60;
			result += nightPeriod * nightExtra;
			// Checks if work time has passed 0:00
		} else if (to.isBefore(from)) {
			double sundayPeriod = (double) (ChronoUnit.MINUTES.between(from, LocalTime.of(23, 59))
					+ 1) / 60;
			result += sundayPeriod * sundayExtra;
			double nightPeriod = (double) ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, sixInMorning) / 60;
			result += nightPeriod * nightExtra;
		}
		return result;
	}
}