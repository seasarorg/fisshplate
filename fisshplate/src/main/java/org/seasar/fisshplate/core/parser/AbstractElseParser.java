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
import org.seasar.fisshplate.core.element.IfBlock;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;


/**
 * else if と else を解析するクラスのための抽象クラスです。
 * @author rokugen
 */
public abstract class AbstractElseParser implements RowParser {



    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.StatementParser#process(org.seasar.fisshplate.wrapper.CellWrapper, org.seasar.fisshplate.core.parser.FPParser)
     */
    public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
        String value = cell.getStringValue();
        Matcher mat = getPattern().matcher(value);
        if(!mat.find()){
            return false;
        }
        checkBlockStack(parser, cell);
        processElse(getCondition(mat), cell, parser);
        return true;
    }

    private void checkBlockStack(FPParser parser, CellWrapper cell) throws FPParseException{
        if (parser.isBlockStackBlank()) {
            throw new FPParseException(FPConsts.MESSAGE_ID_LACK_IF,cell.getRow());
        }
    }

    private String getCondition(Matcher mat){
        String condition = null;
        if(mat.groupCount() > 0){
            condition = mat.group(1);
        }
        return condition;
    }



    private void processElse(String condition,CellWrapper cell, FPParser parser)	throws FPParseException {
        RowWrapper row = cell.getRow();
        AbstractBlock parent = getParentIfBlock(row, parser);
        AbstractBlock block = createElement(condition);
        ((IfBlock) parent).setNextBlock(block);
        parser.pushBlockToStack(block);
    }



    private AbstractBlock getParentIfBlock(RowWrapper row,FPParser parser) throws FPParseException {
        AbstractBlock parent = parser.getLastElementFromStack();
        if (!(parent instanceof IfBlock)) {
            throw new FPParseException(FPConsts.MESSAGE_ID_LACK_IF,row);
        }
        return parent;
    }

    /**
     * 要素クラスを生成し戻します。
     * @param condition 条件式
     * @return 要素クラス
     */
    protected abstract AbstractBlock createElement(String condition);

    /**
     * 解析対象か否かの判定に使う{@link Pattern}を戻します。
     * @return Patternクラス
     */
    protected abstract Pattern getPattern();


}
