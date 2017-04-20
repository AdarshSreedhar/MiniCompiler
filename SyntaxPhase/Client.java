import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Client{
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		Lex l = new Lex();
		
		System.out.println("********************************************* LEX ***********************************");
		System.out.println("TOKENS");

		l.getTokens();
		l.display();

		System.out.println("********************************************* PARSER ********************************");
		PredictiveParser pp = new PredictiveParser();
		
		pp.grammarDisplay();
		pp.firstDisplay();
		pp.followDisplay();
		pp.tableDisplay();

	
		try{
			String sb = new String(Files.readAllBytes(Paths.get("parser_input.txt")));
			if(pp.parse(sb)){
				System.out.println("\n******************************** INTERMEDIATE CODE GENERATOR ***********************************");
				String str = new String(Files.readAllBytes(Paths.get("icg_input.txt")));
				ThreeAddressCodeGenerator icg = new ThreeAddressCodeGenerator(str);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}