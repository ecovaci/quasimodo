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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PositionDialog.java
 *
 * Created on Jan 10, 2011, 11:17:46 PM
 */

package org.chess.quasimodo.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.chess.quasimodo.domain.logic.ChessColor;
import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.domain.logic.Piece;
import org.chess.quasimodo.domain.logic.Position;
import org.chess.quasimodo.domain.validation.PositionValidator;
import org.chess.quasimodo.errors.InvalidFENException;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.gui.model.PositionDialogModel;
import org.chess.quasimodo.message.MessageHandler;
import org.chess.quasimodo.util.LocalIOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.AbstractErrors;
import org.springframework.validation.Validator;


/**
 *
 * @author eugen
 */
@DependsOn ("boardPanel")
@Component ("positionView")
public class PositionDialog extends AbstractDialogForm<PositionDialogModel> implements Definitions {//TODO - refactoring

    /**
	 * 
	 */
	private static final long serialVersionUID = 4556996741031174483L;
	
	private final Logger logger = LoggerFactory.getLogger(PositionDialog.class);
	
	@Autowired
	private EventPublisherAdapter eventPublisher;
	
    @Autowired
    private MessageHandler messageHandler;
    
    @Autowired
    BoardPanel boardPanel;
	
	private Font meridaFontMedium;
	private Font meridaFontBig;
	private Font defaultFont = new Font("Arial", Font.BOLD, 11);
	private Color whiteSquareColor = Color.WHITE;
	private Color blackSquareColor = new Color (88,128,152);
	
	//**** The scaled images ***//
	private Image wpImgScaled;
	private Image bpImgScaled;
	private Image wnImgScaled;
	private Image bnImgScaled;
	private Image wbImgScaled;
	private Image bbImgScaled;
	private Image wrImgScaled;
	private Image brImgScaled;
	private Image wqImgScaled;
	private Image bqImgScaled;
	private Image wkImgScaled;
	private Image bkImgScaled;
	//**************************//
	
	/** Creates new form PositionDialog 
	 * @throws IOException 
	 * @throws FontFormatException */
	@Autowired
    public PositionDialog(@Qualifier ("frame") java.awt.Frame parent) throws FontFormatException, IOException {
        super(parent, true);
        Font tahoma = new Font("Tahoma", Font.PLAIN, 12);//TODO OUT OF HERE
		UIManager.put("Menu.font", tahoma);
		UIManager.put("MenuItem.font", tahoma);
		UIManager.put("Label.font", tahoma);
		UIManager.put("TextField.font",tahoma);
		UIManager.put("ComboBox.font", tahoma);
		UIManager.put("OptionPane.font", tahoma);
	    UIManager.put("Button.font", tahoma);
	    UIManager.put("CheckBox.font", tahoma);
	    UIManager.put("RadioButton.font", tahoma);
        model = new PositionDialogModel();
        
    	Font merida = Font.createFont(Font.TRUETYPE_FONT, 
    			new File (Thread.currentThread().getContextClassLoader()
					      .getResource("images/fonts/MERIFONT.TTF").getFile()));//FIXME
		meridaFontMedium = merida.deriveFont(34f);
		meridaFontBig = merida.deriveFont(38f);
		
        initComponents();
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	dispose();
            }
        });
        setLocationRelativeTo(null);
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        buttonGroup1 = new ButtonGroup();
        boardJPanel = new JPanel() {
			private static final long serialVersionUID = 3822810949491582919L;

			@Override
        	public void paint(Graphics g) {
        		super.paint(g);
        		drawTable (g);
        	}
        };
        boardJPanel.addMouseMotionListener(new MouseMotionAdapter() {
        	@Override
        	public void mouseMoved(MouseEvent e) {
        		boardPanelMouseMoved (e);
        	}
        });
        piecesPanel = new JPanel();
        wkButton = new JButton();
        bkButton = new JButton();
        wqButton = new JButton();
        bqButton = new JButton();
        wrButton = new JButton();
        brButton = new JButton();
        wbButton = new JButton();
        bbButton = new JButton();
        wnButton = new JButton();
        bnButton = new JButton();
        wpButton = new JButton();
        bpButton = new JButton();
        copyButton = new JButton();
        pasteButton = new JButton();
        clearButton = new JButton();
        jPanel1 = new JPanel();
        moveNrSpinner = new JSpinner();
        jPanel2 = new JPanel();
        w00CheckBox = new JCheckBox();
        w000CheckBox = new JCheckBox();
        b00CheckBox = new JCheckBox();
        b000CheckBox = new JCheckBox();
        jPanel3 = new JPanel();
        wRadioButton = new JRadioButton();
        bRadioButton = new JRadioButton();
        buttonGroup1.add(wRadioButton);
        buttonGroup1.add(bRadioButton);
        jPanel4 = new JPanel();
        epTextField = new JTextField();
        doneButton = new JButton();
        cancelButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Set position");
        setName("positionDialog"); 
        setResizable(false);
        
        boardJPanel.setBackground(Color.WHITE);
        boardJPanel.setBorder(BorderFactory.createEtchedBorder());
        boardJPanel.setPreferredSize(new Dimension(405, 405));
        boardJPanel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent e) {
        		boardPanelMousePressed(e);
        	}
        	
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		boardPanelMouseEntered(e);
        	}
        	@Override
        	public void mouseExited(MouseEvent e) {
        		boardPanelMouseExited(e);
        	}
        });

        GroupLayout gl_boardPanel = new GroupLayout(boardJPanel);
        boardJPanel.setLayout(gl_boardPanel);
        gl_boardPanel.setHorizontalGroup(
            gl_boardPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
        );
        gl_boardPanel.setVerticalGroup(
            gl_boardPanel.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGap(0, 401, Short.MAX_VALUE)
        );

        piecesPanel.setBorder(BorderFactory.createEtchedBorder());
        piecesPanel.setDoubleBuffered(true);
        piecesPanel.setPreferredSize(new Dimension(110, 371));

        wkButton.setFont(meridaFontMedium);
        bkButton.setFont(meridaFontMedium);
        wrButton.setFont(meridaFontMedium);
        brButton.setFont(meridaFontMedium);
        wbButton.setFont(meridaFontMedium);
        bbButton.setFont(meridaFontMedium);
        wnButton.setFont(meridaFontMedium);
        bnButton.setFont(meridaFontMedium);
        wqButton.setFont(meridaFontMedium);
        bqButton.setFont(meridaFontMedium);
        wpButton.setFont(meridaFontMedium);
        bpButton.setFont(meridaFontMedium);
        
        wkButton.setPreferredSize(new Dimension(40, 40));
        wkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                wkButtonActionPerformed(evt);
            }
        });
        wkButton.setMargin(new Insets(0, 0, 0, 0));
        wkButton.setText("k");
        piecesPanel.add(wkButton);

        bkButton.setPreferredSize(new Dimension(40, 40));
        bkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bkButtonActionPerformed(evt);
            }
        });
        bkButton.setMargin(new Insets(0, 0, 0, 0));
        bkButton.setText("l");
        piecesPanel.add(bkButton);

        wqButton.setPreferredSize(new Dimension(40, 40));
        wqButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                wqButtonActionPerformed(evt);
            }
        });
        wqButton.setMargin(new Insets(0, 0, 0, 0));
        wqButton.setText("q");
        piecesPanel.add(wqButton);

        bqButton.setPreferredSize(new Dimension(40, 40));
        bqButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bqButtonActionPerformed(evt);
            }
        });
        bqButton.setMargin(new Insets(0, 0, 0, 0));
        bqButton.setText("w");
        piecesPanel.add(bqButton);

        wrButton.setPreferredSize(new java.awt.Dimension(40, 40));
        wrButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                wrButtonActionPerformed(evt);
            }
        });
        wrButton.setMargin(new Insets(0, 0, 0, 0));
        wrButton.setText("r");
        piecesPanel.add(wrButton);

        brButton.setPreferredSize(new java.awt.Dimension(40, 40));
        brButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                brButtonActionPerformed(evt);
            }
        });
        brButton.setMargin(new Insets(0, 0, 0, 0));
        brButton.setText("t");
        piecesPanel.add(brButton);

        wbButton.setPreferredSize(new java.awt.Dimension(40, 40));
        wbButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                wbButtonActionPerformed(evt);
            }
        });
        wbButton.setMargin(new Insets(0, 0, 0, 0));
        wbButton.setText("b");
        piecesPanel.add(wbButton);

        bbButton.setPreferredSize(new java.awt.Dimension(40, 40));
        bbButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bbButtonActionPerformed(evt);
            }
        });
        bbButton.setMargin(new Insets(0, 0, 0, 0));
        bbButton.setText("v");
        piecesPanel.add(bbButton);

        wnButton.setPreferredSize(new java.awt.Dimension(40, 40));
        wnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                wnButtonActionPerformed(evt);
            }
        });
        wnButton.setMargin(new Insets(0, 0, 0, 0));
        wnButton.setText("n");
        piecesPanel.add(wnButton);

        bnButton.setPreferredSize(new java.awt.Dimension(40, 40));
        bnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bnButtonActionPerformed(evt);
            }
        });
        bnButton.setMargin(new Insets(0, 0, 0, 0));
        bnButton.setText("m");
        piecesPanel.add(bnButton);

        wpButton.setPreferredSize(new java.awt.Dimension(40, 40));
        wpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                wpButtonActionPerformed(evt);
            }
        });
        wpButton.setMargin(new Insets(0, 0, 0, 0));
        wpButton.setText("p");
        piecesPanel.add(wpButton);

        bpButton.setPreferredSize(new java.awt.Dimension(40, 40));
        bpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                bpButtonActionPerformed(evt);
            }
        });
        bpButton.setMargin(new Insets(0, 0, 0, 0));
        bpButton.setText("o");
        piecesPanel.add(bpButton);
        
        copyButton.setText("Copy FEN");
        copyButton.setMargin(new Insets(0, 0, 0, 0));
        copyButton.setPreferredSize(new java.awt.Dimension(84, 29));
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });
        piecesPanel.add(copyButton);
        
        pasteButton.setText("Paste FEN");
        pasteButton.setMargin(new Insets(0, 0, 0, 0));
        pasteButton.setPreferredSize(new java.awt.Dimension(84, 29));
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	pasteButtonActionPerformed(evt);
            }
        });
        piecesPanel.add(pasteButton);
        
        clearButton.setText("Clear");
        clearButton.setPreferredSize(new java.awt.Dimension(84, 29));
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        piecesPanel.add(clearButton);

        jPanel1.setBorder(BorderFactory.createTitledBorder("Move number"));

        GroupLayout gl_jPanel1 = new GroupLayout(jPanel1);
        jPanel1.setLayout(gl_jPanel1);
        gl_jPanel1.setHorizontalGroup(
            gl_jPanel1.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel1.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(moveNrSpinner, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );
        gl_jPanel1.setVerticalGroup(
            gl_jPanel1.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel1.createSequentialGroup()
                .addComponent(moveNrSpinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(BorderFactory.createTitledBorder("Castling"));

        w00CheckBox.setText("0-0 White");
        w000CheckBox.setText("0-0-0 White");
        b00CheckBox.setText("0-0 Black");
        b000CheckBox.setText("0-0-0 Black");

        GroupLayout gl_jPanel2 = new GroupLayout(jPanel2);
        jPanel2.setLayout(gl_jPanel2);
        gl_jPanel2.setHorizontalGroup(
            gl_jPanel2.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel2.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_jPanel2.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(w00CheckBox)
                    .addComponent(w000CheckBox)
                    .addComponent(b00CheckBox)
                    .addComponent(b000CheckBox))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        gl_jPanel2.setVerticalGroup(
            gl_jPanel2.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel2.createSequentialGroup()
                .addComponent(w00CheckBox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w000CheckBox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b00CheckBox)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b000CheckBox))
        );

        jPanel3.setBorder(BorderFactory.createTitledBorder("Side to mode"));

        wRadioButton.setText("White");

        bRadioButton.setText("Black");

        GroupLayout gl_jPanel3 = new GroupLayout(jPanel3);
        jPanel3.setLayout(gl_jPanel3);
        gl_jPanel3.setHorizontalGroup(
            gl_jPanel3.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel3.createSequentialGroup()
                .addContainerGap()
                .addGroup(gl_jPanel3.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(bRadioButton)
                    .addComponent(wRadioButton))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        gl_jPanel3.setVerticalGroup(
            gl_jPanel3.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, gl_jPanel3.createSequentialGroup()
                .addComponent(wRadioButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bRadioButton))
        );

        jPanel4.setBorder(BorderFactory.createTitledBorder("En passant"));
       
        epTextField.setColumns(1);

        GroupLayout gl_jPanel4 = new GroupLayout(jPanel4);
        jPanel4.setLayout(gl_jPanel4);
        gl_jPanel4.setHorizontalGroup(
            gl_jPanel4.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel4.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(epTextField, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        gl_jPanel4.setVerticalGroup(
            gl_jPanel4.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(gl_jPanel4.createSequentialGroup()
                .addComponent(epTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        doneButton.setText("Done");
        doneButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boardJPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(piecesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(244, Short.MAX_VALUE)
                .addComponent(doneButton, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
                .addGap(234, 234, 234))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(boardJPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(piecesPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(doneButton))
                .addGap(23, 23, 23))
        );
        defaultValues ();
        this.pack();
    }

    private void wkButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(WHITE_KING);
    }

    private void bkButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(BLACK_KING);
    }

    private void wqButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(WHITE_QUEEN);
    }

    private void bqButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(BLACK_QUEEN);
    }

    private void wrButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(WHITE_ROOK);
    }

    private void brButtonActionPerformed(ActionEvent evt) {
        movingPiece =new Piece(BLACK_ROOK);
    }

    private void wbButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(WHITE_BISHOP);
    }

    private void bbButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(BLACK_BISHOP);
    }

    private void wnButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(WHITE_KNIGHT);
    }

    private void bnButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(BLACK_KNIGHT);
    }

    private void wpButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(WHITE_PAWN);
    }

    private void bpButtonActionPerformed(ActionEvent evt) {
    	movingPiece = new Piece(BLACK_PAWN);
    }

    private void copyButtonActionPerformed(ActionEvent evt) {
    	
    }
    
    private void pasteButtonActionPerformed(ActionEvent evt) {
		try {
			String clipboardContent = LocalIOUtils.getClipboardContents();
			logger.info(clipboardContent);
			if (StringUtils.hasLength(clipboardContent)) {
				Position position = new Position();
				position.load(clipboardContent);
				setModel(position.getModel());
				this.commit();
			}
		} catch (InvalidFENException e) {
			logger.error("", e);
			messageHandler.showErrorMessages(this, "Invalid FEN!");
		} catch (Exception e) {
			logger.error("", e);
			messageHandler.showErrorMessages(this, "Something is wrong with the clipboard, try again.");
		} 
    }
    
    private void clearButtonActionPerformed(ActionEvent evt) {
    	clearBoard();
    }

    private void doneButtonActionPerformed(ActionEvent evt) {
    	updateModel();
    	AbstractErrors result = validateModel();
    	if (result.hasErrors()) {
	    	messageHandler.showErrorMessages(this, result);
    	} else {
    		eventPublisher.publishCommandEvent(evt.getSource(), this, CommandEvent.Command.SET_UP_POSITION);
    		dispose();
    	}
    }

    private void cancelButtonActionPerformed(ActionEvent evt) {
    	dispose();
    }

    private Piece movingPiece;
    
    private Point movingPoint;
    
    private int getOffset (Point point) {
    	int tableSquareSize = Math.round((float)boardJPanel.getWidth() / 8.0f);
    	return point.x / tableSquareSize + (point.y / tableSquareSize * 8);
    }
    
    private void boardPanelMouseMoved(MouseEvent e) {
    	movingPoint = e.getPoint();
    	boardJPanel.repaint();
	}
    
    private void boardPanelMousePressed(MouseEvent e) {
    	if (e.getButton() == MouseEvent.BUTTON1) {
    		int offset = getOffset (e.getPoint());
    		System.out.println("offset " + offset);
    		pieces[offset] = movingPiece;
    	} else {
    	    movingPiece = null;
    	}
    	boardJPanel.repaint();
	}
	
    private boolean mouseOnBoard;
    
    private void boardPanelMouseEntered(MouseEvent e) {
    	mouseOnBoard = true;
    }
    
    private void boardPanelMouseExited(MouseEvent e) {
    	mouseOnBoard = false;
    	boardJPanel.repaint();
    }
    
    private void clearBoard () {
    	Arrays.fill(pieces, null);
    	movingPiece = null;
    	boardJPanel.repaint();
    }
    
    public void reset () {
    	clearBoard();
    	defaultValues();
    }
    
    private void defaultValues () {
    	moveNrSpinner.setValue (new Integer(1));
    	epTextField.setText(null);
        wRadioButton.setSelected(true);
        w00CheckBox.setSelected(false);
        w000CheckBox.setSelected(false);
        b00CheckBox.setSelected(false);
        b000CheckBox.setSelected(false);
    }
    
    private Piece[] pieces = new Piece[64];
    
    private void drawTable(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	//*********** Draw table **************//
    	g2.setFont(meridaFontBig);
    	int tableSquareSize = Math.round((float)boardJPanel.getWidth() / 8.0f);
    	for (int i = 0;i < 8;i++) {
			for (int j = 0;j < 8;j++) {
				if ((i - j) % 2 == 0) {
					g2.setColor(whiteSquareColor);
				} else {
					g2.setColor(blackSquareColor);
				}
				g2.fillRect(i * tableSquareSize, j * tableSquareSize, tableSquareSize, tableSquareSize);
				if (pieces[8 * j + i] != null) {
					g2.setColor(Color.BLACK);
					g2.drawImage(getPieceImage(pieces[j * 8 + i]), i * tableSquareSize + 3, j * tableSquareSize + 2, this);
				}
			}
		}
    	
    	//********** Draw moving piece **********//
    	g2.setColor(Color.BLACK);
    	if (mouseOnBoard && movingPiece != null && movingPoint != null) {
    	    g2.drawImage(getPieceImage(movingPiece), movingPoint.x - 15, movingPoint.y - 18, null);
    	}
    	
    	//********** Draw coordonates ***********//
    	g2.setFont(defaultFont);
    	for (int i = 0;i < 8;i++) {
    		g2.drawString("" + (char)((int)'A' + i), i * tableSquareSize + 2, boardJPanel.getWidth()- 2);
    		g2.drawString("" + (8 - i), 2, 11 + i * tableSquareSize);
    	}
	}

    private JCheckBox b000CheckBox;
    private JCheckBox b00CheckBox;
    private JRadioButton bRadioButton;
    private JButton bbButton;
    private JButton bkButton;
    private JButton bnButton;
    private JPanel boardJPanel;
    private JButton bpButton;
    private JButton bqButton;
    private JButton brButton;
    private ButtonGroup buttonGroup1;
    private JButton cancelButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton clearButton;
    private JButton doneButton;
    private JTextField epTextField;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JSpinner moveNrSpinner;
    private JPanel piecesPanel;
    private JCheckBox w000CheckBox;
    private JCheckBox w00CheckBox;
    private JRadioButton wRadioButton;
    private JButton wbButton;
    private JButton wkButton;
    private JButton wnButton;
    private JButton wpButton;
    private JButton wqButton;
    private JButton wrButton;

	@Override
	public void commit() {
		logger.debug("Model to commit [" + model + "]");
		if (model.getColorToMove().isWhite()) {
			wRadioButton.doClick();
		} else {
			bRadioButton.doClick();
		}
		epTextField.setText(model.getEp());
		pieces = model.getPieces();
		moveNrSpinner.setValue(model.getFullMoveCounter());
		byte castle = model.getCastle();
		w00CheckBox.setSelected((castle & WHITE_CASTLE_KING_SIDE) != NO_CASTLE);
		w000CheckBox.setSelected((castle & WHITE_CASTLE_QUEEN_SIDE) != NO_CASTLE);
		b00CheckBox.setSelected((castle & BLACK_CASTLE_KING_SIDE) != NO_CASTLE);
		b000CheckBox.setSelected((castle & BLACK_CASTLE_QUEEN_SIDE) != NO_CASTLE);
		repaint();
	}

	private double stretch = 0.9;
	
	@PostConstruct
	public void initPieceImage () throws IOException {
		int tableSquareSize = Math.round((float)boardJPanel.getWidth() / 8.0f);
		int imgSize = (int)(tableSquareSize * stretch);
		//scale piece images according to the new size
		wpImgScaled = boardPanel.getWpImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bpImgScaled = boardPanel.getBpImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wnImgScaled = boardPanel.getWnImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bnImgScaled = boardPanel.getBnImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wbImgScaled = boardPanel.getWbImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bbImgScaled = boardPanel.getBbImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wrImgScaled = boardPanel.getWrImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		brImgScaled = boardPanel.getBrImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wqImgScaled = boardPanel.getWqImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bqImgScaled = boardPanel.getBqImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wkImgScaled = boardPanel.getWkImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bkImgScaled = boardPanel.getBkImg().getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
	}
	
	private Image getPieceImage (Piece piece) {
		Piece.Type type = piece.getType();
		if (piece.isWhite()) {
			switch (type) {
			case PAWN:
				return wpImgScaled;
			case KNIGHT:
			    return wnImgScaled;
			case BISHOP:
				return wbImgScaled;
			case ROOK:
				return wrImgScaled;
			case QUEEN:
				return wqImgScaled;
			case KING:
				return wkImgScaled;
			}
		} else {
			switch (type) {
			case PAWN:
				return bpImgScaled;
			case KNIGHT:
			    return bnImgScaled;
			case BISHOP:
				return bbImgScaled;
			case ROOK:
				return brImgScaled;
			case QUEEN:
				return bqImgScaled;
			case KING:
				return bkImgScaled;
			}
		}
		return null;
	}

	@Override
	public void updateModel() {
		model.setColorToMove(wRadioButton.isSelected() ? ChessColor.WHITE : ChessColor.BLACK);
		if (StringUtils.hasLength(epTextField.getText())) {
		    model.setEp(epTextField.getText().trim());
		} 
		model.setPieces(pieces);
		if (moveNrSpinner.getValue() != null && moveNrSpinner.getValue().toString().matches("\\d+")) {
			model.setFullMoveCounter(Integer.parseInt(moveNrSpinner.getValue().toString()));
		}
		byte castle = NO_CASTLE;
		if (w00CheckBox.isSelected()) {
			castle |= WHITE_CASTLE_KING_SIDE;
		}
		if (w000CheckBox.isSelected()) {
			castle |= WHITE_CASTLE_QUEEN_SIDE;
		}
		if (b00CheckBox.isSelected()) {
			castle |= BLACK_CASTLE_KING_SIDE;
		}
		if (b000CheckBox.isSelected()) {
			castle |= BLACK_CASTLE_QUEEN_SIDE;
		}
		model.setCastle(castle);
	}

	@Override
	protected Validator getValidator() {
		return new PositionValidator();
	}
	
}
