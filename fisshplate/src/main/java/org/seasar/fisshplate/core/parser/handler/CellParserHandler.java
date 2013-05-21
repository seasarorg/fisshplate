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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.El;
import org.seasar.fisshplate.core.element.GenericCell;
import org.seasar.fisshplate.core.element.NullCell;
import org.seasar.fisshplate.core.element.Suspend;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.CellParser;
import org.seasar.fisshplate.core.parser.LinkParser;
import org.seasar.fisshplate.core.parser.PictureParser;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * @author rokugen
 */
public class CellParserHandler {
    private static final Pattern patEl = Pattern.compile(FPConsts.REGEX_BIND_VAR);
    private static final Pattern patSuspend = Pattern.compile("^\\s*#suspend\\s+(.*" + FPConsts.REGEX_BIND_VAR + ".*)");

	private static CellParser[] builtInCellParser =
        new CellParser[]{
            new PictureParser(),
            new LinkParser()
        };

    /**
     * セルの内容を解析し、合致した{@link TemplateElement}を戻します。
     * @param cell 解析するセル
     * @return 合致した要素
     */
    public TemplateElement getElement(CellWrapper cell) {
        if (cell.getHSSFCell() == null) {
            return new NullCell();
        }

        String value = getCellValue(cell);
        if(value == null){
            return new GenericCell(cell);
        }


        AbstractCell cellElem = getElementByParsers(cell, value);

        Matcher mat = patEl.matcher(value);
        if(mat.find()){
            return createEl(cellElem,value);
        }else{
            return cellElem;
        }

    }

    private AbstractCell getElementByParsers(CellWrapper cell, String value){
        AbstractCell cellElem = null;
        for(int i=0; i < builtInCellParser.length; i++){
            CellParser parser = builtInCellParser[i];
            cellElem = parser.getElement(cell,value);
            if(cellElem != null){
                break;
            }
        }

        if(cellElem == null){
            cellElem = new GenericCell(cell);
        }

        return cellElem;

    }

    private String getCellValue(CellWrapper cell){
        Cell hssfCell = cell.getHSSFCell();
        String value = null;
        if (hssfCell.getCellType() == Cell.CELL_TYPE_STRING){
            value = hssfCell.getRichStringCellValue().getString();
        }else if(hssfCell.getCellType() == Cell.CELL_TYPE_FORMULA ){
            value = hssfCell.getCellFormula();
        }
        return value;
    }


    private TemplateElement createEl(AbstractCell cellElem, String value){

        Matcher mat = patSuspend.matcher(value);
        if(mat.find()){
            cellElem.setCellValue(mat.group(1));
            El el = new El(cellElem);
            return new Suspend(el);
        }else{
            return new El(cellElem);
        }
    }

}
