import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
/*a class to store the operator and it's position in the given input string*/
class OperatorFields
{
	char operator;
	int index;
	/*constructor to initiaise the values*/
	OperatorFields(char operator,int index)
	{
		this.operator=operator;
		this.index=index;
	}
}
/*a class to store the 4 values of a record ,i.e operator,arg1,arg2,and result*/
class Quadruple
{
	char op;
	char arg1;
	char arg2;
	int res;
	char res1;
	/*constructor for the same*/
	Quadruple(char op,char arg1,char arg2,char res)
	{
		this.op=op;
		this.arg1=arg1;
		this.arg2=arg2;
		this.res=res;
	}
}
class ThreeAddressCodeGenerator
{
	/*a global variable count for the temporary variables like t0,t1 etc.*/
	public static int count=0;
	/*a global variable to store label counts*/
	public static int lcount=0;
	/*a list to store the records*/
	public static List<Quadruple> resultList=new ArrayList<>();
	/*this gives the precedence rules of operators*/
	public static HashMap<Character,Integer> priority=new HashMap<>();
	public static void main(String[] args)
	{
		String in="if(a+b){if(h+g){c=d+f;}else{p=o+l;}}else{k=o+p;}f=h+j;";
		StringBuilder input=new StringBuilder(in);
		priority.put('(',1);
		priority.put(')',1);
		priority.put('/',2);
		priority.put('*',2);
		priority.put('+',3);
		priority.put('-',3);
		priority.put('=',4);
		evaluateBlock(input);
		displayQuadruple(resultList);
	}
	public static void checkForIfCondition(StringBuilder input)
	{
		/*assuming there is no space after if and open parentheses*/
		/*since there might be nested parentheses inside the condition to evaluate we keep a count of open and close parentheses*/
		int countOfOpenParentheses=1;
		int indexOfIfCondition=3;
		while(countOfOpenParentheses>0)
		{
			++indexOfIfCondition;
			if(input.charAt(indexOfIfCondition)=='(')
				++countOfOpenParentheses;
			else if(input.charAt(indexOfIfCondition)==')')
				--countOfOpenParentheses;
		}
		/*note that we are not passing the parentheses also for evaluation hence we start from 3 and not from 2*/
		/*similarly we end ar index-1 and not index*/
		insertQuadruple(new StringBuilder(input.substring(3,indexOfIfCondition)));
		//System.out.println(input.substring(3,indexOfIfCondition).length());
		/*remove the if keyword and the condition*/
		input.delete(0,indexOfIfCondition+1);
		//input.remove(0,indexOfIfCondition);//check whether inclusive or exclusive
		System.out.println("if t"+(count-1)+" goto Label L"+lcount);
		++lcount;
		System.out.println("goto Label L"+lcount);
		int countOfOpenFlowerBracketsIf=1;
		int indexOfIfBlock=1;
		while(countOfOpenFlowerBracketsIf>0)
		{
			++indexOfIfBlock;
			if(input.charAt(indexOfIfBlock)=='{')
				++countOfOpenFlowerBracketsIf;
			else if(input.charAt(indexOfIfBlock)=='}')
				--countOfOpenFlowerBracketsIf;
		}
		/*the index will now be pointing to the last closing flower bracket in the if block*/
		/*note that we are not passing the flower brackets also for evaluation hence we start from index+2 and not from index+1*/
		/*similarly we end ar index-1 and not index*/
		evaluateBlock(new StringBuilder(input.substring(1,indexOfIfBlock)));
		/*since all the statements inside the block are removed*/
		/*remove the flower brackets*/
		input.delete(0,indexOfIfBlock+1);
		/*check whether we reached the end of the string*/
		if(input.length()==0);
		else evaluateBlock(input);
	}
	public static void evaluateBlock(StringBuilder input)
	{
		while(input.length()>0)
		{
			if(input.substring(0,2).equals("if"))
			{
				checkForIfCondition(input);
			}
			else if(input.substring(0,4).equals("else"))
			{
				int countOfOpenFlowerBracketsElse=1;
				int indexOfElseBlock=5;
				while(countOfOpenFlowerBracketsElse>0)
				{
					++indexOfElseBlock;
					if(input.charAt(indexOfElseBlock)=='{')
					++countOfOpenFlowerBracketsElse;
					else if(input.charAt(indexOfElseBlock)=='}')
						--countOfOpenFlowerBracketsElse;
				}
						evaluateBlock(new StringBuilder(input.substring(5,indexOfElseBlock)));
						/*to remove the flower brackets*/
						input.delete(0,indexOfElseBlock+1);
			}
			else
			{
				evaluateExpression(input);
			}
		}
	}
	public static void evaluateExpression(StringBuilder input)
	{
		//System.out.println(input.substring(2,input.length()-1));
		insertQuadruple(new StringBuilder(input.substring(2,input.length()-1)));
		resultList.add(new Quadruple('=',Integer.toString(count-1).charAt(0),'\0',input.charAt(0)));
		input.delete(0,input.indexOf(";")+1);
	}
	public static void insertQuadruple(StringBuilder tester)
	{
		/*a list to store the operators and their indexes in the input expression*/
		List<OperatorFields> indexListOfOperators=new ArrayList<>();
		/*we use a do-while loop here, because on first iteration we insert all the operators into the indexListOfOperators list*/
		do
		{
			insertAllOperators(indexListOfOperators,tester);

			sortAllOperators(indexListOfOperators);
			/*extract the first operator from the list*/
			OperatorFields o=indexListOfOperators.get(0);
			int startIndex=0,endIndex=0;
			/*check if the operator is parentheses*/
			if(o.operator=='(')
			{
				int[] arr=checkForNestedPairs(startIndex,endIndex,indexListOfOperators);
				startIndex=arr[0];
				endIndex=arr[1];
				evaluateExpressionInsideParentheses(startIndex,endIndex,indexListOfOperators,tester);
			}
			else
			{
				/*if not, then it must be a binary operator,which means we take the character before and after the operator to insert into the quadruple*/
				/*what if it's not a character, but rather a number?*/
				/*what if it's not a single character but a bunch of characters?*/
				/*have not handled that case:to do in the future*/
				startIndex=o.index-1;
				endIndex=o.index+2;
				String toReplace=tester.substring(startIndex,endIndex);
				String tempcount=Integer.toString(count);
				tester.replace(startIndex,endIndex,tempcount);
				resultList.add(new Quadruple(o.operator,toReplace.charAt(0),toReplace.charAt(2),tempcount.charAt(0)));
				++count;
				indexListOfOperators.remove(0);
			}
		}
		while(indexListOfOperators.size()>0);
	}
	public static void insertAllOperators(List<OperatorFields> indexListOfOperators,StringBuilder tester)
	{
		indexListOfOperators.clear();
		char expr[]=new char[tester.length()];
		/*getChars is a method to convert stringbuilder to char array,*/
		tester.getChars(0,tester.length(),expr,0);
		for(int i=0;i<expr.length;i++)
		{
			/* checking whether the char is a number or alphabet,  in which case we don't include it into the list*/
			if(!((expr[i]>=48&&expr[i]<58)||(expr[i]>=97&&expr[i]<123)))
			{
				OperatorFields o1=new OperatorFields(expr[i],i);
				indexListOfOperators.add(o1);
			}
		}
	}
	public static void sortAllOperators(List<OperatorFields> indexListOfOperators)
	{
		/*using bubble sort,we sort the list indexListOfOperators based on the priority given in the hashmap*/
		for(int i=0;i<indexListOfOperators.size();i++)
		{
			for(int j=0;j<indexListOfOperators.size()-1-i;j++)
			{
				OperatorFields o2=indexListOfOperators.get(j+1);
				OperatorFields o1=indexListOfOperators.get(j);
				if(priority.get(o1.operator)>priority.get(o2.operator))
					Collections.swap(indexListOfOperators,j,j+1);
			}
		}
	}
	public static int[] checkForNestedPairs(int startIndex,int endIndex,List<OperatorFields> indexListOfOperators)
	{
		/*check if there is a nested pair of parentheses present in which case you will have to evaluate them first*/
		//OperatorFields o=indexListOfOperators.get(0);
		startIndex=indexListOfOperators.get(0).index;
		for(int i=1;i<indexListOfOperators.size();i++)
		{
			OperatorFields o1=indexListOfOperators.get(i);
			if(o1.operator=='(')
			{
				startIndex=o1.index;
				continue;
			}
			else if(o1.operator==')')
			{
				endIndex=o1.index;
				break;
			}
		}
		int[] arr={startIndex,endIndex};
		return arr;
	}
	public static void evaluateExpressionInsideParentheses(int startIndex,int endIndex,List<OperatorFields> indexListOfOperators,StringBuilder tester)
	{
		/*evaluate the expression inside the parentheses*/
		insertQuadruple(new StringBuilder(tester.substring(startIndex+1,endIndex)));
		String tempcount=Integer.toString(count);
		tester.replace(startIndex,endIndex+1,tempcount);
		++count;
		List<Integer> allOperatorsToBeRemoved=new ArrayList<>();
		for(int i=0;i<indexListOfOperators.size();i++)
		{
			int ind=indexListOfOperators.get(i).index;
			if(ind>=startIndex&&ind<=endIndex)
			{
				indexListOfOperators.remove(i);
			}
		}
	}
	public static void displayQuadruple(List<Quadruple> resultList)
	{
		System.out.println("Op\targ1\targ2\tresult");
		for(Quadruple q:resultList)
		{
			System.out.print(q.op+"\t");
			int ascii1=(int)q.arg1;
			int ascii2=(int)q.arg2;
			int ascii3=(int)q.res;
			if(ascii1>=48&&ascii1<58)
				System.out.print("t");
			System.out.print((char)ascii1+"\t");
			if(ascii2>=48&&ascii2<58)
				System.out.print("t");
			System.out.print((char)ascii2+"\t");
			if(ascii3>=48&&ascii3<58)
				System.out.print("t");
			System.out.println((char)q.res);
		}
	}
}
