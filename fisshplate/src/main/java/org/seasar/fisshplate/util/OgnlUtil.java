package org.seasar.fisshplate.util;

import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

public class OgnlUtil {
	private OgnlUtil(){}
	
	public static final Object getValue(String expression, Map<String, Object> data){
		try {
			return Ognl.getValue(expression, data);
		} catch (OgnlException e) {
			throw new RuntimeException(e);
		}
	}

}
