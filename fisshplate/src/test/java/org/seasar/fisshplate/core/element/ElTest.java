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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.element.El;
import org.seasar.fisshplate.core.element.GenericCell;
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
		
		CellWrapper cell0 = workbook.getSheetAt(0).getRow(0).getCell(0);//${code}		
		CellWrapper cell1 = workbook.getSheetAt(0).getRow(0).getCell(1);//${num}		
		
		el = new El(new GenericCell(cell0));
		el.merge(context);
		el = new El(new GenericCell(cell1));
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
		
		CellWrapper cell = workbook.getSheetAt(0).getRow(0).getCell(2);//${hoge}
		CellWrapper cellNull = workbook.getSheetAt(0).getRow(0).getCell(3);//${hoge!}
		CellWrapper cellNullValue = workbook.getSheetAt(0).getRow(0).getCell(4);//${hoge!NULL時デフォルト値}
		
		
		
		el = new El(new GenericCell(cell));
		try{
			el.merge(context);
			fail();
		}catch (FPMergeException e) {
			assertTrue(true);
		}
		
		el = new El(new GenericCell(cellNull));
		el.merge(context);
		HSSFCell actual = template.getSheetAt(0).getRow(0).getCell((short) 0);
		assertEquals("nullString","", actual.getRichStringCellValue().getString());
		
		el = new El(new GenericCell(cellNullValue));
		el.merge(context);
		actual = template.getSheetAt(0).getRow(0).getCell((short) 1);
		assertEquals("default value","NULL時デフォルト値", actual.getRichStringCellValue().getString());
		
		data.put("hoge", null);	
		el = new El(new GenericCell(cellNull));
		el.merge(context);
		actual = template.getSheetAt(0).getRow(0).getCell((short) 2);
		assertEquals("null value","", actual.getRichStringCellValue().getString());
	}
	
	public void test文字列に埋め込み() throws Exception{
		HSSFWorkbook template = getTemplate("/ElTest.xls");
		WorkbookWrapper workbook = new WorkbookWrapper(template);
		Map data = new HashMap();
		data.put("embeded", new Integer(123));		
		FPContext context = new FPContext(template.getSheetAt(0),data);
		context.nextRow();
		
		
		CellWrapper cell = workbook.getSheetAt(0).getRow(1).getCell(0);//埋め込み番号は${embeded}です。
		
		el = new El(new GenericCell(cell));
		
		el.merge(context);
		HSSFCell actual = template.getSheetAt(0).getRow(1).getCell((short)0);
		assertEquals("埋め込み番号は123です。", actual.getRichStringCellValue().getString());
		
		
	}
	
	public void test文字列に埋め込み_複数対応() throws Exception{
		HSSFWorkbook template = getTemplate("/ElTest_multi.xls");
		WorkbookWrapper workbook = new WorkbookWrapper(template);
		Map data = new HashMap();
		data.put("embeded1", new Integer(123));		
		data.put("embeded2", new Integer(456));
		FPContext context = new FPContext(template.getSheetAt(0),data);
		
		CellWrapper cell = workbook.getSheetAt(0).getRow(0).getCell(0);//埋め込み番号は${embeded1}と${embeded2}です。
		
		el = new El(new GenericCell(cell));
		
		el.merge(context);
		HSSFCell actual = template.getSheetAt(0).getRow(0).getCell((short)0);
		assertEquals("埋め込み番号は123と456です。", actual.getRichStringCellValue().getString());
		
		cell = workbook.getSheetAt(0).getRow(1).getCell(0);//${embeded1}と${embeded2}
		context.nextRow();
		
		el = new El(new GenericCell(cell));
		
		el.merge(context);
		actual = template.getSheetAt(0).getRow(1).getCell((short)0);
		assertEquals("123と456", actual.getRichStringCellValue().getString());
		
		cell = workbook.getSheetAt(0).getRow(2).getCell(0);//${embeded1}${embeded2}
		context.nextRow();
		
		el = new El(new GenericCell(cell));
		
		el.merge(context);
		actual = template.getSheetAt(0).getRow(2).getCell((short)0);
		assertEquals("123456", actual.getRichStringCellValue().getString());
		
		
		
	}
	
	public void testElTest()throws Exception{		 
		String str = "埋め込み番号は${embeded1}と${embeded2}です。";
		String[] strs = str.split("\\$\\{[^\\$\\{\\}]+\\}");
		Pattern pat = Pattern.compile("(\\$\\{[^\\$\\{\\}]+\\})");
		Matcher mat = pat.matcher(str);		
		
		for(int i=0; i < strs.length; i++){
			System.out.println(strs[i]);
		}
		
		while(mat.find()){
			System.out.println(mat.group());
		}
		
		
		
	}
	
	public void testEscape(){
		String str = "${hoge}";
		str = str.replaceAll("\\$", "\\\\\\$");
		str = str.replaceAll("\\{", "\\\\\\{");
		str = str.replaceAll("\\}", "\\\\\\}");
		System.out.println(str);
	}
	
	
	
	public void testRegTest(){
		String exp = "${data.val!空文字列}".replaceAll("^\\$\\{(.+)\\}$", "$1");
		assertEquals("data.val!空文字列", exp);
	}
	
}

