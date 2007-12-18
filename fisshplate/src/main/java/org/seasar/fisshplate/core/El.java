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

import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

/**
 * テンプレートのセルの値が評価式の場合の要素クラスです。OGNLで評価します。
 * @author rokugen
 *
 */
public class El extends AbstractCell {
	private ElExpression expression;

	/**
	 * コンストラクタです。
	 * @param sheet テンプレート側のシート
	 * @param cell テンプレート側のセル
	 * @param rowNum 行番号
	 * @param expression 評価式
	 */
	El(HSSFSheet sheet, HSSFCell cell,  int rowNum, String expression) {
		super(sheet, cell, rowNum);
		this.expression = new ElExpression(expression);
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		HSSFCell out = context.getCurrentCell();		
		copyCellStyle(context, out);
		
		Object value = getValue(context);		
		if(value instanceof Date){
			out.setCellValue((Date)value);			
		}else if(isNumber(value)){
			out.setCellValue(Double.valueOf(value.toString()));
		}else{
			out.setCellValue(new HSSFRichTextString(value.toString()));			
		}
		context.nextCell();
	}
	
	private boolean isNumber(Object value){
		if(value instanceof String){
			return false;
		}
		
		try{
			Double.valueOf(value.toString());
			return true;
		}catch (NumberFormatException e) {
			return false;
		}
	}
	
	private Object getValue(FPContext context) throws FPMergeException{
		
		Map<String,Object> data = context.getData();
		
		Object value = OgnlUtil.getValue(expression.getExpression(), data);
		
		if(value == null){
			if(expression.isNullAllowed()){
				return expression.getNullValue();
			}else{		
				throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED,new Object[]{expression.getExpression()});
			}
		}
		return value;
	}	
	

}
