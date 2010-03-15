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
    public IfBlock(String condition){
        this.condition = condition;
    }

    /**
     * 次の条件のブロック要素を設定します。具体的には、{@link ElseIfBlock}か{@link ElseBlock}になります。
     * @param next 次の条件のブロックを保持する要素。
     */
    public void setNextBlock(AbstractBlock next){
        this.nextBlock = next;
    }

    /**
     * 次の条件のブロック要素を戻します。
     * @return 次の条件のブロック要素
     */
    public TemplateElement getNextBlock(){
        return nextBlock;
    }

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
     */
    public void merge(FPContext context) throws FPMergeException {
        Map<String, Object> data = context.getData();
        boolean isTarget = ((Boolean)OgnlUtil.getValue("(" + condition + ")", data)).booleanValue();
        if(isTarget){
            mergeChildren(context);
        }else{
            nextBlock.merge(context);
        }
    }

}
