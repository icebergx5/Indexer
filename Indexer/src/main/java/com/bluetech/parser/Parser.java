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
import java.util.List;

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
    
    
    public static List<IndexPart> parse(String text){
        List<IndexPart> list = new ArrayList<IndexPart>();
        
        
        char[] toCharArray = text.toCharArray();
            
        for (int i = 0; i < toCharArray.length; i++) {
            char ch = toCharArray[i];
            IndexPart io = new IndexPart();

            
            if(ch =='#' || toCharArray[i+1] == '#'){
                
               int start = i + 2;
               int end = i + 6;
               
               int book = charArrayToInt(toCharArray, start, end );
               
               
               start = i + 8;
               end = i + 11;
               int page = charArrayToInt(toCharArray, start, end );
               
               io.setBook(book);
               io.setPage(page);
               
               
               i+=11;
            }
                
            System.out.print(ch);
            
            list.add(io);
        }
        
        return list;
    }
}
