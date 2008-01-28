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
package org.seasar.fisshplate.unit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.template.FPTemplate;

/**
 * @author rokugen
 */
public class MapBuilderTest extends TestCase {

	public MapBuilderTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testMap生成()throws Exception{
		//期待値生成		
		Map expected = new HashMap();
		expected.put("repnum", Double.valueOf(101));
		expected.put("title","タイトルです。");		
		
		//実行		
		InputStream is = getClass().getResourceAsStream("/MapBuilderTest.xls");
		HSSFWorkbook wb = new HSSFWorkbook(is);
		MapBuilder builder = new MapBuilder();
		Map actual = builder.buildMapFrom(wb);
		
		assertEquals(expected.get("repnum"), actual.get("repnum"));
		assertEquals(expected.get("title"), actual.get("title"));		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");		
		assertEquals("2008/01/28", df.format(actual.get("date")));
		
		List itemList = (List) actual.get("itemList");
		assertEquals(10, itemList.size());
		
		is.close();
		
	}
	
	public void testテンプレートへ埋め込み()throws Exception{
		InputStream is = getClass().getResourceAsStream("/MapBuilderTest.xls");
		InputStream tempIs = getClass().getResourceAsStream("/MapBuilderTest_template.xls");
		HSSFWorkbook wb = new HSSFWorkbook(is);
		MapBuilder builder = new MapBuilder();
		Map data = builder.buildMapFrom(wb);
		
		FPTemplate template = new FPTemplate();
		HSSFWorkbook out = template.process(new HSSFWorkbook(tempIs), data);
		
		FileOutputStream os = new FileOutputStream("target/MapBuilderTest_template_out.xls");
		out.write(os);
		is.close();
		tempIs.close();
		os.close();
		
		
		
	}
	

}