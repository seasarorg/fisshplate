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

import java.util.Map;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * whileブロックを保持するクラスです。
 * @author rokugen
 */
public class WhileBlock extends AbstractBlock {
    private String condition;
    private RowWrapper row;
    public WhileBlock(RowWrapper row, String condition){
        this.row =row;
        this.condition = condition;

    }

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.element.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
     */
    public void merge(FPContext context) throws FPMergeException {
        while (IsConditionTrue(context)){
            mergeChildren(context);
        }
    }


    private boolean IsConditionTrue(FPContext context) throws FPMergeException{
        Map<String, Object> data = context.getData();
        try{
            return ((Boolean)OgnlUtil.getValue("(" + condition + ")", data)).booleanValue();
        }catch(RuntimeException e){
            throw new FPMergeException(FPConsts.MESSAGE_ID_WHILE_INVALID_CONDITION,
                    new Object[]{condition},row);
        }
    }

}
