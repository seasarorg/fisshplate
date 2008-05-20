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
package org.seasar.fisshplate.core.parser;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class WhileParserTest extends TestCase {

	public WhileParserTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void test解析テスト() throws Exception{
		HSSFWorkbook hssfWb = new HSSFWorkbook();
		hssfWb.createSheet().createRow(0).createCell((short) 0).setCellValue(new HSSFRichTextString(" #while hoge == 100  "));
		hssfWb.getSheetAt(0).createRow(1).createCell((short) 0).setCellValue(new HSSFRichTextString(" #end  "));
		WorkbookWrapper wb = new WorkbookWrapper(hssfWb);
		
		FPParser fpParser = new FPParser();
		WhileParser parser = new WhileParser();
		
		boolean actual = parser.process(wb.getSheetAt(0).getRow(0).getCell(0), fpParser);
		assertTrue(actual);
		
		hssfWb.getSheetAt(0).getRow(0).getCell((short) 0).setCellValue(new HSSFRichTextString("#hile hoge==100"));
		actual = parser.process(wb.getSheetAt(0).getRow(0).getCell(0), fpParser);
		assertFalse(actual);
		

		
	}	
	
}
