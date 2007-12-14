package org.seasar.fisshplate.core;

import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * @author rokugen
 *
 */
public class ElseBlock extends AbstractBlock {

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		for(TemplateElement elem:childList){
			elem.merge(context);
		}
	}

}
