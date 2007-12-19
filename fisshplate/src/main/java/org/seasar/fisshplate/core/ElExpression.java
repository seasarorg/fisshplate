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
 * @author a-conv
 */
public class ElExpression {
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
	 * <p>
	 * 式の中にpagenumがある場合はページNo.を取得します
	 * </p>
	 * 
	 * @param exp
	 *            評価式
	 */
	ElExpression(String exp) {

		int pageAccessIdx = exp.indexOf(FPConsts.PAGE_NUMBER_NAME);
		if (pageAccessIdx != -1) {
			expression = FPConsts.PAGE_CONTEXT_NAME + "." + FPConsts.PAGE_NUMBER_NAME;
			return;
		}

		int idx = exp.indexOf(FPConsts.NULL_VALUE_OPERATOR);
		nullAllowed = (idx >= 1);
		if (nullAllowed) {
			expression = exp.substring(0, idx);
			nullValue = exp.substring(idx + 1);
		} else {
			expression = exp;
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

}
