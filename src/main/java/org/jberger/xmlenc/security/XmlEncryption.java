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


import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;

import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;

import org.w3c.dom.Document;


public class XmlEncryption {

    private Document document;
    
    public XmlEncryption() {
        org.apache.xml.security.Init.init();
    }
    
    public void encryptXmlDocument(String sourceDocumentFilePath,
                                   String encryptedDocumentFilePath,
                                   Key encryptionKey)
                                   throws Exception {
        document = DocumentFileMapper.load(sourceDocumentFilePath);
        encryptDocument(encryptionKey);
        DocumentFileMapper.save(document, encryptedDocumentFilePath);
    }
    
    private void encryptDocument(Key encryptionKey) throws Exception {
        Key symmetricKey = generateSymmetricKey();
        EncryptedKey encryptedKey = getEncryptedKey(encryptionKey, symmetricKey);
        
        XMLCipher symmetricCipher = XMLCipher.getInstance(XMLCipher.AES_128);
        symmetricCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);

        EncryptedData encryptedData = symmetricCipher.getEncryptedData();
        KeyInfo info = new KeyInfo(document);
        info.add(encryptedKey);
        encryptedData.setKeyInfo(info);

        symmetricCipher.doFinal(document, document.getDocumentElement(), true);
    }
    
    private EncryptedKey getEncryptedKey(Key encryptionKey, Key symmetricKey) throws Exception {
        XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES_KeyWrap);
        keyCipher.init(XMLCipher.WRAP_MODE, encryptionKey);
        return keyCipher.encryptKey(document, symmetricKey);
    }


    private Key generateSymmetricKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();
    }
}