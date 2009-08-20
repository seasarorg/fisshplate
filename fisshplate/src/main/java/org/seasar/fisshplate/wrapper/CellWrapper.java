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
package org.seasar.fisshplate.wrapper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.seasar.fisshplate.util.FpPoiUtil;

/**
 * HSSFCellのラッパークラスです。
 * @author rokugen
 */
public class CellWrapper {
    private HSSFCell hssfCell;
    private RowWrapper row;

    public CellWrapper(HSSFCell cell, RowWrapper row){
        this.row = row;
        this.hssfCell = cell;

    }

    public HSSFCell getHSSFCell(){
        return hssfCell;
    }

    public RowWrapper getRow(){
        return row;
    }

    public boolean isNullCell() {
        return hssfCell == null;
    }

    public int getCellIndex(){
        if(isNullCell()){
            return -1;
        }
        return (int)hssfCell.getCellNum();
    }

    public String getStringValue(){
        return FpPoiUtil.getStringValue(hssfCell);
    }

    public Object getObjectValue() {
        return FpPoiUtil.getCellValueAsObject(hssfCell);
    }

}