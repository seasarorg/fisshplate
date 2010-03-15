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

package org.seasar.fisshplate.core.element;

import java.util.ArrayList;
import java.util.List;

import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;


/**
 * ブロック要素を表す基底クラスです。
 * @author rokugen
 *
 */
public abstract class AbstractBlock implements TemplateElement {
    protected List<TemplateElement> childList = new ArrayList<TemplateElement>();

    /**
     * ブロック内の子要素を追加します。
     * @param element
     */
    public void addChild(TemplateElement element){
        childList.add(element);
    }

    /**
     * ブロック内の子要素のリストを戻します。
     * @return 子要素のリスト
     */
    public List<TemplateElement> getChildList(){
        return childList;
    }

    /**
     * 子要素にデータを埋め込みます
     * @param context コンテキスト
     * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
     */
    protected void mergeChildren(FPContext context) throws FPMergeException{
        for (int i = 0; i < childList.size(); i++) {
            TemplateElement elem = (TemplateElement) childList.get(i);
            elem.merge(context);
        }
    }
}
