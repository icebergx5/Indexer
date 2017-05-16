/*
 * Copyright 2017 yasin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluetech.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yasin
 */
public class Parser {

    private static int charArrayToInt(char []data,int start,int end) throws NumberFormatException{
        int result = 0;
        for (int i = start; i < end; i++)
        {
            int digit = (int)data[i] - (int)'0';
            if ((digit < 0) || (digit > 9)) throw new NumberFormatException();
            result *= 10;
            result += digit;
        }
        return result;
    }
    
     private static String charArrayToString(char []data,int start, int end) throws NumberFormatException{
        List<Character> textBetweenTwoNum = new ArrayList<>();
        char c = data[start];
        int cInt = (int)c;
        
        while(cInt != end){
            textBetweenTwoNum.add(c);
            c = data[++start];
            cInt = (int)c;
        }

        return charListToString(textBetweenTwoNum);
    }
    
    private static String charListToString(List<Character> textBetweenTwoNum){
        StringBuilder builder = new StringBuilder(textBetweenTwoNum.size());
        for(Character chars: textBetweenTwoNum){
            builder.append(chars);
        }
       return builder.toString();
    }
    
    public static List<IndexPart> parse(String text){
        List<IndexPart> list = new ArrayList<>();
        
        
        char[] toCharArray = text.toCharArray();
        List<Character> textBetweenTwoNum = new ArrayList<>();
        boolean textOrNum = false;
        IndexPart io = new IndexPart();
        
        for (int i = 0; i < toCharArray.length; i++) {
            char ch = toCharArray[i];
 
            if(ch =='#' && toCharArray[i+1] == '#'){
                
               if (textBetweenTwoNum.size() > 0){
                
                String textPart = charListToString(textBetweenTwoNum);   
                   
//                String arr = textPart.matches("\\P{InArabic}+");
                
              // String arabic = textPart.replaceAll("\\P{InArabic}+","");
                String arabic = textPart.replaceAll("[^[\\P{InArabic}\\p{P}\\p{Digit}]]+","");
                
                
                
          
                io.setArabicText(arabic);
                

//                io.setArabicText(arabic); 
                io.setText(textPart);
                list.add(io);
                textBetweenTwoNum = new ArrayList<>();
                textOrNum = false;
                io = new IndexPart();
               }
      
               String book = charArrayToString(toCharArray, i + 2, 95); // 95 = '_'
               i += 2 + book.length() + 1; // basllangic + kitap no uzunlugu + '_' uzunlugu
               
               String pageNoStr = charArrayToString(toCharArray, i, 10); // 10 = nl
               i += pageNoStr.length() + 1;// basllangic + sayfa no uzunlugu + nl
              
               io.setBook(book);
               io.setPage(pageNoStr);

               textOrNum = true;
               ch = toCharArray[i];
            }
                
            if(textOrNum){
                textBetweenTwoNum.add(ch);
            }
        }
        
        return list;
    }
}
