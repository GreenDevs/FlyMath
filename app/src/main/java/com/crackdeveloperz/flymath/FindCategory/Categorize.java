package com.crackdeveloperz.flymath.FindCategory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by script on 9/9/15.
 */
public class Categorize
{

    public static int categorize(String input)
    {

        int category = 0;
        if (noOfChracters(input) == 0)
        {
            category = 1;
        }
        else if (noOfChracters(input) == 1)
        {
            return 2;
        }
        else if (noOfChracters(input) == 2)
        {
            return 3;
        }


        return category;
    }


    public static int noOfChracters(String input)
    {
        int number = 0;
        int length = input.length();
        String chracterList = null;


        Set<Character> srt = new HashSet<Character>();
        for (int i = 0; i < length; ++i)
        {
            char temp = input.charAt(i);
            if ((!Character.isDigit(temp)) && (!isMathematicalSign(temp)))
            {
                srt.add(input.charAt(i));
            }
        }


        return srt.size();

    }

    public static boolean isMathematicalSign(char input)
    {
        boolean isSign = false;
        // ' ' checks for white spave and '10 ' is  for newline in case two varibale equations seperated by newline and obviously mathematical signs
        if (input == '=' || input == '+' || input == '-' || input == '/' || input == '*' || input == '.' || input == ' ' || input == 10)
        {
            isSign = true;
        }
        return isSign;
    }
}
