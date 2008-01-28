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
package org.seasar.fisshplate.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.util.StringUtil;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;
import org.seasar.fisshplate.wrapper.SheetWrapper;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * EXCELからテンプレートに埋め込むMapを生成するクラスです。
 * 
 * @author rokugen
 */
public class MapBuilder {
	private static final String ROOT_SHEET_NAME = "root";

	public Map buildMapFrom(HSSFWorkbook wb) {
		WorkbookWrapper workbook = new WorkbookWrapper(wb);
		Map data = new HashMap();
		
		for(int i=0; i < workbook.getSheetCount(); i++){
			SheetWrapper sheet = workbook.getSheetAt(i);
			if(ROOT_SHEET_NAME.equals(sheet.getSheetName())){
				buildRootMap(data, sheet);
			}else{
				buildMap(data, sheet);
			}
		}		

		return data;
	}
	
	private void buildMap(Map data, SheetWrapper sheet){
		String sheetName = sheet.getSheetName();
		RowWrapper keys = sheet.getRow(0);
		
		
		if(sheet.getRowCount() > 2){
			List list = new ArrayList();
			data.put(sheetName, list);
			for(int i=1; i < sheet.getRowCount(); i++){
				Map item = new HashMap();
				RowWrapper vals = sheet.getRow(i);
				putValToMap(item, keys, vals);			
				list.add(item);
			}
			
		}else{
			Map map = new HashMap();
			data.put(sheetName, map);
			RowWrapper vals = sheet.getRow(1);
			putValToMap(map, keys, vals);
		}		
				
		
	}
	
	private void buildRootMap(Map data, SheetWrapper rootSheet){		
		RowWrapper keys = rootSheet.getRow(0);
		RowWrapper vals = rootSheet.getRow(1);		
		putValToMap(data, keys, vals);
	}
	
	private void putValToMap(Map data, RowWrapper keys, RowWrapper vals){
		for(int i=0; i < keys.getCellCount(); i++){
			CellWrapper key = keys.getCell(i);
			if(key.isNullCell()){
				continue;
			}
			CellWrapper val = vals.getCell(i);
			
			String keyStr = key.getStringValue();
			Object valObj = null;
			if(isCellDateFormatted(val)){
				valObj = val.getHSSFCell().getDateCellValue();
			}else{
				valObj = val.getObjectValue();
			}
			
			data.put(keyStr, valObj);
		}
	}
	
    private boolean isCellDateFormatted(CellWrapper cell) {
        HSSFCellStyle cs = cell.getHSSFCell().getCellStyle();
        short dfNum = cs.getDataFormat();
        HSSFDataFormat dataFormat = cell.getRow().getSheet().getWorkbook().getHSSFWorkbook().createDataFormat();
        String format = dataFormat.getFormat(dfNum);
        if (StringUtil.isEmpty(format)) {
            return false;
        }
        if (format.indexOf('/') > 0 || format.indexOf('y') > 0
                || format.indexOf('m') > 0 || format.indexOf('d') > 0) {
            return true;
        }
        return false;
    }

	

}
