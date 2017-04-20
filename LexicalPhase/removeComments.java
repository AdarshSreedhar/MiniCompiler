import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
class removeComments
{
	private static final String filename="sample.c";
	private static final String filewithoutcomments="samplewithoutcomments.c";
	public static void main(String[] args)
	{
		BufferedReader br=null;
		FileReader fr=null;
		try
		{
			fr=new FileReader(filename);
			br=new BufferedReader(fr);
			String currentLine;
				try
				{
					PrintWriter writer=new PrintWriter(filewithoutcomments);
					boolean multiLineComment=false;
					while((currentLine=br.readLine())!=null)
					{
						boolean singleLineComment=false;
						boolean startOfString=false;
						boolean stringInsideComment=false;
						char[] arr=currentLine.toCharArray();
						List<Character> l=new ArrayList<>();
						//we have to handle two main cases here:
						//strings inside comments
						//comments inside strings
						for(int i=0;i<arr.length;i++)
						{
							if(arr[i]=='"')
							{
								//is it a string inside a comment?
								if(singleLineComment||multiLineComment)
								{
									stringInsideComment=true;
									//dont add this to the list as they are still comments
								}
								else
								{
									//since it is neither a single line nor a multi line comment,we add them to list
									//checking whether it is already a part of string
									if(startOfString)
									{
										startOfString=false;
									}
									else
									{
										startOfString=true;
									}
								}
							}
							else if(arr[i]=='/')
							{
								if(i+1==arr.length);
								else
								{
									//for single line comment
									if(arr[i+1]=='/')
									{
										//is it part of a string?
										if(startOfString);
										//so if its not part of a string, then its a comment
										else
										{
											singleLineComment=true;
										}
									}
									//for multiline comment
									else if(arr[i+1]=='*')
									{
										//is it part of a string?
										if(startOfString);
										//so if its not part of a string, then its a comment
										else
										{
											multiLineComment=true;
										}
									}			
								}
							}
							else if(arr[i]=='*')
							{
								if(i+1==arr.length);
								else if(arr[i+1]=='/')
								{
									if(multiLineComment)
									{
									multiLineComment=false;
									i+=2;
									}
								}
							}
							if(!singleLineComment&&!multiLineComment&&i<arr.length)
							{
								l.add(arr[i]);
							}
						}
						if(l.size()==0)continue;
						else
						{
							for(char c:l)
								writer.print(c);
							writer.println();
						}
						l.clear();
					}
					writer.close();
				}
				catch (IOException ew)
				{
					ew.printStackTrace();
				}
			br.close();
			fr.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}