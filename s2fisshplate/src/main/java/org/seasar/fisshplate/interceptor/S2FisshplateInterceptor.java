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

package org.seasar.fisshplate.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.poi.ss.usermodel.Workbook;
import org.seasar.fisshplate.consts.S2FPConsts;
import org.seasar.fisshplate.core.parser.container.AddOnParserContainer;
import org.seasar.fisshplate.meta.TemplateMetaData;
import org.seasar.fisshplate.meta.TemplateMetaDataFactory;
import org.seasar.fisshplate.util.FisshplateUtil;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.util.MethodUtil;

/**
 * S2Fisshplateを利用するためのインターセプタクラスです。
 * @author rokugen
 *
 */
public class S2FisshplateInterceptor extends AbstractInterceptor {
    private TemplateMetaDataFactory metaDataFactory;
    private AddOnParserContainer addOnParserContainer;

    private static final long serialVersionUID = 983269897377553526L;

    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (!MethodUtil.isAbstract(method)) {
            return invocation.proceed();
        }

        Object[] arguments = invocation.getArguments();
        if (arguments == null || arguments.length == 0) {
            return null;
        }

        Object bean = arguments[0];
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(S2FPConsts.DATA_MAP_KEY_FOR_BEAN, bean);

        Class<?> clazz = method.getDeclaringClass();
        TemplateMetaData metaData = metaDataFactory.getMetaData(clazz);
        Workbook workbook = metaData.getWorkbook(method);
        return FisshplateUtil.process(workbook, map, addOnParserContainer);
    }

    /**
     * S2によるインジェクション用のセッタです。
     * @param metaDataFactory
     */
    public void setMetaDataFactory(TemplateMetaDataFactory metaDataFactory) {
        this.metaDataFactory = metaDataFactory;
    }

    /**
     * S2によるインジェクション用のセッタです。
     * @param addOnRowParserContainer
     */
    public void setAddOnParserContainer(
            AddOnParserContainer addOnParserContainer) {
        this.addOnParserContainer = addOnParserContainer;
    }

}
