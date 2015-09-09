package com.crackdeveloperz.flymath.Equation;

import java.util.ArrayList;

/**
 * Created by script on 9/9/15.
 */
public class Equate
{
    public static Solution getSolution(String[] equation, ArrayList<Character> variablechars)
    {

        MatrixGen matrix= new MatrixGen(variablechars.size());
        matrix.generateMatrix(equation, variablechars);
        //System.out.println(matrix.getDeterminant());

        Double[][] inverse = MatrixGen.invert(matrix.getCoefficientMatrix());
        //System.out.println(MatrixGen.determinantOf(inverse));

        Double[] result=MatrixGen.multiply(inverse,matrix.getConstantMatrix());

        return (new Solution(variablechars,result));
//		for(int i=0;i<result.length;++i){
//			System.out.println(variablechars.get(i)+" "+result[i]);
//		}

    }

    public static String kickBrackets(String expression){

        if(expression.contains("(")){
            int startIndex;
            int endIndex = 0;
            int beginIndex;
            int closeIndex;
            int stack;
            String leftappend=new String("");
            String rightappend=new String("");
            while(expression.contains("(")){
                startIndex = expression.indexOf("(");
                stack=1;

                // loop to catch the indices of starting and corresponding ending bracket
                for(int i=startIndex+1;stack!=0;++i){
                    if(expression.charAt(i)=='('){
                        ++stack;
                    }
                    if(expression.charAt(i)==')'){
                        --stack;
                        endIndex=i;
                    }
                }
                // caught indices

                //catch beginIndex
                if(expression.indexOf(startIndex)!=0){
                    if(expression.substring(0,startIndex).contains("+")){
                        beginIndex=expression.lastIndexOf('+',startIndex)+1;
                    }
                    else{
                        beginIndex=0;
                    }
                }
                else{
                    beginIndex = startIndex;
                }

                //catch closeIndex
                closeIndex=endIndex;		//initialization
                stack=0;

                for(int i=endIndex+1;closeIndex==endIndex;++i){
                    if(endIndex==expression.length()-1){
                        closeIndex=expression.length();
                    }
                    else{
                        if(expression.charAt(i)=='('){
                            ++stack;
                        }
                        if(expression.charAt(i)==')'){
                            --stack;
                        }
                        if(expression.charAt(i)=='+' && stack==0){
                            closeIndex=i;
                        }
                        if(i==expression.length()-1){
                            closeIndex=expression.length();
                        }
                    }
                }
                //index complete

                //appending portions
                leftappend = expression.substring(beginIndex,startIndex);
                rightappend = expression.substring(endIndex+1,closeIndex);
                //append done

                String boomerang=kickBrackets(expression.substring(startIndex+1,endIndex));
                //System.out.println(boomerang);

                String[] splitBoomerang = boomerang.split("\\+");
                boomerang=new String("");


                for(int i=0;i<splitBoomerang.length;++i){
                    splitBoomerang[i] = leftappend+splitBoomerang[i]+rightappend;
                    if(splitBoomerang[i].contains("--")){
                        splitBoomerang[i]=splitBoomerang[i].replaceAll("--", "");
                    }

                    if(i!=0){
                        boomerang+="+";
                    }
                    boomerang +=splitBoomerang[i];
                }
                StringBuffer inputBuffer = new StringBuffer(expression);
                inputBuffer.delete(beginIndex, closeIndex);

                inputBuffer.insert(beginIndex, boomerang);

                //System.out.println(inputBuffer);
                expression=inputBuffer.toString();
            }
            expression=kickBrackets(expression);
        }

        // pure simple expression here
        else{
            String constantArray="";
            ArrayList<String> variableBlock=new ArrayList<String>();
            String[] array=expression.split("\\+");
            ArrayList<Character> variableName=new ArrayList<Character>();

            for (String anArray : array) {

                if (!containsVariable(anArray)) {            // DOES NOT CONtAIN VARIABLE

                    if (!constantArray.isEmpty()) {
                        constantArray += '+';
                    }
                    constantArray += anArray;
                } else {

                    //per character level
                    for (int j = 0; j < anArray.length(); ++j) {

                        if (Character.isAlphabetic(anArray.charAt(j))) {
                            boolean variableCheckSwitch = false;
                            int index = 0;

                            for (int k = 0; k < variableName.size(); ++k) {        // k is index of character array

                                if (anArray.charAt(j) == variableName.get(k)) {
                                    variableCheckSwitch = true;
                                    index = k;
                                }

                            }

                            // character array ma variable character bhetiena
                            //array ma kunai character bhetiyo jun character array ma chhaina
                            if (!variableCheckSwitch) {
                                variableName.add(anArray.charAt(j));
                                variableBlock.add(anArray);
                            } else {

                                variableBlock.set(index, variableBlock.get(index) + "+" + anArray);
                            }
                            variableCheckSwitch = false;

                            //this is out of "k" loop
                        }
                    }
                    //this is out of j loop
                }
            }
            //this is out of i loop
            //System.out.println(variableBlock.get(0));
            //resolving individual expression containing variable
            for(int x=0;x<variableBlock.size();++x){
                while(variableBlock.get(x).contains(variableName.get(x)+"")){
                    int index = variableBlock.get(x).lastIndexOf(variableName.get(x));
                    variableBlock.set(x, (new StringBuffer(variableBlock.get(x))).deleteCharAt(index).toString());

                }
                variableBlock.set(x, Arithmetic.resolve(variableBlock.get(x))+variableName.get(x));
            }
            expression="";
            //System.out.println(variableBlock.size());
            for(int m=0;m<variableBlock.size();++m){
                //variableString.set(m, Arithmetic.getValue(variableString.get(m)).toString()+variableName.get(m));
                if(!expression.isEmpty()){
                    expression+='+';
                }expression += variableBlock.get(m);

            }
            if(!constantArray.isEmpty()){
                if(!expression.isEmpty()){
                    expression+='+';
                }
                expression+=Arithmetic.resolve(constantArray);
            }
        }
        //System.out.println(expression);
        return expression;
    }

    private static boolean isOperable(String[] equation) {

        boolean returnState=true;
        for (String anEquation : equation) {

            returnState = returnState && containsVariable(anEquation) && anEquation.contains("=");

        }
        return returnState;
    }

    public static boolean containsVariable(String exp){
        boolean status=false;
        for(int i=0;i<exp.length();++i){
            if(exp.charAt(i)>=97 && exp.charAt(i)<=122){
                status=true;
            }
        }
        return status;
    }
}
