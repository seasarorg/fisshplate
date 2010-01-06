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

import java.util.ResourceBundle;



/**
 * アプリケーションで利用するリソースバンドルを管理するユーティリティクラスです。以下のリソースバンドルを管理します。
 * <dl>
 *  <dt>appexception.properties
 *  <dd>アプリケーション固有の例外に使うメッセージ
 * </dl>
 * @author kei
 *
 */
public class ResourceUtil {
    public static final String MESSAGE_RESOURCE_NAME = "fisshplateMessages";

    /**
     * アプリケーション固有の例外用リソースバンドルを戻します。
     * @return リソースバンドル
     */
    public static final ResourceBundle getAppExceptionBundle(){
        return ResourceBundle.getBundle(MESSAGE_RESOURCE_NAME);
    }

}
