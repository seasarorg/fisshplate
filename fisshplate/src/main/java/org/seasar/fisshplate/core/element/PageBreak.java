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

import org.apache.poi.ss.usermodel.Sheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.context.PageContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * {@link TemplateElement}を実装した改ページ用オブジェクトです。
 *
 * @author a-conv
 */
public class PageBreak implements TemplateElement {
    private Root root;

    public PageBreak(Root root){
        this.root = root;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
     */
    public void merge(FPContext context) throws FPMergeException {
        writeFooter(context);

        pageBreak(context);
        pageCountUp(context);

        context.setShouldHeaderOut(true);
        context.setShouldFooterOut(false);
        if(context.inIteratorBlock()){
            IteratorBlock currentIterator = context.getCurrentIterator();
            currentIterator.initLineNumPerPage();
        }
    }

    private void pageCountUp(FPContext context) {
        ((PageContext) context.getData().get(FPConsts.PAGE_CONTEXT_NAME)).addPageNum();
    }

    private void writeFooter(FPContext context) throws FPMergeException {
        TemplateElement footer = root.getPageFooter();
        footer.merge(context);
    }

    private void pageBreak(FPContext context) {
        Sheet sheet = context.getOutSheet();
        int currentRowNum = context.getCurrentRowNum();
        sheet.setRowBreak(currentRowNum - 1);
    }

}
