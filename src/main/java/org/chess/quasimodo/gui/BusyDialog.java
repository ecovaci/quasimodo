package org.chess.quasimodo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class BusyDialog extends JDialog {
	private JLabel lblMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BusyDialog dialog = new BusyDialog();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public BusyDialog() {
		initialize();
	}
	private void initialize() {
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		setSize(269, 60);
		setLocationRelativeTo(null);
		getContentPane().add(getLblMessage(), BorderLayout.CENTER);
	}

	private JLabel getLblMessage() {
		if (lblMessage == null) {
			lblMessage = new JLabel();
			lblMessage.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblMessage.setOpaque(true);
			lblMessage.setBackground(new Color(112, 128, 144));
			lblMessage.setForeground(new Color(211, 211, 211));
			lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
			lblMessage.setFont(new Font("Arial Black", Font.PLAIN, 18));
		}
		return lblMessage;
	}
	
	public void setMessage (String message) {
		getLblMessage().setText(message);
	}
}
