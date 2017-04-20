import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Client{
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		/*
		//since lexical phase isn't combined into syntax phase, i have commented this out else use this in the future
		Lex l = new Lex();
		
		System.out.println("********************************************* LEX ***********************************");
		System.out.println("TOKENS");

		l.getTokens();
		l.display();
		*/
		System.out.println("********************************************* PARSER ********************************");
		PredictiveParser pp = new PredictiveParser();
		
		pp.grammarDisplay();
		pp.firstDisplay();
		pp.followDisplay();
		pp.tableDisplay();
	
	
		try{
			String sb = new String(Files.readAllBytes(Paths.get("parser_input.txt")));
			if(pp.parse(sb))
			{
				System.out.println("accept");	
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
