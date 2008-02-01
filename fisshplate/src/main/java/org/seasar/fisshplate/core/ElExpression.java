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

import org.seasar.fisshplate.consts.FPConsts;

/**
 * 評価式を表すクラスです。
 * 
 * @author rokugen
 */
public class ElExpression {
	private String baseExpressionString;
	private String expression;
	private boolean nullAllowed;
	private Object nullValue;

	/**
	 * <p>
	 * コンストラクタです。セル上に記載された評価式を受け取ります。
	 * </p>
	 * <p>
	 * 式の中に!がある場合は、NULLを許可します。
	 * </p>
	 * <p>
	 * !の後に値が続く場合は、NULL時のデフォルト値とします。
	 * </p>
	 * 
	 * @param exp
	 *            評価式
	 */
	ElExpression(String exp) {
		baseExpressionString = exp;
		String baseExp = exp.replaceAll("^\\$\\{(.+)\\}$", "$1");
		int idx = baseExp.indexOf(FPConsts.NULL_VALUE_OPERATOR);
		nullAllowed = (idx >= 1);
		if (nullAllowed) {
			expression = baseExp.substring(0, idx);
			nullValue = baseExp.substring(idx + 1);
		} else {
			expression = baseExp;
		}		
	}

	public String getExpression() {
		return expression;
	}

	public Object getNullValue() {
		return nullValue;
	}

	public boolean isNullAllowed() {
		return nullAllowed;
	}
	
	/**
	 * 「${}」で囲まれた、セルに記載された値を戻します。
	 * @return セルに記載された文字列
	 */
	public String getBaseExpressionString(){
		return baseExpressionString;
	}

}
