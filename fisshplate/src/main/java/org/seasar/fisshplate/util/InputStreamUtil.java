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

package org.seasar.fisshplate.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author a-conv
 */
public class InputStreamUtil {

    public static InputStream getResourceAsStream(String name) {
        ClassLoader loader = null;
        loader = Thread.currentThread().getContextClassLoader();
        return loader.getResourceAsStream(name);
    }

    public static void close(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("ファイルを閉じる際にエラーが発生しました", e);
        }
    }

}
