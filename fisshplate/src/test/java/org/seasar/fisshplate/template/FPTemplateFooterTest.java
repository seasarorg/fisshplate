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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;

public class FPTemplateFooterTest extends TestCase {
	private FPTemplate template;

	public FPTemplateFooterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void test行の要素がリストの場合() throws Exception {
		InputStream is = getClass().getResourceAsStream("/FPTemplateFooterTest.xls");
		try {
			template = new FPTemplate(is);
		} catch (FPParseException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			is.close();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "タイトルである");
		List<A> aList = new ArrayList<A>();
		aList.add(new A("1行目", 10, new Date()));
		aList.add(new A("2行目", 20, new Date()));
		aList.add(new A("3行目", 30, new Date()));
		aList.add(new A("4行目", 10, new Date()));
		aList.add(new A("5行目", 20, new Date()));
		aList.add(new A("6行目", 30, new Date()));
		map.put("b", aList);

		HSSFWorkbook wb;
		try {
			wb = template.process(map);
		} catch (FPMergeException e) {
			throw e;
		}

		FileOutputStream fos = new FileOutputStream("target/out_footer.xls");
		wb.write(fos);
		fos.close();

	}

	public class A {
		private String name;
		private int num;
		private Date date;

		A(String name, int num, Date date) {
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
