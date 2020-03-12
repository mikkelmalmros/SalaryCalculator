package gui;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.LocalTimeStringConverter;

public class TimeSpinner extends Spinner<LocalTime> {

	Spinner<LocalTime> spinner = new Spinner<LocalTime>(new SpinnerValueFactory<LocalTime>() {
		{
			setConverter(new LocalTimeStringConverter(DateTimeFormatter.ofPattern("HH:mm"),
					DateTimeFormatter.ofPattern("HH:mm")));
		}

		@Override
		public void decrement(int steps) {
			if (getValue() == null)
				setValue(LocalTime.now());
			else {
				try {
					LocalTime time = (LocalTime) getValue();
					setValue(time.minusMinutes(steps));
				} catch (DateTimeParseException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fejl");
					alert.setContentText("Tiden skal være tt:mm");
					alert.setHeaderText("Indtast en korrekt tid");
					alert.show();
				}
			}
		}

		@Override
		public void increment(int steps) {
			if (this.getValue() == null)
				setValue(LocalTime.now());
			else {
				try {
					LocalTime time = (LocalTime) getValue();
					setValue(time.plusMinutes(steps));
				} catch (DateTimeParseException e) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fejl");
					alert.setContentText("Tiden skal være tt:mm");
					alert.setHeaderText("Indtast en korrekt tid");
					alert.show();
				}
			}
		}
	});

	public Spinner<LocalTime> getSpinner() {
		return spinner;
	}
}