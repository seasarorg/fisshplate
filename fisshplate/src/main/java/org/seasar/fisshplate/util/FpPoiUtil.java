package org.seasar.fisshplate.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

public class FpPoiUtil {
    private FpPoiUtil(){}

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
        if(hssfCell == null){
            return null;
        }
        HSSFRichTextString richVal =  hssfCell.getRichStringCellValue();
        if(richVal == null){
            return null;
        }
        return richVal.getString();
    }

}
