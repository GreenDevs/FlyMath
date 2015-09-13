/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crackdeveloperz.flymath.StringPreProcessing;

/**
 *
 * @author trees
 */
public class Stack 
{
    private int count;
    
    public Stack()
    {
        count=0;
    }
    
    public boolean startStack(String rawString)
    {
        char c;
        for(int i=0; i<rawString.length();i++)
        {
            c=rawString.charAt(i);
            if(c=='(') push();
            if(c==')') pop();
            
            if(count<0) return false;
        }
        if(count!=0) return false;
        else return true;
        
    }
    
    private void push(){count++;};
    private void pop(){count--;}
    
}
