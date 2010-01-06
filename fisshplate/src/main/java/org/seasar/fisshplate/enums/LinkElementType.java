/**
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 */

package org.seasar.fisshplate.enums;

import org.apache.poi.hssf.usermodel.HSSFHyperlink;

/**
 * @author rokugen
 */
public class LinkElementType {
    private int linkType;

    public static final LinkElementType URL = new LinkElementType(HSSFHyperlink.LINK_URL);
    public static final LinkElementType EMAIL = new LinkElementType(HSSFHyperlink.LINK_EMAIL);
    public static final LinkElementType THIS = new LinkElementType(HSSFHyperlink.LINK_DOCUMENT);
    public static final LinkElementType FILE = new LinkElementType(HSSFHyperlink.LINK_FILE);

    private LinkElementType(int type){
        this.linkType = type;
    }

    public int getType(){
        return linkType;
    }

    public HSSFHyperlink createHyperLink(){
        return new HSSFHyperlink(linkType);
    }

    public static LinkElementType get(String type){
        if("url".equals(type)){
            return URL;
        }else if("email".equals(type)){
            return EMAIL;
        }else if("file".equals(type)){
            return FILE;
        }else if("this".equals(type)){
            return THIS;
        }else {
            return null;
        }
    }
}
