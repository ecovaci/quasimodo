package org.chess.quasimodo.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class OptionalMessageDialog extends JDialog {
	private JButton btnOk;
	private JButton btnCancel;
	private JCheckBox chckbxDoNotShow;
	private JLabel lblMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionalMessageDialog dialog = new OptionalMessageDialog();
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
	public OptionalMessageDialog() {
		initialize();
	}
	private void initialize() {
		setTitle("Quasimodo Message");
		setBounds(100, 100, 373, 249);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(119)
							.addComponent(getBtnOk())
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(getBtnCancel()))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addComponent(getLblMessage(), GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE))
						.addComponent(getChckbxDoNotShow(), GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(22, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(getLblMessage(), GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(getBtnOk())
						.addComponent(getBtnCancel()))
					.addGap(5)
					.addComponent(getChckbxDoNotShow(), GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
		);
		getContentPane().setLayout(groupLayout);
	}
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.setMargin(new Insets(0, 0, 0, 0));
			btnOk.setMinimumSize(new Dimension(65, 23));
		}
		return btnOk;
	}
	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Cancel");
			btnCancel.setMargin(new Insets(0, 0, 0, 0));
			btnCancel.setMinimumSize(new Dimension(65, 23));
		}
		return btnCancel;
	}
	private JCheckBox getChckbxDoNotShow() {
		if (chckbxDoNotShow == null) {
			chckbxDoNotShow = new JCheckBox("Do not show this message again");
		}
		return chckbxDoNotShow;
	}
	private JLabel getLblMessage() {
		if (lblMessage == null) {
			lblMessage = new JLabel("Message");
			lblMessage.setVerticalAlignment(SwingConstants.TOP);
		}
		return lblMessage;
	}
}
