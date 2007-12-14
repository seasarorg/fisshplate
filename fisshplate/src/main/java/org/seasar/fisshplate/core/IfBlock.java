package org.seasar.fisshplate.core;

import java.util.Map;

import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

/**
 * @author rokugen
 *
 */
public class IfBlock extends AbstractBlock {
	private String condition;
	private TemplateElement nextBlock = new NullElement();
	/**	 
	 * @param condition 条件式
	 */
	IfBlock(String condition){
		this.condition = condition;
	}
	
	/**
	 * 次の条件のブロック要素を設定します。具体的には、{@link ElseIfBlock}か{@link ElseBlock}になります。
	 * @param next 次の条件のブロックを保持する要素。 
	 */
	public void setNextBlock(AbstractBlock next){
		this.nextBlock = next;
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		Map<String, Object> data = context.getData();
		boolean isTarget = (Boolean)OgnlUtil.getValue("(" + condition + ")", data);
		if(isTarget){
			for(TemplateElement elem:childList){
				elem.merge(context);
			}
		}else{			
			nextBlock.merge(context);
		}
	}

}
