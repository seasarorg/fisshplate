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

import java.util.ArrayList;
import java.util.List;

import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * @author rokugen
 */
public class Root implements TemplateElement {
	private TemplateElement pageHeaderBlock = new NullElement();
	private List bodyElementList = new ArrayList();
	private TemplateElement pageFooterBlock = new NullElement();

	public void merge(FPContext context) throws FPMergeException {
		//pageHeaderBlock.merge(context);
		for(int i=0; i < bodyElementList.size(); i++){
			TemplateElement elem = (TemplateElement) bodyElementList.get(i);
			elem.merge(context);			
		}
		
		if(context.getCurrentRowNum() != context.getLastPageBreakRowNum()){
			pageFooterBlock.merge(context);
		}
	}
	
	public TemplateElement getPageHeader(){
		return pageHeaderBlock;
	}
	
	public void setPageHeader(PageHeaderBlock pageHeader){
		this.pageHeaderBlock = pageHeader;
	}
	
	public TemplateElement getPageFooter(){
		return pageFooterBlock;
	}
	
	public void setPageFooter(PageFooterBlock pageFooter){
		this.pageFooterBlock = pageFooter;
	}
	
	public void addBody(TemplateElement element){
		bodyElementList.add(element);		
	}

}
