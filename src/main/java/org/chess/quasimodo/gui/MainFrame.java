/*******************************************************************************
 * Quasimodo - a chess interface for playing and analyzing chess games.
 * Copyright (C) 2011 Eugen Covaci.
 * All rights reserved.
 *  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 ******************************************************************************/
package org.chess.quasimodo.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.chess.quasimodo.annotation.Design;
import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.config.design.Designable;
import org.chess.quasimodo.config.design.Designer;
import org.chess.quasimodo.domain.logic.FormView;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.gui.model.MainFrameModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.AbstractErrors;



/**
 * 
 * @author Eugen Covaci
 *
 */
@Component("frame")
public class MainFrame extends JFrame implements FormView<MainFrameModel> , Designable {
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -4767215060840052767L;
	
	private JPanel contentPane;
	
	@Autowired
	private BoardPanel boardPanel;
	
	@Autowired
	private ClockPanel clockView;
	
	@Autowired
	private NotationPanel notationPanel;
	
	@Autowired
	private EnginePanel engineView;
	
	@Autowired
	private EnginePanel extraEngineView;
	
	@Autowired
	private BookPanel bookView;

	@Autowired
	private PositionDialog positionView;
	
	@Autowired
	private EventPublisherAdapter eventPublisher;

	@Autowired
	private Designer designer;
	
	@Autowired
	private QuasimodoContext context;
	
	@Autowired
	private ApplicationContextAdapter contextAdapter;
	
	private JSplitPane verticalSplitPane;
	private JSplitPane horizontalSplitPane;
	
	private JSplitPane enginesPanel;
	
	private Rectangle bounds;
	
	public void injectDesign () {
		designer.injectDesign(this);
	}
	
	@PostConstruct
	public void initialize () {
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new CustomKeyEventDispatcher());
		//configure menu
		setJMenuBar(getMainMenuBar());
		//set content
		setContentPane(getMainPane());
		setMinimumSize(new Dimension(400, 400));
		setTitle("Quasimodo");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setFocusable(true);
		//configure and add listeners
		addListeners ();
		//apply design
		injectDesign ();
	}
	
	private void addListeners () {
		//add listeners for saving bounds purposes
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				saveNormalBounds ();
			}
			@Override
			public void componentMoved(ComponentEvent e) {
				saveNormalBounds ();
			}
		});
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent e) {
		        saveNormalBounds ();
			}
		});
	}
	
	private Rectangle computeDefaultBounds () {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = new Rectangle();
		bounds.setLocation((int)(screenSize.width * 0.1) , (int)(screenSize.height * 0.1));
		screenSize.height = (int)(screenSize.height * 0.8);
		screenSize.width = (int)(screenSize.width * 0.8);
		bounds.setSize(screenSize);
		return bounds;
	}
	
	private JMenuBar menuBar;
	
	private JMenuBar getMainMenuBar () {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMnFile());
			menuBar.add(getMnEdit());
			menuBar.add(getMnGame());
			menuBar.add(getMnAction());
			menuBar.add(getMnEngine());
			menuBar.add(getMnSettings());
			menuBar.add(getMnHelp());
			menuBar.setBorder(null);
		}
		return menuBar;
	}
	
	private JMenu mnFile;
	
	private JMenu getMnFile () {
		if (mnFile == null) {
			mnFile = new JMenu("File");
			mnFile.add(getMntmOpen());
			mnFile.add(getMntmExit());
		}
		return mnFile;
	}
	
	private JMenuItem mntmOpen;
	
	private JMenuItem getMntmOpen () {
		if (mntmOpen == null) {
			mntmOpen = new JMenuItem("Open ..");
			mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		}
		return mntmOpen;
	}
	
	private JMenuItem mntmExit;
	
	private JMenuItem getMntmExit () {
		if (mntmExit == null) {
			mntmExit = new JMenuItem("Exit");
			mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		}
		return mntmExit;
	}
	
	private JMenu mnEdit;
	
	private JMenu getMnEdit () {
		if (mnEdit == null) {
			mnEdit = new JMenu("Edit");
		}
		return mnEdit;
	}
	
	private JMenu mnGame;
	
	private JMenu getMnGame () {
		if (mnGame == null) {
			mnGame = new JMenu("Game");
			mnGame.add(getMntmNew());
			mnGame.add(getMntmSetUpGame());
			mnGame.add(getMntmSetUpPosition());
		}
		return mnGame;
	}
	
	private JMenuItem mntmNew;
	
	private JMenuItem getMntmNew () {
		if (mntmNew == null) {
			mntmNew = new JMenuItem("New Game");
			mntmNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					doMntmNewAction (e);
				}
			});
			mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK | Event.SHIFT_MASK));
		}
		return mntmNew;
	}
	
	private void doMntmNewAction (ActionEvent e) {
		eventPublisher.publishEvent(new CommandEvent(e.getSource(), CommandEvent.Command.NEW_GAME));
	}
	
	private JMenuItem mntmSetUpGame;
	
	private JMenuItem getMntmSetUpGame () {
		if (mntmSetUpGame == null) {
			mntmSetUpGame = new JMenuItem("Set up game ..");
			mntmSetUpGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
			mntmSetUpGame.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onSetupGame(e);
				}
			});
		}
		return mntmSetUpGame;
	}
	
	private JMenuItem mntmSetUpPosition;
	
	private JMenuItem getMntmSetUpPosition () {
		if (mntmSetUpPosition == null) {
			mntmSetUpPosition = new JMenuItem("Set up position ..");
			mntmSetUpPosition.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
			mntmSetUpPosition.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onSetupPosition(e);
				}
			});
		}
		return mntmSetUpPosition;
	}
	
	protected void onSetupPosition (ActionEvent e) {
		positionView.reset();
		positionView.setLocationRelativeTo(null);
		positionView.setVisible(true);
	}
	
	protected void onSetupGame (ActionEvent e) {
		eventPublisher.publishCommandEvent(e.getSource(), CommandEvent.Command.SET_UP_GAME_SHOW);
	}
	
	private JMenu mnAction;
	
	private JMenu getMnAction () {
		if (mnAction == null) {
			mnAction = new JMenu("Actions");
			mnAction.add(getTakebackPosition());
			mnAction.add(getForceMoveMenuItem());
			mnAction.add(getPauseMenuItem());
			mnAction.add(getResumeMenuItem());
			mnAction.add(getDrawMenuItem());
			mnAction.add(getResignMenuItem());
			mnAction.add(new JSeparator());
			mnAction.add(getAbortGameMenuItem());
		}
		return mnAction;
	}
	
	JMenuItem abortGameMenuItem;
	
	private JMenuItem getAbortGameMenuItem () {
		if (abortGameMenuItem == null) {
			abortGameMenuItem = new JMenuItem("Abort game");
			abortGameMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					abortGameMenu(e);
				}
			});
		}
		return abortGameMenuItem;
	}
	
	private void abortGameMenu (ActionEvent e) {
		eventPublisher.publishCommandEvent(e.getSource(), CommandEvent.Command.ABORT_GAME);
	}
	
	private JMenuItem takebackPosition;
	
	private JMenuItem getTakebackPosition () {
		if (takebackPosition == null) {
			takebackPosition = new JMenuItem("Takeback");
		}
		return takebackPosition;
	}
	
	private JMenuItem forceMoveMenuItem;
	
	private JMenuItem getForceMoveMenuItem () {
		if (forceMoveMenuItem == null) {
			forceMoveMenuItem = new JMenuItem("Force move");
		}
		return forceMoveMenuItem;
	}
	
	private JMenuItem pauseMenuItem;
	
	private JMenuItem getPauseMenuItem () {
		if (pauseMenuItem == null) {
			pauseMenuItem = new JMenuItem("Pause");
			
		}
		return pauseMenuItem;
	}
	
	private JMenuItem resumeMenuItem;
	
	private JMenuItem getResumeMenuItem () {
		if (resumeMenuItem == null) {
			resumeMenuItem = new JMenuItem("Resume");
			
		}
		return resumeMenuItem;
	}
	
	private JMenuItem drawMenuItem;
	
	private JMenuItem getDrawMenuItem () {
		if (drawMenuItem == null) {
			drawMenuItem = new JMenuItem("Offer draw");
		}
		return drawMenuItem;
	}
	
	private JMenuItem resignMenuItem;
	
	private JMenuItem getResignMenuItem () {
		if (resignMenuItem == null) {
			resignMenuItem = new JMenuItem("Resign");
		}
		return resignMenuItem;
	}
	
	private JMenu mnEngine;
	
	private JMenu getMnEngine () {
		if (mnEngine == null) {
			mnEngine = new JMenu("Engine");
			mnEngine.add(getMntmChangeEngine());
			mnEngine.add(getMntmswitchOffEngine());
			mnEngine.add(new JSeparator());
			mnEngine.add(getMnManageEngines());
		}
		return mnEngine;
	}
	
	private JMenuItem mntmchangeEngine;
	
	private JMenuItem getMntmChangeEngine () {
		if (mntmchangeEngine == null) {
			mntmchangeEngine = new JMenuItem("Change engine ..");
			mntmchangeEngine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		}
		return mntmchangeEngine;
	}
	
    private JMenuItem mntmswitchOffEngine;
	
	private JMenuItem getMntmswitchOffEngine() {
		if (mntmswitchOffEngine == null) {
			mntmswitchOffEngine = new JMenuItem("Switch off engine");
			mntmswitchOffEngine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
		}
		return mntmswitchOffEngine;
	}
	
	private JMenuItem mntmManageEngines;
		
	private JMenuItem getMnManageEngines() {
		if (mntmManageEngines == null) {
			mntmManageEngines = new JMenuItem("Manage engines ..");
			mntmManageEngines.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
			mntmManageEngines.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					manageEnginesAction (e);
				}
			});
		}
		return mntmManageEngines;
	}
	
	private void manageEnginesAction (ActionEvent e) {
		eventPublisher.publishCommandEvent(e.getSource(), this, CommandEvent.Command.MANAGE_ENGINES);
	}
	
	private JMenu mnOptions;
	
	private JMenu getMnSettings () {
		if (mnOptions == null) {
			mnOptions = new JMenu("Options");
			mnOptions.add(getMntmPlayers());
		}
		return mnOptions;
	}
	
	private JMenuItem mntmPlayers;
	
	private JMenuItem getMntmPlayers() {
		if (mntmPlayers == null) {
			mntmPlayers = new JMenuItem("Players");
			mntmPlayers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
			mntmPlayers.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onMenuItemPlayersAction (e);
				}
			});
		}
		return mntmPlayers;
	}
	
	private void onMenuItemPlayersAction (ActionEvent e) {
		eventPublisher.publishCommandEvent(e.getSource(), CommandEvent.Command.MANAGE_PLAYERS);
	}
	
	private JMenu mnHelp;
	
	private JMenu getMnHelp() {
		if (mnHelp == null) {
			mnHelp = new JMenu("Help");
			mnHelp.add(getMntmAbout());
		}
		return mnHelp;
	}
	
	private JMenuItem mntmAbout;
	
	private JMenuItem getMntmAbout() {
		if (mntmAbout == null) {
			mntmAbout = new JMenuItem("About");
		}
		return mntmAbout;
	}
	
	private JToolBar toolBar;
	
	private JToolBar getToolBar () {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setPreferredSize(new Dimension(10, 30));
			toolBar.setFloatable(false);
			toolBar.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			toolBar.setLayout(null);
			toolBar.add(getForwardButton());
			toolBar.add(getFastForwardButton());
			toolBar.add(getBackButton());
			toolBar.add(getRwindButton());
			toolBar.add(getToggleButton());
			
		}
		return toolBar;
	}
	
	private int buttonOffset = 220;
	
	private JButton forwardButton;
	
	private JButton getForwardButton () {
		if (forwardButton == null) {
			forwardButton = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader()
				      .getResource("images/buttons/forward.png")));
			forwardButton.setBounds(buttonOffset + 140, 5, 30, 20);
		}
		return forwardButton;
	}
	
    private JButton fastForwardButton;
	
	private JButton getFastForwardButton () {
		if (fastForwardButton == null) {
			fastForwardButton = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader()
				      .getResource("images/buttons/ffward.png")));
			fastForwardButton.setBounds(buttonOffset + 180, 5, 30, 20);
		}
		return fastForwardButton;
	}
	
    private JButton backButton;
	
	private JButton getBackButton() {
		if (backButton == null) {
			backButton = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader()
				      .getResource("images/buttons/back.png")));
			backButton.setBounds(buttonOffset + 60, 5, 30, 20);
		}
		return backButton;
	}
	
    private JButton rwindButton;
	
	private JButton getRwindButton() {
		if (rwindButton == null) {
			rwindButton = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader()
				      .getResource("images/buttons/rwind.png")));
			rwindButton.setBounds(buttonOffset + 20, 5, 30, 20);
		}
		return rwindButton;
	}
	
    private JButton toggleButton;
	
	private JButton getToggleButton() {
		if (toggleButton == null) {
			toggleButton = new JButton(new ImageIcon(Thread.currentThread().getContextClassLoader()
				      .getResource("images/buttons/toggle.png")));
			toggleButton.setBounds(buttonOffset + 100, 5, 30, 20);
		}
		return toggleButton;
	}
	
	private MainPanel mainPanel;
	
	private JPanel getMainPanel () {
		if (mainPanel == null) {
			mainPanel = new MainPanel();
			mainPanel.setName("mainPanel");
			//mainPanel.add(getVerticalSplitPane(), BorderLayout.CENTER);
			//mainPanel.getHeaderPanel().add(clockView, BorderLayout.CENTER);
			mainPanel.getInnerSplitPane().setLeftComponent(boardPanel);
			mainPanel.getInnerSplitPane().setRightComponent(getInfoPanel());
			mainPanel.getOutterSplitPane().setBottomComponent(getEnginesPanel());
		}
		return mainPanel;
	}
	
	private JSplitPane getVerticalSplitPane () {
		if (verticalSplitPane == null) {
			verticalSplitPane = new JSplitPane();
			verticalSplitPane.setContinuousLayout(true);
			verticalSplitPane.setBorder(null);
			verticalSplitPane.setResizeWeight(0.7);
			verticalSplitPane.setLeftComponent(boardPanel);
			verticalSplitPane.setRightComponent(getHorizontalSplitPane());
		}
		return verticalSplitPane;
	}
	
	private JPanel infoPanel;
	
	private JPanel getInfoPanel () {
		if (infoPanel == null) {
			infoPanel = new JPanel();
			infoPanel.setLayout(new BorderLayout(0, 0));
			infoPanel.add(clockView, BorderLayout.NORTH);
			infoPanel.add(getInfoTabbedPane(), BorderLayout.CENTER);
		}
		return infoPanel;
	}
	
	private JSplitPane getEnginesPanel () {
		if (enginesPanel == null) {
			initEnginesPanelSingleEngine();
		}
		return enginesPanel;
	}
	
	private JSplitPane createEnginesPanel () {
		JSplitPane enginesPanel = new JSplitPane();
		enginesPanel.setResizeWeight(0.5);
		enginesPanel.setContinuousLayout(true);
		enginesPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		enginesPanel.setDividerSize(4);
		enginesPanel.setLeftComponent(engineView);
		return enginesPanel;
	}
	
	private void initEnginesPanelSingleEngine() {
		enginesPanel = createEnginesPanel();
		enginesPanel.getRightComponent().setVisible(false);
	}
	
	private void initEnginesPanelExtraEngine () {
		enginesPanel = createEnginesPanel();
		enginesPanel.setRightComponent(extraEngineView);
	}
	
	public void showExtraEnginePanel () {
		horizontalSplitPane.setDividerLocation(0.5);
		initEnginesPanelExtraEngine();
		horizontalSplitPane.setRightComponent(enginesPanel);
	}
	
	private JSplitPane getHorizontalSplitPane () {
		if (horizontalSplitPane == null) {
			horizontalSplitPane = new JSplitPane();
			horizontalSplitPane.setPreferredSize(new Dimension(185, 25));
			horizontalSplitPane.setContinuousLayout(true);
			horizontalSplitPane.setBorder(null);
			horizontalSplitPane.setResizeWeight(0.7);
			horizontalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			horizontalSplitPane.setLeftComponent(getInfoPanel());
			horizontalSplitPane.setRightComponent(getEnginesPanel());
		}
		return horizontalSplitPane;
	}
	
	private JTabbedPane infoTabbedPane;
	
	private JTabbedPane getInfoTabbedPane () {
		if (infoTabbedPane == null) {
			infoTabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
			infoTabbedPane.addTab("Notation", null, notationPanel, null);
			infoTabbedPane.setEnabledAt(0, true);
			infoTabbedPane.addTab("Opening Book", null, bookView, null);
			infoTabbedPane.setEnabledAt(1, true);
		}
		return infoTabbedPane;
	}
	
	private JPanel statusPanel;
	
	private JPanel getStatusPanel () {
		if (statusPanel == null) {
			statusPanel = new JPanel();
			statusPanel.setPreferredSize(new Dimension(10, 22));
			statusPanel.setLayout(new BorderLayout(0, 0));
			JSeparator separator = new JSeparator();
			statusPanel.add(separator, BorderLayout.NORTH);
			statusPanel.add(getMessagePanel(), BorderLayout.EAST);
		}
		return statusPanel;
	}
	
	private JPanel messagePanel;
	
	private JPanel getMessagePanel () {
		if (messagePanel == null) {
			messagePanel = new JPanel();
			messagePanel.add(getMessageLabel());
			messagePanel.setPreferredSize(new Dimension(550, 22));
		}
		return messagePanel;
	}
	
	private JLabel messageLabel;
	
	private JLabel getMessageLabel () {
		if (messageLabel == null) {
			messageLabel = new JLabel();
			messageLabel.setPreferredSize(new Dimension(540, 20));
			messageLabel.setVerticalAlignment(SwingConstants.TOP);
			messageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return messageLabel;
	}
	
	public void printStatusMessage (String text) {
		getMessageLabel ().setText(text);
	}
	
	public void clearStatusMessage () {
		getMessageLabel ().setText(null);
	}
	
	private JPanel getMainPane () {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setPreferredSize(new Dimension(20, 20));
			contentPane.setMinimumSize(new Dimension(20, 20));
			contentPane.setBorder(null);
			contentPane.setLayout(new BorderLayout(0, 0));
			contentPane.add(getToolBar(), BorderLayout.NORTH);
			contentPane.add(getMainPanel(), BorderLayout.CENTER);
			contentPane.add(getStatusPanel(), BorderLayout.SOUTH);
		}
		return contentPane;
	}
	
	public void setVerticalSplitLocation (int location) {//FIXME - use config
		verticalSplitPane.setDividerLocation(location);
	}
	
	public int getVerticalSplitLocation () {//FIXME - use config
		return verticalSplitPane.getDividerLocation();
	}
	
	public void setHorizontalSplitLocation (int location) {//FIXME - use config
		horizontalSplitPane.setDividerLocation(location);
	}
	
	public int getHorizontalSplitLocation () {//FIXME - use config
		return horizontalSplitPane.getDividerLocation();
	}
	
	public void saveNormalBounds() {
		synchronized (this) {
			if (getExtendedState() == NORMAL) {
				bounds = getBounds();
			}
		}
	}


	@Override
	public void updateModel() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setModel(MainFrameModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showView() {
		// TODO Auto-generated method stub
		
	}

	public AbstractErrors validateModel() {
		return null;
	}

	@Design(key={"menu.font"})
	public void applyMenuFont(Font font) {
		JMenu menu;
		for (int i=0;i < menuBar.getMenuCount();i++) {
			menu = menuBar.getMenu(i);
			menu.setFont(font);
			for (java.awt.Component component : menu.getMenuComponents()) {
				if (component instanceof JMenuItem) {
					((JMenuItem)component).setFont(font);
				}
			}
		}
	}
	
	@Design(key={"frame.split.vertical"})
	public void applyVerticalSplitLocation (Integer splitLocation) {
		if (splitLocation != null) {
		    verticalSplitPane.setDividerLocation(splitLocation);
		}
	}
	
	@Design(key={"frame.split.horizontal"})
	public void applyHorizontalSplitLocation (Integer splitLocation) {
		if (splitLocation != null) {
		    horizontalSplitPane.setDividerLocation(splitLocation);
		}
	}
	
	@Design(key={"frame.bounds"})
	public void applyBounds(Rectangle bounds) {
		if (bounds == null) {
			bounds = computeDefaultBounds ();
		}
		this.bounds = bounds;
		setBounds(bounds);
        setPreferredSize(bounds.getSize());//FIXME necesar?
		
		//the window content changed, so re-arrange it.
		pack();//FIXME necesar?
	}
	
	@Design(key={"frame.state"})
	public void applyState(Integer state) {
		setExtendedState(state);
	}

	@Override
	public void refreshDesign() {
		repaint();
	}

	@Override
	public MainFrameModel getModel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class CustomKeyEventDispatcher implements KeyEventDispatcher {
		
	    @Override
	    public boolean dispatchKeyEvent(KeyEvent e) {
	        if (e.getID() == KeyEvent.KEY_PRESSED) {
	        	//System.out.println("key pressed " + e.getID());
				if (context.existCurrentGame() 
						&& !context.hasActiveGame()) {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						eventPublisher.publishEvent(
								new CommandEvent(e.getSource(), CommandEvent.Command.PREVIOUS_MOVE));
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						eventPublisher.publishEvent(
								new CommandEvent(e.getSource(), CommandEvent.Command.NEXT_MOVE));
					}
				}
	        }
	        return false;
	    }
	}
}
