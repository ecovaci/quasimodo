package org.chess.quasimodo.engine;

import java.util.Arrays;

import org.chess.quasimodo.engine.model.BestMove;
import org.chess.quasimodo.engine.model.Info;
import org.chess.quasimodo.engine.model.Option;
import org.chess.quasimodo.errors.EngineException;
import org.chess.quasimodo.event.AbstractOutputListener;
import org.chess.quasimodo.event.EngineOutputAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Scope ("prototype")
@Component ("analyzerEngine")
public class AnalyzerEngine  extends AbstractEngine {
	private int multipv = 1;
	
	public AnalyzerOutput analyze (String fen, int movetime, int multipv) {
		if (this.multipv != multipv) {
			getEngineHandler().setOption(Option.MULTIPV, String.valueOf(multipv));
			this.multipv = multipv;
		}
		return analyze(fen, movetime);
	}

	public AnalyzerOutput analyze (String fen, int movetime) {
		getEngineHandler().positionFen(fen);
		AnalyzerOutput output = new AnalyzerOutput();
		getEngineHandler().goInfinite(movetime, output);
		return output;
	}
	
	public int getMultipv() {
		return multipv;
	}

    /**
     * Switches to analysis mode.
     */
	public void analyzeMode () {
		getEngineHandler().setOption(Option.ANALYSE_MODE, "true");
	}
	
	@Override
	public void registerEngineOutputListener(EngineOutputAware engineOutputAware) {}
	
	public class AnalyzerOutput extends AbstractOutputListener {
		private BestMove    bestMove;
		private Info[]      infoWithPV;
		
		AnalyzerOutput() {
			this.infoWithPV = new Info[multipv];
		}

		public BestMove getBestMove() {
			return bestMove;
		}

		public String getUCIBestMove () {
			if (bestMove != null) {
				return bestMove.move;
			}
			return null;
		}
		
		public Info[] getInfoWithPV() {
			return infoWithPV;
		}

		public boolean isMultiPv() {
			return multipv > 1;
		}

		public boolean isBestMove (String move) {
			if (bestMove != null && bestMove.move != null) {
				return bestMove.move.equals(move);
			}
			return false;
		}
		
		public boolean isMateFound () {
			return infoWithPV != null && infoWithPV[0].mate != null;
		}
		
		public int getPVCp () {
			return Integer.parseInt(infoWithPV[0].cp);
		}
		
		public int getPVMate () {
			return Integer.parseInt(infoWithPV[0].mate);
		}
		
		@Override
		public void onReceiveBestMove(BestMove bestMove) throws EngineException {
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<< best move " + bestMove);
			this.bestMove = bestMove;
			getEngineHandler().stopCommandAndListen();
			
		}

		@Override
		public void onReceiveInfo(Info info) {
			if (StringUtils.hasLength(info.pv)) {
				if (multipv > 1) {
					infoWithPV[Integer.valueOf(info.multipv) - 1] = info;
				} else {
					infoWithPV[0] = info;
				}
			} 
		}

		@Override
		public String toString() {
			return "AnalyzerOutput [bestMove=" + bestMove + ", infoWithPV="
					+ Arrays.toString(infoWithPV) + "]";
		}
	}
}
