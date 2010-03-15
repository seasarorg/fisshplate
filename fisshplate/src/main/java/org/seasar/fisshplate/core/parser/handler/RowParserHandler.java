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

package org.seasar.fisshplate.core.parser.handler;

import java.util.ArrayList;
import java.util.List;

import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.CommentParser;
import org.seasar.fisshplate.core.parser.ElseBlockParser;
import org.seasar.fisshplate.core.parser.ElseIfBlockParser;
import org.seasar.fisshplate.core.parser.EndParser;
import org.seasar.fisshplate.core.parser.ExecParser;
import org.seasar.fisshplate.core.parser.FPParser;
import org.seasar.fisshplate.core.parser.HorizontalIteratorBlockParser;
import org.seasar.fisshplate.core.parser.IfBlockParser;
import org.seasar.fisshplate.core.parser.IteratorBlockParser;
import org.seasar.fisshplate.core.parser.PageBreakParser;
import org.seasar.fisshplate.core.parser.PageFooterBlockParser;
import org.seasar.fisshplate.core.parser.PageHeaderBlockParser;
import org.seasar.fisshplate.core.parser.ResumeParser;
import org.seasar.fisshplate.core.parser.RowParser;
import org.seasar.fisshplate.core.parser.VarParser;
import org.seasar.fisshplate.core.parser.WhileParser;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * 行単位のタグを解析するパーサーを管理し、パースするクラスです。
 * @author rokugen
 */
public class RowParserHandler {
    private RowParser[] builtInRowParsers = {
            new IteratorBlockParser(),
            new IfBlockParser(),
            new ElseIfBlockParser(),
            new ElseBlockParser(),
            new EndParser(),
            new PageBreakParser(),
            new CommentParser(),
            new VarParser(),
            new ExecParser(),
            new PageHeaderBlockParser(),
            new PageFooterBlockParser(),
            new ResumeParser(),
            new WhileParser(),
            new HorizontalIteratorBlockParser()
    };

    private List<RowParser> addOnRowParser = new ArrayList<RowParser>();

    /**
     * 自身に登録された{@link RowParser}を使ってcellを解析します。
     * 解析対象であれば{@link TemplateElement}を生成し、
     * 呼び出し元{@link FPParser}に追加してtrueを戻します。
     * 解析対象外であれば、falseを戻します。
     * @param cell パースするセル
     * @param parser 呼び出し元FPParser
     * @return パース対象であればtrue
     * @throws FPParseException 解析時にエラーが発生した際に投げられます。
     */
    public boolean parse(CellWrapper cell,FPParser parser) throws FPParseException{
        for(int i=0; i < builtInRowParsers.length; i++){
            if(builtInRowParsers[i].process(cell,parser)){
                return true;
            }
        }

        for(int i=0; i < addOnRowParser.size(); i++){
            if ( ((RowParser) addOnRowParser.get(i)).process(cell, parser) ){
                return true;
            }
        }
        return false;
    }

    /**
     * 独自にカスタマイズしたパーサを追加します。
     * @param parser 追加するパーサ
     */
    public void addRowParser(RowParser parser){
        addOnRowParser.add(parser);
    }

}
