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
import org.seasar.fisshplate.util.StringUtil;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;
import org.seasar.fisshplate.wrapper.SheetWrapper;

/**
 * @author rokugen
 */
public class FPMapData  {
	protected String keyName;
	protected SheetWrapper sheet;
	protected List childList = new ArrayList();
	
	FPMapData(SheetWrapper sheet, String keyName){
		this.sheet = sheet;
		this.keyName = keyName;
	}
	
	
	public String getKeyName(){
		return keyName;
	}	
	
	public void addChild(SheetWrapper sheet, String keyName) {		
		childList.add(new FPMapData(sheet, keyName));		
	}
	
	public Object buildData() {
		if(sheet == null || sheet.getRowCount() <= 2){
			return buildMapData();
		}else{
			return buildListData();			
		}
	}
	
	public FPMapData getChildByKey(String keyName){
		for(int i=0; i < childList.size(); i++){
			FPMapData mapData = (FPMapData) childList.get(i);
			if(mapData.getKeyName().equals(keyName)){
				return mapData;
			}
		}
		return null;
	}

	
	protected Map buildMapData() {
		Map data = new HashMap();
		if(sheet != null){
			RowWrapper keys = sheet.getRow(0);
			RowWrapper vals = sheet.getRow(1);		
			putValToMap(data, keys, vals);
		}
		
		buildChildData(data);

		return data;
	}
	
	protected List buildListData() {
		RowWrapper keys = sheet.getRow(0);
		List list = new ArrayList();
		for(int i=1; i < sheet.getRowCount(); i++){
			Map item = new HashMap();
			RowWrapper vals = sheet.getRow(i);
			putValToMap(item, keys, vals);
			buildChildData(item);
			list.add(item);
		}
		return list;
	}
	
	protected void buildChildData(Map data){
		for(int i=0; i < childList.size(); i++){
			FPMapData mapData = (FPMapData) childList.get(i);
			Object childData = mapData.buildData();
			data.put(mapData.getKeyName(), childData);			
		}
	}

	protected void putValToMap(Map data, RowWrapper keys, RowWrapper vals){
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
