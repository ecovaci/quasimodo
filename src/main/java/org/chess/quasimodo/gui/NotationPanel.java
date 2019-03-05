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

import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.domain.logic.Notation;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.event.NotationChangedAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import java.awt.*;


@SuppressWarnings("serial")
@Component("notationPanel")
public class NotationPanel extends JPanel implements NotationChangedAware {

    private final Logger logger = LoggerFactory.getLogger(NotationPanel.class);

    @Autowired
    private QuasimodoContext context;

    @Autowired
    private EventPublisherAdapter eventPublisher;

    @Autowired
    private ApplicationContextAdapter contextAdapter;

    private JScrollPane scrollPane;

    private JTextPane notationTextPane;
    private JTextPane materialTextPane;

    public NotationPanel() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 0));
        add(getMaterialTextPane(), BorderLayout.SOUTH);
        add(getScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBorder(null);
            scrollPane.setViewportView(getNotationTextPane());
        }
        return scrollPane;
    }

    private JTextPane getMaterialTextPane() {
        if (materialTextPane == null) {
            materialTextPane = new JTextPane();
            materialTextPane.setEditable(false);
            materialTextPane.setPreferredSize(new Dimension(6, 50));
            materialTextPane.setRequestFocusEnabled(false);
            materialTextPane.setFocusable(false);
        }
        return materialTextPane;
    }

    protected JTextPane getNotationTextPane() {
        if (notationTextPane == null) {
            notationTextPane = new JTextPane();
            notationTextPane.setEditable(false);
            notationTextPane.addHyperlinkListener(new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == EventType.ACTIVATED) {
                        if (e.getSource() instanceof Notation) {
                            Highlighter highlighter = notationTextPane.getHighlighter();
                            //clear existent selections
                            highlighter.removeAllHighlights();

                            Element sourceElement = e.getSourceElement();
                            //highlight the clicked move
                            try {
                                highlighter.addHighlight(sourceElement.getStartOffset(), sourceElement.getEndOffset(),
                                        new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY));

                            } catch (BadLocationException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            notationTextPane.setCaretPosition(sourceElement.getStartOffset());
                        } else if (e.getURL() != null) {
                            if (context.hasActiveGame()) {
                                CommandEvent goToMove = new CommandEvent(this,
                                        CommandEvent.Command.GO_TO_MOVE);
                                goToMove.addParameter("move", e.getURL().getHost());
                                eventPublisher.publishEvent(goToMove);
                            }
                        }
                    }
                }
            });
			
			/*notationTextPane.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					System.out.println("Dispatch event");
					contextAdapter.getMainFrame().dispatchEvent(e);
				}
			});*/

            notationTextPane.setContentType("text/html");
        }
        return notationTextPane;
    }

    /* (non-Javadoc)
     * @see org.chess.quasimodo.gui.NotationChangedAware#setNotationContent(java.lang.String)
     */
    public void notationContentChanged(String content, HyperlinkEvent event) {
        notationTextPane.setText(content);
        notationTextPane.requestFocusInWindow();
        notationTextPane.fireHyperlinkUpdate(event);
    }
}
