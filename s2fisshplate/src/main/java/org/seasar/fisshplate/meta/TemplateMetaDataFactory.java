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
package org.seasar.fisshplate.meta;

import java.lang.reflect.Method;

/**
 * テンプレートのメタデータを生成・保持するファクトリークラスです。
 * @author rokugen
 */
public interface TemplateMetaDataFactory {
    /**
     * {@link Method}に紐付いた{@link TemplateMetaData}を戻します。
     * @param fisshplateClass 実行されたクラス
     * @return メタデータ
     */
    TemplateMetaData getMetaData(Class fisshplateClass);
}
