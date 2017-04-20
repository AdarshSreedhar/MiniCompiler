import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.lang.*;
import java.util.*;
public class SymbolTableGenerator
{
	public static int count=0;
  public static enum TokenType 
  {
    // Token types cannot have underscores
    KEYWORD("abstract|continue|for|new|switch|assert|default|goto|package|synchronized|boolean|do|if|private|this|break|double|implements|protected|throw|byte|else|import|public|throws|case|enum|instanceof|return|transient|catch|extends|int|short|try|char|final|interface|static|void|class|finally|long|strictfp|volatile|const|float|native|super|while"),
    IDENTIFIER("[a-zA-Z][a-zA-Z_0-9]*"),
    NUMBER("-?[0-9]+"),
    BINARYOP("[*|/|+|-|,]"),
    LPAREN("[(]"),
    RPAREN("[)]"),
    //UNARYOP("[**/++/--]"),
    SEMICOLON("[;]+"),
    WHITESPACE("[ \t\f\r\n]+");

    public final String pattern;

    private TokenType(String pattern)
    {
      this.pattern = pattern;
    }
  }

  public static class Token
  {
    public TokenType type;
    public String data;
    public int entryNumber=0;
    public Token(TokenType type, String data)
    {
      this.type = type;
      this.data = data;
    }
    public Token(TokenType type, String data,int entryNumber)
    {
      this.type = type;
      this.data = data;
      this.entryNumber=entryNumber;
    }
    @Override
    public String toString()
    {
      return String.format("(%s %s %s)", type.name(), data,entryNumber);
    }
  }

  public static ArrayList<Token> lex(String input)
  {
    // The tokens to return
    ArrayList<Token> tokens = new ArrayList<Token>();

    // Lexer logic begins here
    StringBuffer tokenPatternsBuffer = new StringBuffer();
    //The values( ) method returns an array that contains a list of the enumeration constants
    for (TokenType tokenType : TokenType.values())
      tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));

    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

    // Begin matching tokens
    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find())
    {
      if (matcher.group(TokenType.NUMBER.name()) != null)
      {
        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name()),++count));
        continue;
      }
      else if (matcher.group(TokenType.IDENTIFIER.name()) != null)
      {
        tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(TokenType.IDENTIFIER.name()),++count));
        continue;
      }
      else if (matcher.group(TokenType.KEYWORD.name()) != null)
      {
        tokens.add(new Token(TokenType.KEYWORD, matcher.group(TokenType.KEYWORD.name())));
        continue;
      }
      else if (matcher.group(TokenType.BINARYOP.name()) != null) 
      {
        tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
        continue;
      }
      else if (matcher.group(TokenType.LPAREN.name()) != null) 
      {
        tokens.add(new Token(TokenType.LPAREN, matcher.group(TokenType.LPAREN.name())));
        continue;
      }
      else if (matcher.group(TokenType.RPAREN.name()) != null) 
      {
        tokens.add(new Token(TokenType.RPAREN, matcher.group(TokenType.RPAREN.name())));
        continue;
      }
      else if (matcher.group(TokenType.WHITESPACE.name()) != null)
        continue;
    }

    return tokens;
  }

  public static void main(String[] args)
  {
      try
      {
        BufferedReader br = new BufferedReader(new FileReader("samplewithoutcomments.c"));
        try 
        {
          String line;
          while ((line=br.readLine()) != null)
          {
              ArrayList<Token> tokens = lex(line);
              for (Token token : tokens)
                System.out.println(token);
          }
        }
      catch(IOException e)
      {
            e.printStackTrace();
        } 
      finally
      {
          br.close();
      }
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    
  }
}
