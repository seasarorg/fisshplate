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

package org.seasar.fisshplate.core.parser.container;

import java.util.ArrayList;
import java.util.List;

import org.seasar.fisshplate.core.parser.RowParser;

/**
 * S2Fisshplateで使用する独自パーサを保持するクラスです。
 * @author rokugen
 *
 */
public class AddOnParserContainer {
    private List addOnRowParsers = new ArrayList();

    /**
     * 独自の行パーサを登録します。
     * @param parser 独自の行パーサ
     */
    public void addRowParser(RowParser parser){
        addOnRowParsers.add(parser);
    }

    /**
     * 指定されたインデックスに該当する、登録された行パーサを戻します。
     * @param i パーサのインデックス
     * @return インデックスに該当するパーサ
     */
    public RowParser getRowParser(int i){
        return (RowParser) addOnRowParsers.get(i);
    }

    /**
     * 登録された行パーサの総数を戻します。
     * @return 行パーサの総数
     */
    public int rowParserCount(){
        return addOnRowParsers.size();
    }
}
