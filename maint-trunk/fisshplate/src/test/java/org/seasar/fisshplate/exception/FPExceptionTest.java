package org.seasar.fisshplate.exception;

import java.util.ResourceBundle;

import junit.framework.TestCase;

public class FPExceptionTest extends TestCase {
	@SuppressWarnings("serial")
	public void testリソースのカスタマイズ(){
		FPException ex = new FPException("00001"){
			@Override
			protected ResourceBundle getExceptionBundle(){
				return ResourceBundle.getBundle("customMessages");
			}
		};
		
		assertEquals("this is customized message.", ex.getMessage());
	}
}
