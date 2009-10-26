package org.seasar.fisshplate.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import junit.framework.TestCase;

public class FPPoiUtilTest extends TestCase {
    public void testGetStringValue(){
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCell cell = wb.createSheet().createRow(0).createCell(0);
        assertEquals( HSSFCell.CELL_TYPE_BLANK,cell.getCellType());
        String actual = FPPoiUtil.getStringValue(cell);
        assertEquals("", actual);

        cell.setCellValue("hoge");
        assertEquals(HSSFCell.CELL_TYPE_STRING,cell.getCellType());
        actual = FPPoiUtil.getStringValue(cell);
        assertEquals("hoge", actual);

        cell.setCellValue(1.12D);
        assertEquals(HSSFCell.CELL_TYPE_NUMERIC, cell.getCellType());
        actual = FPPoiUtil.getStringValue(cell);
        assertNull(actual);
    }
}
