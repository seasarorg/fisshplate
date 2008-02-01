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

import java.util.List;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class RowTest extends TestCase {
	
	public RowTest(String name){
		super (name);
	}
	
	protected void setUp() throws Exception {		
		super.setUp();
	}
	
	public void testコンストラクタ(){
		Root root = new Root();
		HSSFWorkbook templateWb = new HSSFWorkbook();
		HSSFSheet templateSheet = templateWb.createSheet();
		HSSFRow templateRow = templateSheet.createRow(0);
		HSSFCell cell = templateRow.createCell((short)0);
		cell.setCellValue(new HSSFRichTextString("リテラル"));
		cell = templateRow.createCell((short)1);
		cell.setCellValue(10D);
		//cellNum 2 は設定しない。
		cell = templateRow.createCell((short)3);
		cell.setCellValue(new HSSFRichTextString("${data}"));
		cell = templateRow.createCell((short)4);
		cell.setCellValue(new HSSFRichTextString("#picture(/hoge/fuga.png cell=1 row=1)"));
		cell = templateRow.createCell((short)5);
		cell.setCellValue(new HSSFRichTextString("#picture(${data.path} cell=1 row=1)"));
		
		WorkbookWrapper workbook = new WorkbookWrapper(templateWb);
		
		Row row = new Row(workbook.getSheetAt(0).getRow(0), root);
		List elementList = row.cellElementList;		
		
		TemplateElement elem = (TemplateElement) elementList.get(0);
		assertTrue(elem.getClass() == Literal.class );
		elem = (TemplateElement) elementList.get(1);
		assertTrue(elem.getClass() == Literal.class);
		elem = (TemplateElement) elementList.get(2);
		assertTrue(elem.getClass() == NullCell.class);
		elem = (TemplateElement) elementList.get(3);
		assertTrue(elem.getClass() == El.class);
		assertTrue(((El)elem).targetElement.getClass() == Literal.class);
		elem = (TemplateElement) elementList.get(4);
		assertTrue(elem.getClass() == Picture.class);
		elem = (TemplateElement) elementList.get(5);
		assertTrue(elem.getClass() == El.class);
		assertTrue(((El)elem).targetElement.getClass() == Picture.class);		
		
	}

}
