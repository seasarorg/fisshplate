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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author a-conv
 */
public class ImageIOUtil {

    public static BufferedImage read(FileInputStream fis) {
        try {
            return ImageIO.read(fis);
        } catch (IOException e) {
            throw new RuntimeException("対象ファイルを開く際にエラーが発生しました", e);
        }
    }

    public static void write(BufferedImage img, String suffix, ByteArrayOutputStream baos) {
        try {
            ImageIO.write(img, suffix, baos);
        } catch (IOException e) {
            throw new RuntimeException("対象ファイルを開く際にエラーが発生しました", e);
        }
    }

    public static void close(ByteArrayOutputStream baos) {
        try {
            if (baos != null) {
                baos.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("対象ファイルを閉じる際にエラーが発生しました", e);
        }
    }

}
