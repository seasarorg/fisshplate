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

package org.seasar.fisshplate.template;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.exception.FPException;

public class FPTemplatePictureTest extends TestCase {
    private FPTemplate template;

    public FPTemplatePictureTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test行の要素がリストの場合() throws Exception {
        InputStream is = getClass().getResourceAsStream("/FPTemplatePictureTest2.xls");
        HSSFWorkbook wb;
        try {
            template = new FPTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "タイトルである");
            List<A> aList = new ArrayList<A>();
            aList.add(new A("1行目", 10, new Date(), "image/logoKarmokar4.png"));
            aList.add(new A("2行目", 20, new Date(), null));
            aList.add(new A("3行目", 30, new Date(), "image/logoKarmokar5.png"));
            aList.add(new A("4行目", 10, new Date(), "image/logoKarmokar4.png"));
            aList.add(new A("5行目", 20, new Date(), "image/logoKarmokar4.png"));
            aList.add(new A("6行目", 30, new Date(), "image/logoKarmokar5.png"));
            map.put("b", aList);

            wb = template.process(is, map);
        } catch (FPException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            is.close();
        }

        FileOutputStream fos = new FileOutputStream("target/out_picture2.xls");
        wb.write(fos);
        fos.close();

    }

    public void test画像出力() throws Exception {
        HSSFWorkbook wb;
        try {
            template = new FPTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("data", new A("1行目", 10, new Date(), "image/logoKarmokar4.png"));
            wb = template.process("FPTemplatePictureTest.xls", map);
        } catch (FPException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
        }

        FileOutputStream fos = new FileOutputStream("target/out_picture.xls");
        wb.write(fos);
        fos.close();

    }

    public void test画像出力_パスをリテラルにて指定() throws Exception{
        HSSFWorkbook wb;
        try {
            template = new FPTemplate();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("data", "hoge");
            wb = template.process("FPTemplatePictureTest3.xls", map);
        } catch (FPException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
        }

        FileOutputStream fos = new FileOutputStream("target/out_picture3.xls");
        wb.write(fos);
        fos.close();

    }

    public class A {
        private String name;
        private int num;
        private Date date;
        private String picture;

        A(String name, int num, Date date, String picture) {
            this.name = name;
            this.num = num;
            this.date = date;
            this.picture = picture;
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

        /**
         * @return picture
         */
        public String getPicture() {
            return this.picture;
        }

        /**
         * @param picture
         *            設定する picture
         */
        public void setPicture(String picture) {
            this.picture = picture;
        }

    }

}
