#ThreeAddressCodeGenerator

For now , i have hard-coded the input, which would be in the form
if(some equation like a+b*(c+d))
{
  //bunch of statements
}
else
{
//another bunch of statements
}
however, this can also be taken as input from a file, we just have to use bufferedreader and add all the lines.
Since my grammar does not support conditional operators,the expression inside if condition would be any mathematical expression, that would support associativity and precedence.
Since i recursively call the evaluateBlock function, even if the if-block had a nested if , it would process those statements with different labels given to each executing block.
