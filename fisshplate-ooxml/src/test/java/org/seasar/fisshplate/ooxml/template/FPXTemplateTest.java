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

package org.seasar.fisshplate.ooxml.template;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Workbook;
import org.seasar.fisshplate.exception.FPException;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.template.FPTemplate;

public class FPXTemplateTest extends TestCase {
    private FPXTemplate template;

    public FPXTemplateTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test行の要素がリストの場合() throws Exception  {
        InputStream is = getClass().getResourceAsStream("/FPXTemplateTest.xlsx");
        Workbook wb;
        try {
            template = new FPXTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "タイトルである");
            List<A> aList = new ArrayList<A>();
            aList.add(new A("1行目",10,new Date()));
            aList.add(new A("2行目",20,new Date()));
            aList.add(new A("3行目",30,new Date()));
            aList.add(new A("4行目",10,new Date()));
            aList.add(new A("5行目",20,new Date()));
            aList.add(new A("6行目",30,new Date()));
            map.put("b", aList);

            wb = template.process(is,map);
        } catch (FPParseException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }finally{
            is.close();
        }

        FileOutputStream fos = new FileOutputStream("target/out.xlsx");
        wb.write(fos);
        fos.close();

    }

    public void test行の要素がリストの場合_1件だけ() throws Exception  {
        InputStream is = getClass().getResourceAsStream("/FPXTemplateTest.xlsx");
        Workbook wb;
        try {
            template = new FPXTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "タイトルである");
            List<A> aList = new ArrayList<A>();
            aList.add(new A("1行目",10,new Date()));
            map.put("b", aList);

            wb = template.process(is,map);
        } catch (FPParseException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }finally{
            is.close();
        }

        FileOutputStream fos = new FileOutputStream("target/out_1.xlsx");
        wb.write(fos);
        fos.close();

    }


    public void test空行指定テスト() throws Exception{
        InputStream is = getClass().getResourceAsStream("/FPXTemplateTest_iteratorMax.xlsx");
        Workbook wb;
        try {
            template = new FPXTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "タイトルである");
            List<A> aList = new ArrayList<A>();
            aList.add(new A("1行目",10,new Date()));
            aList.add(new A("2行目",20,new Date()));
            aList.add(new A("3行目",30,new Date()));
            aList.add(new A("4行目",10,new Date()));
            aList.add(new A("5行目",20,new Date()));
            aList.add(new A("6行目",30,new Date()));
            map.put("b", aList);

            wb = template.process(is,map);
        } catch (FPException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }finally{
            is.close();
        }

        FileOutputStream fos = new FileOutputStream("target/out_iteratorMax.xlsx");
        wb.write(fos);
    }
    public void test行の要素がリストの場合_マクロあり() throws Exception  {
        InputStream is = getClass().getResourceAsStream("/FPXTemplateTest_macro.xlsm");
        Workbook wb;
        try {
            template = new FPXTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "タイトルである");
            List<A> aList = new ArrayList<A>();
            aList.add(new A("1行目",10,new Date()));
            aList.add(new A("2行目",20,new Date()));
            aList.add(new A("3行目",30,new Date()));
            aList.add(new A("4行目",10,new Date()));
            aList.add(new A("5行目",20,new Date()));
            aList.add(new A("6行目",30,new Date()));
            map.put("b", aList);

            wb = template.process(is,map);
        } catch (FPParseException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }finally{
            is.close();
        }

        FileOutputStream fos = new FileOutputStream("target/out_macro.xlsm");
        wb.write(fos);
        fos.close();

    }
//    public void test空行指定テスト_改ページ対応() throws Exception{
//        InputStream is = getClass().getResourceAsStream("/FPTemplateTest_iteratorMax_pageBreak.xls");
//        HSSFWorkbook wb;
//        try {
//            template = new FPTemplate();
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", "タイトルである");
//            List<A> aList = new ArrayList<A>();
//            aList.add(new A("1行目",10,new Date()));
//            aList.add(new A("2行目",20,new Date()));
//            aList.add(new A("3行目",30,new Date()));
//            aList.add(new A("4行目",10,new Date()));
//            aList.add(new A("5行目",20,new Date()));
//            aList.add(new A("6行目",30,new Date()));
//            aList.add(new A("7行目",10,new Date()));
//            aList.add(new A("8行目",20,new Date()));
//            aList.add(new A("9行目",30,new Date()));
//            aList.add(new A("10行目",10,new Date()));
//            aList.add(new A("11行目",20,new Date()));
//            aList.add(new A("12行目",30,new Date()));
//            map.put("b", aList);
//
//            wb = template.process(is,map);
//        } catch (FPException e) {
//            throw e;
//        } catch (IOException e) {
//            throw e;
//        }finally{
//            is.close();
//        }
//
//        FileOutputStream fos = new FileOutputStream("target/out_iteratorMax_pageBreak.xls");
//        wb.write(fos);
//    }
//
//    public void test独自パーサ適用例() throws Exception  {
//        InputStream is = getClass().getResourceAsStream("/FPTemplateTest.xls");
//        HSSFWorkbook wb;
//        try {
//            template = new FPTemplate();
//            template.addRowParser(new RowParser(){
//                public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
//                    String value =cell.getStringValue();
//                    if(!"あれやこれや".equals(value)){
//                        return false;
//                    }
//                    TemplateElement elem = new Areya(cell);
//                    parser.addTemplateElement(elem);
//                    return true;
//                }});
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", "タイトルである");
//            List<A> aList = new ArrayList<A>();
//            aList.add(new A("1行目",10,new Date()));
//            aList.add(new A("2行目",20,new Date()));
//            aList.add(new A("3行目",30,new Date()));
//            aList.add(new A("4行目",10,new Date()));
//            aList.add(new A("5行目",20,new Date()));
//            aList.add(new A("6行目",30,new Date()));
//            map.put("b", aList);
//
//            wb = template.process(is,map);
//        } catch (FPParseException e) {
//            throw e;
//        } catch (IOException e) {
//            throw e;
//        }finally{
//            is.close();
//        }
//
//        FileOutputStream fos = new FileOutputStream("target/out_customparser.xls");
//        wb.write(fos);
//        fos.close();
//
//    }
//
//    public void test一行だけの場合のエラーテスト()throws Exception{
//        InputStream is = getClass().getResourceAsStream("/onlyOneRowErrorTest.xls");
//        HSSFWorkbook wb;
//        try {
//            template = new FPTemplate();
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", "タイトルである");
//            List<A> aList = new ArrayList<A>();
//            aList.add(new A("1行目",10,new Date()));
//            aList.add(new A("2行目",20,new Date()));
//            aList.add(new A("3行目",30,new Date()));
//            aList.add(new A("4行目",10,new Date()));
//            aList.add(new A("5行目",20,new Date()));
//            aList.add(new A("6行目",30,new Date()));
//            map.put("b", aList);
//
//            wb = template.process(is,map);
//        } catch (FPException e) {
//            throw e;
//        } catch (IOException e) {
//            throw e;
//        }finally{
//            is.close();
//        }
//
//        FileOutputStream fos = new FileOutputStream("target/out_onlyOneRowErrorTest.xls");
//        wb.write(fos);
//    }
//
//    public void testカメラがある場合のテスト() throws Exception{
//        InputStream is = getClass().getResourceAsStream("/withCameraTest.xls");
//        new HSSFWorkbook(is);
//    }
//
//    private class Areya implements TemplateElement{
//        private CellWrapper originalCell;
//
//        public Areya(CellWrapper cell){
//            originalCell = cell;
//        }
//
//        public void merge(FPContext context) throws FPMergeException {
//            Cell currentCell = context.getCurrentCell();
//            currentCell.setCellStyle(originalCell.getHSSFCell().getCellStyle());
//            currentCell.setCellValue(new HSSFRichTextString("独自タグテストです"));
//            context.nextRow();
//        }
//
//    }


    public class A{
        private String name;
        private int num;
        private Date date;
        A(String name, int num, Date date){
            this.name = name;
            this.num = num;
            this.date = date;
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getNum() {
            return num;
        }
        public void setNum(int num) {
            this.num = num;
        }


    }

}
