package org.chess.quasimodo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;


@SuppressWarnings("serial")
public class MessageDialog extends JDialog {
	private JTextPane messagesTextPane;

	private ScheduledThreadPoolExecutor    executor = new ScheduledThreadPoolExecutor(1);
	
	private int rate = 3000000;
	private int step = 5;
	
	private int x = 100;
	private int y = 100;
	
	private int width = 300;
	private int minHeight = 5;
	private int maxHeight = 350;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final MessageDialog dialog = new MessageDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.makeVisible();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public MessageDialog() {
		initialize();
	}

	public void initialize () {
		setUndecorated(true);
		setBounds(x, y, width, minHeight);
		getContentPane().add(getMessagesTextPane(), BorderLayout.CENTER);
	}
	
	public void makeVisible () {
		setVisible(true);
		
		executor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				Dimension size = getSize();
				if (size.height > maxHeight) {
					executor.shutdown();
				}
				setSize (size.width, size.height + step);
			}
		}, 0, rate, TimeUnit.NANOSECONDS);
	}
	
	public void hide () {
		executor.shutdown();
		dispose();
	}
	
	public void setMessage (String message) {
		getMessagesTextPane().setText(message);
	}
	
	private JTextPane getMessagesTextPane() {
		if (messagesTextPane == null) {
			messagesTextPane = new JTextPane();
			messagesTextPane.setEnabled(false);
			messagesTextPane.setEditable(false);
			messagesTextPane.setBackground(new Color(143, 188, 143));
			messagesTextPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		}
		return messagesTextPane;
	}
}
