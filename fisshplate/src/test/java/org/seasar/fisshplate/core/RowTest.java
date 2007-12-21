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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;

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
		
		Row row = new Row(templateSheet, templateRow, root);
		List elementList = row.cellElementList;		
		
		TemplateElement elem = (TemplateElement) elementList.get(0);
		assertTrue(elem.getClass() == Literal.class );
		elem = (TemplateElement) elementList.get(1);
		assertTrue(elem.getClass() == Literal.class);
		elem = (TemplateElement) elementList.get(2);
		assertTrue(elem.getClass() == NullElement.class);
		elem = (TemplateElement) elementList.get(3);
		assertTrue(elem.getClass() == El.class);
		
	}
	
	public void testデータ埋め込み_ヘッダ出力()throws Exception{
		//テストデータ準備
		PageHeaderBlock header = (PageHeaderBlock) org.easymock.classextension.EasyMock.createMock(PageHeaderBlock.class);
		Root root = new Root();
		root.setPageHeader(header);
		
		Map data = new HashMap();
		HSSFWorkbook templateWb = new HSSFWorkbook();
		HSSFSheet templateSheet= templateWb.createSheet();
		HSSFRow templateRow = templateSheet.createRow(0);
		templateRow.setHeight((short)12);
		HSSFWorkbook outWb = new HSSFWorkbook();
		FPContext context = new FPContext(templateWb,outWb,data);
		context.setShouldHeaderOut(true);
		Row row = new Row(templateSheet, templateRow, root);
		//モック動作登録
		header.merge(context);
		org.easymock.classextension.EasyMock.replay(new Object[]{header});
		//実行
		context.init();
		row.merge(context);
		org.easymock.classextension.EasyMock.verify(new Object[]{header});
		
		assertFalse(context.shouldHeaderOut());
		assertTrue(context.shouldFooterOut());
		assertEquals(1, context.getCurrentRowNum());
		assertEquals(Integer.valueOf(1), data.get(FPConsts.ROW_NUMBER_NAME));
		assertEquals(templateRow.getHeight(), outWb.getSheetAt(0).getRow(0).getHeight());
		
		
	}
	
	public void testデータ埋め込み_ヘッダ出力なし()throws Exception{
		//テストデータ準備
		PageHeaderBlock header = (PageHeaderBlock) org.easymock.classextension.EasyMock.createMock(PageHeaderBlock.class);
		Root root = new Root();
		root.setPageHeader(header);
		
		Map data = new HashMap();
		HSSFWorkbook templateWb = new HSSFWorkbook();
		HSSFSheet templateSheet= templateWb.createSheet();
		HSSFRow templateRow = templateSheet.createRow(0);		
		HSSFWorkbook outWb = new HSSFWorkbook();
		FPContext context = new FPContext(templateWb,outWb,data);
		context.setShouldHeaderOut(false);
		Row row = new Row(templateSheet, templateRow, root);
		//モック動作登録		
		org.easymock.classextension.EasyMock.replay(new Object[]{header});
		//実行
		context.init();
		row.merge(context);
		org.easymock.classextension.EasyMock.verify(new Object[]{header});
		
		assertFalse(context.shouldHeaderOut());
		assertTrue(context.shouldFooterOut());
		assertEquals(1, context.getCurrentRowNum());
		assertEquals(Integer.valueOf(1), data.get(FPConsts.ROW_NUMBER_NAME));
		
		
	}
	
	

}
