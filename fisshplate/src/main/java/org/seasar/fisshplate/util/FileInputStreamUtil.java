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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.seasar.fisshplate.exception.FPMergeException;

/**
 * @author a-conv
 */
public class FileInputStreamUtil {

	public static FileInputStream createFileInputStream(String filePath) throws FPMergeException {
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new FPMergeException("対象ファイルが見つかりません");
		}
	}

	public static void close(FileInputStream fis) throws FPMergeException {
		try {
			if (fis != null) {
				fis.close();
			}
		} catch (IOException e) {
			throw new FPMergeException("対象ファイルを閉じる際にエラーが発生しました。");
		}
	}

}
