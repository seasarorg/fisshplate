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
import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.Link;
import org.seasar.fisshplate.enums.LinkElementType;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * linkを解析するクラスです。
 * @author rokugen
 */
public class LinkParser implements CellParser {
    private static final Pattern pat = Pattern.compile(FPConsts.REGEX_LINK);
    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.CellParser#getElement(org.seasar.fisshplate.wrapper.CellWrapper, java.lang.String)
     */
    public AbstractCell getElement(CellWrapper cell, String value) {
        Matcher mat = pat.matcher(value);

        if(!mat.find() || !isValidType(mat)){
            return null;
        }

        return new Link(cell);
    }

    private boolean isValidType(Matcher mat){
        String type = mat.group(1);
        LinkElementType elemType = LinkElementType.get(type);
        return elemType != null;
    }
}
