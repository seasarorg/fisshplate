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

package org.seasar.fisshplate.ooxml.preview;

import java.io.FileOutputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author rokugen
 */
public class FPPreviewUtilTest extends TestCase {

    public FPPreviewUtilTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testプレビュー() throws Exception{
        InputStream data = getClass().getResourceAsStream("/MapBuilderTest.xlsx");
        InputStream template = getClass().getResourceAsStream("/MapBuilderTest_template.xlsx");

        Workbook out = FPXPreviewUtil.getWorkbook(template, data);

        FileOutputStream os = new FileOutputStream("target/FPXPreviewTest_stream_out.xlsx");
        out.write(os);
        data.close();
        template.close();
        os.close();
    }

}
