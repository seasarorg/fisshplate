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

package org.seasar.fisshplate.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

/**
 * POIの操作の便利メソッドを集めたユーティリティクラスです。
 * @author rokugen
 *
 */
public class FPPoiUtil {
    private FPPoiUtil(){}

    /**
     * セルの書式設定に基いてセルの値を戻します。
     * @param hssfCell
     * @return セルの値
     */
    public static Object getCellValueAsObject(HSSFCell hssfCell) {
        if(hssfCell == null){
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

    private static Object getValueFromNumericCell(HSSFCell cell){
        String str = cell.toString();
        if(str.matches("\\d+-.+-\\d+")){
            return cell.getDateCellValue();
        }else{
            return new Double(cell.getNumericCellValue());
        }
    }

    /**
     *文字列を含むセルの値を文字列として戻します。
     *セルの書式が文字列でない場合はnullを戻します。
     * @param hssfCell
     * @return セルの値
     */
    public static String getStringValue(HSSFCell hssfCell){
        if(! isStringCell(hssfCell)){
            return null;
        }
        HSSFRichTextString richVal =  hssfCell.getRichStringCellValue();
        if(richVal == null){
            return null;
        }
        return richVal.getString();
    }

    private static boolean isStringCell(HSSFCell hssfCell){
        if(hssfCell == null){
            return false;
        }
        int type = hssfCell.getCellType();
        if(type != HSSFCell.CELL_TYPE_BLANK &&
                type != HSSFCell.CELL_TYPE_STRING){
            return false;
        }
        return true;
    }

}
