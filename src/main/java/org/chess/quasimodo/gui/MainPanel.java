package org.chess.quasimodo.gui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JSplitPane;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6232506147907600575L;
	private JSplitPane outterSplitPane;
	private JSplitPane innerSplitPane;

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		setLayout(new BorderLayout(0, 0));
		add(getOutterSplitPane(), BorderLayout.CENTER);

	}

	protected JSplitPane getOutterSplitPane() {
		if (outterSplitPane == null) {
			outterSplitPane = new JSplitPane();
			outterSplitPane.setContinuousLayout(true);
			outterSplitPane.setResizeWeight(0.7);
			outterSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			outterSplitPane.setLeftComponent(getInnerSplitPane());
		}
		return outterSplitPane;
	}

	protected JSplitPane getInnerSplitPane() {
		if (innerSplitPane == null) {
			innerSplitPane = new JSplitPane();
			innerSplitPane.setContinuousLayout(true);
			innerSplitPane.setResizeWeight(0.7);
			//innerSplitPane.setPreferredSize(new Dimension(179, 225));
		}
		return innerSplitPane;
	}
}
