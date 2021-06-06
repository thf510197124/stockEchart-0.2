package com.example.czsc.utils;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * @FileName: Utils
 * @Author: Haifeng Tong
 * @Date: 2021/4/101:21 下午
 * @Description:
 * @History:
 */
public class Utils {
    //private static PrintStream  ps = System.out;
    private static PrintStream  ps = null;

    static {
        try {
            ps = new PrintStream("/Users/haifeng/Downloads/log.txt","utf-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void println(String args){
        ps.println(args);
    }
    public static void printSplit(String args){
        for (int i=0;i<100;i++){
            print(args);
        }
        println();
    }
    public static void println(){
        ps.println();
    }
    public static void print(String args){
        ps.print(args);
    }
    public static void println(Object args){
        if (args instanceof Collection){
            println((Collection)args);
        }else {
            println(args.toString());
        }
    }
    public static void println(Collection<Object> objs){
        for(Object obj : objs){
            println(obj.toString());
        }
    }
    public static String unicodeDecode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);

                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static void main(String[] args) {
        String name = "\u4e07  \u79d1\uff21";
        println(unicodeDecode(name));
    }
}
