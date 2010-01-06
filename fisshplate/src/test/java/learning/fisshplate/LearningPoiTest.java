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

package learning.fisshplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author a-conv
 */
public class LearningPoiTest extends TestCase {

    public void test() throws Exception {
        ClassLoader loader = null;
        loader = Thread.currentThread().getContextClassLoader();
        Assert.assertNotNull(loader.getResourceAsStream("FPTemplateTest.xls"));
    }

    private HSSFWorkbook setupInputWorkbook(String filePath) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        POIFSFileSystem poifs = new POIFSFileSystem(fis);
        fis.close();
        return new HSSFWorkbook(poifs);
    }

    /**
     * シート初期化処理テスト
     *
     * @throws Exception
     */
    public void testInithialize() throws Exception {
        String filePath = "src/test/resources/LearningPOITest.xls";
        HSSFWorkbook input = setupInputWorkbook(filePath);
        HSSFSheet inputSheet = input.getSheetAt(0);

        for (int rowNo = 0; rowNo <= inputSheet.getLastRowNum(); rowNo++) {
            HSSFRow row = inputSheet.getRow(rowNo);
            if (row == null) {
                continue;
            }
            for (int columnNo = 0; columnNo <= row.getLastCellNum(); columnNo++) {
                HSSFCell cell = row.getCell(columnNo);
                if (cell == null) {
                    continue;
                }
                HSSFRichTextString richText = new HSSFRichTextString(null);
                cell.setCellValue(richText);
                HSSFCellStyle style = input.createCellStyle();
                style.setFillPattern(HSSFCellStyle.NO_FILL);
                cell.setCellStyle(style);
            }
        }

        FileOutputStream fos = new FileOutputStream("target/outLearningTest.xls");
        input.write(fos);
        fos.close();
    }

    public void testCreateRowTest()throws Exception{
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest_template.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        HSSFSheet ws = wb.getSheetAt(0);
        for(int i=0; i <= ws.getLastRowNum();i++){
            HSSFRow hssfRow = ws.getRow(i);
            if(hssfRow != null){
                ws.removeRow(hssfRow);
            }
        }

        FileOutputStream os = new FileOutputStream("target/createRowTest.xls");
        wb.write(os);
        os.close();
        is.close();

    }
}
