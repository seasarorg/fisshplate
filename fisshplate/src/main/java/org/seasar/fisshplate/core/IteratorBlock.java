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
	
	/**
	 * @param varName イテレータ内の要素を保持する変数名
	 * @param iteratorName イテレータ名
	 */
	IteratorBlock(String varName, String iteratorName){
		this.varName = varName;
		this.iteratorName = iteratorName;		
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		Map<String,Object> data = context.getData();
		Object o = OgnlUtil.getValue(iteratorName, data);
		Iterator ite = getIterator(o);
		mergeIteratively(context, ite, data);
	}
	
	private Iterator<?> getIterator(Object o) throws FPMergeException{
		Iterator ite;
		if(o instanceof List){
			ite = ((List)o).iterator();
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
			data.put(FPConsts.ITERATOR_INDEX_NAME, line);
			for(TemplateElement elem : childList){
				elem.merge(context);
			}			
			line ++;
		}		
	}

}
