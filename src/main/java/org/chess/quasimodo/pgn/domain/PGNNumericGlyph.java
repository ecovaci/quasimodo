package org.chess.quasimodo.pgn.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.chess.quasimodo.errors.PGNGenerateException;
import org.chess.quasimodo.errors.PGNParseException;
import org.chess.quasimodo.util.Utils;

public class PGNNumericGlyph implements PGNEntity<Integer>, PGNParsable {
	
	public static final Pattern PATTERN = Pattern.compile("\\$\\d+");
	
	public static final Pattern START_WITH = Pattern.compile("^\\$\\d+");
	
	public static final Pattern CONTENT = Pattern.compile("(?<=\\$)\\d+");
	
    private Integer glyph;
    
    private String text;
	
	public PGNNumericGlyph (String text) {
    	this.text = text;
    }
	
	public PGNNumericGlyph(Integer glyph) {
		this.glyph = glyph;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toPGN() {
		if (this.glyph == null) {
			throw new PGNGenerateException("glyph value has not been set");
		} else if (this.glyph < 1) {
			throw new PGNGenerateException("Invalid glyph value [" + this.glyph + "]");
		}
		return "$" + this.glyph;
	}

	@Override
	public void parse() {
		Matcher matcher = CONTENT.matcher(this.text);
		if (matcher.find()) {
			this.glyph = Integer.parseInt(matcher.group());
		} else {
			throw new PGNParseException ("Wrong glyph format [" + this.text + "]");
		}
	}

	@Override
	public Integer getContent() {
		return glyph;
	}
	
	public static String stripAll (String text) throws PGNParseException {
		return Utils.stripRegex(text, PATTERN, " ");
	}

	public static String parseNext (final StringBuffer text) {
		String glyph = null;
		Matcher matcher = START_WITH.matcher(text);
		if (matcher.find()) {
			glyph = matcher.group();
			Utils.deleteSequence(text, matcher.start(), matcher.end());
		}
		return glyph;
	}

	@Override
	public String toString() {
		return "PGNNumericGlyph [glyph=" + glyph + ", text=" + text + "]";
	}
	
}
