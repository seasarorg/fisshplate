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
package org.seasar.fisshplate.creator;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * SMART deployでS2Fisshplateを利用するためのCreatorクラスです。
 * @author rokugen
 */
public class FisshplateCreator extends ComponentCreatorImpl {
	private static final String DEFAULT_SUFFIX = "Fpao";

	public FisshplateCreator(NamingConvention namingConvention) {
		super(namingConvention);
		setNameSuffix(DEFAULT_SUFFIX);
        setEnableInterface(true);
        setEnableAbstract(true);
        setInstanceDef(InstanceDefFactory.SINGLETON);
	}
	
    /**
     * {@link ComponentCustomizer}を戻します。
     * customizer.dicon上でコンポーネント名を<code>fpaoCustomizer</code>とする必要があります。
     * @return Fpao用ComponentCustomizer
     */
    public ComponentCustomizer getFpaoCustomizer() {
        return getCustomizer();
    }

    /**
     * {@link ComponentCustomizer}を設定します。
     * customizer.dicon上でコンポーネント名を<code>fpaoCustomizer</code>とする必要があります。
     * @param customizer Fpao用ComponentCustomizer
     */
    public void setFpaoCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }


}
