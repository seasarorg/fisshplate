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
package org.seasar.fisshplate.template;

import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author rokugen
 */
public class VarExecAndWhileTest extends TestCase {

    public VarExecAndWhileTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test変数宣言と実行でwhileループ()throws Exception{
        InputStream template = getClass().getResourceAsStream("/VarExecAndWhileTest.xls");
        FPTemplate fpt = new FPTemplate();
        HSSFWorkbook out = fpt.process(template, null);
        FileOutputStream os = new FileOutputStream("target/VarExecAndWhileTest_out.xls");
        out.write(os);
        template.close();
        os.close();
    }


}
