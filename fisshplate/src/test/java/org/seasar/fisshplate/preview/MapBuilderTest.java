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

package org.seasar.fisshplate.preview;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
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

    @SuppressWarnings({ "deprecation", "unchecked" })
    public void testMap生成()throws Exception{
        //期待値生成
        Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("repnum", new Double(101D));
        expected.put("title","タイトルです。");

        //実行
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        MapBuilder builder = new MapBuilder();
        Map<String, Object> actual = builder.buildMapFrom(wb);

        assertEquals(expected.get("repnum"), actual.get("repnum"));
        assertEquals(expected.get("title"), actual.get("title"));

        assertEquals(new Date(2008 - 1900,0,28), actual.get("date"));

        List<Map<String,Object>> itemList = (List<Map<String, Object>>) actual.get("itemList");
        assertEquals(10, itemList.size());
        for(int i=0; i < itemList.size();i++){
            Map<String, Object> item = itemList.get(i);
            assertEquals(new Double(i + 1), item.get("num"));
        }

        Map item = itemList.get(0);
        List childList = (List) item.get("childList");

        assertEquals(5, childList.size());


        Map<String, Object> data = (Map<String, Object>) actual.get("data");
        assertEquals("ループじゃないの", data.get("val"));
        itemList = (List<Map<String,Object>>) data.get("itemList");
        assertEquals(6, itemList.size());

        Map<String, Object> dataChild = (Map<String, Object>) data.get("child");
        assertEquals("子供のデータ", dataChild.get("childVal"));

        Map<String, Object> dataGrandChild = (Map<String, Object>) dataChild.get("grandChild");
        assertEquals("dataの孫の値", dataGrandChild.get("grandChildVal"));

        Map<String, Object> dataGrand2 = (Map<String, Object>) dataGrandChild.get("grand2");
        assertEquals("dataのひ孫", dataGrand2.get("val"));


    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    public void testMap生成_要素名を1行目に書くバージョン()throws Exception{
        //期待値生成
        HashMap<String, Object> expected = new HashMap<String, Object>();
        expected.put("repnum", new Double(101D));
        expected.put("title","タイトルです。");

        //実行
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest2.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        MapBuilder builder = new MapBuilder();
        Map<String, Object> actual = builder.buildMapFrom(wb);

        assertEquals(expected.get("repnum"), actual.get("repnum"));
        assertEquals(expected.get("title"), actual.get("title"));
        assertEquals(new Date(2008 - 1900, 0, 28), actual.get("date"));

        List<Map<String,Object>> itemList = (List<Map<String, Object>>) actual.get("itemList");
        assertEquals(10, itemList.size());
        for(int i=0; i < itemList.size();i++){
            Map<String, Object> item = itemList.get(i);
            assertEquals(new Double(i + 1), item.get("num"));
        }

        Map<String, Object> item = itemList.get(0);
        List<Map<String,Object>> childList = (List<Map<String, Object>>) item.get("childList");

        assertEquals(5, childList.size());


        Map<String,Object> data = (Map<String, Object>) actual.get("data");
        assertEquals("ループじゃないの", data.get("val"));
        itemList = (List<Map<String, Object>>) data.get("itemList");
        assertEquals(6, itemList.size());

        Map<String, Object> dataChild = (Map<String, Object>) data.get("child");
        assertEquals("子供のデータ", dataChild.get("childVal"));

        Map<String, Object> dataGrandChild = (Map<String, Object>) dataChild.get("grandChild");
        assertEquals("dataの孫の値", dataGrandChild.get("grandChildVal"));

        Map<String, Object> dataGrand2 = (Map<String, Object>) dataGrandChild.get("grand2");
        assertEquals("dataのひ孫", dataGrand2.get("val"));


    }
    public void testテンプレートへ埋め込み()throws Exception{
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest.xls");
        InputStream tempIs = getClass().getResourceAsStream("/MapBuilderTest_template.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        MapBuilder builder = new MapBuilder();
        Map<String, Object> data = builder.buildMapFrom(wb);

        FPTemplate template = new FPTemplate();
        HSSFWorkbook out = template.process(new HSSFWorkbook(tempIs), data);

        FileOutputStream os = new FileOutputStream("target/MapBuilderTest_template_out.xls");
        out.write(os);
        is.close();
        tempIs.close();
        os.close();



    }

    public void testテンプレートへ埋め込み_0件リスト()throws Exception{
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest_emptyList.xls");
        InputStream tempIs = getClass().getResourceAsStream("/MapBuilderTest_template.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        MapBuilder builder = new MapBuilder();
        Map<String, Object> data = builder.buildMapFrom(wb);

        FPTemplate template = new FPTemplate();
        HSSFWorkbook out = template.process(new HSSFWorkbook(tempIs), data);

        FileOutputStream os = new FileOutputStream("target/MapBuilderTest_emptyList_out.xls");
        out.write(os);
        is.close();
        tempIs.close();
        os.close();



    }

    public void testテンプレートへ埋め込み_rootなし()throws Exception{
        InputStream is = getClass().getResourceAsStream("/MapBuilderTest_without_root.xls");
        InputStream tempIs = getClass().getResourceAsStream("/MapBuilderTest_without_root_template.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        MapBuilder builder = new MapBuilder();
        Map<String, Object> data = builder.buildMapFrom(wb);

        FPTemplate template = new FPTemplate();
        HSSFWorkbook out = template.process(new HSSFWorkbook(tempIs), data);

        FileOutputStream os = new FileOutputStream("target/MapBuilderTest_without_root_template_out.xls");
        out.write(os);
        is.close();
        tempIs.close();
        os.close();


    }


}
