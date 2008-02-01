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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

/**
 * テンプレートのセルの値が評価式の場合の要素クラスです。OGNLで評価します。
 * @author rokugen
 *
 */
public class El implements TemplateElement{
	private ElExpression expression;	
	protected AbstractCell targetElement;
	private String preEl;
	private String postEl;

	/**
	 * コンストラクタです。
	 * @param cell テンプレート側のセル
	 * @param expression 評価式
	 */
	El(AbstractCell target) {
		this.targetElement = target;
		String originalCellValue = target.cell.getStringValue();
		Pattern patEl = Pattern.compile("(.*)(" + FPConsts.REGEX_EL +")(.*)");
		Matcher mat = patEl.matcher(originalCellValue);
		mat.find();
		preEl = mat.group(1);
		String exp = mat.group(2);
		postEl = mat.group(3);
		this.expression = new ElExpression(exp);
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		Object value = null;
		if(context.isSkipMerge()){
			value = "";
		}else{
			value = getValue(context);
		}
		
		if(preEl.length() !=0 || postEl.length() != 0){
			value = preEl + value.toString() + postEl;
		}
		targetElement.setCellValue(value);
		targetElement.merge(context);
	}
	
	private Object getValue(FPContext context) throws FPMergeException{
		
		Map data = context.getData();
		
		Object value = OgnlUtil.getValue(expression.getExpression(), data);
		
		if(value == null){
			if(expression.isNullAllowed()){
				return expression.getNullValue();
			}else{		
				throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED,
						new Object[]{expression.getExpression(),new Integer(targetElement.cell.getRow().getHSSFRow().getRowNum() + 1)});
			}
		}
		return value;
	}	
	

}

