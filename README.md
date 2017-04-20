# MiniCompiler
This is a small compiler to process if-else statements in c, taking care of dangling-if-else problems and nested if-statements etc.

## Instructions:

- This compiler can be broken down into 3 main phases- lexical phase,syntax phase and Intermediate code phase.I have not made a semantic phase, so i am assuming that the input will be void of errors such as initialising before declaring a variable, or type conversions(downcasting) etc.

- The lexical phase mainly breaks down input C code into lexemes, for example:(Keyword,if) or (Identifier,variablea,1).This phase also takes care of removing whitespaces and comments, both single line and multiline.The output will be a symbol table of lexemes.

- The next phase is the parsing phase,we use Top down Parser, i.e predictive parser, so we first generate first and follow, after which we generate a parsing table and then given the input string we insert them into a stack and push and pop accordingly until the string is accepted.I have not taken care of sync(Panic mode recovery/phrase level recovery) so i have assumed the input string given is correct.

- The next phase is the Intermediate code phase,wherin we generate 3 address code.Since i have not linked the phases, i have given an input.c text file in each phase, where in reality, each phase gives its output as the input to next subsequent phase.This can be fed both normal statements and nested if else blocks.However each statement can have identifiers as characters and not strings and also it is assumed that all numbers are stored in a variable, so numbers aren't supported as well.Providing support for this will be left for improvement in the future.
