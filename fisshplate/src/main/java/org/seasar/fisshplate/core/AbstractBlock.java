package org.seasar.fisshplate.core;

import java.util.ArrayList;
import java.util.List;


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
}
