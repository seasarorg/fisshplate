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

package org.seasar.fisshplate.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * 繰り返し処理を行うクラスに共通するユーティリティクラスです。
 * @author rokugen
 */
public class IteratorUtil {
    /**
     * @param o 配列もしくはリスト
     * @param iteratorName イテレータの名前（エラー表示用）
     * @param row 行ラッパクラス（エラー表示用）
     * @return {@link Iterator}クラス
     * @throws FPMergeException 第1引数が{@link Iterator}を持たないクラスだった場合に投げられます。
     */
    public static Iterator<?> getIterator(Object o, String iteratorName, RowWrapper row) throws FPMergeException{
        Iterator<?> ite;
        if(o instanceof List<?>){
            ite = ((List<?>)o).iterator();
        } else if(o instanceof Object[]){
            ite = getIterator(Arrays.asList((Object[])o),iteratorName,row);
        } else{
            throw new FPMergeException(FPConsts.MESSAGE_ID_NOT_ITERATABLE,
                    new Object[]{iteratorName},row);
        }
        return ite;
    }


}
