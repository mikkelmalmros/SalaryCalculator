package model;

import java.time.LocalDate;
import java.time.LocalTime;

public interface SalaryCalculator {
	public double calculateSalary(LocalDate day, LocalTime from, LocalTime to, double pause);
}
