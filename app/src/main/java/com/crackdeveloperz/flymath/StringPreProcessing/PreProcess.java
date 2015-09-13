/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crackdeveloperz.flymath.StringPreProcessing;

import android.util.Log;

import com.crackdeveloperz.flymath.Translator.Translate;

import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author trees
 */
public class PreProcess 
{
    
    private StringBuilder rawString;

    public PreProcess(String rawEquation) 
    {
        this.rawString=new StringBuilder(rawEquation);
        
    }

    //THIS REMOVES ALL THE UNNECESSARY SYMBOLS AND REPLACES NEEDY SYMBOLS
    public String removeUnnecesarySymbols()
    {
        StringBuilder temp=new StringBuilder();
        char c;
        for(int i=0;i<rawString.length();i++)
        {
              c=rawString.charAt(i);
              if(c>=48 && c<=57 || c>=65 && c<=90 || c>=97 && c<=122 || c=='(' || c==')' ||
                      c=='^' || c=='*'||c=='+' || c=='-'||c=='=' || c=='/' || c==':'|| c=='.' || c=='[' || c==']' || c=='}' || c=='{')
              {
                  temp.append(c);
              }
        }

        String processed=temp.toString().replaceAll("\\s+", "");
        processed=processed.replace("[", "(").replace("]",")").replace("{","(").replace("}", ")");
        processed=processed.replace("()", "");
        processed=processed.replace("A", "^");
        processed=processed.replace("(-)","(-1)").replace("(+)","(1)").replace("(+","(");

        if(processed.charAt(0)=='+') processed=processed.substring(1,processed.length());
        rawString=new StringBuilder(processed);
        Log.i("UNNECESSARY SYMBOLS", processed);
        return processed;
    }


    //THIS VLIDATES THE BRACKETS CLOSING AND OPENING USING STACK
    public boolean bracketValidation()
    {
       removeUnnecesarySymbols();
       String rawData=rawString.toString();
       Stack stack=new Stack();
       return stack.startStack(rawData);
    }

    ///this method genralizes the equation to make equation more solvable
    public String generalizeEquation()
    {
        
        boolean futureValueFlag=false;
        String futureValue="";
        char c;
        StringBuilder processedString=new StringBuilder();
        for(int i=0; i<rawString.length(); i++)
        {
//            c=rawString.charAt(i);
//            if(c=='-')
//            {
//                processedString.append("+");
//            }
            c=rawString.charAt(i);
     
            if(futureValueFlag)
            {
                processedString.append(futureValue); 
                futureValueFlag=false;
            }
            if(c=='(' && i<rawString.length())
            {
                
                if(i>0)
                {
                    char oneStepBack=rawString.charAt(i-1);
                    if(oneStepBack=='-' || oneStepBack=='+' || oneStepBack=='(' || oneStepBack>=65 && oneStepBack<=90 || oneStepBack>=97 && oneStepBack<=122)
                    {
                        processedString.append("1*");
                    }
                    else if(oneStepBack>=48 && oneStepBack<=57)
                    {
                          processedString.append("*");
                    }
               
                   
                }
                else
                {
                    processedString.append("1*");
                }
               
            }
            
            if(c==')' && i<rawString.length()-1)
            {
                
                if(i>0 )
                {
                    char oneStepForward=rawString.charAt(i+1);
                    if(oneStepForward>=48 && oneStepForward<=57 || oneStepForward=='(' || oneStepForward>=65 && oneStepForward<=90 || oneStepForward>=97 && oneStepForward<=122)
                     {
                          futureValueFlag=true;
                          futureValue="*";
                     }
                    else if(oneStepForward=='+' || oneStepForward=='-'|| oneStepForward==')')
                    {
                        futureValueFlag=true;
                        futureValue="*1";
                    }
               
                   
                }
                else
                {
                    System.err.println("invalid");
                }
               
            }
            
            
            processedString.append(c);
        }
 
        return processedString.toString();
    }
    
    
    public void splitAndCheck(String expression)
    {
        boolean isValid=true;

        String operands[]=expression.split("(\\+)|(\\-)|(\\*)|(\\/)|(\\^)|(\\=)");

        for(String operand:operands)
        {
            Log.i("OPERANDS"," SPLITTED "+operand);
        }
    }

    //THIS CHECKS THE OPERATORS AND BRACKETS POSITIONS AND VLIDATE

    public boolean checkBracketsAndOperators(String expression)
    {
        char nowChar,aheadChar,backChar;
        for(int i=0;i<expression.length();i++)
        {
            aheadChar=';';
            backChar=';';

            nowChar=expression.charAt(i);
            if(i!=expression.length()-1)
            {
                aheadChar=expression.charAt(i+1);

            }
            if(i!=0)
            {
                backChar=expression.charAt(i-1);
            }

            if(nowChar=='(')
            {
                if(aheadChar=='*'|| aheadChar=='/'|| aheadChar=='^' || aheadChar=='=')
                {
                    return false;
                }

            }
            if(nowChar==')')
            {
                return !isOperator(backChar);
            }
        }

        return true;
    }

    //THIS CHECKS IF THE USE OF OPERATORS ARE RANDOOMS
    public boolean checkOperators(String expression)
    {
        char nowChar,aheadChar;
         for(int i=0;i<expression.length();i++)
         {
             aheadChar=';';
             nowChar=expression.charAt(i);
             if(i!=expression.length()-1) { aheadChar = expression.charAt(i + 1); }
             else
             {
                 if (isOperator(nowChar))
                     return false;
             }

             if(isOperator(nowChar)&&isOperator(aheadChar))
             {
                 return false;
             }
         }

        nowChar=expression.charAt(0);
        return !(nowChar=='*' || nowChar=='/' || nowChar=='^' || nowChar=='=');

    }


    private boolean isOperator(char c)
    {
        return c == '^' || c == '*' || c == '+' || c == '-' || c == '=' || c == '/';
    }


    //TO CHECK IF THE VARIABLE NAMES ARE VALID OR NOT
    public boolean checkVariableNames(String experssion)
    {
        char nowChar,aheadChar;
        for(int i=0;i<experssion.length();i++)
        {
            aheadChar=';';
            nowChar=experssion.charAt(i);
            if(i!=experssion.length()-1) { aheadChar=experssion.charAt(i+1);}

            if(isAlphabet(nowChar) && isAlphabet(aheadChar))
            {
                return false;
            }

        }

        return true;
    }

    private boolean isAlphabet(char c)
    {
        return (c>='a' && c<='z' || c>='A' && c<='Z');
    }

    private int getCharCount(String expession, char c)
    {
        int count=0;
        for(char ch:expession.toCharArray())
        {
            if(ch==c) count++;
        }
        return count;
    }

    ///DECIDE TYPE OF EXPRESSION
    public boolean decideTypeNSplitCheck(String expression)
    {
        if(Translate.containsVariable(expression)&& expression.contains("="))
        {
            Log.e("Expression gh","IT IS EQUATION");
            return splitAndCheckEQ(expression);
        }

        else if(!Translate.containsVariable(expression) && !expression.contains("="))
        {
            Log.e("Expression gh","expression normal arithmatic");
        }

        else
        {
            Log.i("Expression", "expression ERROR:Not valide mathematical expression!");
        }
            return false;
    }


    ///SPLITS AND CHECK THE EQUATION
    private boolean splitAndCheckEQ(String expression)
    {
        String equations[]=expression.split(Translate.EQ_DELEMINATOR);
        int noOfEQs=equations.length;
        int noOfUniqueVariables=getUniqVarbleCount(expression);
        if(noOfUniqueVariables!=noOfEQs || (noOfEQs!=getCharCount(expression, ':')+1))
        {
            Log.i("EQUATION","EQUATION SET INVALID");
            return false;
        }

        for (String equation : equations)
        {
            Log.i("EQUATION", "EQUATION=" + equation);
            if(!(getUniqVarbleCount(equation)<=noOfUniqueVariables) ||  getCharCount(equation,'=')!=1 || equation.split("=").length!=2)
            {
                Log.i("EQUATION"," EQUATION INVALID");
                return false;
            }

        }

        return true;
    }

    ////CHECK THE DIFFERENT VARIABLES COUNT IN THE EUQATION
    private int getUniqVarbleCount(String expression)
    {
        Set<Character> variables=new TreeSet<>();
        for(char c:expression.toCharArray())
        {
            if(isAlphabet(c)) variables.add(c);
        }
        return variables.size();
    }
    
}
