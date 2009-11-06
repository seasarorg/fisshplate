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
package org.seasar.fisshplate.core.parser;

import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.Root;
import org.seasar.fisshplate.core.element.Row;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.handler.CellParserHandler;
import org.seasar.fisshplate.core.parser.handler.RowParserHandler;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;
import org.seasar.fisshplate.wrapper.SheetWrapper;

/**
 * テンプレート側のシートを解析し、要素クラスの構造を組み立てて保持します。
 *
 * @author rokugen
 * @author a-conv
 *
 */
public class FPParser {

    private Root rootElement;
    private Stack blockStack = new Stack();

    private RowParserHandler rowParserHandler = new RowParserHandler();
    private CellParserHandler cellParserHandler = new CellParserHandler();

    /**
     * コンストラクタです。
     */
    public FPParser(){

    }

    /**
     * 引数で渡されたテンプレートのシートを元に解析し、ルートの要素リストを戻します。
     * @param sheet
     *            テンプレートのシート
     * @return 要素リスト
     * @throws FPParseException
     *             テンプレートの解析時に構文上のエラーが判明した際に投げられます。
     */
    public Root parse(SheetWrapper sheet) throws FPParseException {
        rootElement = new Root();
        for (int i = 0; i < sheet.getRowCount(); i++) {
            parseRow(sheet.getRow(i));
        }
        // スタックにまだブロックが残ってたら#end不足
        if (blockStack.size() > 0) {
            throw new FPParseException(FPConsts.MESSAGE_ID_END_ELEMENT  ,
                    new Object[]{"?"});
        }

        return rootElement;
    }


    /**
     * ルートの要素リストを戻します。
     * @return 要素リスト
     */
    public Root getRoot()  {
        return rootElement;
    }


    private void parseRow(RowWrapper row) throws FPParseException {
        if (row.isNullRow()) {
            createRowElement(row);
            return;
        }

        for(int i=0; i < row.getCellCount(); i ++){
            CellWrapper cell = row.getCell(i);

            if(!isCellParsable(cell)){
                continue;
            }

            if(rowParserHandler.parse(cell, this)){
                return;
            }
        }
        createRowElement(row);
    }


    private void createRowElement(RowWrapper row){
        Row rowElem = new Row(row, rootElement,cellParserHandler);
        addTemplateElement(rowElem);
    }

    private boolean isCellParsable(CellWrapper cell){
        if(cell == null){
            return false;
        }

        HSSFCell hssfCell = cell.getHSSFCell();
        if (hssfCell == null || (hssfCell.getCellType() != HSSFCell.CELL_TYPE_STRING)) {
            return false;
        }

        return true;
    }

    /**
     * ブロック要素に親要素がある場合、その親要素にブロック要素を子要素として追加します。
     * @param block ブロック要素
     */
    public void addBlockElement(AbstractBlock block){
        if (! isBlockStackBlank()) {
            AbstractBlock parentBlock = (AbstractBlock) blockStack.lastElement();
            parentBlock.addChild(block);
        }
        pushBlockToStack(block);
    }

    /**
     * ブロックの閉じ判定用スタックにブロック要素を追加します。
     * @param block ブロック要素
     */
    public void pushBlockToStack(AbstractBlock block){
        blockStack.push(block);
    }

    /**
     * 要素を親要素があれば子要素として追加します。親要素がなければルートにボディ要素として追加します。
     * @param elem 要素
     */
    public void addTemplateElement(TemplateElement elem){
        if (!isBlockStackBlank()) {
            AbstractBlock block = (AbstractBlock) blockStack.lastElement();
            block.addChild(elem);
        } else {
            rootElement.addBody(elem);
        }
    }

    /**
     * ブロックの閉じ判定用スタックが空か否かを戻します。
     * @return 空ならばtrue。
     */
    public boolean isBlockStackBlank(){
        return (blockStack.size() < 1);
    }

    /**
     * ブロックの閉じ判定用スタックからポップします。
     * @return ブロック要素
     */
    public AbstractBlock popFromBlockStack(){
        return (AbstractBlock) blockStack.pop();
    }

    /**
     * ブロックの閉じ判定用スタックから最後の要素を取得して戻します。
     * @return 最後の要素
     */
    public AbstractBlock getLastElementFromStack(){
        return (AbstractBlock) blockStack.lastElement();
    }

    /**
     * 独自にカスタマイズした行単位のパーサを追加します。
     * @param parser 追加するパーサ
     */
    public void addRowParser(RowParser parser){
        rowParserHandler.addRowParser(parser);
    }

}
