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
        if(isNullCell()){
            return null;
        }
        HSSFRichTextString richVal =  hssfCell.getRichStringCellValue();
        if(richVal == null){
            return null;
        }

        return richVal.getString();
    }

    public Object getObjectValue() {
        if(isNullCell()){
            return null;
        }
        int cellType = hssfCell.getCellType();
        Object ret = null;

        switch(cellType){
        case HSSFCell.CELL_TYPE_NUMERIC:
            ret = getValueFromNumericCell(hssfCell);
            break;
        case HSSFCell.CELL_TYPE_STRING:
            ret = hssfCell.getRichStringCellValue().getString();
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            ret = Boolean.valueOf(hssfCell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_FORMULA:
            ret = hssfCell.getCellFormula();
            break;
        case HSSFCell.CELL_TYPE_ERROR:
            ret = new Byte(hssfCell.getErrorCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            break;
        default:
            return null;
        }

        return ret;
    }
    private Object getValueFromNumericCell(HSSFCell cell){
        String str = cell.toString();
        if(str.matches("\\d+-.+-\\d+")){
            return cell.getDateCellValue();
        }else{
            return new Double(cell.getNumericCellValue());
        }


    }

}