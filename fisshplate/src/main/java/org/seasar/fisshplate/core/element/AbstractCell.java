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

package org.seasar.fisshplate.core.element;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * セル要素の基底抽象クラスです。
 * @author rokugen
 *
 */
public abstract class AbstractCell implements TemplateElement {
    /**
     * テンプレート側のセル
     */
    protected CellWrapper cell;
    private boolean isMergedCell;
    private short relativeMergedColumnTo;
    private int relativeMergedRowNumTo;
    private Object cellValue;


    /**
     * コンストラクタです。
     *
     * @param cell
     */
    AbstractCell(CellWrapper cell) {
        this.cell = cell;
        Sheet templateSheet = cell.getRow().getSheet().getHSSFSheet();
        int rowNum = cell.getRow().getHSSFRow().getRowNum();

        //マージ情報をなめて、スタート地点が合致すれば保存しておく。
        for(int i=0; i < templateSheet.getNumMergedRegions();i++){
            CellRangeAddress reg = templateSheet.getMergedRegion(i);
            setUpMergedCellInfo(cell.getHSSFCell().getColumnIndex(), rowNum, reg);
            if(isMergedCell){
                break;
            }
        }

        cellValue = cell.getObjectValue();
    }



    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.element.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
     */
    public void merge(FPContext context) throws FPMergeException {
        Cell out = context.getCurrentCell();
        copyCellStyle(context, out);

        mergeImpl(context,out);

        context.nextCell();
    }

    /**
     * このクラスを継承したクラスで実装される、データ埋め込み処理の実装です。
     * @param context コンテキスト
     * @param out 出力先のセル
     * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられる例外です。
     */
    abstract void mergeImpl(FPContext context, Cell out) throws FPMergeException ;



    private void setUpMergedCellInfo(int cellNum, int rowNum, CellRangeAddress reg){
        if(reg.getFirstColumn() != cellNum || reg.getFirstRow() != rowNum){
            isMergedCell = false;
            return;
        }
        isMergedCell = true;
        relativeMergedColumnTo = (short) (reg.getLastColumn() - reg.getFirstColumn());
        relativeMergedRowNumTo = reg.getLastRow() - reg.getFirstRow();
    }


    /**
     * テンプレート側のセルのスタイルを出力側へコピーします。フォントやデータフォーマットも反映されます。
     * @param context コンテキスト
     * @param outCell 出力するセル
     */
    protected void copyCellStyle(FPContext context, Cell outCell) {
        CellStyle outStyle = cell.getCellStyle();
        outCell.setCellStyle(outStyle);
        if(isMergedCell){
            mergeCell(context);
        }
    }

    private void mergeCell(FPContext context){
        int columnFrom = context.getCurrentCellNum();
        int rowFrom = context.getCurrentRowNum();

        CellRangeAddress reg = new CellRangeAddress(
                rowFrom, rowFrom + relativeMergedRowNumTo,
                columnFrom, columnFrom + relativeMergedColumnTo);
        Sheet hssfSheet = context.getOutSheet();
        hssfSheet.addMergedRegion(reg);
    }

    public Object getCellValue() {
        return cellValue;
    }

    public void setCellValue(Object cellValue) {
        this.cellValue = cellValue;
    }

}
