package org.seasar.fisshplate.core;

import org.seasar.fisshplate.context.FPContext;

/**
 * {@link TemplateElement}を実装したNULLオブジェクトです。何も処理を行いません。
 * @author rokugen
 *
 */
public class NullElement implements TemplateElement {

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) {
		//no code;
	}

}
