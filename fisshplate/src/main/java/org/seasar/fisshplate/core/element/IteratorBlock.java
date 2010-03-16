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

import java.util.Iterator;
import java.util.Map;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.IteratorUtil;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.wrapper.RowWrapper;

public class IteratorBlock extends AbstractBlock{
    private String varName;
    private String iteratorName;
    private String indexName;
    private int max;
    private RowWrapper row;
    private int lineNumPerPage;

    /**
     * 要素を保持する変数名とイテレータ自身の名前とループのインデックス名とループの最大繰り返し回数を受け取ります。
     * @param row テンプレート上の行
     * @param varName イテレータ内の要素を保持する変数名
     * @param iteratorName イテレータ名
     * @param indexName ループのインデックス名
     * @param max ループの最大繰り返し回数
     */
    public IteratorBlock(RowWrapper row, String varName, String iteratorName, String indexName, int max){
        this.varName = varName;
        this.iteratorName = iteratorName;
        this.max = max;
        this.row = row;
        if(indexName == null || "".equals(indexName.trim())){
            this.indexName = FPConsts.DEFAULT_ITERATOR_INDEX_NAME;
        }else{
            this.indexName = indexName;
        }
    }

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
     */
    public void merge(FPContext context) throws FPMergeException {
        Map<String, Object> data = context.getData();
        Object o = OgnlUtil.getValue(iteratorName, data);
        Iterator<?> ite = IteratorUtil.getIterator(o,iteratorName,row);
        mergeIteratively(context, ite, data);
    }


    private void mergeIteratively(FPContext context, Iterator<?> ite,Map<String, Object> data) throws FPMergeException{
        context.setCurrentIterator(this);
        initLineNumPerPage();
        int index = 0;
        while(ite.hasNext()){
            Object var = ite.next();
            data.put(varName, var);
            data.put(indexName, Integer.valueOf(index));
            lineNumPerPage ++;
            index ++;
            mergeChildren(context);

        }

        context.setSkipMerge(true);
        context.clearCurrentIterator();
        while (max > lineNumPerPage){
            data.put(indexName, Integer.valueOf(index));
            lineNumPerPage ++;
            index ++;
            mergeChildren(context);
        }
        context.setSkipMerge(false);
    }


    /**
     * 現在のページごとの行番号を戻します。
     * @return ページごとの行番号
     */
    public int getLineNumPerPage() {
        return lineNumPerPage;
    }

    /**
     * 現在のページごとの行番号を初期化します。
     */
    public void initLineNumPerPage(){
        lineNumPerPage = 0;
    }

}
