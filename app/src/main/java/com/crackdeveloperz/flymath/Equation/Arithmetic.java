package com.crackdeveloperz.flymath.Equation;

import java.util.ArrayList;

/**
 * Created by script on 9/9/15.
 */
public class Arithmetic
{

    public static String resolve(String cacheInput)
    {

        String returnValue=new String(cacheInput);

        for(int i=1;i<cacheInput.length();++i)
        {

            if(cacheInput.charAt(i)=='-' )
            {

                if(Character.isDigit(cacheInput.charAt(i-1)) || cacheInput.charAt(i-1)==')')
                {

                    cacheInput = (new StringBuffer(cacheInput)).insert(i,'+').toString();
                    ++i;
                }

            }
        }

        if(cacheInput.contains("--"))
        {
            cacheInput=cacheInput.replaceAll("--", "");
        }
        // MANAGE BRACKETS FIRST

        while(cacheInput.contains("("))
        {

            int endIndex = cacheInput.indexOf(")");
            String str = cacheInput.substring(0, endIndex);
            int startIndex = str.lastIndexOf("(");
            boolean symbolSwitch=false;
            String returnedAnswer =  resolve(cacheInput.substring(startIndex + 1, endIndex));

            StringBuffer buffer = new StringBuffer(cacheInput);
            buffer = buffer.delete(startIndex, endIndex + 1);
            buffer = buffer.insert(startIndex,returnedAnswer);


            cacheInput = buffer.toString();

        }

        //DOESNT contain BRACKETS

            if(cacheInput.contains("+"))
            {
                ArrayList<Double> number=new ArrayList<Double>();
                String[] expression=cacheInput.split("\\+");
                Double sum=0.0;

                for(int i=0;i<expression.length;++i)
                {
                    expression[i] = resolve(expression[i]);
                    try {
                        number.add(Double.parseDouble(expression[i]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    sum+=number.get(i);
                }
                returnValue=sum+"";

            }
            else if(cacheInput.contains("*") ||cacheInput.contains("/") || cacheInput.contains("^") )
            {

                ArrayList<Double> DoubleArray = new ArrayList<Double>();
                ArrayList<Character> symbols = new ArrayList<Character>();

                // in case first number is negative .. i.e. first char of string
                // is minus '-'
                String BufferForDouble = (cacheInput.charAt(0)+"");
                boolean checkSwitch=false;



                for (int i = 1; i < cacheInput.length(); i++)
                { // sort number
                    // and

                    if ((Character.isDigit(cacheInput.charAt(i))
                            || cacheInput.charAt(i) == '.') || checkSwitch== true)
                    {
                        BufferForDouble += cacheInput.charAt(i);
                        checkSwitch=false;
                    }

                    // current character is symbol
                    else {

                        DoubleArray.add(Double.parseDouble(BufferForDouble));
                        BufferForDouble = "";
                        symbols.add(cacheInput.charAt(i));
                        checkSwitch=true;
                    }

                }

                try {
                    DoubleArray.add(Double.parseDouble(BufferForDouble));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                int position;

                while(symbols.contains('^'))
                {

                    position = symbols.lastIndexOf('^');
                    DoubleArray.set(position , Math.pow(DoubleArray.get(position),DoubleArray.get(position+1)));
                    DoubleArray.remove(position+1);
                    symbols.remove(position);

                }
                while(symbols.contains('/'))
                {

                    position = symbols.indexOf('/');
                    DoubleArray.set(position , DoubleArray.get(position)/DoubleArray.get(position+1));
                    DoubleArray.remove(position+1);
                    symbols.remove(position);

                }


                while(symbols.contains('*'))
                {

                    position = symbols.indexOf('*');
                    DoubleArray.set(position , DoubleArray.get(position)*DoubleArray.get(position+1));
                    DoubleArray.remove(position+1);
                    symbols.remove(position);

                }
                returnValue=DoubleArray.get(0).toString();
            }


        return returnValue;

    }
}
