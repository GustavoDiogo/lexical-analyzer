package lexicalanalyzer.compiler.main;

import lexicalanalyzer.compiler.exception.LexicalException;
import lexicalanalyzer.compiler.lexical.Scanner;
import lexicalanalyzer.compiler.lexical.Token;

public class MainClass {
	public static void main(String[] args) {
		try {			
			Scanner scanner = new Scanner("main.gus");
			Token token = null;
			
			do {
				token = scanner.nextToken();
				
				if (token != null) {
					System.out.println(token);
				}
				
			} while (token != null);
		} catch (LexicalException ex) {
			System.out.println("Lexical ERROR: " + ex.getMessage());
		} catch (Exception ex) {
			System.out.println("Unmapped error: " + ex.getMessage());
		}
	}
}
