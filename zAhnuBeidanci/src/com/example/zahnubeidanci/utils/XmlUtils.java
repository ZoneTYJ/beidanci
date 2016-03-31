package com.example.zahnubeidanci.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import com.example.zahnubeidanci.bean.DictXmlInfo;
import com.example.zahnubeidanci.bean.DictXmlInfo.Means;
import com.example.zahnubeidanci.bean.DictXmlInfo.Sent;
import com.example.zahnubeidanci.bean.DictXmlInfo.Symbols;

import android.util.Xml;

public class XmlUtils {
	private DictXmlInfo dictXmlInfo;
	private Symbols symbol;
	private Means mean;
	private Sent sent;

	public  DictXmlInfo parseToDictXmlInfo(String data) {
		if (data != null && data.trim().equals("") == false) {
			try {
				ByteArrayInputStream is = new ByteArrayInputStream(
						data.getBytes());
				XmlPullParser newPullParser = Xml.newPullParser();
				newPullParser.setInput(is, "UTF-8");
				int event = 0;
				while ((event = newPullParser.next()) != XmlPullParser.END_DOCUMENT) {
					String tagName = newPullParser.getName();
					if (event == XmlPullParser.START_TAG) {
						if ("dict".equals(tagName)) {
							dictXmlInfo = new DictXmlInfo();
						} else if ("key".equals(tagName)) {
							dictXmlInfo.key = newPullParser.nextText();
						} else if ("ps".equals(tagName)) {
							if (dictXmlInfo.symbols == null) {
								dictXmlInfo.symbols = new ArrayList<DictXmlInfo.Symbols>();
							}
							symbol = new Symbols();
							symbol.ps = newPullParser.nextText();
						} else if ("pron".equals(tagName)) {
							symbol.pron = newPullParser.nextText();
						} else if ("pos".equals(tagName)) {
							if (dictXmlInfo.means == null) {
								dictXmlInfo.means = new ArrayList<Means>();
							}
							mean = new Means();
							mean.pos = newPullParser.nextText();
						} else if ("acceptation".equals(tagName)) {
							mean.acceptation = newPullParser.nextText();
						} else if ("orig".equals(tagName)) {
							if (dictXmlInfo.sent == null) {
								dictXmlInfo.sent = new ArrayList<Sent>();
							}
							sent = new Sent();
							sent.orig = newPullParser.nextText();
						} else if ("trans".equals(tagName)) {
							sent.trans = newPullParser.nextText();
						}
					} else {
						if ("pron".equals(tagName)) {
							dictXmlInfo.symbols.add(symbol);
						} else if ("acceptation".equals(tagName)) {
							dictXmlInfo.means.add(mean);
						} else if ("trans".equals(tagName)) {
							dictXmlInfo.sent.add(sent);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return dictXmlInfo;
	}
}
