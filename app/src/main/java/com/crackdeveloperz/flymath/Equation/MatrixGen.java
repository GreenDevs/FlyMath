package com.crackdeveloperz.flymath.Equation;

import java.util.ArrayList;

/**
 * Created by script on 9/9/15.
 */
public class MatrixGen
{
    private Double[][] coefficientMatrix;
    private Double[] constantMatrix;

    public MatrixGen(Double[][] coefficientMatrix , Double[] constantMatrix){
        this.coefficientMatrix=coefficientMatrix;
        this.constantMatrix=constantMatrix;
        //System.out.println(coefficientMatrix[0].length);
    }

    public MatrixGen(int length){
        this.coefficientMatrix=new Double[length][length];
        this.constantMatrix=new Double[length];
    }

    public Double[][] getCoefficientMatrix(){
        return coefficientMatrix;
    }

    public Double[] getConstantMatrix(){
        return constantMatrix;
    }

    public void generateMatrix(String[] equation,ArrayList<Character> charArray){

        for(int i=0;i<equation.length;++i){
            String[] component=equation[i].split("\\+");

            for(int j=0;j<component.length;++j){
                if(Equate.containsVariable(component[j])){

                    for(int k=0;k<charArray.size();++k){
                        if(component[j].contains(charArray.get(k).toString())){
                            component[j]=component[j].replace(charArray.get(k).toString(),"");
                            //System.out.println(splitEquation[j]);
                            try {
                                coefficientMatrix[i][k]=Double.parseDouble(component[j]);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            //System.out.println(coefficientMatrix[i][k]+" "+charArray.get(k));
                        }
                    }

                }
                else{
                    constantMatrix[i]=-Double.parseDouble(component[j]);
                }
            }

            for(int m=0;m<coefficientMatrix[i].length;++m){
                if(coefficientMatrix[i][m]==null){
                    coefficientMatrix[i][m]=0.0;
                }
                //System.out.println(coefficientMatrix[i][m]);
            }
            if(constantMatrix[i]==null){
                constantMatrix[i]=0.0;
            }
        }
//		for(int i=0;i<3;i++){
//			for(int j=0;j<3;j++){
//				System.out.println(coefficientMatrix[i][j]);
//			}System.out.println(constantMatrix[i]);
//		}

    }

    //Object Call
    public  Double getDeterminant(){
        return determinantOf(this.coefficientMatrix);
    }


    //Static Call
    public static Double determinantOf(Double A[][]){

        Double det=0.0;
        int N=A.length;

        if(N == 1){
            det = A[0][0];
        }

        else if (N == 2){
            det = A[0][0]*A[1][1] - A[1][0]*A[0][1];

        }

        else{
            det=0.0;

            for(int j1=0;j1<N;j1++){

                Double[][] m = new Double[N-1][];

                for(int k=0;k<(N-1);k++){
                    m[k] = new Double[N-1];
                }

                for(int i=1;i<N;i++)
                {
                    int j2=0;
                    for(int j=0;j<N;j++)
                    {
                        if(j == j1)
                            continue;
                        m[i-1][j2] = A[i][j];
                        j2++;
                    }
                }

                det += Math.pow(-1.0,1.0+j1+1.0)* A[0][j1] * determinantOf(m);

            }

        }

        return det;

    }


    //INVERSE of a MATRIX
    public static Double[][] invert(Double a[][]) {

        int n = a.length;

        Double x[][] = new Double[n][n];

        Double b[][] = new Double[n][n];

        int index[] = new int[n];

        for (int i=0; i<n; ++i){
            for(int j=0; j<n; ++j){
                if(i==j){
                    b[i][j] = 1.0;
                }
                else{
                    b[i][j] = 0.0;
                }
            }

        }



        for(int i=0;i<a.length;++i){
            for(int j=0;j<a[0].length;++j){
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }
        // Transform the matrix into an upper triangle
        gaussian(a, index);

        System.out.println();
        for(int i=0;i<a.length;++i){
            for(int j=0;j<a[0].length;++j){
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }

        // Update the matrix b[i][j] with the ratios stored
        for (int i=0; i<n-1; ++i){
            for (int j=i+1; j<n; ++j){
                for (int k=0; k<n; ++k){

                    b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }

        // Perform backward substitutions
        for (int i=0; i<n; ++i){

            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];

            for (int j=n-2; j>=0; --j){

                x[j][i] = b[index[j]][i];

                for (int k=j+1; k<n; ++k){

                    x[j][i] -= a[index[j]][k]*x[k][i];

                }

                x[j][i] /= a[index[j]][j];

            }
        }

        return x;

    }



    // Method to carry out the partial-pivoting Gaussian elimination.  Here index[] stores pivoting order.
    public static void gaussian(Double a[][], int index[]){

        int n = index.length;
        Double c[] = new Double[n];

        // Initialize the index

        for (int i=0; i<n; ++i){
            index[i] = i;
        }

        // Find the rescaling factors, one from each row

        for (int i=0; i<n; ++i){

            Double c1 = 0.0;

            for (int j=0; j<n; ++j){

                Double c0 = Math.abs(a[i][j]);
                if (c0 > c1){
                    c1 = c0;
                }
            }

            c[i] = c1;
        }



        // Search the pivoting element from each column

        int k = 0;

        for (int j=0; j<n-1; ++j){

            Double pi1 = 0.0;

            for (int i=j; i<n; ++i){

                Double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];

                if (pi0 > pi1){
                    pi1 = pi0;
                    k = i;
                }
            }



            // Interchange rows according to the pivoting order

            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;

            for (int i=j+1; i<n; ++i){

                Double pj = a[index[i]][j]/a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly

                for (int l=j+1; l<n; ++l){
                    a[index[i]][l] -= pj*a[index[j]][l];
                }
            }
        }
    }


    public static Double[] multiply (Double[][] mainArray, Double[] constantArray)
    {
        Double[] result = new Double[constantArray.length];
        System.out.println(mainArray[0].length+" "+constantArray.length);

        for (int i = 0; i < mainArray.length; i++) {
            result[i]=0.0;
            for (int k = 0; k < constantArray.length; k++){
                result[i] = result[i] + mainArray[i][k] * constantArray[k];

            }

        }

        return result;
    }
}
