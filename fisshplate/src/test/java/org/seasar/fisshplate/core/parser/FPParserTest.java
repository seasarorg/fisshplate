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

import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.core.element.El;
import org.seasar.fisshplate.core.element.ElseBlock;
import org.seasar.fisshplate.core.element.ElseIfBlock;
import org.seasar.fisshplate.core.element.Exec;
import org.seasar.fisshplate.core.element.IfBlock;
import org.seasar.fisshplate.core.element.IteratorBlock;
import org.seasar.fisshplate.core.element.GenericCell;
import org.seasar.fisshplate.core.element.NullCell;
import org.seasar.fisshplate.core.element.NullElement;
import org.seasar.fisshplate.core.element.PageBreak;
import org.seasar.fisshplate.core.element.Root;
import org.seasar.fisshplate.core.element.Row;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.element.Var;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class FPParserTest extends TestCase {

	public FPParserTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void test解析テスト1()throws Exception{
		InputStream is = getClass().getResourceAsStream("/FPTemplateTest.xls");		
		WorkbookWrapper workbook = new WorkbookWrapper(new HSSFWorkbook(is));
		FPParser parser = new FPParser();		
		Root root = parser.parse(workbook.getSheetAt(0));
		
		assertEquals(root.getPageHeader().getClass(), NullElement.class);
		assertEquals(root.getPageFooter().getClass(), NullElement.class);
		
		List bodyList = root.getBodyElementList();
		TemplateElement row = (TemplateElement) bodyList.get(0);
		assertEquals(Row.class,row.getClass());
		List cellList = ((Row)row).getCellElementList();		
		TemplateElement cell = (TemplateElement) cellList.get(0);
		assertEquals(GenericCell.class,cell.getClass());
		cell = (TemplateElement) cellList.get(1);
		assertEquals(NullCell.class,cell.getClass());
		
		row = (TemplateElement) bodyList.get(1);
		cellList = ((Row)row).getCellElementList();
		cell = (TemplateElement) cellList.get(0);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(2);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(3);
		assertEquals(GenericCell.class,cell.getClass());		
		
		row = (TemplateElement) bodyList.get(2);
		cellList = ((Row)row).getCellElementList();
		cell = (TemplateElement) cellList.get(0);
		assertEquals(GenericCell.class,cell.getClass());		
		cell = (TemplateElement) cellList.get(1);
		assertEquals(GenericCell.class,cell.getClass());
		cell = (TemplateElement) cellList.get(2);
		assertEquals(GenericCell.class,cell.getClass());		
		
		row = (TemplateElement) bodyList.get(3);
		assertEquals(IteratorBlock.class, row.getClass());
		List childList = ((IteratorBlock)row).getChildList();
		assertEquals(1, childList.size());
		TemplateElement child = (TemplateElement) childList.get(0);
		assertEquals(IfBlock.class, child.getClass());
		row = (TemplateElement) ((IfBlock)child).getChildList().get(0);
		cellList = ((Row)row).getCellElementList();
		cell = (TemplateElement) cellList.get(0);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(1);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(2);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(3);
		assertEquals(NullCell.class, cell.getClass());
		cell = (TemplateElement) cellList.get(4);
		assertEquals(NullCell.class, cell.getClass());
		cell = (TemplateElement) cellList.get(5);
		assertEquals(El.class, cell.getClass());
		
		TemplateElement next = ((IfBlock)child).getNextBlock();
		assertEquals(ElseBlock.class, next.getClass());
		row = (TemplateElement) ((ElseBlock)next).getChildList().get(0);
		cellList = ((Row)row).getCellElementList();
		cell = (TemplateElement) cellList.get(0);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(1);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(2);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(3);
		assertEquals(El.class, cell.getClass());
		row = (TemplateElement) ((ElseBlock)next).getChildList().get(1);
		assertEquals(PageBreak.class, row.getClass());
		
		row = (TemplateElement) bodyList.get(4);
		assertEquals(PageBreak.class, row.getClass());
		
		row = (TemplateElement) bodyList.get(5);
		assertEquals(IteratorBlock.class, row.getClass());
		childList = ((IteratorBlock)row).getChildList();
		assertEquals(2, childList.size());
		child = (TemplateElement) childList.get(0);
		assertEquals(IfBlock.class, child.getClass());
		row = (TemplateElement) ((IfBlock)child).getChildList().get(0);
		cellList = ((Row)row).getCellElementList();		
		assertEquals(GenericCell.class, cellList.get(0).getClass());
		assertEquals(GenericCell.class, cellList.get(1).getClass());
		assertEquals(GenericCell.class, cellList.get(2).getClass());
		assertEquals(El.class, cellList.get(3).getClass());
		
		next = ((IfBlock)child).getNextBlock();
		assertEquals(ElseIfBlock.class, next.getClass());
		row = (TemplateElement) ((ElseIfBlock)next).getChildList().get(0);
		cellList = ((Row)row).getCellElementList();		
		assertEquals(GenericCell.class, cellList.get(0).getClass());
		assertEquals(GenericCell.class, cellList.get(1).getClass());
		assertEquals(GenericCell.class, cellList.get(2).getClass());
		assertEquals(El.class, cellList.get(3).getClass());
		
		TemplateElement next2 = ((ElseIfBlock)next).getNextBlock();
		assertEquals(ElseIfBlock.class, next2.getClass());
		row = (TemplateElement) ((ElseIfBlock)next2).getChildList().get(0);
		cellList = ((Row)row).getCellElementList();		
		assertEquals(GenericCell.class, cellList.get(0).getClass());
		assertEquals(GenericCell.class, cellList.get(1).getClass());
		assertEquals(GenericCell.class, cellList.get(2).getClass());
		assertEquals(El.class, cellList.get(3).getClass());
		
		child = (TemplateElement) childList.get(1);
		assertEquals(Row.class, child.getClass());
		cellList = ((Row)child).getCellElementList();		
		assertEquals(El.class, cellList.get(0).getClass());
		assertEquals(El.class, cellList.get(1).getClass());
		assertEquals(El.class, cellList.get(2).getClass());
		assertEquals(El.class, cellList.get(3).getClass());
		
		row = (TemplateElement) bodyList.get(6);
		assertEquals(Var.class, row.getClass());
		
		row = (TemplateElement) bodyList.get(7);
		assertEquals(Exec.class, row.getClass());
		
	}
	
	public void test解析テスト_アドオンパーサ対応()throws Exception{
		StatementParser addon = new StatementParser(){
			public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
				String value = cell.getStringValue();
				if(!"あれやこれや".equals(value)){
					return false;
				}
				parser.addTemplateElement(new NullElement());
				return true;
			}			
		};
		
		
		InputStream is = getClass().getResourceAsStream("/FPTemplateTest.xls");		
		WorkbookWrapper workbook = new WorkbookWrapper(new HSSFWorkbook(is));
		FPParser parser = new FPParser();
		parser.addRowParser(addon);
		Root root = parser.parse(workbook.getSheetAt(0));
		
		assertEquals(root.getPageHeader().getClass(), NullElement.class);
		assertEquals(root.getPageFooter().getClass(), NullElement.class);
		
		//1行目がNullElementになるだけで後は変わらず		
		List bodyList = root.getBodyElementList();
		TemplateElement row = (TemplateElement) bodyList.get(0);
		assertEquals(NullElement.class,row.getClass());
		
		
		row = (TemplateElement) bodyList.get(1);
		List cellList = ((Row)row).getCellElementList();
		TemplateElement cell = (TemplateElement) cellList.get(0);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(2);
		assertEquals(El.class, cell.getClass());
		cell = (TemplateElement) cellList.get(3);
		assertEquals(GenericCell.class,cell.getClass());		
		
		
	}

}
