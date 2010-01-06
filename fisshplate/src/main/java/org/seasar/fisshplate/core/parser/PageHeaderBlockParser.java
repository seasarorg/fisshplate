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

import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.PageHeaderBlock;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * pageHeaderを解析するクラスです。
 * @author rokugen
 */
public class PageHeaderBlockParser implements RowParser {
    private static final Pattern patPageHeaderStart = Pattern.compile("#pageHeaderStart");
    public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
        String value = cell.getStringValue();
        Matcher mat = patPageHeaderStart.matcher(value);
        if(!mat.find()){
            return false;
        }
        AbstractBlock block = new PageHeaderBlock();
        parser.pushBlockToStack(block);
        return true;
    }

}
