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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.element.Var;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class VarTest extends TestCase {

	public VarTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test変数一個だけ()throws Exception{
		String varName = " hoge ";
		Var var = new Var(varName,null);
		Map data = new HashMap();
		FPContext context = new FPContext(null,data);
		var.merge(context);
		assertNotNull(data.get("hoge"));
		
	}
	
	public void test変数複数() throws Exception{
		String varName = " hoge ,foo, bar,fuga";
		Var var = new Var(varName,null);
		Map data = new HashMap();
		FPContext context = new FPContext(null,data);
		var.merge(context);
		assertNotNull(data.get("hoge"));
		assertNotNull(data.get("foo"));
		assertNotNull(data.get("bar"));
		assertNotNull(data.get("fuga"));
	}
	
	public void test既存の変数を宣言したら例外() throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		wb.createSheet();
		wb.getSheetAt(0).createRow(0);		
		
		WorkbookWrapper workbook = new WorkbookWrapper(wb);
		
		String varName = " hoge ,foo, bar,fuga";
		Var var = new Var(varName,workbook.getSheetAt(0).getRow(0));
		Map data = new HashMap();
		data.put("foo", null);
		FPContext context = new FPContext(null,data);
		try{
			var.merge(context);
			fail();
		}catch (FPMergeException e) {
			System.out.println(e.getMessage());
			assertTrue(true);			
		}		
	}
	
	public void test変数宣言と初期化() throws Exception{
		String varName = " hoge ,foo=0, bar,fuga = 'initVal'";
		Var var = new Var(varName,null);
		Map data = new HashMap();
		FPContext context = new FPContext(null,data);
		var.merge(context);
		assertNotNull(data.get("hoge"));
		assertNotNull(data.get("foo"));
		assertEquals(Integer.valueOf("0"), data.get("foo"));
		assertNotNull(data.get("bar"));
		assertNotNull(data.get("fuga"));
		assertEquals("initVal", data.get("fuga"));
		
	}
	
	public void test初期化でエラーで例外() throws Exception{
		HSSFWorkbook wb = new HSSFWorkbook();
		wb.createSheet();
		wb.getSheetAt(0).createRow(0);		
		
		WorkbookWrapper workbook = new WorkbookWrapper(wb);
		
		String varName = " foo =12fdk=df";
		Var var = new Var(varName,workbook.getSheetAt(0).getRow(0));
		Map data = new HashMap();		
		FPContext context = new FPContext(null,data);
		try{
			var.merge(context);
			fail();
		}catch (FPMergeException e) {
			System.out.println(e.getMessage());
			assertTrue(true);
		}	
		
	}
	
	public void test初期化Pattern(){
		Pattern pat = Pattern.compile("([^=\\s]+)\\s*(=\\s*[^=\\s]+)?");
		
		Matcher mat = pat.matcher("hoge");		
		assertTrue(mat.find());
		assertEquals("hoge", mat.group(0));
		assertEquals("hoge", mat.group(1));
		assertEquals(null, mat.group(2));		
		
		mat = pat.matcher("hoge=1");
		assertTrue(mat.find());
		assertEquals("hoge=1", mat.group(0));
		assertEquals("hoge", mat.group(1));
		assertEquals("=1", mat.group(2));		
		
		mat = pat.matcher("hoge =  1");		
		assertTrue(mat.find());
		assertEquals("hoge =  1", mat.group(0));
		assertEquals("hoge", mat.group(1));
		assertEquals("=  1", mat.group(2));		
		
	}


}


