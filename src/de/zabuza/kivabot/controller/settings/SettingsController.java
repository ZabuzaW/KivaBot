package de.zabuza.kivabot.controller.settings;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTextField;

import de.zabuza.kivabot.controller.listener.CloseAtCancelActionListener;
import de.zabuza.kivabot.controller.listener.ClosingCallbackWindowListener;
import de.zabuza.kivabot.controller.listener.FileChooseSetActionListener;
import de.zabuza.kivabot.controller.listener.SaveActionListener;
import de.zabuza.kivabot.controller.listener.SettingsActionListener;
import de.zabuza.kivabot.controller.logging.Logger;
import de.zabuza.kivabot.model.IBrowserSettingsProvider;
import de.zabuza.kivabot.model.tasks.EKivaTask;
import de.zabuza.kivabot.view.MainFrameView;
import de.zabuza.kivabot.view.SettingsDialog;
import de.zabuza.sparkle.freewar.EWorld;
import de.zabuza.sparkle.freewar.movement.network.EMoveType;
import de.zabuza.sparkle.webdriver.EBrowser;

/**
 * The controller of the settings.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class SettingsController implements ISettingsProvider, IBrowserSettingsProvider {
	/**
	 * Text to save for a value if a key is unknown.
	 */
	public static final String UNKNOWN_KEY_VALUE = "";
	/**
	 * Key identifier for binary setting.
	 */
	private static final String KEY_IDENTIFIER_BINARY = "binary";
	/**
	 * Key identifier for the selected browser.
	 */
	private static final String KEY_IDENTIFIER_BROWSER = "browser";
	/**
	 * Key identifier for driver settings.
	 */
	private static final String KEY_IDENTIFIER_DRIVER = "driver";
	/**
	 * Key identifier for the movement options.
	 */
	private static final String KEY_IDENTIFIER_MOVEMENT_OPTION = "movement_option";
	/**
	 * Key identifier for the password.
	 */
	private static final String KEY_IDENTIFIER_PASSWORD = "password";
	/**
	 * Key identifier for the protection spell setting.
	 */
	private static final String KEY_IDENTIFIER_PROTECTION_SPELL = "protection_spell";
	/**
	 * Key identifier for the tasks.
	 */
	private static final String KEY_IDENTIFIER_TASK = "task";
	/**
	 * Key identifier for the use of the protection spell.
	 */
	private static final String KEY_IDENTIFIER_USE_PROTECTION_SPELL = "use_protection_spell";
	/**
	 * Key identifier for the use of the special skill.
	 */
	private static final String KEY_IDENTIFIER_USE_SPECIAL_SKILL = "use_special_skill";
	/**
	 * Key identifier for the username.
	 */
	private static final String KEY_IDENTIFIER_USERNAME = "username";
	/**
	 * Key identifier for the selected world.
	 */
	private static final String KEY_IDENTIFIER_WORLD = "world";
	/**
	 * Separator which separates several information in a key.
	 */
	private static final String KEY_INFO_SEPARATOR = "@";
	/**
	 * The logger used by this object.
	 */
	private final Logger mLogger;
	/**
	 * The owning frame of this controller.
	 */
	private final JFrame mOwner;
	/**
	 * The object for the settings.
	 */
	private final Settings mSettings;
	/**
	 * The settings dialog or <tt>null</tt> if currently not opened.
	 */
	private SettingsDialog mSettingsDialog;
	/**
	 * Structure which saves all currently loaded settings.
	 */
	private final Map<String, String> mSettingsStore;
	/**
	 * The view of the main frame.
	 */
	private final MainFrameView mView;

	/**
	 * Creates a new controller of the settings.
	 * 
	 * @param owner
	 *            The owning frame of this controller
	 * @param view
	 *            The view to control
	 * @param logger
	 *            The logger to use
	 */
	public SettingsController(final JFrame owner, final MainFrameView view, final Logger logger) {
		mView = view;
		mLogger = logger;
		mOwner = owner;

		mSettingsStore = new HashMap<>();
		mSettings = new Settings(mLogger);
		mSettingsDialog = null;
	}

	/**
	 * Call whenever the settings dialog is closing. This is used as callback to
	 * free the parent window of the dialog.
	 */
	public void closingSettingsDialog() {
		mView.setAllInputEnabled(true);
		mView.setStartButtonEnabled(true);
		mView.setStopButtonEnabled(false);
		mView.setSettingsButtonEnabled(true);
		mSettingsDialog = null;
	}

	/**
	 * Call whenever the save action is to be executed. This will save all
	 * settings and close the settings dialog, if opened.
	 */
	public void executeSaveAction() {
		// Save dialog settings if dialog is opened
		if (mSettingsDialog != null) {
			// Driver settings
			for (final EBrowser browser : EBrowser.values()) {
				final JTextField field = mSettingsDialog.getBrowserDriverField(browser);
				final String value = field.getText();
				if (!value.equals(UNKNOWN_KEY_VALUE)) {
					final String key = KEY_IDENTIFIER_DRIVER + KEY_INFO_SEPARATOR + browser;
					setSetting(key, value);
				}
			}

			// Binary setting
			final JTextField binaryField = mSettingsDialog.getBrowserBinaryField();
			final String binaryValue = binaryField.getText();
			if (!binaryValue.equals(UNKNOWN_KEY_VALUE)) {
				final String key = KEY_IDENTIFIER_BINARY;
				setSetting(key, binaryValue);
			}

			// Protection spell setting
			final JTextField protectionSpellField = mSettingsDialog.getProtectionSpellField();
			final String protectionSpellValue = protectionSpellField.getText();
			if (!protectionSpellValue.equals(UNKNOWN_KEY_VALUE)) {
				final String key = KEY_IDENTIFIER_PROTECTION_SPELL;
				setSetting(key, protectionSpellValue);
			}
		}

		// Save the current content of the main view
		// Username
		final String username = mView.getUsername();
		if (!username.equals(UNKNOWN_KEY_VALUE)) {
			final String key = KEY_IDENTIFIER_USERNAME;
			setSetting(key, username);
		}

		// Password
		final String password = mView.getPassword();
		if (!password.equals(UNKNOWN_KEY_VALUE)) {
			final String key = KEY_IDENTIFIER_PASSWORD;
			setSetting(key, password);
		}

		// World
		final EWorld world = mView.getWorld();
		if (world != null) {
			final String key = KEY_IDENTIFIER_WORLD;
			setSetting(key, world.toString());
		}

		// Selected browser
		final EBrowser browser = mView.getBrowser();
		if (browser != null) {
			final String key = KEY_IDENTIFIER_BROWSER;
			setSetting(key, browser.toString());
		}

		// Movement options
		final Set<EMoveType> selectedOptions = mView.getMovementOptions();
		for (final EMoveType moveType : EMoveType.values()) {
			final boolean value = selectedOptions.contains(moveType);
			final String key = KEY_IDENTIFIER_MOVEMENT_OPTION + KEY_INFO_SEPARATOR + moveType;
			setSetting(key, Boolean.toString(value));
		}

		// Tasks
		final Set<EKivaTask> selectedTasks = mView.getKivaTasks();
		for (final EKivaTask task : EKivaTask.values()) {
			final boolean value = selectedTasks.contains(task);
			final String key = KEY_IDENTIFIER_TASK + KEY_INFO_SEPARATOR + task;
			setSetting(key, Boolean.toString(value));
		}

		// Use protection spell setting
		final boolean useProtectionSpell = mView.isUseProtectionSpellChecked();
		String key = KEY_IDENTIFIER_USE_PROTECTION_SPELL;
		setSetting(key, Boolean.toString(useProtectionSpell));

		// Use special skill setting
		final boolean useSpecialSkill = mView.isUseSpecialSkillChecked();
		key = KEY_IDENTIFIER_USE_SPECIAL_SKILL;
		setSetting(key, Boolean.toString(useSpecialSkill));

		// Save settings
		mSettings.saveSettings(this);

		// Close the settings dialog, if opened
		if (mSettingsDialog != null) {
			mSettingsDialog.dispatchEvent(new WindowEvent(mSettingsDialog, WindowEvent.WINDOW_CLOSING));
		}
	}

	/**
	 * Call whenever the settings action is to be executed. This will open a
	 * settings dialog.
	 */
	public void executeSettingsAction() {
		// Deactivate all actions until the settings dialog has closed
		mView.setAllInputEnabled(false);
		mView.setStartButtonEnabled(false);
		mView.setStopButtonEnabled(false);
		mView.setSettingsButtonEnabled(false);

		// Open the dialog
		mSettingsDialog = new SettingsDialog(mOwner);
		linkDialogListener();

		// Load settings to the store
		passSettingsToSettingsDialogView();
		mSettingsDialog.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.zabuza.kivabot.controller.settings.ISettingsProvider#getAllSettings()
	 */
	@Override
	public Map<String, String> getAllSettings() {
		return mSettingsStore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.zabuza.kivabot.model.IBrowserSettingsProvider#getBrowserBinary()
	 */
	@Override
	public String getBrowserBinary() {
		String binary = getSetting(KEY_IDENTIFIER_BINARY);
		if (binary.equals(UNKNOWN_KEY_VALUE)) {
			return null;
		} else {
			return binary;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.zabuza.kivabot.model.IBrowserSettingsProvider#getDriverForBrowser(de.
	 * zabuza.sparkle.webdriver.EBrowser)
	 */
	@Override
	public String getDriverForBrowser(final EBrowser browser) {
		String key = KEY_IDENTIFIER_DRIVER + KEY_INFO_SEPARATOR + browser;
		String driver = getSetting(key);
		if (driver.equals(UNKNOWN_KEY_VALUE)) {
			return null;
		} else {
			return driver;
		}
	}

	/**
	 * Gets the name of the protection spell to use.
	 * 
	 * @return The name of the protection spell to use or <tt>null</tt> if not
	 *         set
	 */
	public String getProtectionSpell() {
		String protectionSpell = getSetting(KEY_IDENTIFIER_PROTECTION_SPELL);
		if (protectionSpell.equals(UNKNOWN_KEY_VALUE)) {
			return null;
		} else {
			return protectionSpell;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.zabuza.kivabot.controller.settings.ISettingsProvider#getSetting(java.
	 * lang.String)
	 */
	@Override
	public String getSetting(final String key) {
		String value = mSettingsStore.get(key);
		if (value == null) {
			value = UNKNOWN_KEY_VALUE;
		}
		return value;
	}

	/**
	 * Initializes the controller.
	 */
	public void initialize() {
		linkListener();
		mSettings.loadSettings(this);
	}

	/**
	 * Passes the settings of the store to the main view for display.
	 */
	public void passSettingsToMainView() {
		for (final Entry<String, String> entry : mSettingsStore.entrySet()) {
			final String[] keySplit = entry.getKey().split(KEY_INFO_SEPARATOR);
			final String keyIdentifier = keySplit[0];

			if (keyIdentifier.equals(KEY_IDENTIFIER_USERNAME)) {
				// Username
				mView.setUsername(entry.getValue());
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_PASSWORD)) {
				// Password
				mView.setPassword(entry.getValue());
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_WORLD)) {
				// World
				mView.setWorld(EWorld.valueOf(entry.getValue()));
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_BROWSER)) {
				// Browser
				mView.setBrowser(EBrowser.valueOf(entry.getValue()));
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_MOVEMENT_OPTION)) {
				// Movement option
				final EMoveType moveType = EMoveType.valueOf(keySplit[1]);
				final boolean isSelected = Boolean.valueOf(entry.getValue());
				mView.setMovementOption(moveType, isSelected);
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_TASK)) {
				// Task
				final EKivaTask task = EKivaTask.valueOf(keySplit[1]);
				final boolean isSelected = Boolean.valueOf(entry.getValue());
				mView.setKivaTask(task, isSelected);
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_USE_PROTECTION_SPELL)) {
				// Use protection spell setting
				mView.setUseProtectionSpell(Boolean.valueOf(entry.getValue()));
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_USE_SPECIAL_SKILL)) {
				// Use special skill setting
				mView.setUseSpecialSkill(Boolean.valueOf(entry.getValue()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.zabuza.kivabot.controller.settings.ISettingsProvider#setSetting(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public void setSetting(final String key, final String value) {
		mSettingsStore.put(key, value);
	}

	/**
	 * Links the listener of the dialog to it.
	 */
	private void linkDialogListener() {
		// Window listener
		mSettingsDialog.addWindowListener(new ClosingCallbackWindowListener(this));

		// Browser field listener
		for (EBrowser browser : EBrowser.values()) {
			ActionListener listener = new FileChooseSetActionListener(mSettingsDialog,
					mSettingsDialog.getBrowserDriverField(browser));
			mSettingsDialog.addListenerToBrowserDriverSelectionAction(browser, listener);
		}

		// Binary listener
		ActionListener listener = new FileChooseSetActionListener(mSettingsDialog,
				mSettingsDialog.getBrowserBinaryField());
		mSettingsDialog.addListenerToBrowserBinarySelectionAction(listener);

		// Save and cancel listener
		mSettingsDialog.addListenerToSaveAction(new SaveActionListener(this));
		mSettingsDialog.addListenerToCancelAction(new CloseAtCancelActionListener(mSettingsDialog));
	}

	/**
	 * Links the listener to the view.
	 */
	private void linkListener() {
		mView.addListenerToSettingsAction(new SettingsActionListener(this));
	}

	/**
	 * Passes the settings of the store to the settings dialog view for display.
	 */
	private void passSettingsToSettingsDialogView() {
		for (final Entry<String, String> entry : mSettingsStore.entrySet()) {
			final String[] keySplit = entry.getKey().split(KEY_INFO_SEPARATOR);
			final String keyIdentifier = keySplit[0];

			if (keyIdentifier.equals(KEY_IDENTIFIER_DRIVER)) {
				// Driver settings
				final EBrowser browser = EBrowser.valueOf(keySplit[1]);
				final JTextField field = mSettingsDialog.getBrowserDriverField(browser);
				field.setText(entry.getValue());
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_BINARY)) {
				// Binary settings
				final JTextField field = mSettingsDialog.getBrowserBinaryField();
				field.setText(entry.getValue());
			} else if (keyIdentifier.equals(KEY_IDENTIFIER_PROTECTION_SPELL)) {
				// Protection spell settings
				final JTextField field = mSettingsDialog.getProtectionSpellField();
				field.setText(entry.getValue());
			}
		}
	}
}
