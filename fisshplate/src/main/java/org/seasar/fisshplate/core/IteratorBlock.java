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
package org.seasar.fisshplate.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

public class IteratorBlock extends AbstractBlock{	
	private String varName;
	private String iteratorName;
	private String indexName;
	
	/**
	 * 要素を保持する変数名とイテレータ自身の名前を受け取ります。ループのインデックス名はデフォルト値「index」になります。
	 * @param varName イテレータ内の要素を保持する変数名
	 * @param iteratorName イテレータ名
	 */
	IteratorBlock(String varName, String iteratorName){
				this(varName, iteratorName, FPConsts.DEFAULT_ITERATOR_INDEX_NAME);
	}
	
	/**
	 * 要素を保持する変数名とイテレータ自身の名前とループのインデックス名を受け取ります。
	 * @param varName イテレータ内の要素を保持する変数名
	 * @param iteratorName イテレータ名
	 * @param indexName ループのインデックス名
	 */
	IteratorBlock(String varName, String iteratorName, String indexName){
		this.varName = varName;
		this.iteratorName = iteratorName;
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
		Map<String,Object> data = context.getData();
		Object o = OgnlUtil.getValue(iteratorName, data);
		Iterator<?> ite = getIterator(o);
		mergeIteratively(context, ite, data);
	}
	
	private Iterator<?> getIterator(Object o) throws FPMergeException{
		Iterator<?> ite;
		if(o instanceof List){
			ite = ((List<?>)o).iterator();
		} else if(o instanceof Object[]){
			ite = getIterator(Arrays.asList((Object[])o));
		} else{
			throw new FPMergeException(FPConsts.MESSAGE_ID_NOT_ITERATABLE,new Object[]{iteratorName});			
		}
		return ite;	
	}
	
	private void mergeIteratively(FPContext context, Iterator<?> ite,Map<String, Object> data) throws FPMergeException{
		int line = 0;
		while(ite.hasNext()){
			Object var = ite.next();
			data.put(varName, var);	
			data.put(indexName, line);
			for(TemplateElement elem : childList){
				elem.merge(context);
			}			
			line ++;
		}		
	}

}
