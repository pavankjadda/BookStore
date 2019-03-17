package com.books;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

class Solution
{

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            String IP = in.next();
            System.out.println(IP.matches(new MyRegex().pattern));
        }

    }
}

class MyRegex
{
    public String pattern=null;
    public MyRegex()
    {
        //Pattern pattern1 = Pattern.compile("((25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0]?[0-9]?[0-9])(\\\\.|$)){4}", Pattern.MULTILINE);
        //Matcher matcher=pattern1.matcher(IP);
        //System.out.println(matcher.matches());
        this.pattern="^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    }

}