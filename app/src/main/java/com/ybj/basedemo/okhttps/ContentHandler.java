package com.ybj.basedemo.okhttps;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by 杨阳洋 on 2018/7/17.
 */
public class ContentHandler extends DefaultHandler {

    String nodeName;
    StringBuilder id;
    StringBuilder name;
    StringBuilder version;

    @Override
    public void startDocument() throws SAXException {
        id = new StringBuilder();
        name = new StringBuilder();
        version = new StringBuilder();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //记录当前节点名
        nodeName = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("app".equals(localName)) {
            Log.e("TAG", "id is " + id);
            Log.e("TAG", "name is " + name);
            Log.e("TAG", "version is " + version);
            id.setLength(0);
            name.setLength(0);
            version.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if("id".equals(nodeName)) {
            id.append(ch,start,length);
        }else if("name".equals(nodeName)) {
            name.append(ch,start,length);
        }else if("version".equals(nodeName)) {
            version.append(ch,start,length);
        }
    }

}
