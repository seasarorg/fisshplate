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
package org.seasar.fisshplate.core.element;

import java.util.List;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.core.parser.handler.CellParserHandler;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class SuspendTest extends TestCase {

	public SuspendTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void test埋め込み(){
		Root root = new Root();
		HSSFWorkbook templateWb = new HSSFWorkbook();
		HSSFSheet templateSheet = templateWb.createSheet();
		HSSFRow templateRow = templateSheet.createRow(0);
		HSSFCell cell = templateRow.createCell((short)0);
		cell = templateRow.createCell((short)0);
		cell.setCellValue(new HSSFRichTextString("#suspend id=test expr=TEST is ${hoge}"));
			
		WorkbookWrapper workbook = new WorkbookWrapper(templateWb);
		
		Row row = new Row(workbook.getSheetAt(0).getRow(0), root,new CellParserHandler());
		List elementList = row.getCellElementList();		
		TemplateElement elem = (TemplateElement) elementList.get(0);
		assertTrue(elem.getClass() == Suspend.class);
		
		
		
	}

}
