package org.chess.quasimodo.pgn.domain;

public class Opening {
    private Long id;
    private String code;
    private String name;
    private String variation;
    private String moves;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVariation() {
		return variation;
	}
	public void setVariation(String variation) {
		this.variation = variation;
	}
	public String getMoves() {
		return moves;
	}
	public void setMoves(String moves) {
		this.moves = moves;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Opening [id=" + id + ", code=" + code + ", name="
				+ name + ", variation=" + variation + ", moves=" + moves
				+ "]";
	}
}
