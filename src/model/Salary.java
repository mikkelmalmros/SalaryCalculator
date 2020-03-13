package model;

import java.time.LocalDate;
import java.time.LocalTime;

public interface Salary {
	public double calculate(LocalDate day, LocalTime from, LocalTime to, double pauseInMinutes);
}
