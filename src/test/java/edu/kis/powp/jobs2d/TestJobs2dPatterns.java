package edu.kis.powp.jobs2d;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.drivers.DriverManager;
import edu.kis.powp.jobs2d.drivers.adapter.DrawerDriverAdapter;
import edu.kis.powp.jobs2d.drivers.adapter.LineDrawerAdapter;
import edu.kis.powp.jobs2d.events.SelectChangeVisibleOptionListener;
import edu.kis.powp.jobs2d.events.SelectTestFigureOptionListener;
import edu.kis.powp.jobs2d.features.DrawerFeature;
import edu.kis.powp.jobs2d.features.DriverFeature;
import edu.kis.powp.jobs2d.gui.LineSettingsWindow;
import edu.kis.powp.jobs2d.helpers.LineSettings;
import edu.kis.powp.jobs2d.magicpresets.FiguresJane;
import edu.kis.powp.jobs2d.magicpresets.FiguresJoe;

public class TestJobs2dPatterns {
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private final static LineSettings lineSettings = new LineSettings();

	/**
	 * Setup test concerning preset figures in context.
	 * 
	 * @param application Application context.
	 */
	private static void setupPresetTests(Application application) {
		DriverManager driverManager = DriverFeature.getDriverManager();
		application.addTest("Figure Joe 1",  new SelectTestFigureOptionListener(
			driverManager, driver -> FiguresJoe.figureScript1(driver)));
		application.addTest("Figure Joe 2",  new SelectTestFigureOptionListener(
			driverManager, driver -> FiguresJoe.figureScript2(driver)));
		application.addTest("Figure Jane",  new SelectTestFigureOptionListener(
			driverManager, driver -> FiguresJane.figureScript(driver)));
	}

	/**
	 * Setup driver manager, and set default driver for application.
	 * 
	 * @param application Application context.
	 */
	private static void setupDrivers(Application application) {
		Job2dDriver loggerDriver = new LoggerDriver();
		DriverFeature.addDriver("Logger Driver", loggerDriver);
		DriverFeature.getDriverManager().setCurrentDriver(loggerDriver);

		Job2dDriver drawerDriver = new DrawerDriverAdapter(DrawerFeature.getDrawerController());
		DriverFeature.addDriver("Drawer Driver", drawerDriver);

		Job2dDriver lineDrawerAdapter = new LineDrawerAdapter(DrawerFeature.getDrawerController(), lineSettings::getLine);
		DriverFeature.addDriver("Line drawer driver", lineDrawerAdapter);

		DriverFeature.updateDriverInfo();
	}

	/**
	 * Setup menu for adjusting logging settings.
	 * 
	 * @param application Application context.
	 */
	private static void setupLogger(Application application) {
		application.addComponentMenu(Logger.class, "Logger", 0);
		application.addComponentMenuElement(Logger.class, "Clear log",
				(ActionEvent e) -> application.flushLoggerOutput());
		application.addComponentMenuElement(Logger.class, "Fine level", (ActionEvent e) -> logger.setLevel(Level.FINE));
		application.addComponentMenuElement(Logger.class, "Info level", (ActionEvent e) -> logger.setLevel(Level.INFO));
		application.addComponentMenuElement(Logger.class, "Warning level",
				(ActionEvent e) -> logger.setLevel(Level.WARNING));
		application.addComponentMenuElement(Logger.class, "Severe level",
				(ActionEvent e) -> logger.setLevel(Level.SEVERE));
		application.addComponentMenuElement(Logger.class, "OFF logging", (ActionEvent e) -> logger.setLevel(Level.OFF));
	}

	/**
	 * Setup menu for adjusting line settings.
	 * 
	 * @param application Application context.
	 */
	private static void setupLineSettings(Application application) {
		JFrame lineSettingsWindow = new LineSettingsWindow(lineSettings);
		lineSettingsWindow.setVisible(false);
		application.addComponentMenu(LineDrawerAdapter.class, "Line Settings");
		application.addComponentMenuElement(LineDrawerAdapter.class, "Open line settings",
				new SelectChangeVisibleOptionListener(lineSettingsWindow));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Application app = new Application("2d jobs Visio");
				DrawerFeature.setupDrawerPlugin(app);

				DriverFeature.setupDriverPlugin(app);
				setupDrivers(app);
				setupPresetTests(app);
				setupLogger(app);
				setupLineSettings(app);

				app.setVisibility(true);
			}
		});
	}

}
