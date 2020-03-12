package gui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

public class MainApp extends Application {

	// Singleton pattern controller
	private Controller controller = Controller.getController();

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Udregn din løn");
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.setHeight(450);
		stage.setWidth(250);
		stage.setResizable(false);
		stage.show();
	}

	// Radiobuttons to select who is using the program
	private ToggleGroup toggleGroup = new ToggleGroup();
	private RadioButton rbMikkel = new RadioButton("Mikkel");
	private RadioButton rbSara = new RadioButton("Sara");

	// Date picker to choose the day you are working
	private DatePicker datePicker = new DatePicker(LocalDate.now());

	// Time spinners to select your working hours
	Spinner<LocalTime> spinnerFrom = new Spinner<LocalTime>(new SpinnerValueFactory<LocalTime>() {
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

	Spinner<LocalTime> spinnerTo = new Spinner<LocalTime>(new SpinnerValueFactory<LocalTime>() {
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
//	TimeSpinner spinnerFrom = new TimeSpinner();
//
//	TimeSpinner spinnerTo = new TimeSpinner();

	// Text field to use for inputting pause
	TextField txfPause = new TextField();

	// Button to start calculation
	Button btnCalculate = new Button("Udregn løn");

	// Text area to show salary
	TextField txfSalary = new TextField();

	private void initContent(GridPane pane) {
		pane.setGridLinesVisible(false);
		pane.setPadding(new Insets(20));
		pane.setHgap(20);
		pane.setVgap(10);

		// Adds label and radiobuttons to select who is using the program
		Label labelWho = new Label("Hvem bruger programmet?");
		pane.add(labelWho, 0, 0);
		rbMikkel.setToggleGroup(toggleGroup);
		rbSara.setToggleGroup(toggleGroup);
		HBox hBox = new HBox(rbMikkel, rbSara);
		pane.add(hBox, 0, 1);
		hBox.setSpacing(50);
		hBox.setAlignment(Pos.CENTER);

		// Adds label and date picker
		Label labelWhen = new Label("Hvilken dag skal du arbejde?");
		pane.add(labelWhen, 0, 2);
		pane.add(datePicker, 0, 3);
		datePicker.setPrefWidth(220);

		// Adds label and time spinner for when you start working
		Label labelFrom = new Label("Hvornår møder du ind?");
		pane.add(labelFrom, 0, 4);

		spinnerFrom.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				spinnerFrom.increment(0); // won't change value, but will commit editor
			}
		});
		spinnerFrom.setEditable(true);
		spinnerFrom.getValueFactory().setValue(LocalTime.of(0, 0));
		pane.add(spinnerFrom, 0, 5);
		spinnerFrom.setPrefWidth(220);

//		spinnerFrom.getSpinner().getValueFactory().setValue(LocalTime.of(0, 0));

		// Adds label and time spinner for when you stop working
		Label labelTo = new Label("Hvornår har du fri?");
		pane.add(labelTo, 0, 6);

		spinnerTo.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				spinnerTo.increment(0); // won't change value, but will commit editor
			}
		});
		spinnerTo.setEditable(true);
		spinnerTo.getValueFactory().setValue(LocalTime.of(23, 59));
		pane.add(spinnerTo, 0, 7);
		spinnerTo.setPrefWidth(220);

//		spinnerTo.getSpinner().getValueFactory().setValue(LocalTime.of(23, 59));

		// Adds label and textfield to put how long your break is
		Label labelPause = new Label("Hvor lang er din pause i minutter?");
		pane.add(labelPause, 0, 8);
		pane.add(txfPause, 0, 9);
		txfPause.setMaxWidth(220);

		// Adds button to calculate salary
		pane.add(btnCalculate, 0, 10);
		GridPane.setHalignment(btnCalculate, HPos.CENTER);
		btnCalculate.setOnAction(event -> this.calculate());

		// Adds label and text area to show your salary
		Label labelSalary = new Label("Så meget tjener du denne dag:");
		txfSalary.setPrefWidth(220);
		pane.add(labelSalary, 0, 11);
		txfSalary.setEditable(false);
		pane.add(txfSalary, 0, 12);
	}

	public void calculate() {
		// Creates all variables for the constructor
		LocalDate day = null;
		LocalTime from = null;
		LocalTime to = null;
		double pause = 0;
		double result = 0;
		boolean calculate = true;

		// Checks whether the day is set
		if (datePicker.getValue() != null) {
			day = datePicker.getValue();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fejl");
			alert.setContentText("Du skal vælge en dato");
			alert.setHeaderText("Vælg en dato");
			alert.show();
			calculate = false;
			txfSalary.clear();
		}

		// Checks whether the time from is set
		if (spinnerFrom.getValue() != null) {
			try {
				from = spinnerFrom.getValue();
			} catch (DateTimeParseException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Fejl");
				alert.setContentText("Du skal indtaste tiden som tt:mm");
				alert.setHeaderText("Indtast en korrekt tid");
				alert.show();
				calculate = false;
				txfSalary.clear();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fejl");
			alert.setContentText("Du skal vælge hvornår du møder ind");
			alert.setHeaderText("Vælg en tid");
			alert.show();
			calculate = false;
			txfSalary.clear();
		}

		// Checks whether the time to is set
		if (spinnerTo.getValue() != null) {
			try {
				to = spinnerTo.getValue();
			} catch (DateTimeParseException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Fejl");
				alert.setContentText("Du skal indtaste tiden som tt:mm");
				alert.setHeaderText("Indtast en korrekt tid");
				alert.show();
				calculate = false;
				txfSalary.clear();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fejl");
			alert.setContentText("Du skal vælge hvornår du har fri");
			alert.setHeaderText("Vælg en tid");
			alert.show();
			calculate = false;
			txfSalary.clear();
		}

		// Checks whether there is a pasuse, if not it is set to 0
		if (txfPause.getLength() > 0) {
			// Checks if anything else than numbers has been input
			try {
				pause = Double.parseDouble(txfPause.getText());
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Fejl");
				alert.setContentText("Du skal indtaste et tal");
				alert.setHeaderText("Indtast et tal");
				alert.show();
				calculate = false;
				txfSalary.clear();
			}
		}

		// Checks who is the user of the program
		if (rbMikkel.isSelected())
			result = controller.calculateSalary(day, from, to, pause, true);
		else if (rbSara.isSelected())
			result = controller.calculateSalary(day, from, to, pause, false);
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fejl");
			alert.setContentText("Du skal vælge hvem der bruger programmet");
			alert.setHeaderText("Vælg en person");
			alert.show();
			calculate = false;
		}

		// Sets the final text field
		if (calculate)
			txfSalary.setText(String.valueOf(result));
	}
}
