package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Salary {

	// Strategy pattern field
	private SalaryCalculator calculator;
	// Day work time starts
	private LocalDate day;
	// Time work time starts
	private LocalTime start;
	// Time work time ends
	private LocalTime end;
	// Pause in minutes
	private double pauseInMinutes;

	public Salary(LocalDate day, LocalTime start, LocalTime end, double pauseInMinutes) {
		this.day = day;
		this.start = start;
		this.end = end;
		this.pauseInMinutes = pauseInMinutes / 60;
	}

	// Sets the calculator (strategy pattern)
	public void setCalculator(SalaryCalculator calculator) {
		this.calculator = calculator;
	}

	// Calls the calculate method from the calculator class
	public double calculate() {
		return calculator.calculateSalary(day, start, end, pauseInMinutes);
	}
}
