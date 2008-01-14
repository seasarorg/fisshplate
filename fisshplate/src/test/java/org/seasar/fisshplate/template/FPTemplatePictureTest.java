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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;

public class FPTemplatePictureTest extends TestCase {
	private FPTemplate template;

	public FPTemplatePictureTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test画像出力() throws Exception {
		InputStream is = getClass().getResourceAsStream("/FPTemplatePictureTest.xls");
		try {
			template = new FPTemplate(is);
		} catch (FPParseException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			is.close();
		}
		Map map = new HashMap();

		map.put("data", new A("image/sample.jpg"));

		HSSFWorkbook wb;
		try {
			wb = template.process(map);
		} catch (FPMergeException e) {
			throw e;
		}

		FileOutputStream fos = new FileOutputStream("target/out_picture.xls");
		wb.write(fos);
		fos.close();

	}

	public class A {
		private String picture;

		A(String picture) {
			this.picture = picture;
		}

		public String getPicture() {
			return picture;
		}

		public void setPicture(String picture) {
			this.picture = picture;
		}

	}

}
