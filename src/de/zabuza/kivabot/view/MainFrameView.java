package de.zabuza.kivabot.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import de.zabuza.kivabot.model.tasks.EKivaTask;
import de.zabuza.sparkle.freewar.EWorld;
import de.zabuza.sparkle.freewar.movement.network.EMoveType;
import de.zabuza.sparkle.webdriver.EBrowser;

/**
 * View of the main frame.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class MainFrameView {
	/**
	 * Height of the view.
	 */
	public static final int HEIGHT = 425;
	/**
	 * Width of the view.
	 */
	public static final int WIDTH = 450;
	/**
	 * The default amount of columns for fields of the view.
	 */
	private static final int DEFAULT_FIELD_COLUMNS = 10;
	/**
	 * The default font of the view.
	 */
	private static final String DEFAULT_FONT = "Tahoma";
	/**
	 * The default font size of the view.
	 */
	private static final int DEFAULT_FONT_SIZE = 11;
	/**
	 * Check box for the blue sphere movement option.
	 */
	private JCheckBox mBlueSphereMovementOptionsBox;
	/**
	 * The browser choice of the view.
	 */
	private JComboBox<EBrowser> mBrowserChoiceBox;
	/**
	 * Container of the view.
	 */
	private final Container mContainer;
	/**
	 * The frame of the view.
	 */
	private final JFrame mFrame;
	/**
	 * List of all input elements.
	 */
	private final List<JComponent> mInputElements;
	/**
	 * Log area of the view.
	 */
	private JTextPane mLogArea;
	/**
	 * Log pane of the view.
	 */
	private JScrollPane mLogPane;
	/**
	 * The main panel of the view.
	 */
	private JPanel mMainPanel;
	/**
	 * Password field of the view.
	 */
	private JTextField mPasswordField;
	/**
	 * Settings button of the view.
	 */
	private JButton mSettingsBtn;
	/**
	 * Start button of the view.
	 */
	private JButton mStartBtn;
	/**
	 * Stop button of the view.
	 */
	private JButton mStopBtn;
	/**
	 * A list of all selectable tasks.
	 */
	private List<JCheckBox> mTaskList;
	/**
	 * The trailer panel of the view.
	 */
	private JPanel mTrailerPanel;
	/**
	 * Check box for the protection spell option.
	 */
	private JCheckBox mUseProtectionSpell;
	/**
	 * Username field of the view.
	 */
	private JTextField mUsernameField;
	/**
	 * Check box for the special skill option.
	 */
	private JCheckBox mUseSpecialSkill;
	/**
	 * The world choice of the view.
	 */
	private JComboBox<EWorld> mWorldChoiceBox;

	/**
	 * Creates the view.
	 * 
	 * @param frame
	 *            Frame of the view
	 */
	public MainFrameView(final JFrame frame) {
		mFrame = frame;
		mContainer = frame.getContentPane();
		mInputElements = new LinkedList<>();
		initialize();
	}

	/**
	 * Adds an action listener to the settings action.
	 * 
	 * @param listener
	 *            Listener to add
	 */
	public void addListenerToSettingsAction(final ActionListener listener) {
		mSettingsBtn.addActionListener(listener);
	}

	/**
	 * Adds an action listener to the start action.
	 * 
	 * @param listener
	 *            Listener to add
	 */
	public void addListenerToStartAction(final ActionListener listener) {
		mStartBtn.addActionListener(listener);
	}

	/**
	 * Adds an action listener to the stop action.
	 * 
	 * @param listener
	 *            Listener to add
	 */
	public void addListenerToStopAction(final ActionListener listener) {
		mStopBtn.addActionListener(listener);
	}

	/**
	 * Adds a window listener to the view window.
	 * 
	 * @param listener
	 *            Listener to add
	 */
	public void addWindowListener(final WindowListener listener) {
		mFrame.addWindowListener(listener);
	}

	/**
	 * Gets the selected input browser.
	 * 
	 * @return The selected input browser
	 */
	public EBrowser getBrowser() {
		return (EBrowser) mBrowserChoiceBox.getSelectedItem();
	}

	/**
	 * Gets the selected kiva tasks.
	 * 
	 * @return A set containing all selected kiva tasks
	 */
	public Set<EKivaTask> getKivaTasks() {
		final Set<EKivaTask> kivaTasks = new HashSet<>();
		for (final JCheckBox taskBox : mTaskList) {
			if (taskBox.isSelected()) {
				final String text = taskBox.getText();
				kivaTasks.add(EKivaTask.valueOf(text));
			}
		}
		return kivaTasks;
	}

	/**
	 * Gets the selected movement options.
	 * 
	 * @return A set containing all selected movement options
	 */
	public Set<EMoveType> getMovementOptions() {
		final Set<EMoveType> movementOptions = new HashSet<>();
		if (mBlueSphereMovementOptionsBox.isSelected()) {
			movementOptions.add(EMoveType.BLUE_SPHERE);
		}
		return movementOptions;
	}

	/**
	 * Gets the input password.
	 * 
	 * @return The input password
	 */
	public String getPassword() {
		return mPasswordField.getText();
	}

	/**
	 * Gets the input username.
	 * 
	 * @return The input username
	 */
	public String getUsername() {
		return mUsernameField.getText();
	}

	/**
	 * Gets the selected input world.
	 * 
	 * @return The selected input world
	 */
	public EWorld getWorld() {
		return (EWorld) mWorldChoiceBox.getSelectedItem();
	}

	/**
	 * Gets whether the use protection spell box is checked or not.
	 * 
	 * @return <tt>True</tt> if the use protection spell box is checked,
	 *         <tt>false</tt> otherwise
	 */
	public boolean isUseProtectionSpellChecked() {
		return mUseProtectionSpell.isSelected();
	}

	/**
	 * Gets whether the use special skill box is checked or not.
	 * 
	 * @return <tt>True</tt> if the use special skill box is checked,
	 *         <tt>false</tt> otherwise
	 */
	public boolean isUseSpecialSkillChecked() {
		return mUseSpecialSkill.isSelected();
	}

	/**
	 * Appends a line to the log area.
	 * 
	 * @param line
	 *            line to append
	 */
	public void log(final String line) {
		appendToLog(line + "\n", Color.BLACK);
	}

	/**
	 * Appends a line to the log area using a red font.
	 * 
	 * @param line
	 *            line to append
	 */
	public void logError(final String line) {
		appendToLog(line + "\n", Color.RED);
	}

	/**
	 * Enables or disables all input fields.
	 * 
	 * @param enabled
	 *            Whether the fields should be enabled or disabled
	 */
	public void setAllInputEnabled(final boolean enabled) {
		for (JComponent element : mInputElements) {
			element.setEnabled(enabled);
		}
	}

	/**
	 * Enables or disables the settings button.
	 * 
	 * @param enabled
	 *            Whether the button should be enabled or disabled
	 */
	public void setSettingsButtonEnabled(final boolean enabled) {
		mSettingsBtn.setEnabled(enabled);
	}

	/**
	 * Enables or disables the start button.
	 * 
	 * @param enabled
	 *            Whether the button should be enabled or disabled
	 */
	public void setStartButtonEnabled(final boolean enabled) {
		mStartBtn.setEnabled(enabled);
	}

	/**
	 * Enables or disables the stop button.
	 * 
	 * @param enabled
	 *            Whether the button should be enabled or disabled
	 */
	public void setStopButtonEnabled(final boolean enabled) {
		mStopBtn.setEnabled(enabled);
	}

	/**
	 * Appends a message to the logging area.
	 * 
	 * @param message
	 *            Message to add
	 * @param color
	 *            Color of the message
	 */
	private void appendToLog(final String message, final Color color) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, DEFAULT_FONT);
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = mLogArea.getDocument().getLength();
		mLogArea.setCaretPosition(len);
		mLogArea.setCharacterAttributes(aset, false);
		mLogArea.setEditable(true);
		mLogArea.replaceSelection(message);
		mLogArea.setEditable(false);
	}

	/**
	 * Initialize the contents of the view.
	 */
	private void initialize() {
		initializePanels();
		initializeLabels();
		initializeButtons();
		initializeInputFields();
		initializeTextAreas();

		setStopButtonEnabled(false);
	}

	/**
	 * Initialize the buttons.
	 */
	private void initializeButtons() {
		mStartBtn = new JButton("Start");
		mStartBtn.setBounds(200, 210, 100, 23);
		mMainPanel.add(mStartBtn);

		mStopBtn = new JButton("Stop");
		mStopBtn.setBounds(320, 210, 100, 23);
		mMainPanel.add(mStopBtn);

		mSettingsBtn = new LinkButton("Settings");
		mSettingsBtn.setBounds(350, 0, 90, 23);
		mTrailerPanel.add(mSettingsBtn);
	}

	/**
	 * Initialize the text fields.
	 */
	private void initializeInputFields() {
		mBlueSphereMovementOptionsBox = new JCheckBox(EMoveType.BLUE_SPHERE.name(), true);
		mBlueSphereMovementOptionsBox.setHorizontalAlignment(SwingConstants.LEFT);
		mBlueSphereMovementOptionsBox.setBounds(0, 20, 150, 20);
		mMainPanel.add(mBlueSphereMovementOptionsBox);
		mInputElements.add(mBlueSphereMovementOptionsBox);

		mUseProtectionSpell = new JCheckBox("Use protection spell", true);
		mUseProtectionSpell.setHorizontalAlignment(SwingConstants.LEFT);
		mUseProtectionSpell.setBounds(0, 70, 150, 20);
		mMainPanel.add(mUseProtectionSpell);
		mInputElements.add(mUseProtectionSpell);

		mUseSpecialSkill = new JCheckBox("Use special skill", false);
		mUseSpecialSkill.setHorizontalAlignment(SwingConstants.LEFT);
		mUseSpecialSkill.setBounds(0, 90, 150, 20);
		mMainPanel.add(mUseSpecialSkill);
		mInputElements.add(mUseSpecialSkill);

		final int taskBoxInitialY = 140;
		final int taskBoxYPadding = 20;
		final EKivaTask[] tasks = EKivaTask.values();
		mTaskList = new LinkedList<>();
		for (int i = 0; i < tasks.length; i++) {
			final EKivaTask task = tasks[i];
			final JCheckBox taskBox = new JCheckBox(task.name(), true);
			taskBox.setHorizontalAlignment(SwingConstants.LEFT);
			taskBox.setBounds(0, taskBoxInitialY + (i * taskBoxYPadding), 180, 20);
			mMainPanel.add(taskBox);
			mInputElements.add(taskBox);
			mTaskList.add(taskBox);
		}

		mUsernameField = new JTextField();
		mUsernameField.setHorizontalAlignment(SwingConstants.LEFT);
		mUsernameField.setBounds((mMainPanel.getWidth() / 2) + 90, 0, 123, 20);
		mMainPanel.add(mUsernameField);
		mInputElements.add(mUsernameField);
		mUsernameField.setColumns(DEFAULT_FIELD_COLUMNS);

		mPasswordField = new JPasswordField();
		mPasswordField.setHorizontalAlignment(SwingConstants.LEFT);
		mPasswordField.setBounds((mMainPanel.getWidth() / 2) + 90, 30, 123, 20);
		mMainPanel.add(mPasswordField);
		mInputElements.add(mPasswordField);
		mPasswordField.setColumns(DEFAULT_FIELD_COLUMNS);

		mWorldChoiceBox = new JComboBox<>();
		for (final EWorld world : EWorld.values()) {
			mWorldChoiceBox.addItem(world);
			if (world == EWorld.ONE) {
				mWorldChoiceBox.setSelectedItem(world);
			}
		}
		mWorldChoiceBox.setBounds((mMainPanel.getWidth() / 2) + 90, 60, 123, 20);
		mMainPanel.add(mWorldChoiceBox);
		mInputElements.add(mWorldChoiceBox);

		mBrowserChoiceBox = new JComboBox<>();
		for (final EBrowser browser : EBrowser.values()) {
			mBrowserChoiceBox.addItem(browser);
			if (browser == EBrowser.CHROME) {
				mBrowserChoiceBox.setSelectedItem(browser);
			}
		}
		mBrowserChoiceBox.setBounds((mMainPanel.getWidth() / 2) + 90, 170, 123, 20);
		mMainPanel.add(mBrowserChoiceBox);
		mInputElements.add(mBrowserChoiceBox);
	}

	/**
	 * Initialize the labels.
	 */
	private void initializeLabels() {
		final JLabel movementOptionsLbl = new JLabel("Movement options:");
		movementOptionsLbl.setHorizontalAlignment(SwingConstants.LEFT);
		movementOptionsLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		movementOptionsLbl.setBounds(0, 0, 120, 14);
		mMainPanel.add(movementOptionsLbl);

		final JLabel additionalOptionsLbl = new JLabel("Additional options:");
		additionalOptionsLbl.setHorizontalAlignment(SwingConstants.LEFT);
		additionalOptionsLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		additionalOptionsLbl.setBounds(0, 50, 120, 14);
		mMainPanel.add(additionalOptionsLbl);

		final JLabel tasksLbl = new JLabel("Tasks:");
		tasksLbl.setHorizontalAlignment(SwingConstants.LEFT);
		tasksLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		tasksLbl.setBounds(0, 120, 60, 14);
		mMainPanel.add(tasksLbl);

		final JLabel usernameLbl = new JLabel("Username:");
		usernameLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		usernameLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		usernameLbl.setBounds((mMainPanel.getWidth() / 2) + 20, 0, 65, 14);
		mMainPanel.add(usernameLbl);

		final JLabel passwordLbl = new JLabel("Password:");
		passwordLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		passwordLbl.setBounds((mMainPanel.getWidth() / 2) + 20, 30, 65, 14);
		mMainPanel.add(passwordLbl);

		final JLabel worldChoiceLbl = new JLabel("World:");
		worldChoiceLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		worldChoiceLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		worldChoiceLbl.setBounds((mMainPanel.getWidth() / 2) + 20, 60, 65, 14);
		mMainPanel.add(worldChoiceLbl);

		final JLabel browserChoiceLbl = new JLabel("Browser:");
		browserChoiceLbl.setHorizontalAlignment(SwingConstants.RIGHT);
		browserChoiceLbl.setFont(new Font(DEFAULT_FONT, Font.BOLD, DEFAULT_FONT_SIZE + 1));
		browserChoiceLbl.setBounds((mMainPanel.getWidth() / 2) + 20, 170, 65, 14);
		mMainPanel.add(browserChoiceLbl);
	}

	/**
	 * Initialize the panels.
	 */
	private void initializePanels() {
		mMainPanel = new JPanel();
		mMainPanel.setBounds(10, 10, WIDTH - 25, 240);
		mContainer.add(mMainPanel);
		mMainPanel.setLayout(null);

		mLogPane = new JScrollPane();
		mLogPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mLogPane.setBounds(10, 270, WIDTH - 25, 100);
		mContainer.add(mLogPane);

		mTrailerPanel = new JPanel();
		mTrailerPanel.setBounds(10, 370, WIDTH - 25, 50);
		mContainer.add(mTrailerPanel);
		mTrailerPanel.setLayout(null);
	}

	/**
	 * Initialize the logging area.
	 */
	private void initializeTextAreas() {
		mLogArea = new JTextPane();
		mLogArea.setEditable(false);
		mLogPane.setViewportView(mLogArea);
	}
}