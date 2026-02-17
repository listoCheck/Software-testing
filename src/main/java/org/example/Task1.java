package org.example;

public class Task1 {
    public static void main(String[] args) {

    }
    public static double sin(double x){
        double sumNew, sumOld, sum;
        int i = 1;
        sum = sumNew = x;
        do {
            sumOld = sumNew;
            i++; sum = sum * x * x / i;
            i++; sum = sum / i;
            sum = -sum;
            sumNew = sumOld + sum;
        }while( sumNew != sumOld);
        System.out.println(sumNew + " <- Teylor | Java sin -> " + Math.sin(x));
        return sumNew;
    }
}
