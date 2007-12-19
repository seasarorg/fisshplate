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
package org.seasar.fisshplate.meta.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.S2FPConsts;
import org.seasar.fisshplate.meta.TemplateMetaData;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * テンプレートのメタデータクラスの実装です。
 * @author rokugen
 */
public class TemplateMetaDataImpl implements TemplateMetaData {
	private Map<String, HSSFWorkbook> templateCache = new HashMap<String, HSSFWorkbook>();

	public synchronized HSSFWorkbook getTemplateWorkbook(Method method) {
		HSSFWorkbook wb = templateCache.get(method.getName());
		if(wb != null){
			return wb;
		}
		return createTemplate(method);
	}

	protected HSSFWorkbook createTemplate(final Method method) {
		Class<?> clazz = method.getDeclaringClass();
		String templatePath = clazz.getName().replaceAll("\\.", "/") + "_"
				+ method.getName() + "." + S2FPConsts.EXCEL_EXTENSION;
		InputStream is = ResourceUtil.getResourceAsStream(templatePath);		
		try {
			HSSFWorkbook wb =  new HSSFWorkbook(is);
			templateCache.put(method.getName(), wb);
			return wb;
		}catch(IOException e){
			throw new IORuntimeException(e);
		} finally {
			InputStreamUtil.close(is);
		}
	}

}
