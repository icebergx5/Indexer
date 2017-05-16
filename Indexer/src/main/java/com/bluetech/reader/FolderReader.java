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
package com.bluetech.reader;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author yasin
 */
public class FolderReader {
    
    public static File[] getSubFolder(String filePath){
        File file = new File(filePath);
        if(file.isDirectory()){
            return file.listFiles();
        }
        return null;
    }
    
    public static void listfiles(String directoryName, ArrayList<File> files) {
    File directory = new File(directoryName);

    // get all the files from a directory
    File[] fList = directory.listFiles();
    for (File file : fList) {
       
        if(file.getName().startsWith(".") || file.isHidden()){
            continue;
        }
        
        if (file.isFile()) {
            files.add(file);
        } else if (file.isDirectory()) {
            listfiles(file.getAbsolutePath(), files);
        }
    }
}
    
}
