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
package com.bluetech.converter;

import com.bluetech.core.IndexObject;
import com.bluetech.parser.IndexPart;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yasin
 */
public class IndexConverter {
    
    
    public static List<IndexObject> convert(List<IndexPart> objects){
        List<IndexObject> indexes = new ArrayList<>();
        
        for (IndexPart part : objects) {
            
            IndexObject object = new IndexObject();
            
            object.field("book", part.getBook());
            object.field("page", part.getPage());
            object.field("text", part.getText());
            
            indexes.add(object);
        }
        
        return indexes;
    }
    
    
}
