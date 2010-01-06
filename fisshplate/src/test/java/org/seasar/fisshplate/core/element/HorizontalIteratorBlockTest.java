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

package org.seasar.fisshplate.core.element;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.template.FPTemplate;

/**
 * @author rokugen
 */
public class HorizontalIteratorBlockTest extends TestCase {
    public void test普通に横展開()throws Exception{

        InputStream is = getClass().getResourceAsStream("/HorizontalIteratorTest.xls");
        List list = new ArrayList();
        list.add(new Data(){{setMonth(1);setAmountA(10);setAmountB(20);setAmountC(30);setAmountD(40);}});
        list.add(new Data(){{setMonth(2);setAmountA(12);setAmountB(21);setAmountC(31);setAmountD(50);}});
        list.add(new Data(){{setMonth(3);setAmountA(14);setAmountB(19);setAmountC(32);setAmountD(60);}});
        list.add(new Data(){{setMonth(4);setAmountA(16);setAmountB(18);setAmountC(33);setAmountD(70);}});
        Map data = new HashMap();
        data.put("list", list);
        FPTemplate fp = new FPTemplate();
        HSSFWorkbook wb = fp.process(is, data);
        OutputStream os = new FileOutputStream("target/HorizontalIterator.xls");
        wb.write(os);
        os.close();
    }

    public void test普通に横展開_素のテンプレート()throws Exception{

        InputStream is = getClass().getResourceAsStream("/HorizontalTest2.xls");
        List list = new ArrayList();
        list.add(new Data(){{setMonth(1);setAmountA(10);setAmountB(20);setAmountC(30);setAmountD(40);}});
        list.add(new Data(){{setMonth(2);setAmountA(12);setAmountB(21);setAmountC(31);setAmountD(50);}});
        list.add(new Data(){{setMonth(3);setAmountA(14);setAmountB(19);setAmountC(32);setAmountD(60);}});
        list.add(new Data(){{setMonth(4);setAmountA(16);setAmountB(18);setAmountC(33);setAmountD(70);}});
        Map data = new HashMap();
        data.put("list", list);
        FPTemplate fp = new FPTemplate();
        HSSFWorkbook wb = fp.process(is, data);
        OutputStream os = new FileOutputStream("target/HorizontalIterator2.xls");
        wb.write(os);
        os.close();
    }

    public class Data{
        private int amountA;
        private int amountB;
        private int amountC;
        private int amountD;
        private int month;

        public int getAmountA() {
            return amountA;
        }
        public void setAmountA(int amountA) {
            this.amountA = amountA;
        }
        public int getAmountB() {
            return amountB;
        }
        public void setAmountB(int amountB) {
            this.amountB = amountB;
        }
        public int getAmountC() {
            return amountC;
        }
        public void setAmountC(int amountC) {
            this.amountC = amountC;
        }
        public int getAmountD() {
            return amountD;
        }
        public void setAmountD(int amountD) {
            this.amountD = amountD;
        }
        public int getMonth() {
            return month;
        }
        public void setMonth(int month) {
            this.month = month;
        }
    }

}
