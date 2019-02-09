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

import org.chess.quasimodo.annotation.Design;
import org.chess.quasimodo.application.ApplicationContextAdapter;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.concurrent.ErrorHandlingThread;
import org.chess.quasimodo.config.design.Designable;
import org.chess.quasimodo.config.design.Designer;
import org.chess.quasimodo.domain.logic.*;
import org.chess.quasimodo.errors.DefaultErrorHandler;
import org.chess.quasimodo.errors.DesignException;
import org.chess.quasimodo.event.CommandEvent;
import org.chess.quasimodo.event.EventPublisherAdapter;
import org.chess.quasimodo.event.PiecesChangedAware;
import org.chess.quasimodo.message.MessageHandler;
import org.chess.quasimodo.util.LocalIOUtils;
import org.chess.quasimodo.util.SoundUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ErrorHandler;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


@Component ("boardPanel")
public class BoardPanel extends JPanel implements MoveSource, PiecesChangedAware, Designable {
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 6548920074622725274L;
	
	private final Logger logger = LoggerFactory.getLogger(BoardPanel.class);
    
	@Autowired
	private EventPublisherAdapter eventPublisher;
	
	@Autowired
	private ApplicationContextAdapter contextAdapter;
	
	@Autowired
	private QuasimodoContext context;
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private Designer designer;
	
	@Autowired
	private DefaultErrorHandler errorHandler;
	
	@Autowired
	private SoundUtils soundUtils;
	
	private Color borderColor;
	
	private Stroke borderStroke;
	
	private int borderThick;
	
	private int highlightMoveThick = 3;//FIXME - proportional to square size !!!
	
	private  Stroke highlightMoveStroke = new BasicStroke(
			highlightMoveThick, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
	
	
	private Color highlightMoveColor;
	private Color whiteSquareColor;
	private Color blackSquareColor;
	
	private Piece[] pieces;
	
	private String piecesetFilename;
	private String piecesetSize;

	//*** The loaded images - should be relocated ***//
	private Image wpImg;
	private Image bpImg;
	private Image wnImg;
	private Image bnImg;
	private Image wbImg;
	private Image bbImg;
	private Image wrImg;
	private Image brImg;
	private Image wqImg;
	private Image bqImg;
	private Image wkImg;
	private Image bkImg;
	//**************************//
	
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
	
	private int   tableSize;
	private int   tableOffset = 10;
	private int   tableSquareSize;
	private float pieceScale = 0.98f;
	private int   pieceOffset;
	
	private final MoveInfo moveInfo = new MoveInfo();
	
	public BoardPanel() {
		pieces = Board.getInitialPieces();
	}
	
	protected Image getWpImg() {
		return wpImg;
	}

	protected Image getBpImg() {
		return bpImg;
	}

	protected Image getWnImg() {
		return wnImg;
	}

	protected Image getBnImg() {
		return bnImg;
	}

	protected Image getWbImg() {
		return wbImg;
	}

	protected Image getBbImg() {
		return bbImg;
	}

	protected Image getWrImg() {
		return wrImg;
	}

	protected Image getBrImg() {
		return brImg;
	}

	protected Image getWqImg() {
		return wqImg;
	}

	protected Image getBqImg() {
		return bqImg;
	}

	protected Image getWkImg() {
		return wkImg;
	}

	protected Image getBkImg() {
		return bkImg;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void injectDesign () throws DesignException {
		designer.injectDesign(this);
	}
	
	//----------------- Design section
	
	@Design(key="board.border.color")
	public void setBorderColor (Color color) {
		this.borderColor = color;
	}
	
	@Design(key="board.border.stroke")
	public void setBorderStroke (Stroke stroke) {
		borderThick = (int)((BasicStroke)stroke).getLineWidth();
		this.borderStroke = stroke;
	}
	
	@Design(key="board.highlight.color")
	public void setHighlightMoveColor (Color color) {
		this.highlightMoveColor = color;
	}
	
	@Design(key="board.white.square.color")
	public void setWhiteSquareColor (Color color) {
		this.whiteSquareColor = color;
	}
	
	@Design(key="board.black.square.color")
	public void setBlackSquareColor (Color color) {
		this.blackSquareColor = color;
	}
	
	@Design(key={"board.pieceset.filename", "board.pieceset.size"})
	public void setPieceset (String filename, String size) throws IOException {
		loadPiecesetImages(filename, size);
		this.piecesetFilename = filename;
		this.piecesetSize = size;
	}
	
	@Design(key="board.pieceset.filename")
	public String getPiecesetFilename() {
		return piecesetFilename;
	}

	@Design(key="board.pieceset.size")
	public String getPiecesetSize () {
		return piecesetSize;
	}

	@Design(key="board.border.stroke")
	public Stroke getBorderStroke() {
		return borderStroke;
	}

	@Design(key="board.highlight.color")
	public Color getHighlightMoveColor() {
		return highlightMoveColor;
	}

	@Design(key="board.black.square.color")
	public Color getBlackSquareColor() {
		return blackSquareColor;
	}
	
	//------------------------------------
	
	@PostConstruct
	public void initialize () {
		try {
			setUpListeners();
			setMinimumSize(new Dimension(200, 200));
			injectDesign ();
			computeTableSize ();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setUpListeners () {
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				onMouseDragged(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				onMousePressed (e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				onMouseReleased(e);
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				onResize(e);
			}
		});
	}
	
	/**
	 * Calculates the current table size and square size and offset.
	 */
	private void computeTableSize () {
		tableSize = Math.min (this.getWidth(), this.getHeight()) - 2 * (tableOffset + borderThick);
		tableSquareSize =  Math.round(((float)tableSize) / 8.0f);
		pieceOffset = Math.round((1f - pieceScale) * tableSquareSize * 0.5f);
	}
	
	private void onResize (ComponentEvent e) {
		//the panel's size is changing, 
		//let's recompute the table/square size.
		computeTableSize ();
		int imgSize = (int)(tableSquareSize * pieceScale);
		//scale piece images according to the new size
		wpImgScaled = wpImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bpImgScaled = bpImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wnImgScaled = wnImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bnImgScaled = bnImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wbImgScaled = wbImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bbImgScaled = bbImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wrImgScaled = wrImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		brImgScaled = brImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wqImgScaled = wqImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bqImgScaled = bqImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		wkImgScaled = wkImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		bkImgScaled = bkImg.getScaledInstance(imgSize, imgSize, java.awt.Image.SCALE_SMOOTH);
		
		if (lastMoveFromSquare > -1) {
			setStartHighlighting(lastMoveFromSquare);
		}
		if (lastMoveToSquare > -1) {
			setEndHighlighting(lastMoveToSquare);
		}
		//now repaint the panel
		repaint();
	}
	
	private void onMousePressed (MouseEvent e) {
		if (context.isIgnoreUserInput()) {
			return;
		}
		for (int i = 0;i < 8;i++) {
			for (int j = 0;j < 8;j++) {
				if (pieces[j * 8 + i] != null) {
					if (e.getX() > tableOffset + borderThick + pieceOffset + i * tableSquareSize
							&& e.getX() < tableOffset + borderThick - pieceOffset + (i + 1) * tableSquareSize) {
						if (e.getY() > tableOffset + borderThick + pieceOffset + j * tableSquareSize
								&& e.getY() < tableOffset + borderThick - pieceOffset + (j + 1) * tableSquareSize) {
							setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
							moveInfo.from = j * 8 + i;
							moveInfo.x = e.getX();
							moveInfo.y = e.getY();
							moveInfo.xoffset = e.getX() - (tableOffset + borderThick + pieceOffset + i * tableSquareSize);
							moveInfo.yoffset = e.getY() - (tableOffset + borderThick + pieceOffset + j * tableSquareSize);
							startHighlighting(moveInfo.from);
							repaint();
						}
					}
				}
			}
		}
	}
	
	private void onMouseDragged(MouseEvent e) {
		if (moveInfo.from > -1) {
			if (e.getX() > tableOffset + borderThick
					&& e.getX() < tableOffset + borderThick + 8 * tableSquareSize) {
				if (e.getY() > tableOffset + borderThick
						&& e.getY() < tableOffset + borderThick + 8 * tableSquareSize) {
					moveInfo.x = e.getX();
					moveInfo.y = e.getY();
					repaint();
				}
			}
		}
	}
	
	private void onMouseReleased (MouseEvent e) {
		//restore cursor
		setCursor(Cursor.getDefaultCursor());
		outter:
		for (int i = 0;i < 8;i++) {
			for (int j = 0;j < 8;j++) {
				if (e.getX() > tableOffset + borderThick + pieceOffset + i * tableSquareSize
						&& e.getX() < tableOffset + borderThick - pieceOffset + (i + 1) * tableSquareSize) {
					if (e.getY() > tableOffset + borderThick + pieceOffset + j * tableSquareSize
							&& e.getY() < tableOffset + borderThick - pieceOffset + (j + 1) * tableSquareSize) {
						if (moveInfo.from > -1 && moveInfo.from != j * 8 + i) {
							//make the move on board
							pieces[j * 8 + i] = pieces[moveInfo.from];
							pieces[moveInfo.from] = null;
							//save the move coordonates
							final int moveFrom = moveInfo.from;
							final int moveTo = j * 8 + i;
							//cancel piece moving state
							stopMoving ();
							//highlight requested move
							highlightMove(moveFrom, moveTo);
							
							//request move to system 
							if (isPlayerBinded())  {
								requestMove(moveFrom, moveTo);
							} else {
								//this might be a time expensive operation!!!
								new RequestMoveThread(moveFrom, moveTo, errorHandler).start();
							}
							if (context.getCurrentGame().isSoundRequired()) {
								soundUtils.playAudio();//FIXME - for testing only
							}
						}
						break outter;
					}
				}
			}
		}
		if (moveInfo.isMoving()) {
		    stopMoving ();
		}
	}
	
	private void highlightMove (final int moveFrom, final int moveTo) {
		setStartHighlighting(moveFrom);
		setEndHighlighting(moveTo);
	}
	
	private void requestMove (final int moveFrom, final int moveTo) {
		if (pieces[moveTo].belongsTo(player.getColor())) {
			Move move = player.requestMove(moveFrom, moveTo);
			logger.debug("move " + move);
			if (move != null) {
				if (move.isPromote()) {
					move.setPromotedPiece(getPromotedPiece());
				}
				//context.setIgnoreUserInput(true);
				if (player.makeMove(move)) {
					setEndHighlighting(moveTo);
				}
			} else {
				handleMoveError("Illegal move!");
			}
		} else {
			handleMoveError("It is " + player.getColor().asString() + " turn!");
		}
		repaint();
	}
	
	private void handleMoveError (String message) {
		messageHandler.showErrorMessages(this, message);
		clearCurrentHighlighting();
		//rollback move
		pieces = context.getCurrentGame().getPieces();
		repaint();
	}
	
	private Piece getPromotedPiece () {
		PromotionDialog dialog = player.isWhite() ? contextAdapter.getWhitePromotionDialog() 
				: contextAdapter.getBlackPromotionDialog();
		dialog.showView();
		dialog.updateModel();
		return new Piece(dialog.getModel().getPiece());
	}
	
	private void stopMoving () {
		moveInfo.reset();
		repaint();
	}
	
	//**************************** Play moves ****************************//
	private int xplay = -1;
	private int yplay = -1;
	private Image playingImage;
	
	public synchronized void playMove (final Move move) {
		Animator animator = new Animator(move);
		animator.prepare();
		animator.start (getPieceLocation(move.from), getPieceLocation(move.to));
		if (context.getCurrentGame().isSoundRequired()) {
			soundUtils.playAudio();//FIXME - for testing only
		}
	}
	
	private Point getPieceLocation  (int offset) {
		Point location = new Point();
		location.x = tableOffset + borderThick + pieceOffset + (offset & 7) * tableSquareSize;
		location.y = tableOffset + borderThick + pieceOffset + (offset >> 3) * tableSquareSize;
		return location;
	}
	//********************************************************************//
	
	//************************ Highlight move squares ********************//
	private int lastMoveFromSquare = -1;
	private int lastMoveToSquare = -1;
	
	private	Point moveStart;
	private	Point moveEnd;
	
	private Point getSquareLocation  (int offset) {
		Point location = new Point();
		location.x = tableOffset + borderThick + (offset & 7) * tableSquareSize;
		location.y = tableOffset + borderThick + (offset / 8) * tableSquareSize;
		return location;
	}
	
	private void clearCurrentHighlighting () {
		moveStart = null;
		moveEnd   = null;
		lastMoveFromSquare = -1;
		lastMoveToSquare   = -1;
	}

	private void startHighlighting (int from) {
		clearCurrentHighlighting();
		setStartHighlighting(from);
	}
	
	private void setStartHighlighting (int from) {
		moveStart = getSquareLocation(lastMoveFromSquare = from);
	}
	
    private void setEndHighlighting (int to) {
    	moveEnd = getSquareLocation (lastMoveToSquare = to);
	}
	//********************************************************************//
	
	private void loadPiecesetImages (String filename, String size) throws IOException {
		Map<String, Image> piecesetImages =  LocalIOUtils.loadPiecesetImages (
				filename, size);
		wpImg = piecesetImages.get("wp");
		bpImg = piecesetImages.get("bp");
		wnImg = piecesetImages.get("wn");
		bnImg = piecesetImages.get("bn");
		wbImg = piecesetImages.get("wb");
		bbImg = piecesetImages.get("bb");
		wrImg = piecesetImages.get("wr");
		brImg = piecesetImages.get("br");
		wqImg = piecesetImages.get("wq");
		bqImg = piecesetImages.get("bq");
		wkImg = piecesetImages.get("wk");
		bkImg = piecesetImages.get("bk");
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawTable ((Graphics2D)g);
	}
	
	private void drawTable(Graphics2D g2) {
		//************* antialiasing on  *************//
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//********************************************//
		//**************** Draw border ***************//
		g2.setColor(borderColor);
		g2.setStroke(borderStroke);
		g2.drawRect(tableOffset + borderThick / 2, tableOffset + borderThick / 2, 
				tableSquareSize * 8 + borderThick, tableSquareSize * 8 + borderThick);
		//************* antialiasing off *************//
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		//********************************************//
		//************** Draw table ******************//
		for (int i = 0;i < 8;i++) {
			for (int j = 0;j < 8;j++) {
				if ((i - j) % 2 == 0) {
					g2.setColor(whiteSquareColor);
				} else {
					g2.setColor(blackSquareColor);
				}
				g2.fillRect(tableOffset + borderThick + i * tableSquareSize, 
						tableOffset + borderThick + j * tableSquareSize, tableSquareSize, tableSquareSize);
				if (pieces[j * 8 + i] != null && (j * 8 + i) != moveInfo.from) {
					g2.drawImage(getPieceImage(pieces[j * 8 + i]), 
							tableOffset + borderThick + pieceOffset + i * tableSquareSize, 
							tableOffset + borderThick + pieceOffset + j * tableSquareSize, null);
				}
			}
		}
		//********************************************//
		//********** Highlight move squares **********//
		g2.setColor(highlightMoveColor);
		g2.setStroke(highlightMoveStroke);
		if (moveStart != null) {
			g2.drawRect (moveStart.x, moveStart.y, tableSquareSize, tableSquareSize);
		}
		if (moveEnd != null) {
			g2.drawRect (moveEnd.x, moveEnd.y, tableSquareSize, tableSquareSize);
		}
		//******************************************************//
		
		//**************** Draw the moving piece ***************//
		if (playingImage != null && xplay > -1 && yplay > -1) {//animated move
		    g2.drawImage(playingImage, xplay, yplay, null);
		} else	if (moveInfo.isMoving()) {//user move
			g2.drawImage(getPieceImage(pieces[moveInfo.from]), moveInfo.x - moveInfo.xoffset, moveInfo.y - moveInfo.yoffset, null);
		} 
		//******************************************************//
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

	private Player player;
	
	@Override
	public boolean isPlayerBinded () {
		return player != null;
	}
	
	public Player getBindedPlayer () {
		return player;
	}
	
	@Override
	public void bindPlayer(Player player) {
		Assert.notNull(player, "Player cannot be null");
		this.player = player;
		logger.info("Binded on board: " + player);
	}

	@Override
	public void removePlayer() {
		this.player = null;
	}

	public void reset () {
		clearCurrentHighlighting();
		piecesChanged(Board.getInitialPieces());
		repaint();
	}
	
	/* (non-Javadoc)
	 * @see org.chess.quasimodo.gui.PiecesChangedAware#piecesAfterMove(org.chess.quasimodo.domain.logic.Piece[])
	 */
	public void piecesChanged(Piece[] pieces) {
		this.pieces = pieces;
	}

	@Override
	public void myTurn() {
		logger.info("My turn on board: " + player);
		context.setIgnoreUserInput(false);
		context.getCurrentGame().getClock().onPlayerReady();
	}
	
	@Override
	public void positionChanged() {
		clearCurrentHighlighting();
		repaint();
	}
	
	@Override
	public void refreshDesign() {
		repaint();
	}
	
	private class RequestMoveThread extends ErrorHandlingThread {
		int from;
		int to;
		
		public RequestMoveThread(int from, int to, ErrorHandler errorHandler) {
			super(errorHandler);
			this.from = from;
			this.to = to;
		}
		
		@Override
		public void doWork() throws Exception {
			if (!isPlayerBinded()) {
				//no active game, let's start a new one
				eventPublisher.publishEvent(new CommandEvent(this, CommandEvent.Command.NEW_DEFAULT_GAME));
			}
			requestMove(from, to);
		}
	}
	
	private class MoveInfo {
		int from = -1;
		int x    = -1;
		int y    = -1;
		
		int xoffset = -1;
		int yoffset = -1;
		
		void reset () {
			from = -1;
			x = -1;
			y = -1;
			xoffset = -1;
			yoffset = -1;
		}
		
		boolean isMoving () {
			return from > -1 && x > -1 && y > -1;
		}
	}

	private class Animator {
		private final int                      animationDelay = 15;//FIXME - config
		private Timer                          timer = new Timer();
		private Move                           playingMove;
		private double                         step = 12.0;
		private double                         xstart;
		private double                         ystart;
		private double                         alpha;
		private double                         betha;
		
		/**
		 * @param playingMove
		 */
		Animator(Move playingMove) {
			this.playingMove = playingMove;
		}

		void prepare () {
			startHighlighting(playingMove.from);
			playingImage = getPieceImage(pieces[playingMove.from]);
			pieces[playingMove.from] = null;
		}
		
		void start (Point start, Point end) {
			logger.debug("Starting animation ...");
			if (!start.equals(end)) {
				if (Math.abs(start.x - end.x) >= Math.abs(start.y - end.y)) {
					step = Math.abs(start.x - end.x) * step 
					/ Math.sqrt(Math.pow(Math.abs(start.x - end.x), 2) 
							+ Math.pow(Math.abs(start.y - end.y), 2));
					animateX(start, end);
				} else {
					step = Math.abs(start.y - end.y) * step 
					/ Math.sqrt(Math.pow(Math.abs(start.x - end.x), 2) 
							+ Math.pow(Math.abs(start.y - end.y), 2));
					animateY(start, end);
				}
				//lock the table until animation finishes 
				synchronized (this) {
					try {
						logger.debug("Animation before wait");
						this.wait();
						logger.debug("Animation after wait");
					} catch (InterruptedException e) {
						logger.error("Error on interrupting current thread", e);
					}
				}
			}
		}
		
		void animateX (final Point start, final Point end) {
			System.out.println("Animate X");
			alpha = ((double)(end.y - start.y))/((double)(end.x - start.x));
			betha = start.y - start.x * alpha;
			xstart = start.x;
			TimerTask task;
			if (start.x < end.x) {
				task = new TimerTask() {
					@Override
					public void run() {
						xstart += step;
						if (xstart < end.x) {
							xplay = (int)xstart;
							yplay = (int)(alpha * xstart + betha);
							repaint();
						} else {
							stop();
						}
					}
				};
			} else {
				task = new TimerTask() {
					@Override
					public void run() {
						xstart -= step;
						if (xstart > end.x) {
							xplay = (int)xstart;
							yplay = (int)(alpha * xstart + betha);
							repaint();
						} else {
							stop();
						}
					}
				};
			}
			timer.scheduleAtFixedRate(task, 0, animationDelay);
		}
		
		void animateY (final Point start, final Point end) {
			System.out.println("Animate Y");
			alpha = ((double)(end.x - start.x))/((double)(end.y - start.y));
			betha = start.x - start.y * alpha;
			ystart  = start.y;
			TimerTask task;
			if (start.y < end.y) {
				task = new TimerTask() {
					@Override
					public void run() {
						ystart += step;
						if (ystart < end.y) {
							xplay = (int)(alpha * ystart + betha);
							yplay = (int)ystart;
							repaint();
						} else {
							stop();
						}
					}
				};
			} else {
				task = new TimerTask() {
					@Override
					public void run() {
						ystart -= step;
						if (ystart > end.y) {
							xplay = (int)(alpha * ystart + betha);
							yplay = (int)ystart;
							repaint();
						} else {
							stop();
						}
					}
				};
			}
			timer.scheduleAtFixedRate(task, 0, animationDelay);
		}
		
		void stop () {
			timer.cancel();
			playingImage = null;
			xplay = -1;
			yplay = -1;
			setEndHighlighting(playingMove.to);
			piecesChanged(context.getCurrentGame().getPieces());
			repaint();
			//animation done, now unlock the table
			synchronized (this) {
				logger.debug("Animation before notify");
				this.notify();
				logger.debug("Animation after notify");
			}
			logger.debug("Animation stoppped");
		}
	}
}
