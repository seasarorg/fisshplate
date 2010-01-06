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

package org.seasar.fisshplate.core.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.IteratorBlock;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * foreach要素を解析するクラスです。
 * @author rokugen
 */
public class IteratorBlockParser implements RowParser{
    private static final Pattern patIterator = Pattern.compile("^\\s*#foreach\\s+(\\S+)\\s*:\\s*(\\S+)(\\s+index=(\\S+))*(\\s+max=(\\S+))*\\s*$");


    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.StatementParser#process(org.seasar.fisshplate.wrapper.CellWrapper, org.seasar.fisshplate.core.parser.FPParser)
     */
    public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
        String value = cell.getStringValue();
        Matcher mat = patIterator.matcher(value);
        if(!mat.find()){
            return false;
        }
        AbstractBlock block = buildElement(cell, mat);

        parser.addBlockElement(block);
        return true;
    }

    private AbstractBlock buildElement(CellWrapper cell, Matcher mat) throws FPParseException{
        RowWrapper row = cell.getRow();

        String varName = mat.group(1);
        String iteratorName = mat.group(2);
        String indexName = mat.group(4);
        String maxString = mat.group(6);
        int max = 0;
        if(maxString != null){
            try{
                max = Integer.parseInt(maxString);
            }catch(NumberFormatException ex){
                throw new FPParseException(FPConsts.MESSAGE_ID_NOT_ITERATOR_INVALID_MAX,row);
            }
        }

        return new IteratorBlock(row, varName, iteratorName, indexName, max);

    }

}
