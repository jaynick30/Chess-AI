package chess.ai;

import java.util.List;

import chess.core.Chessboard;
import chess.core.Move;

public class AdvancedSearch extends Searcher {
	
	@Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
		setup(board, eval, depth);
		
		int alpha = -1;
		int beta = 1;
		
		MoveScore result = evalMoves(board, eval, depth, alpha, beta, 0);
		tearDown();
		return result;
	}
	
	int quiescentSearch(Chessboard board, BoardEval eval, int alpha, int beta, int flag){
		int evalTest = evalBoard(board, eval, 1, alpha, beta, flag);
		//System.out.println("qui searching");
		if(evalTest >= beta){
			return beta;
		}
		
		if(evalTest > alpha){
			alpha = evalTest;
		}
		
		List<Move> captureMoves = board.getLegalMoves();
		
		for(Move m: captureMoves){
			if(m.captures()){
				//System.out.println("For each capture");
				Chessboard next = generate(board, m);
				int eval2 = quiescentSearch(next, eval, -beta, -alpha, flag);
				
				if(eval2 >= beta){
					return beta;
				}
				if(eval2 > alpha){
					return alpha;
				}
			}
		}		
		
		return alpha;
	}
	
	MoveScore evalMoves(Chessboard board, BoardEval eval, int depth, int alpha, int beta, int flag) {
		MoveScore best = null;
		for (Move m: board.getLegalMoves()) {
			Chessboard next = generate(board, m);
			MoveScore result = new MoveScore(-evalBoard(next, eval, depth - 1, -beta, -alpha, flag), m);
			if (best == null || result.getScore() > best.getScore()) {
				best = result;
				alpha = best.getScore();
			}
			
			if( alpha > beta){
				if(best.getMove().captures()){
					if(flag != 1){
						//System.out.println("quiescent searching");
						alpha = quiescentSearch(next, eval, alpha, beta, 1);
						if(alpha > beta){
							return best;
						}
					}
				}
				if(alpha - beta > 10){
					if(flag != 1){
						//System.out.println("extended searching");
						MoveScore lookAhead = new MoveScore(evalBoard(next, eval, 2, alpha, beta, 1), best.getMove());
						if(lookAhead.getScore() > best.getScore()){
							best = lookAhead;
							alpha = best.getScore();
						}
						return best;
					}
				}
				else{
					return best;
				}
			}
		}
		return best;
		
	}	
	
	int evalBoard(Chessboard board, BoardEval eval, int depth, int alpha, int beta, int flag) {
		if (!board.hasKing(board.getMoverColor()) || board.isCheckmate()) {
			return -eval.maxValue();
		} else if (board.isStalemate()) {
			return 0;
		} else if (depth == 0) {
			return evaluate(board, eval);
		} else {
			return evalMoves(board, eval, depth, alpha, beta, flag).getScore();
		}
	}
}
