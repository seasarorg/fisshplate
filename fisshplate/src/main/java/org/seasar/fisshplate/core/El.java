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

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.util.StringUtil;

/**
 * テンプレートのセルの値が評価式の場合の要素クラスです。OGNLで評価します。
 * @author rokugen
 *
 */
public class El implements TemplateElement{
	private List expressionList = new ArrayList();
	protected AbstractCell targetElement;
	private String[] literals;

	/**
	 * コンストラクタです。
	 * @param cell テンプレート側のセル
	 * @param expression 評価式
	 */
	El(AbstractCell target) {
		this.targetElement = target;
		String originalCellValue = target.cell.getStringValue();
		Pattern patEl = Pattern.compile(FPConsts.REGEX_EL);
		Matcher mat = patEl.matcher(originalCellValue);
		while(mat.find()){
			expressionList.add(new ElExpression(mat.group()));
		}
		literals = originalCellValue.split(FPConsts.REGEX_EL);		
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {		
		Object value = null;
		if(context.isSkipMerge()){
			value = "";
		}else{
			List valueList = getValueList(context);
			value = buildValue(valueList);			
		}
		targetElement.setCellValue(value);
		targetElement.merge(context);
	}
	
	private Object buildValue(List valueList){		
		if(isLiteralBlank()){
			if(expressionList.size() == 1){
				return valueList.get(0);				
			}else{
				return valueListToString(valueList);								
			}
		}else{		
			return joinValueAndLiteral(valueList);
		}
	}
	
	private String valueListToString(List valueList){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < valueList.size();i++){
			sb.append(valueList.get(i));
		}
		return sb.toString();		
	}
	
	private String joinValueAndLiteral(List valueList){
		StringBuffer sb = new StringBuffer();			
		for(int i=0; i < literals.length; i++){
			sb.append(literals[i]);
			if(i < valueList.size()){
				sb.append(valueList.get(i));
			}		
		}		
		return sb.toString();
	}
	
	private boolean isLiteralBlank(){		
		for(int i=0; i < literals.length; i++){
			if(!StringUtil.isEmpty(literals[i])){
				return false;
			}
		}
		return true;
	}	
	
	
	private List getValueList(FPContext context) throws FPMergeException{
		List valueList = new ArrayList();		
		Map data = context.getData();		
		for(int i=0; i < expressionList.size(); i++){
			ElExpression expression = (ElExpression) expressionList.get(i);
			Object value = getValue(data,expression);			
			valueList.add(value);
		}
		return valueList;
	}	
	
	private Object getValue(Map data, ElExpression expression) throws FPMergeException{		
		Object value = OgnlUtil.getValue(expression.getExpression(), data);		
		if(value == null){
			if(expression.isNullAllowed()){
				value = expression.getNullValue();
			}else{
				throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED,
						new Object[]{expression.getExpression(),new Integer(targetElement.cell.getRow().getHSSFRow().getRowNum() + 1)});
			}		
		}
		return value;		
	}
	

}

