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

package org.seasar.fisshplate.context;

/**
 * シート単位の値を保管しています。
 *
 * @author a-conv
 */
public class PageContext {

    /**
     * ページカウント
     */
    private int pagenum = 1;

    /**
     * ページカウントを加算します
     */
    public void addPageNum() {
        pagenum++;
    }

    /**
     * ページカウントを返却します
     *
     * @return ページ番号
     */
    public int getPagenum() {
        return pagenum;
    }

    /**
     * ページ番号を設定します
     *
     * @param pagenum
     *            ページ番号
     */
    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

}
