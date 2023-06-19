package lexicalanalyzer.compiler.lexical;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import lexicalanalyzer.compiler.exception.LexicalException;

public class Scanner {
	private char[] content;
	private int state;
	private int position;
	
	public Scanner(String filename) {
		try {
			String textContent;
			textContent = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
			System.out.println("DEBUGING -----------");
			System.out.println(textContent);
			System.out.println("FINISHED -----------");
			
			content = textContent.toCharArray();
			position = 0;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Token nextToken() {
		if (isEOF()) {
			return null;
		}
		
		Token token = new Token();
		state = 0;
		char currentChar;
		String term = "";

		while (true) {
			currentChar = nextChar();
			
			switch (state) {
			case 0:
				if (isChar(currentChar)) {
					state = 1;
					term += currentChar;
				}
				else if (isDigit(currentChar)) {
					state = 3;
					term += currentChar;
				}
				else if (isSpace(currentChar)) {
					state = 0;
				}
				else if (isOperator(currentChar)) {
					state = 5;
				}
				else {
					throw new LexicalException("Unrecognized SYMBOL: " + term + currentChar);
				}
				break;
			case 1:
				if (isChar(currentChar) || isDigit(currentChar)) {
					state = 1;
					term += currentChar;
				}
				else if (isSpace(currentChar) || isOperator(currentChar)){
					state = 2;
				}
				else {
					throw new LexicalException("Malformed Identifier: " + term + currentChar);
				}
				break;
			case 2:
				token = new Token();
				token.setType(Token.TK_IDENTIFIER);
				token.setText(term);
				back();
				
				return token;
				
			case 3: 
				if (isDigit(currentChar)) {
					state = 3;
					term += currentChar;
				}
				else if (!isChar(currentChar)) {
					state = 4;
				} 
				else {
					throw new LexicalException("Unrecognized NUMBER: " + term + currentChar);
				}
				break;
			case 4:
				token = new Token();
				token.setType(Token.TK_NUMBER);
				token.setText(term);
				back();
				
				return token;
			case 5:
				term += currentChar;
				
				token = new Token();
				token.setType(Token.TK_OPERATOR);
				token.setText(term);
				
				return token;
				
			}
		}
	}
	
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isChar(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	private boolean isOperator(char c) {
		return c == '>' || c == '<' || c == '=' || c == '!';
	}
	
	private boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
	private char nextChar() {
		return content[position++];
	}
	
	private boolean isEOF() {
		return position == content.length;
	}
	
	private void back() {
		position--;
	}
}
