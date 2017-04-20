//package parser;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
	
class PredictiveParser{
	/* Hash maps for grammar productions, firstSet, followSet */
	HashMap<String,HashSet<String>> gram;
	HashMap<String,HashSet<String>> first;
	HashMap<String,HashSet<String>> follow;

	/* String representation of input grammar */
	String[] sourceGrammar;

	/* List of non terminals that has productions in Grammar */
	ArrayList<String> nonTermList;

	/* List of terminals that has productions in Grammar */
	ArrayList<String> termList;


	/* Parser Table */
	ArrayList<ArrayList<String>> parserTable;

	public PredictiveParser(){
		/* Initialize Hashmaps */
		gram = new HashMap<String,HashSet<String>>();
		first = new HashMap<String,HashSet<String>>();
		follow = new HashMap<String,HashSet<String>>();

		nonTermList = new ArrayList<String>();
		termList = new ArrayList<String>();

		/* Scan the file and store it in a String variable */
		try{
			//String sb = new String(Files.readAllBytes(Paths.get("sourceGrammar.txt")));
			String sb = new String(Files.readAllBytes(Paths.get("ifelse_gram.txt")));
			sourceGrammar = sb.toString().split("\n");
		}
		catch(IOException e){
			e.printStackTrace();
		}

		/* Check and split grammar having multiple productions */ 
		splitGrammar(sourceGrammar);

		/* Find First set */
		findFirst();

		/* Find follow set */
		findFollow();

		/* Initialise the parser Table */
		parserTable = new ArrayList<ArrayList<String>>(nonTermList.size());

		/* Fill the parser Table */
		fillTable();
	}

	/* Grammar is checked for multiple productions and splitted */
	public void splitGrammar(String[] src){
		for(String i: src){
			String[] x = i.split(":");
			String key = Character.toString(x[0].charAt(0));
			if(!gram.containsKey(key)){
				nonTermList.add(key);
				gram.put(key,new HashSet<String>());
				HashSet<String> h = gram.get(key);
				String[] valueSet = x[1].split("/");
				for(String str : valueSet){
					h.add(str);
				}
			}else{
				HashSet<String> h = gram.get(key);
				String[] valueSet = x[1].split("/");
				for(String str : valueSet){
					h.add(str);
				}
			}
		}
	}

	public void findFirst(){
		/* Find First for every non terminal in grammar */
		for(String i : nonTermList){
			fillFirst(i);
		}
	}

	public void fillFirst(String x){
		HashSet<String> valueSet = (HashSet<String>) gram.get(x);
		if(!first.containsKey(x))
		    first.put(x,new HashSet<String>());
		for (String s : valueSet){
			String str="";
			for(int i=0;i<s.length();i++){
				char ch = s.charAt(i);
				if(i>0 && i<s.length()){
					if(Character.isLowerCase(s.charAt(i-1)) && !(Character.isLowerCase(s.charAt(i)))){
						first.get(x).add(str);
						if(!termList.contains(str))
							termList.add(str);
						str="";
		    			break;
					}
				}

				if(Character.isUpperCase(ch)){
					fillFirst(Character.toString(ch));
					if(!first.containsKey(Character.toString(ch))){
						first.put(Character.toString(ch),new HashSet<String>());
					}
					// union of first set found from multiple productions to eliminate duplicates
					first.get(x).addAll(first.get(Character.toString(ch)));
					if(!first.get(x).contains("e"))
		    				break;	
				}else if(Character.isLowerCase(ch)){
						str+=Character.toString(ch);
						if(i==s.length()-1){
							first.get(x).add(str);
							if(!termList.contains(str))
								termList.add(str);
						}
				}else{
					first.get(x).add(Character.toString(ch));
					if(!termList.contains(str))
						termList.add(Character.toString(ch));
		    		break;
				}
			}
		}
	}

	public void initFollow(){
		/* initialize null to all follow sets */
		/* initialize '$' to start symbol of follow set */
		for(String i : nonTermList){
			follow.put(i,new HashSet<String>());
			if(nonTermList.indexOf(i) == 0){
				follow.get(i).add("$");
				if(termList.contains("e")){
					termList.remove("e");
					termList.add("$");
				}
			}
		}
	}

	public void fillFollow(String ch){
		for(String i : nonTermList){
			// Traverse each production for a non terminal 
			Iterator it = gram.get(i).iterator();
			while(it.hasNext()){
				String j = (String)it.next();
				// traverse production
				for(int k=0;k<j.length();k++){
					int x = k;
					if(j.charAt(x)!= ch.charAt(0))
						continue;
					if(Character.isUpperCase(j.charAt(x))){
						while(x+1 < j.length()){
							if(Character.isUpperCase(j.charAt(x+1))){
								follow.get(Character.toString(j.charAt(k))).addAll(first.get(Character.toString(j.charAt(x+1))));
								if(first.get(Character.toString(j.charAt(x+1))).contains("e")){
									follow.get(Character.toString(j.charAt(k))).remove("e");
									x+=1;
								}else{
									break;
								}
							}else{
								if(j.charAt(x+1) != 'e'){		
									follow.get(Character.toString(j.charAt(k))).addAll(Arrays.asList(Character.toString(j.charAt(x+1))));
									if(!termList.contains(Character.toString(j.charAt(x+1))))
										termList.add(Character.toString(j.charAt(x+1)));
									break;
								}else{
									follow.get(Character.toString(j.charAt(k))).addAll(follow.get(i));
									break;
								}
							}
						}
						if(x+1 >= j.length()){
							follow.get(Character.toString(j.charAt(k))).addAll(follow.get(i));
						}
					}
				}
			}
		}
	}

	public void findFollow(){
		initFollow();
		for(String i : nonTermList){
			// Traverse each production for a non termiSystem.out.println(i);
			fillFollow(i);
			Iterator it = gram.get(i).iterator();
			while(it.hasNext()){
				String j = (String)it.next();
				// traverse production
				for(int k=0;k<j.length();k++){
					int x = k;
					if(Character.isUpperCase(j.charAt(x))){
						while(x+1 < j.length()){
							if(Character.isUpperCase(j.charAt(x+1))){
								follow.get(Character.toString(j.charAt(k))).addAll(first.get(Character.toString(j.charAt(x+1))));
								if(first.get(Character.toString(j.charAt(x+1))).contains("e")){
									follow.get(Character.toString(j.charAt(k))).remove("e");
									x+=1;
								}else{
									break;
								}
							}else{
								if(j.charAt(x+1) != 'e'){		
									follow.get(Character.toString(j.charAt(k))).addAll(Arrays.asList(Character.toString(j.charAt(x+1))));
									break;
								}else{
									follow.get(Character.toString(j.charAt(k))).addAll(follow.get(i));
									break;
								}
							}
						}
						if(x+1 >= j.length()){
							follow.get(Character.toString(j.charAt(k))).addAll(follow.get(i));
						}
					}
				}
			}
		}
	}

	/* Fills the parser Table */
	void fillTable(){
		/* Initialise the values to null */
		for(int i=0;i<nonTermList.size() + 1;i++){
			parserTable.add(new ArrayList<String>(termList.size()));
			for(int j=0;j<termList.size() + 1;j++){
				parserTable.get(i).add(null);
			}
		}

		/* Fill the table */
		/* Initialise the null values to all elements */
		for(int i=0;i<nonTermList.size() + 1;i++){
			for(int j=0;j<termList.size() + 1;j++){
				if(j==0 && i>0){
					parserTable.get(i).add(j,nonTermList.get(i-1));
				}
				if(i==0 && j>0){
					parserTable.get(i).add(j,termList.get(j-1));
				}
			}
		}

		/*for (int j=1;j<termList.size()+1;j++) {
			System.out.println("Terminal:"+termList.get(j));
		}				
		return;
		*/

		/* Fill the table */
		for(int i=1;i<nonTermList.size()+1;i++){
			String[] str = new String[gram.get(parserTable.get(i).get(0)).size()];
			gram.get(parserTable.get(i).get(0)).toArray(str);
			for (String s: str) {
				if(Character.isUpperCase(s.charAt(0))){
					for (int j=1;j<termList.size()+1;j++) {
						String[] str1 = new String[first.get(parserTable.get(i).get(0)).size()];
						first.get(parserTable.get(i).get(0)).toArray(str1);
						for(String x : str1){
							if(parserTable.get(i).get(0)!=null && (x.equals(parserTable.get(0).get(j)))){
									parserTable.get(i).set(j,s);
							}else if(x.equals("e")){
									String[] str2 = new String[follow.get(parserTable.get(i).get(0)).size()];
									follow.get(parserTable.get(i).get(0)).toArray(str2);
									for(String y : str2){
										if(y.equals(parserTable.get(0).get(j))){
											parserTable.get(i).set(j,s);
										}
									}
							}
						} 
					}
				}else if(Character.isLowerCase(s.charAt(0))){
					for (int j=1;j<termList.size()+1;j++) {
						if(s.equals("e")){
							String[] str2 = new String[follow.get(parserTable.get(i).get(0)).size()];
							follow.get(parserTable.get(i).get(0)).toArray(str2);
							for(String y : str2){
								if(y.equals(parserTable.get(0).get(j))){	
									parserTable.get(i).set(j,s);
								}
							}
						}else if(s.equals(parserTable.get(0).get(j))){
								parserTable.get(i).set(j,s);
								break;
						}
					}
				}else{
					for (int j=1;j<termList.size()+1;j++) {
						if(s.equals("e")){
							String[] str2 = new String[follow.get(parserTable.get(i).get(0)).size()];
							follow.get(parserTable.get(i).get(0)).toArray(str2);
							for(String y : str2){
								if(y.equals(parserTable.get(0).get(j))){
									if(parserTable.get(i).get(j).equals(null))
										parserTable.get(i).add(j,s);
									else
										parserTable.get(i).set(j,s);
								}
							}
						}else if(Character.toString(s.charAt(0)).equals(parserTable.get(0).get(j))){
								parserTable.get(i).set(j,s);
								break;
						}
					}
				}
			}
		}
	}

	/* Parse the input string */
	public boolean parse(String s){
		s = s.trim();
		s = s.replaceAll("\\s","");
		Stack inBuf = new Stack();
		Stack parseBuf = new Stack();	
		
		/* Push initial values to respective stacks */
		parseBuf.push(nonTermList.get(0));
		pushStack(inBuf,new StringBuilder(s).reverse().toString());

		System.out.println("Parser Stack Record");
		while(!inBuf.isEmpty()){
			if(parseBuf.isEmpty()){
				System.out.println("****************** COMPILE TIME ERROR *********************");
				return false;
			}

			if(inBuf.peek().equals(parseBuf.peek())){
				inBuf.pop();
				parseBuf.pop();
				if(!parseBuf.isEmpty() && parseBuf.peek().equals("e")){
					parseBuf.pop();
				}
				System.out.printf("%-15s %-45s %-15s\n",toStr(parseBuf),toStr(inBuf),"Match");
			}else{
				String inTop = (String) inBuf.peek();
				String parseTop = (String) parseBuf.peek();
				if(!parserTable.get(0).contains(inTop)){
					System.out.println("****************** COMPILE TIME ERROR *********************");
					System.out.println("Invalid symbol: '"+inTop+"'");
					return false;
				}
				boolean flag = false;
				for(int i=1;i<nonTermList.size()+1;i++){
					if(parserTable.get(i).get(0).equals(parseTop)){
						for(int j=1;j<termList.size()+1;j++){
							if(parserTable.get(0).get(j).equals(inTop)){
								if(parserTable.get(i).get(j)==null){
									System.out.println("****************** COMPILE TIME ERROR *********************");
									System.out.println("Missing ';'");
									return false;
								}
								flag = true;
								parseBuf.pop();
								pushStack(parseBuf,new StringBuilder(parserTable.get(i).get(j)).reverse().toString());
								break;
							}
						}
					}
				}
				if(!flag){
					System.out.println("****************** COMPILE TIME ERROR *********************");
					System.out.println("Missing: '"+parseTop+"'");
					return false;
				}
				if(parseBuf.peek().equals("e")){
					parseBuf.pop();
				}
				System.out.printf("%-15s %-45s %-15s\n",toStr(parseBuf),toStr(inBuf),"Action");
			}
		}
		if(!parseBuf.isEmpty()){
			System.out.println("****************** COMPILE TIME ERROR *********************");
			return false;
		}else{
			System.out.println("****************** SUCCESSFUL COMPILATION *********************");
			return true;	
		}
	}

	public void pushStack(Stack st,String s){
		String str = "";
		for (int i=0;i<s.length();i++) {
			char ch = s.charAt(i);	
			if(Character.isUpperCase(s.charAt(i))){
				if(str!=""){
					st.push(new StringBuilder(str).reverse().toString());
					str="";
				}
				st.push(Character.toString(s.charAt(i)));
			}else if(Character.isLowerCase(s.charAt(i))){
				str+=Character.toString(ch);
			}else{
				if(str!=""){
					st.push(new StringBuilder(str).reverse().toString());	
					str="";
				}
				st.push(Character.toString(s.charAt(i)));
			}		
		}
		if(!str.equals(""))
			st.push(new StringBuilder(str).reverse().toString());
	}

	public String toStr(Stack stack){
		List<String> list = new ArrayList<String>(stack);
		String str = "";
		for(int i=list.size()-1;i>=0;i--){
			str+=list.get(i);
		}	
		return str;
	}

	public void tableDisplay(){
		System.out.println("Parser Table");
		for(int i=0;i<nonTermList.size() + 1;i++){
			for(int j=0;j<termList.size() + 1;j++){	
				if(parserTable.get(i).get(j)==null){
					if(j==1){
						System.out.print("\t\t|");
					}else if(j==0){
						System.out.print(" |");
					}else{
						System.out.print("\t|");
					}
				}
				else{
					if(j==1 && i!=1){
						System.out.print(parserTable.get(i).get(j)+"\t\t|");
					}else if(j==1 && i==1){
						System.out.print(parserTable.get(i).get(j)+"\t|");
					}else if(j==0){
						System.out.print(parserTable.get(i).get(j)+"|");
					}else{
						System.out.print(parserTable.get(i).get(j)+"\t|");
					}
				}
			}
			if(i==0){
				System.out.println();
				for(int j=0;j<termList.size()*9;j++){
					System.out.print("-");
				}
			}
			System.out.println();
		}
		System.out.println();		
	}

	/* Displays grammar */
	public void grammarDisplay(){
		Set keys = gram.keySet();
		System.out.println("INPUT GRAMMAR");
	   	for (Iterator i = keys.iterator(); i.hasNext(); ) {
	    	String key = (String) i.next();
	       	HashSet<String> valueSet = (HashSet<String>) gram.get(key);
	       	System.out.print(key+": ");
	        System.out.println(valueSet);
	   	}
	   	System.out.println();
	}

	/* Displays first set */
	public void firstDisplay(){
		Set keys = first.keySet();
		System.out.println("FIRST SET");
	   	for (Iterator i = keys.iterator(); i.hasNext(); ) {
	    	String key = (String) i.next();
	       	HashSet<String> valueSet = (HashSet<String>) first.get(key);
	       	System.out.print(key+": ");
	        System.out.println(valueSet);
	   	}
	   	System.out.println();
	}

	/* Displays follow set */
	public void followDisplay(){
		Set keys = follow.keySet();
		System.out.println("FOLLOW SET");
	   	for (Iterator i = keys.iterator(); i.hasNext(); ) {
	    	String key = (String) i.next();
	       	HashSet<String> valueSet = (HashSet<String>) follow.get(key);
	       	System.out.print(key+": ");
	        System.out.println(valueSet);
	   	}
	   	System.out.println();
	}
}

