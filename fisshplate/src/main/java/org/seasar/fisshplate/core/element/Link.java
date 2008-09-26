/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
 */
package org.seasar.fisshplate.core.element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.enums.LinkElementType;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * セルにハイパーリンクを埋めこむ要素です。
 * @author rokugen
 */
public class Link extends AbstractCell {
    
    private static Pattern pat = Pattern.compile(FPConsts.REGEX_LINK);
    
    public Link(CellWrapper cell){
        super(cell);
    }

    void mergeImpl(FPContext context, HSSFCell out) throws FPMergeException {
        Matcher mat = pat.matcher(getCellValue().toString());
        mat.find();
        String type = mat.group(1);
        String link = mat.group(2);
        String text = mat.group(3);
        
        LinkElementType linkType = LinkElementType.get(type);
        HSSFHyperlink hyperLink = linkType.createHyperLink();
        hyperLink.setAddress(link);
        out.setHyperlink(hyperLink);
        out.setCellValue(new HSSFRichTextString(text));

    }

}
