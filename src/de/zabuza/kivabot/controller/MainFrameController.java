package de.zabuza.kivabot.controller;

import javax.swing.JFrame;

import de.zabuza.kivabot.controller.listener.StartActionListener;
import de.zabuza.kivabot.controller.listener.StopActionListener;
import de.zabuza.kivabot.controller.listener.StopAtWindowCloseListener;
import de.zabuza.kivabot.controller.logging.Logger;
import de.zabuza.kivabot.controller.settings.SettingsController;
import de.zabuza.kivabot.model.tasks.RoutineTask;
import de.zabuza.kivabot.view.MainFrameView;

/**
 * The controller of the main frame.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 * 
 */
public final class MainFrameController {
	/**
	 * The time to wait for a thread to be finished after interrupting, in
	 * milliseconds.
	 */
	private static final long INTERRUPT_WAIT = 2000;
	/**
	 * The current executing routine.
	 */
	private RoutineTask mCurrentRoutine;
	/**
	 * Logger of the main frame.
	 */
	private final Logger mLogger;
	/**
	 * The controller for the settings.
	 */
	private final SettingsController mSettingsController;
	/**
	 * The view of the main frame.
	 */
	private final MainFrameView mView;

	/**
	 * Creates a new controller of the main frame by connecting it to the view.
	 * 
	 * @param owner
	 *            The owning frame of this controller
	 * @param view
	 *            view of the main frame
	 * @param logger
	 *            logger of the main frame
	 */
	public MainFrameController(final JFrame owner, final MainFrameView view, final Logger logger) {
		mView = view;
		mLogger = logger;
		mSettingsController = new SettingsController(owner, view, logger);
		mCurrentRoutine = null;
	}

	/**
	 * Initializes the controller.
	 */
	public void initialize() {
		linkListener();
		mSettingsController.initialize();
	}

	/**
	 * Call this method when the routine has finished.
	 */
	public void routineFinished() {
		mCurrentRoutine = null;
		mLogger.logInfo("Routine finished.", Logger.TOP_LEVEL);
		mView.setAllInputEnabled(true);
		mView.setStartButtonEnabled(true);
		mView.setStopButtonEnabled(false);
		mView.setSettingsButtonEnabled(true);
	}

	/**
	 * Starts the controller.
	 */
	public void start() {

	}

	/**
	 * Starts the routine.
	 */
	public void startRoutine() {
		mLogger.logInfo("Routine started.", Logger.TOP_LEVEL);
		mView.setAllInputEnabled(false);
		mView.setStartButtonEnabled(false);
		mView.setStopButtonEnabled(true);
		mView.setSettingsButtonEnabled(false);

		mCurrentRoutine = new RoutineTask(mView.getUsername(), mView.getPassword(), mView.getBrowser(), mLogger, this,
				mSettingsController);
		mCurrentRoutine.start();
	}

	/**
	 * Stops the routine.
	 */
	public void stopRoutine() {
		mLogger.logInfo("Routine stopped.", Logger.TOP_LEVEL);
		if (mCurrentRoutine != null) {
			mCurrentRoutine.interrupt();
			try {
				mCurrentRoutine.join(INTERRUPT_WAIT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Links the listener to the view.
	 */
	private void linkListener() {
		mView.addListenerToStartAction(new StartActionListener(this));
		mView.addListenerToStopAction(new StopActionListener(this));
		mView.addWindowListener(new StopAtWindowCloseListener(this));
	}
}