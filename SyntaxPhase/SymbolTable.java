import java.util.*;

public class SymbolTable {
    HashSet<Token> hash;

    public SymbolTable() {
        hash = new HashSet<Token>();
    }

    public void add(Token token) {        
        Iterator<Token> iterator = hash.iterator(); 
      
        while (iterator.hasNext()){
            /* Check token already there in Symbol Table entry */
            if(iterator.next().value.equals(token.value)){
                return;
            }  
        }    
        hash.add(token);
    }

    public void display(){
        Iterator<Token> iterator = hash.iterator(); 
      
        while (iterator.hasNext()){
            iterator.next().display();  
        }
        System.out.println();
	}
}