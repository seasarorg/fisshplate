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
package org.seasar.fisshplate.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * {@link FPMergeException}をラップする実行時例外です。
 * @author rokugen
 */
public class FPMergeRuntimeException extends SRuntimeException {
	private static final long serialVersionUID = 2037875540340213759L;

	public FPMergeRuntimeException(FPMergeException cause){
		super("EFSSHPLT0002",new Object[]{cause},cause);				
	}

}
