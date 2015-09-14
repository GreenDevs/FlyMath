package com.crackdeveloperz.flymath.Translator;

import android.util.Log;

import com.crackdeveloperz.flymath.Equation.Arithmetic;
import com.crackdeveloperz.flymath.Equation.Equate;
import com.crackdeveloperz.flymath.Equation.Solution;

import java.util.ArrayList;

/**
 * Created by script on 9/9/15.
 */
public class Translate
{

    public static final String EQ_DELEMINATOR=":";
    public Translate()
    {

    }


    public static String sort(String inputExpression) throws Exception {

        String sortResult=null;
        //count number of opening and closing braces
        //insert 1

        //insert *
        //insert +


        if(containsVariable(inputExpression)&& inputExpression.contains("="))
        {
          Solution solution=inspectEquation(inputExpression);

            sortResult = solution.getResult();

            Log.e("Expression gh","expression valid equation ");



           // sortResult = Solution.getResult(solution);
        }



        else if(!containsVariable(inputExpression) && !inputExpression.contains("="))
        {

            sortResult= inspectArithmetic(inputExpression);

            Log.e("Expression gh","expression normal arithmatic");
        }


        else
        {
            //error statement
            Log.i("Expression", "expression ERROR:Not valide mathematical expression!");
        }


        return sortResult;
    }

    private static String inspectArithmetic(String expression) throws Exception {

        //Balancing the number of braces
        if(countOf('(',expression) > countOf(')',expression)){
            for(int i=0 ; i< (countOf('(',expression)- countOf(')',expression)) ; ++i){
                expression += ')';
            }
        }
        if(countOf('(',expression) < countOf(')',expression)){
            for(int i=0 ; i< (countOf(')',expression)- countOf('(',expression)) ; ++i){
                expression = '(' +expression;
            }
        }
        //brackets count done

        expression = expression.replaceAll("()","");

        //inserting * before opening braces
        for(int c =1;c<expression.length();++c){
            if(expression.charAt(c) == '('){
                if(expression.charAt(c-1)==')' || Character.isDigit(expression.charAt(c-1))){
                    expression = (new StringBuffer(expression)).insert(c,'*').toString();
                    ++c;
                }
            }
            if(c<expression.length()-1){
                if(expression.charAt(c) == ')'){
                    if( Character.isDigit(expression.charAt(c+1)) ){

                        expression = (new StringBuffer(expression)).insert(c+1,'*').toString();
                        ++c;
                    }
                }
            }
        }
        // Bracketings complete
        //Multiplier symbol done

        //Handling + operator
        for(int i=1;i<expression.length();++i){

            if(expression.charAt(i) == '-') {

                if(Character.isDigit(expression.charAt(i-1)) || expression.charAt(i-1)==')'){

                    expression = (new StringBuffer(expression)).insert(i,'+').toString();
                    ++i;
                }

            }
        }

        if(expression.contains("++")){
            expression=expression.replaceAll("\\++","+");
        }
        if(expression.contains("--")){
            expression=expression.replaceAll("--", "");
        }

        if(expression.contains(" ")){
            expression = expression.replaceAll(" ","");
        }

      return (Arithmetic.resolve(expression));

    }




    private static Solution inspectEquation(String setOfEquations) throws Exception {

        //split equations

        String[] equation;
        ArrayList<Character> variablechars=new ArrayList<Character>();
        equation=setOfEquations.split(EQ_DELEMINATOR);

        // variable characters

        for(int i=0;i<setOfEquations.length();++i)
        {

            if(Character.isAlphabetic(setOfEquations.charAt(i))){
                boolean variableCheckSwitch=false;
                for(int k=0;k<variablechars.size();++k){		// k is index of character array

                    if(setOfEquations.charAt(i)==variablechars.get(k)){
                        variableCheckSwitch=true;
                    }

                }
                // character array ma variable character bhetiena
                //array ma kunai character bhetiyo jun character array ma chhaina
                if(!variableCheckSwitch){
                    variablechars.add(setOfEquations.charAt(i));
                }
                variableCheckSwitch=false;

                //this is out of "k" loop

            }
        }
        //i loop end

        // inserting 1 before lone variable
        for(int i=0 ; i < equation.length;++i){

            for(int j=0;j<equation[i].length();++j){
                if(Character.isAlphabetic(equation[i].charAt(j))){

                    if(j==0){
                        equation[i]="1"+equation[i];
                        ++j;
                    }

                    else{
                        if(!Character.isDigit(equation[i].charAt(j-1))){

                            equation[i]=(new StringBuffer(equation[i])).insert(j,'1').toString();
                            ++j;
                        }
                    }
                }
            }
            //System.out.println(equation[i]);
        }
        // 1 ko tension khatam

        if(variablechars.size()!=equation.length && isOperable(equation)){
            //System.out.println("Not Operable");
            //error expression
        }
        else{			//Is Operable


            //System.out.println("Count Match = "+variablechars.size());
            for(int i=0;i<equation.length;++i){
                //one equation at a time
                String[] hs = equation[i].split("\\=");
                equation[i]=hs[0]+"+-("+hs[1]+")";

                equation[i]=addPlus(equation[i]);

                //System.out.println(equation[i]);
                equation[i] = Equate.kickBrackets(equation[i]);
            }
        }

      return (Equate.getSolution(equation, variablechars));
    }

    private boolean isRegularExpression(String expression){
        return false;
    }

    private static boolean isOperableEquation(String[] equation)
    {

        boolean returnState=true;
        for (String anEquation : equation) {

            returnState = returnState && containsVariable(anEquation) && anEquation.contains("=");

        }
        return returnState;
    }

    public static boolean containsVariable(String exp)
    {
        boolean status=false;
        for(int i=0;i<exp.length();++i){
            if((exp.charAt(i)>='A' && exp.charAt(i)<='Z') || (exp.charAt(i)>='a' && exp.charAt(i)<='z')){
                status=true;
            }
        }
        return status;
    }

    private static int countOf(char ch, String string)

    {
        int count=0;

        for(int i=0;i<string.length();++i){
            if(string.charAt(i)==ch){
                ++count;
            }
        }

        return count;
    }


    private static boolean isOperable(String[] equation)
    {

        boolean returnState=true;
        for (String anEquation : equation) {

            returnState = returnState && containsVariable(anEquation) && anEquation.contains("=");

        }
        return returnState;
    }

    private static String addPlus(String expression){
        StringBuffer buffer=new StringBuffer(expression);
        for(int index=1;index<expression.length();++index){

            if(buffer.charAt(index)=='-'){
                char ch=buffer.charAt(index-1);
                if(ch==')' || Character.isAlphabetic(ch) || Character.isAlphabetic(ch)){
                    buffer=buffer.insert(index,'+');
                }
            }
        }
        expression=buffer.toString();
        if(expression.contains("--")){
            expression=expression.replaceAll("--","");
        }
        if(expression.contains("++")){
            expression=expression.replaceAll("\\++","+");
        }
        return expression;
    }
}
