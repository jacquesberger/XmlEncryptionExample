/*
 * Copyright 2011 Jacques Berger.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jberger.xmlenc.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.xml.security.utils.JavaUtils;

public class EncryptionKey {

    private SecretKey encryptKey;

    public EncryptionKey() throws Exception {
        generateEncryptionKey();
    }

    private void generateEncryptionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
        encryptKey = keyGenerator.generateKey();
    }

    public Key getKey() {
        return encryptKey;
    }

    public void saveToFile(String filePath) throws IOException {
        byte[] rawData = encryptKey.getEncoded();
        FileOutputStream outputStream = new FileOutputStream(new File(filePath));
        outputStream.write(rawData);
        outputStream.close();
    }
    
    public void loadFromFile(String filePath) throws Exception {
        DESedeKeySpec keySpec = new DESedeKeySpec(JavaUtils.getBytesFromFile(filePath));
        SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
        encryptKey = factory.generateSecret(keySpec);
    }
}
