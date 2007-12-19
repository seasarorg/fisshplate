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

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * {@link TemplateElement}を実装した改ページ用オブジェクトです。
 * 
 * @author a-conv
 */
public class PageBreakElement implements TemplateElement {

	private List<TemplateElement> headerList;
	private List<TemplateElement> footerList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		writeFooter(context);
		pageBreak(context);
		writeHeader(context);
	}

	private void writeFooter(FPContext context) throws FPMergeException {
		if (context.isUseFooter()) {
			footerList = context.getFooterList();
			if (footerList == null) {
				throw new FPMergeException(FPConsts.MESSAGE_FOOTER_INVALID);
			}
			for (TemplateElement elem : footerList) {
				elem.merge(context);
			}
		}
	}

	private void pageBreak(FPContext context) {
		HSSFSheet sheet = context.getOutSheet();
		int currentRowNum = context.getCurrentRowNum();
		sheet.setRowBreak(currentRowNum - 1);
	}

	private void writeHeader(FPContext context) throws FPMergeException {
		if (context.isUseHeader()) {
			headerList = context.getHeaderList();
			if (headerList == null) {
				throw new FPMergeException(FPConsts.MESSAGE_HEADER_INVALID);
			}
			for (TemplateElement elem : headerList) {
				elem.merge(context);
			}
		}
	}
}
