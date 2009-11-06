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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.preview.FPPreviewUtil;

import junit.framework.TestCase;

/**
 * @author rokugen
 */
public class VarAndExecTest extends TestCase {

    public VarAndExecTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test変数宣言と実行でキーブレイク()throws Exception{
        InputStream template = getClass().getResourceAsStream("/VarAndExecTest_template.xls");
        InputStream data = getClass().getResourceAsStream("/VarAndExecTest_data.xls");

        HSSFWorkbook out = FPPreviewUtil.getWorkbook(template, data);
        FileOutputStream os = new FileOutputStream("target/VarAndExecTest_out.xls");
        out.write(os);
        template.close();
        data.close();
        os.close();
    }


}
