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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;
import org.seasar.fisshplate.wrapper.SheetWrapper;

/**
 * プレビュー用データを保持するクラスです。
 * @author rokugen
 */
public class FPMapData  {
    protected String keyName;
    protected SheetWrapper sheet;
    protected List<FPMapData> childList = new ArrayList<FPMapData>();

    /**
     * @param sheet シート
     * @param keyName テンプレートから参照するキー文字列
     */
    FPMapData(SheetWrapper sheet, String keyName){
        this.sheet = sheet;
        this.keyName = keyName;
    }


    /**
     * このデータをテンプレートから参照するキー文字列を戻します。
     * @return キー文字列
     */
    public String getKeyName(){
        return keyName;
    }

    /**
     * 子要素を追加します。
     * @param sheet 子要素データが記載されたシート
     * @param keyName 子要素をテンプレートから参照するキー文字列
     */
    public void addChild(SheetWrapper sheet, String keyName) {
        childList.add(new FPMapData(sheet, keyName));
    }

    public FPMapData getChildByKey(String keyName){
        for(int i=0; i < childList.size(); i++){
            FPMapData mapData = (FPMapData) childList.get(i);
            if(mapData.getKeyName().equals(keyName)){
                return mapData;
            }
        }
        return null;
    }

    /**
     * 埋め込み用データを生成します。データ行が1行の場合は{@link Map}、
     * 複数行の場合は{@link Map}の{@link List}を戻します。
     * ヘッダ行に「empty list」と書いてある場合は、0件の{@link List}を戻します。
     * @return 埋め込み用データ
     */
    public Object buildData() {
        //行が3行以上ある場合はリストとします。
        if(sheet == null){
            return buildMapData();
        }else if(sheet.getRowCount() <= 2){
            String firstCell = sheet.getRow(0).getCell(0).getStringValue();
            if(FPConsts.PREVIEW_EMPTY_LIST_SIGN.equals(firstCell)){
                return new ArrayList<Object>();
            }else{
                return buildMapData();
            }
        }else{
            return buildListData();
        }
    }


    /**
     * {@link Map}として埋め込みデータを生成します。
     * @return 埋め込みデータ
     */
    protected Map<String, Object> buildMapData() {
        Map<String, Object> data = new HashMap<String, Object>();
        if(sheet != null){
            RowWrapper keys = sheet.getRow(0);
            RowWrapper vals = sheet.getRow(1);
            putValueToMap(data, keys, vals);
        }

        buildChildData(data);

        return data;
    }

    /**
     * {@link Map}の{@link List}として埋め込みデータを生成します。
     * @return 埋め込みデータ
     */
    protected List<Map<String, Object>> buildListData() {
        RowWrapper keys = sheet.getRow(0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=1; i < sheet.getRowCount(); i++){
            Map<String, Object> item = new HashMap<String, Object>();
            RowWrapper vals = sheet.getRow(i);
            putValueToMap(item, keys, vals);
            buildChildData(item);
            list.add(item);
        }
        return list;
    }

    /**
     * このデータが保持する子要素の埋め込みデータを生成します。
     * @param data 子要素データを追加する{@link Map}
     */
    protected void buildChildData(Map<String, Object> data){
        for(int i=0; i < childList.size(); i++){
            FPMapData mapData = (FPMapData) childList.get(i);
            Object childData = mapData.buildData();
            data.put(mapData.getKeyName(), childData);
        }
    }

    /**
     * 埋め込み用データが記載されたExcelシートから{@link Map}にデータを追加します。
     * @param data 埋め込みデータ用{@link Map}
     * @param keys {@link Map}のキーとなる行
     * @param vals {@link Map}の値となる行
     */
    protected void putValueToMap(Map<String, Object> data, RowWrapper keys, RowWrapper vals){
        for(int i=0; i < keys.getCellCount(); i++){
            CellWrapper key = keys.getCell(i);
            if(key.isNullCell()){
                continue;
            }
            CellWrapper val = vals.getCell(i);

            String keyStr = key.getStringValue();
            Object valObj = val.getObjectValue();
            data.put(keyStr, valObj);
        }
    }

}
