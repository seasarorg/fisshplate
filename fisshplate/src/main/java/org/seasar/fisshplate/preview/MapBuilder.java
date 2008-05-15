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
package org.seasar.fisshplate.preview;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.core.BindVariable;
import org.seasar.fisshplate.exception.FPPreviewException;
import org.seasar.fisshplate.util.StringUtil;
import org.seasar.fisshplate.wrapper.SheetWrapper;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * EXCELからテンプレートに埋め込むMapを生成するクラスです。
 * 
 * @author rokugen
 */
public class MapBuilder {
	private static final String ROOT_SHEET_NAME = "root";

	/**
	 * EXCELに記載されたデータから、テンプレート埋め込み用{@link Map}を生成して戻します。
	 * @param wb 埋め込みデータが記載された{@link HSSFWorkbook}
	 * @return テンプレート埋め込み用{@link Map}
	 */
	public Map buildMapFrom(HSSFWorkbook wb) {
		WorkbookWrapper workbook = new WorkbookWrapper(wb);
		
		SheetWrapper sheet = workbook.getSheetByName(ROOT_SHEET_NAME);
		FPMapData root = new FPMapData(sheet, ROOT_SHEET_NAME);		
		buildRootMapData(workbook, root);
		return (Map) root.buildData();
	}

	private void buildRootMapData(WorkbookWrapper workbook, FPMapData root) {		
		for(int i=0; i < workbook.getSheetCount(); i++){
			SheetWrapper sheet = workbook.getSheetAt(i);
			String keyName = getKeyNameFrom(sheet);
			if(! ROOT_SHEET_NAME.equals(keyName)){
				buildMapData(root, sheet, keyName);
			}
		}
	}
	
	private String getKeyNameFrom(SheetWrapper sheet){
		String col = sheet.getRow(0).getCell(0).getStringValue();				
		
		if(!StringUtil.isEmpty(col) && Pattern.matches(FPConsts.REGEX_BIND_VAR, col)){
			sheet.removeRow(0);
			BindVariable var = new BindVariable(col);
			return var.getName();
		}else{		
			return sheet.getSheetName();
		}
		
	}
	
	private void buildMapData(FPMapData parent, SheetWrapper sheet, String keyName){
		int idx = keyName.indexOf('#');
		if(idx == -1){
			parent.addChild(sheet, keyName);			
		}else{
			buildMapDataComposition(parent, sheet, keyName, idx);
		}
	}

	private void buildMapDataComposition(FPMapData grandParent, SheetWrapper sheet,String keyName, int idx) {
		String selfKeyName = keyName.substring(idx + 1);
		String parentKeyName = keyName.substring(0, idx);
		FPMapData parent= grandParent.getChildByKey(parentKeyName);
		if(parent == null){
			throw new FPPreviewException(FPConsts.MESSAGE_ID_PREVIEW_LACCK_OF_PARENT,new Object[]{parentKeyName});
		}
		buildMapData(parent, sheet, selfKeyName);
	}
	
	

}
