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


import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.utils.EncryptionConstants;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XmlDecryption {

    private Document document;
    
    public XmlDecryption() {
        org.apache.xml.security.Init.init();
    }

    public void decryptXmlDocument(String sourceFilePath,
                                   String decryptedFilePath,
                                   Key encryptionKey)
                                   throws Exception {
        document = DocumentFileMapper.load(sourceFilePath);
        decryptDocument(encryptionKey);
        DocumentFileMapper.save(document, decryptedFilePath);
    }
    
    private void decryptDocument(Key encryptionKey) throws Exception {
        XMLCipher cipher = XMLCipher.getInstance();
        cipher.init(XMLCipher.DECRYPT_MODE, null);
        cipher.setKEK(encryptionKey);
        cipher.doFinal(document, getEncryptedData());
    }
    
    private Element getEncryptedData() {
        String namespaceURI = EncryptionConstants.EncryptionSpecNS;
        String localName = EncryptionConstants._TAG_ENCRYPTEDDATA;
        return (Element) document.getElementsByTagNameNS(namespaceURI, localName).item(0);
    }
}
