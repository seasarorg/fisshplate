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
import org.seasar.fisshplate.core.element.ElseBlock;
import org.seasar.fisshplate.core.element.ElseIfBlock;
import org.seasar.fisshplate.core.element.IfBlock;
import org.seasar.fisshplate.core.element.PageFooterBlock;
import org.seasar.fisshplate.core.element.PageHeaderBlock;
import org.seasar.fisshplate.core.element.Root;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * endを解析するクラスです。
 * @author rokugen
 */
public class EndParser implements RowParser {
    private static final Pattern patEnd = Pattern.compile("(^\\s*#end\\s*$|#pageHeaderEnd|#pageFooterEnd)");
    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.parser.StatementParser#process(org.seasar.fisshplate.wrapper.CellWrapper, org.seasar.fisshplate.core.parser.FPParser)
     */
    public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
        String value = cell.getStringValue();
        Matcher mat  = patEnd.matcher(value);
        if(!mat.find()){
            return false;
        }
        checkBlockStack(cell, parser);
        processEnd(parser);
        return true;
    }

    private void checkBlockStack(CellWrapper cell, FPParser parser)	throws FPParseException {
        if (parser.isBlockStackBlank()) {
            throw new FPParseException(FPConsts.MESSAGE_ID_END_ELEMENT,cell.getRow());
        }
    }

    private void processEnd(FPParser parser)	throws FPParseException {
        Root root = parser.getRoot();
        AbstractBlock block = parser.popFromBlockStack();
        Class<? extends AbstractBlock> clazz = block.getClass();

        if (clazz == ElseBlock.class || clazz == ElseIfBlock.class) {
            block = getIfBlockFromStack(parser);
        }else if (clazz == PageHeaderBlock.class) {
            root.setPageHeader(block);
            return ;
        }else if (clazz == PageFooterBlock.class) {
            root.setPageFooter(block);
            return ;
        }

        if(parser.isBlockStackBlank()) {
            root.addBody(block);
        }
    }

    /**
     * Else If か Elseの場合、元になるIfが出るまで継続してPopする。
     * @param parser 呼び出し元FPParser
     * @return Ifブロック
     */
    private AbstractBlock getIfBlockFromStack(FPParser parser){
        // elseとelse ifの場合、ifが出るまでpop継続する
        AbstractBlock block = null;
        while (block == null || block.getClass() != IfBlock.class) {
            block = parser.popFromBlockStack();
        }
        return block;
    }

}
