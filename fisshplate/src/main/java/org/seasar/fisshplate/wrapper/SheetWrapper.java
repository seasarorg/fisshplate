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

package org.seasar.fisshplate.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * HSSFSheetのラッパークラスです。
 * @author rokugen
 */
public class SheetWrapper {
    private Sheet hssfSheet;
    private WorkbookWrapper workbook;
    private List<RowWrapper> rowList = new ArrayList<RowWrapper>();
    private int sheetIndex;

    public SheetWrapper(Sheet sheet, WorkbookWrapper workbook, int sheetIndex){
        this.workbook = workbook;
        this.hssfSheet = sheet;
        this.sheetIndex = sheetIndex;
        for(int i=0; i <= sheet.getLastRowNum(); i++){
            rowList.add(new RowWrapper(sheet.getRow(i),this));
        }
    }

    public Sheet getHSSFSheet(){
        return hssfSheet;
    }

    public WorkbookWrapper getWorkbook(){
        return workbook;
    }

    public RowWrapper getRow(int index){
        return (RowWrapper) rowList.get(index);
    }

    public int getRowCount() {
        return rowList.size();
    }
    public int getSheetIndex() {
        return sheetIndex;
    }

    public String getSheetName(){
        return workbook.getHSSFWorkbook().getSheetName(sheetIndex);
    }

    public void removeRow(int i){
        RowWrapper row = this.getRow(i);
        hssfSheet.removeRow(row.getHSSFRow());
        rowList.remove(i);
    }

    /**
     * データ埋め込みの準備のために、シートを初期化します。
     */
    public void prepareForMerge(){
        removeAllRow();
        removeAllMergedRegion();
    }

    private void removeAllRow(){
       for(int i=0; i < getRowCount();i++){
            Row hssfRow = getRow(i).getHSSFRow();
            if(hssfRow != null){
                hssfSheet.removeRow(hssfRow);
            }
        }
    }

    @SuppressWarnings("unused")
	private void removeAllMergedRegion(){
        for(int i=0; 0 < hssfSheet.getNumMergedRegions();i++){
            hssfSheet.removeMergedRegion(0);
        }
    }

}
