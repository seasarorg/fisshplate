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
package org.seasar.fisshplate.util;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPMergeRuntimeException;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.exception.FPParseRuntimeException;
import org.seasar.fisshplate.template.FPTemplate;

/**
 * @author rokugen
 */
public class FisshplateUtil {
	private FisshplateUtil(){}	
	/**
	 * テンプレート用の{@link HSSFWorkbook}から{@link FPTemplate}を生成します。
	 * @param templateWb テンプレート用ワークブック
	 * @return テンプレートオブジェクト
	 */
	public static final FPTemplate createTemplate(HSSFWorkbook templateWb){
		try {
			return new FPTemplate(templateWb);
		} catch (FPParseException e1) {
			throw new FPParseRuntimeException(e1);
		}
	}
	
	/**
	 * テンプレートにデータを埋め込みます。
	 * @param template テンプレートオブジェクト
	 * @param data 埋め込み用データ
	 * @return 出力するワークブック
	 */
	public static final HSSFWorkbook process(FPTemplate template, Map data){
		try {					
			return template.process(data);		
		} catch (FPMergeException e) {
			throw new FPMergeRuntimeException(e);
		}
	}

}
