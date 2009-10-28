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
package org.seasar.fisshplate.meta.impl;

import java.util.HashMap;
import java.util.Map;

import org.seasar.fisshplate.meta.TemplateMetaData;
import org.seasar.fisshplate.meta.TemplateMetaDataFactory;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * テンプレートのメタデータを生成・保持するファクトリークラスの実装です。
 * @author rokugen
 */
public class TemplateMetaDataFactoryImpl implements TemplateMetaDataFactory,Disposable {
    private Map metaDataCache = new HashMap();
    private boolean initialized;

    public TemplateMetaDataFactoryImpl(){
        initialize();
    }

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.meta.TemplateMetaDataFactory#getMetaData(java.lang.reflect.Method)
     */
    public synchronized TemplateMetaData getMetaData(final Class fisshplateClass) {
        initialize();

        TemplateMetaData metaData = (TemplateMetaData) metaDataCache.get(fisshplateClass.getName());
        if(metaData != null){
            return metaData;
        }
        return createMetaData(fisshplateClass);
    }

    protected TemplateMetaData createMetaData(final Class fisshplateClass){
        TemplateMetaData metaData = new TemplateMetaDataImpl();
        metaDataCache.put(fisshplateClass.getName(), metaData);
        return metaData;
    }

    public void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(this);
        initialized = true;
    }

    public void dispose() {
        metaDataCache.clear();
        initialized = false;
    }

}
