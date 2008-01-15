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
package org.seasar.fisshplate.util;

import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.PaneInformation;

/**
 * @author rokugen
 */
public class PoiUtil {
	private PoiUtil(){}	
	
	public static void copyPaneInfo(HSSFSheet srcSheet, HSSFSheet destSheet){
		PaneInformation pi = srcSheet.getPaneInformation();
		if(pi == null){
			return;
		}
		
		if(pi.isFreezePane()){
			destSheet.createFreezePane(pi.getVerticalSplitPosition(), pi.getHorizontalSplitPosition(), pi.getVerticalSplitLeftColumn(), pi.getHorizontalSplitTopRow());
		}else{
			destSheet.createSplitPane(pi.getVerticalSplitPosition(), pi.getHorizontalSplitPosition(), pi.getVerticalSplitLeftColumn(), pi.getHorizontalSplitTopRow(), pi.getActivePane());			
		}
	}
	
	public static void copyPrintSetup(HSSFSheet srcSheet, HSSFSheet destSheet){		
		HSSFPrintSetup srcPs = srcSheet.getPrintSetup();
		HSSFPrintSetup destPs = destSheet.getPrintSetup();
		destPs.setLandscape(srcPs.getLandscape());
		destPs.setHeaderMargin(srcPs.getHeaderMargin());
		destPs.setFooterMargin(srcPs.getFooterMargin());
		destPs.setLeftToRight(srcPs.getLeftToRight());
		destPs.setScale(srcPs.getScale());
		destPs.setCopies(srcPs.getCopies());
		destPs.setFitHeight(srcPs.getFitHeight());
		destPs.setFitWidth(srcPs.getFitWidth());
		destPs.setValidSettings(srcPs.getValidSettings());
		destPs.setNoOrientation(srcPs.getNoOrientation());
		destPs.setPaperSize(srcPs.getPaperSize());
		destPs.setHResolution(srcPs.getHResolution());
		destPs.setVResolution(srcPs.getVResolution());
		
		copyMargin(srcSheet, destSheet, HSSFSheet.TopMargin);
		copyMargin(srcSheet, destSheet, HSSFSheet.BottomMargin);
		copyMargin(srcSheet, destSheet, HSSFSheet.LeftMargin);
		copyMargin(srcSheet, destSheet, HSSFSheet.RightMargin);
		
		destSheet.setHorizontallyCenter(srcSheet.getHorizontallyCenter());
		destSheet.setVerticallyCenter(srcSheet.getVerticallyCenter(false));
		
		HSSFHeader srcHeader = srcSheet.getHeader();
		HSSFHeader destHeader = destSheet.getHeader();
		
		destHeader.setCenter(srcHeader.getCenter());
		destHeader.setLeft(srcHeader.getLeft());
		destHeader.setRight(srcHeader.getRight());
		
		HSSFFooter srcFooter = srcSheet.getFooter();
		HSSFFooter destFooter = destSheet.getFooter();
		
		destFooter.setCenter(srcFooter.getCenter());
		destFooter.setLeft(srcFooter.getLeft());
		destFooter.setRight(srcFooter.getRight());		
		
		
	}
	
	public static void copyMargin(HSSFSheet srcSheet, HSSFSheet destSheet, short marginType){
		destSheet.setMargin(marginType, srcSheet.getMargin(marginType));
	}

}
