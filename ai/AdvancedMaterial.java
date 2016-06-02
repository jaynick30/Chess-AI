package chess.ai;

import java.util.ArrayList;
import java.util.EnumMap;
import chess.core.BoardSquare;
import chess.core.ChessPiece;
import chess.core.Chessboard;


public class AdvancedMaterial implements BoardEval {
	final static int MAX_VALUE = 1000;
	private EnumMap<ChessPiece,Integer> pieceValues = new EnumMap<ChessPiece,Integer>(ChessPiece.class);
	private EnumMap<BoardSquare,Integer> positionValues = new EnumMap<BoardSquare,Integer>(BoardSquare.class);
	
	
	private ArrayList<BoardSquare> pawnGoals = new ArrayList<BoardSquare>();
	
	public AdvancedMaterial() {
		pieceValues.put(ChessPiece.BISHOP, 30);
		pieceValues.put(ChessPiece.KNIGHT, 30);
		pieceValues.put(ChessPiece.PAWN, 10);
		pieceValues.put(ChessPiece.QUEEN, 100);
		pieceValues.put(ChessPiece.ROOK, 50);
		pieceValues.put(ChessPiece.KING, MAX_VALUE);
		
		positionValues.put(BoardSquare.D5, 10);
		positionValues.put(BoardSquare.D4, 10);
		positionValues.put(BoardSquare.E5, 10);
		positionValues.put(BoardSquare.E4, 10);
		
		positionValues.put(BoardSquare.C6, 5);
		positionValues.put(BoardSquare.D6, 5);
		positionValues.put(BoardSquare.E6, 5);
		positionValues.put(BoardSquare.F6, 5);
		positionValues.put(BoardSquare.C5, 5);
		positionValues.put(BoardSquare.F5, 5);
		positionValues.put(BoardSquare.C4, 5);
		positionValues.put(BoardSquare.F4, 5);
		positionValues.put(BoardSquare.C3, 5);
		positionValues.put(BoardSquare.D3, 5);
		positionValues.put(BoardSquare.E3, 5);
		positionValues.put(BoardSquare.F3, 5);
		
		pawnGoals.add(BoardSquare.A1);
		pawnGoals.add(BoardSquare.B1);
		pawnGoals.add(BoardSquare.C1);
		pawnGoals.add(BoardSquare.D1);
		pawnGoals.add(BoardSquare.E1);
		pawnGoals.add(BoardSquare.F1);
		pawnGoals.add(BoardSquare.G1);
		pawnGoals.add(BoardSquare.H1);
		pawnGoals.add(BoardSquare.A8);
		pawnGoals.add(BoardSquare.B8);
		pawnGoals.add(BoardSquare.C8);
		pawnGoals.add(BoardSquare.D8);
		pawnGoals.add(BoardSquare.E8);
		pawnGoals.add(BoardSquare.F8);
		pawnGoals.add(BoardSquare.G8);
		pawnGoals.add(BoardSquare.H8);
		
		// this doesn't work?
		//pawns.addAll(BoardSquare.A1, BoardSquare.B1, BoardSquare.C1, BoardSquare.D1, BoardSquare.E1, BoardSquare.F1, BoardSquare.G1, BoardSquare.H1, 
				//BoardSquare.A8, BoardSquare.B8, BoardSquare.C8, BoardSquare.D8, BoardSquare.E8, BoardSquare.F8, BoardSquare.G8, BoardSquare.H8);
		
	}
	

	@Override
	public int eval(Chessboard board) {
		int total = 0;
		for (BoardSquare s: board.allPieces()) {
			ChessPiece type = board.at(s);
			if (pieceValues.containsKey(type)) {
				if (board.colorAt(s).equals(board.getMoverColor())) {
					if(type.equals(ChessPiece.PAWN) && pawnGoals.contains(s)){
						total+= 2;
					}
					if(positionValues.containsKey(s)){
						if(type.equals(ChessPiece.KNIGHT)){
							total+= 5;
						}
						total += positionValues.get(s);
					}
					total += pieceValues.get(type);
				} else {
					if(type.equals(ChessPiece.KNIGHT)){
						total -= 5;
					}
					total -= pieceValues.get(type);
				}
			}
		}
		return total;
	}

	@Override
	public int maxValue() {
		return MAX_VALUE;
	}
	
	public boolean hasValue(ChessPiece piece) {
		return pieceValues.containsKey(piece);
	}
	
	public int valueOf(ChessPiece piece) {
		return pieceValues.get(piece);
	}
}
