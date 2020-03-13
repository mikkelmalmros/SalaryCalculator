package controller;

import java.time.LocalDate;
import java.time.LocalTime;

import model.MikkelsCalculator;
import model.Salary;
import model.SarasCalculator;

public class Controller {

	// Singleton pattern controller
	private static Controller controller;

	private Controller() {
	}

	public static Controller getController() {
		if (controller == null) {
			controller = new Controller();
		}
		return controller;
	}

	public double calculateSalary(LocalDate day, LocalTime start, LocalTime end, double pause, boolean mikkel) {
		Salary salary;
		// Checks which calculator should be created
		if (mikkel)
			salary = new MikkelsCalculator();
		else
			salary = new SarasCalculator();
		return salary.calculate(day, start, end, pause);
	}
}
