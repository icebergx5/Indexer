/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.indexer;

import com.bluetech.converter.IndexConverter;
import com.bluetech.core.ElasticClient;
import com.bluetech.core.ElasticClientFactory;
import com.bluetech.core.IndexObject;
import com.bluetech.parser.IndexPart;
import com.bluetech.parser.Parser;
import com.bluetech.reader.FolderReader;
import com.bluetech.reader.WordReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yasin
 */
public class Indexer {
    
    private static String test_index = "test_search";
    private String mainPath = "/home/yasin/NetBeansProjects/Indexer/Indexer/Test/test1";
    private static String indexName = "mehaz_index";
    
    public Indexer() {
        
    }

    public static void main(String[] args) throws IOException {
       
       String[] hosts = {"127.0.0.1:9300"};
       
       ElasticClient client =  ElasticClientFactory.create("mehaz", hosts);
       /*
       IndexObject indexObject = new IndexObject("1");

       indexObject.field("name", "aaaa");
       indexObject.field("age", 32);
       indexObject.field("height", 178);
       indexObject.field("comment", "aaaaaaaaaa");

       boolean test_search = client.index(test_index, indexObject);

       client.close();
       */
       Indexer indexer = new Indexer();
       ArrayList<File> files = new ArrayList<>();
       FolderReader.listfiles(indexer.getMainPath(), files);
       
       // Read All files
       for (File file : files) {
            System.out.println("com.bluetech.indexer.Indexer.main():  Dosya ismi: "+ file.getName()+ " path: "+ file.getCanonicalPath() );
           
            String textInWord = WordReader.readWordDoc(file.getCanonicalPath());
            List<IndexPart> IndexList = Parser.parse(textInWord);
            List<IndexObject> indexObjects = IndexConverter.convert(IndexList);
            
            client.index(indexName, indexObjects);
            
       }
       
       
       
    }
    
    public String getMainPath() {
        return mainPath;
    }

    public void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }
}
