package org.chess.quasimodo.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.chess.quasimodo.domain.validation.SelectFileValidator;
import org.chess.quasimodo.gui.model.SelectFileDialogModel;
import org.springframework.validation.Validator;


@SuppressWarnings("serial")
public class SelectFileDialog extends AbstractDialogForm<SelectFileDialogModel> {
	private JPanel           contentPanel;
	private JTextField       textField;
	private JButton          fileButton;
	private JLabel           lblFileLocation;

	/**
	 * Create the dialog.
	 */
	public SelectFileDialog() {
		model = new SelectFileDialogModel();
		initialize();
	}
	
	private void initialize() {
		setResizable(false);
		setTitle("Select File");
		setBounds(100, 100, 360, 149);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getContentPanel(), BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						acceptActionPerformed(e);
					}
				});
				okButton.setMargin(new Insets(0, 0, 0, 0));
				okButton.setPreferredSize(new Dimension(60, 23));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelActionPerformed(e);
					}
				});
				cancelButton.setMargin(new Insets(0, 0, 0, 0));
				cancelButton.setPreferredSize(new Dimension(60, 23));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLocationRelativeTo(null);
	}
	
	private JPanel getContentPanel () {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			contentPanel.setLayout(null);
			contentPanel.add(getLblFileLocation());
			contentPanel.add(getTextField());
			contentPanel.add(getFileButton());
		}
		return contentPanel;
	}
	
	private JLabel getLblFileLocation () {
		if (lblFileLocation == null) {
			lblFileLocation = new JLabel("Path to file:");
			lblFileLocation.setBounds(22, 11, 171, 22);
		}
		return lblFileLocation;
	}
	
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setBounds(22, 44, 266, 20);
			textField.setColumns(10);
		}
		return textField;
	}
	private JButton getFileButton() {
		if (fileButton == null) {
			fileButton = new JButton("...");
			fileButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fileButtonActionPerformed(e);
				}
			});
			fileButton.setPreferredSize(new Dimension(47, 20));
			fileButton.setBounds(295, 43, 37, 21);
		}
		return fileButton;
	}
	
	protected void acceptActionPerformed(ActionEvent e) {
		updateModel();
	}
	
	private void cancelActionPerformed(ActionEvent e) {
		dispose();
	}
	
	private void fileButtonActionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();//TODO - point to engine dir
		int result = fileChooser.showOpenDialog(this); 	
		if (result == JFileChooser.APPROVE_OPTION) {
			textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	public void setFilepath (String filepath) {
		textField.setText(filepath);
	}
	
	@Override
	public void updateModel() {
        model.setPathname(textField.getText().trim());
	}
	
	@Override
	protected Validator getValidator() {
		return new SelectFileValidator();
	}	

}
