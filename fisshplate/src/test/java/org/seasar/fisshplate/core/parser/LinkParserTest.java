/**
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 *
 */

package org.seasar.fisshplate.core.parser;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.Link;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * @author rokugen
 */
public class LinkParserTest extends TestCase {

    private LinkParser parser;

    protected void setUp() throws Exception {
        super.setUp();
        parser = new LinkParser();
    }


    public void testパース成功(){
        HSSFWorkbook wb = new HSSFWorkbook();
        wb.createSheet().createRow(0).createCell(0);
        WorkbookWrapper ww = new WorkbookWrapper(wb);

        CellWrapper cell = ww.getSheetAt(0).getRow(0).getCell(0);
        String value = "#link-url link=http://www.gyoizo.com text=ほげ";
        cell.getHSSFCell().setCellValue(new HSSFRichTextString(value));

        AbstractCell actual = parser.getElement(cell, value);
        assertEquals(Link.class, actual.getClass());

        value = "#link-file link=http://www.gyoizo.com text=ほげ";
        actual = parser.getElement(cell, value);
        assertEquals(Link.class, actual.getClass());

        value = "#link-this link=http://www.gyoizo.com text=ほげ";
        actual = parser.getElement(cell, value);
        assertEquals(Link.class, actual.getClass());

        value = "#link-email link=http://www.gyoizo.com text=ほげ";
        actual = parser.getElement(cell, value);
        assertEquals(Link.class, actual.getClass());

    }


    public void testパースはずれ(){
        HSSFWorkbook wb = new HSSFWorkbook();
        wb.createSheet().createRow(0).createCell(0);
        WorkbookWrapper ww = new WorkbookWrapper(wb);

        CellWrapper cell = ww.getSheetAt(0).getRow(0).getCell(0);
        String value = "#link-hoge link=http://www.gyoizo.com text=ほげ";
        cell.getHSSFCell().setCellValue(new HSSFRichTextString(value));

        AbstractCell actual = parser.getElement(cell, value);
        assertNull(actual);

    }
}
