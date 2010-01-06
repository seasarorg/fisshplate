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

import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.Picture;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * pictureを解析するクラスです。
 * @author rokugen
 *
 */
public class PictureParser implements CellParser {
    private static final Pattern patPicture = Pattern.compile("^\\s*\\#picture\\(.+\\s+cell=.+\\s*\\s+row=.+\\)");

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.CellParser#getElement(org.seasar.fisshplate.wrapper.CellWrapper, java.lang.String)
     */
    public AbstractCell getElement(CellWrapper cell, String value) {
        AbstractCell cellElem = null;
        Matcher mat = patPicture.matcher(value);

        if(mat.find()){
            cellElem = new Picture(cell);
        }
        return cellElem;
    }

}
