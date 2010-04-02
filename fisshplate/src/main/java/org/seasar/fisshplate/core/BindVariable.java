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

package org.seasar.fisshplate.core;

import org.seasar.fisshplate.consts.FPConsts;

/**
 * バインド変数を表すクラスです。
 *
 * @author rokugen
 */
public class BindVariable {
    private String name;
    private boolean nullAllowed;
    private Object nullValue;

    /**
     * <p>
     * コンストラクタです。セル上に記載されたバインド変数を受け取ります。
     * </p>
     * <p>
     * 式の中に!がある場合は、NULLを許可します。
     * </p>
     * <p>
     * !の後に値が続く場合は、NULL時のデフォルト値とします。
     * </p>
     *
     * @param var
     *            セル上に記載されたバインド変数
     */
    public BindVariable(String var) {
        String baseVar = var.replaceAll(FPConsts.REGEX_BIND_VAR_START,"").replaceAll(FPConsts.REGEX_BIND_VAR_END, "");
        int idx = baseVar.indexOf(FPConsts.NULL_VALUE_OPERATOR);
        nullAllowed = (idx >= 1);
        if (nullAllowed) {
            name = baseVar.substring(0, idx);
            nullValue = baseVar.substring(idx + 1);
        } else {
            name = baseVar;
        }
    }

    /**
     * 変数名を戻します。
     * @return 変数名
     */
    public String getName() {
        return name;
    }

    /**
     * NULL時のデフォルト値を戻します。
     * @return デフォルト値
     */
    public Object getNullValue() {
        return nullValue;
    }

    /**
     * NULLを許可するか否かを戻します。
     * @return NULLを許可する場合true。
     */
    public boolean isNullAllowed() {
        return nullAllowed;
    }


}
