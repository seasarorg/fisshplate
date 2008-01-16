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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class ElTest extends TestCase {
	private El el;	
	
	public ElTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {		
		super.setUp();
	}
	
	private HSSFWorkbook getTemplate(String path) throws Exception{
		InputStream is = getClass().getResourceAsStream(path);
		HSSFWorkbook template = new HSSFWorkbook(is);
		is.close();
		return template;
	}
	
	public void testデータが数字だけど文字列型の場合は文字列型で埋め込む() throws Exception{
		HSSFWorkbook template = getTemplate("/ElTest.xls");
		WorkbookWrapper workbook = new WorkbookWrapper(template);
		
		Map data = new HashMap();
		data.put("code", "01234");
		data.put("num", new Integer(-1234));
	
		FPContext context = new FPContext(template.getSheetAt(0),data);
		
		CellWrapper cell0 = workbook.getSheetAt(0).getRow(0).getCell(0);		
		CellWrapper cell1 = workbook.getSheetAt(0).getRow(0).getCell(1);
		
		el = new El(cell0,"code");
		el.merge(context);
		el = new El(cell1,"num");
		el.merge(context);
		
		HSSFCell actual = template.getSheetAt(0).getRow(0).getCell((short) 0);
		assertEquals("celltype",HSSFCell.CELL_TYPE_STRING, actual.getCellType());		
		assertEquals("value", "01234", actual.getRichStringCellValue().getString());
		
		actual = template.getSheetAt(0).getRow(0).getCell((short) 1);
		assertEquals("celltype",HSSFCell.CELL_TYPE_NUMERIC, actual.getCellType());
		assertEquals("value",-1234D, actual.getNumericCellValue(),0D);		
	}
	
	public void testOGN式の変数名にびっくりマークをつけた場合はNULL回避する() throws Exception{
		HSSFWorkbook template = getTemplate("/ElTest.xls");
		WorkbookWrapper workbook = new WorkbookWrapper(template);
		
		Map data = new HashMap();
		
		FPContext context = new FPContext(template.getSheetAt(0),data);
		
		CellWrapper cell = workbook.getSheetAt(0).getRow(0).getCell(0);
		
		el = new El(cell,"hoge");
		try{
			el.merge(context);
			fail();
		}catch (FPMergeException e) {
			assertTrue(true);
		}
		
		el = new El(cell,"hoge!");
		el.merge(context);
		HSSFCell actual = template.getSheetAt(0).getRow(0).getCell((short) 0);
		assertEquals("nullString","", actual.getRichStringCellValue().getString());
		
		el = new El(cell,"hoge!NULL時デフォルト値");
		el.merge(context);
		actual = template.getSheetAt(0).getRow(0).getCell((short) 1);
		assertEquals("default value","NULL時デフォルト値", actual.getRichStringCellValue().getString());
		
		data.put("hoge", null);	
		el = new El(cell,"hoge!");
		el.merge(context);
		actual = template.getSheetAt(0).getRow(0).getCell((short) 2);
		assertEquals("null value","", actual.getRichStringCellValue().getString());
	}
	
}
