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
package org.seasar.fisshplate.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * 行要素クラスです。行の中にあるセルの情報を保持します。
 * @author rokugen
 *
 */
public class Row implements TemplateElement {
	private List cellElementList = new ArrayList();	
	private HSSFRow templateRow;
	private Root root;
	
	private static final Pattern patEl = Pattern.compile("^\\s*\\$\\{(.+)\\}");
	
	/**
	 * コンストラクタです。テンプレート側の行オブジェクトを受け取り、その行内のセル情報を解析して保持します。
	 * @param templateRow テンプレート側の行オブジェクト
	 */
	Row(HSSFSheet templateSheet, HSSFRow templateRow, Root root){		
		this.root = root;
		this.templateRow = templateRow;
		if(templateRow == null){
			return;
		}		
		int rowNum = templateRow.getRowNum();
		//TODO リファクタリングしましょうよ
		for(int i=0; i <= templateRow.getLastCellNum();i++){
			HSSFCell templateCell = templateRow.getCell((short) i);
			if(templateCell == null){
				continue;
			}
			TemplateElement elem = new NullElement();
			if(templateCell.getCellType() == HSSFCell.CELL_TYPE_STRING){			
				String value = templateCell.getRichStringCellValue().getString();
				Matcher mat;			
				if((mat = patEl.matcher(value)).find()){
					String expression = mat.group(1);
					elem = new El(templateSheet,templateCell, rowNum, expression);
				}else{
					elem = new Literal(templateSheet,templateCell,rowNum);
				}
			}else{
				elem = new Literal(templateSheet,templateCell,rowNum);
			}
			cellElementList.add(elem);
		}
	}
	
	public void merge(FPContext context) throws FPMergeException {
		if(context.shouldHeaderOut()){
			context.setShouldHeaderOut(false);
			root.getPageHeader().merge(context);			
		}
		HSSFRow outRow = context.getcurrentRow();
		if(templateRow != null){
			outRow.setHeight(templateRow.getHeight());
		}
		Map data = context.getData();
		data.put(FPConsts.ROW_NUMBER_NAME, Integer.valueOf(context.getCurrentRowNum() + 1));
		for(int i=0; i < cellElementList.size(); i++){
			TemplateElement elem = (TemplateElement) cellElementList.get(i);			
			elem.merge(context);
		}
		context.nextRow();
	}

}
