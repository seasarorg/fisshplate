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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.exception.FPException;
import org.seasar.fisshplate.template.FPTemplate;

/**
 * @author rokugen
 */
public class FPPreviewUtil {
	private FPPreviewUtil(){}
	
	public static final HSSFWorkbook getWorkbook(HSSFWorkbook template, HSSFWorkbook data) throws FPException{
		FPTemplate fptemp = new FPTemplate();
		MapBuilder mb = new MapBuilder();
		Map map = mb.buildMapFrom(data);
		return fptemp.process(template, map);
	}
	
	public static final HSSFWorkbook getWorkbook(InputStream template, InputStream data) throws FPException, IOException{
		HSSFWorkbook tempWb = new HSSFWorkbook(template);
		HSSFWorkbook dataWb = new HSSFWorkbook(data);
		return getWorkbook(tempWb, dataWb);
	}

}
