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

import java.util.Iterator;
import java.util.Set;

import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * {@link Suspend}で保留された評価を再開するクラスです。
 * @author rokugen
 */
public class Resume implements TemplateElement {
    private String targetVar;

    public Resume(String targetVar){
        this.targetVar = targetVar;
    }

    public void merge(FPContext context) throws FPMergeException {
        Set<Suspend> susSet = context.getSuspendedSet();
        for(Iterator<Suspend> itr = susSet.iterator();itr.hasNext();){
            Suspend sus = itr.next();
            String targetStr = sus.getEl().getOriginalCellValue();
            if(targetStr.contains(targetVar)){
                sus.resume(context);
                susSet.remove(sus);
                break;
            }
        }
    }
}
