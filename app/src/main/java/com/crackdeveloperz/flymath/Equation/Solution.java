package com.crackdeveloperz.flymath.Equation;

import java.util.ArrayList;

/**
 * Created by script on 9/9/15.
 */
public class Solution
{
    ArrayList<Character> var;
    Double[] value;

    public Solution(ArrayList<Character> var, Double[] value )
    {
        this.var=var;
        this.value=value;
    }

    public ArrayList<Character> getVariable()
    {
        return var;
    }
    public char getVariable(int i)
    {
        return var.get(i);
    }
    public Double[] getValue()
    {
        return value;
    }
    public Double getValue(int i)
    {
        return value[i];
    }

    public int size(){
        return value.length;
    }

    public  String getResult()
    {
        String result="";

        for(int i=0;i<this.size();++i)
        {
            result+=var.get(i)+" "+"="+" "+value[i]+"\n";
        }

        return result;
    }
}
