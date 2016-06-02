package chess.ai;

import chess.core.Chessboard;
import chess.core.Move;

public class AlphaBeta extends Searcher {
	
	@Override
	public MoveScore findBestMove(Chessboard board, BoardEval eval, int depth) {
		setup(board, eval, depth);
		
		int alpha = -1;
		int beta = 1;
		
		MoveScore result = evalMoves(board, eval, depth, alpha, beta);
		tearDown();
		return result;
	}
	
	MoveScore evalMoves(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		MoveScore best = null;
		for (Move m: board.getLegalMoves()) {
			Chessboard next = generate(board, m);
			MoveScore result = new MoveScore(-evalBoard(next, eval, depth - 1, -beta, -alpha), m);
			if (best == null || result.getScore() > best.getScore()) {
				best = result;
				alpha = best.getScore();
			}
			
			if( alpha > beta){
				return best;
			}
		}
		return best;
	}	
	
	int evalBoard(Chessboard board, BoardEval eval, int depth, int alpha, int beta) {
		if (!board.hasKing(board.getMoverColor()) || board.isCheckmate()) {
			return -eval.maxValue();
		} else if (board.isStalemate()) {
			return 0;
		} else if (depth == 0) {
			return evaluate(board, eval);
		} else {
			return evalMoves(board, eval, depth, alpha, beta).getScore();
		}
	}
}
