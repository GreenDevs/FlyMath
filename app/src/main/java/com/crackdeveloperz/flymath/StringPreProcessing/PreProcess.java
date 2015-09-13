/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crackdeveloperz.flymath.StringPreProcessing;

import android.util.Log;

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
        processed=processed.replace("[", "(").replace("]",")").replace("{","(").replace("}",")");
        processed=processed.replace("()","");
        processed=processed.replaceAll("A", "^");
        processed=processed.replace("(-)","(-1)").replace("(+)","(1)");
        rawString=new StringBuilder(processed);
        Log.i("UNNECESSARY SYMBOLS", processed);
        return processed;
    }
    
    public boolean bracketValidation()
    {
       removeUnnecesarySymbols();
       String rawData=rawString.toString();
       Stack stack=new Stack();
       return stack.startStack(rawData);
    }
    
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
                if(isOperator(backChar)) return false;
            }
        }

        return true;
    }

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

        return true;
    }


    private boolean isOperator(char c)
    {
        if(c=='^' || c=='*'||c=='+' || c=='-'||c=='=' || c=='/') { return true;}
        else  {return false;}
    }
    
}
