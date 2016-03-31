package com.example.zahnubeidanci.bean;

import java.util.List;

public class DictXmlInfo {
	/** 记录当前单词状态 */
	public int state;
	/** 单词名称*/
	public String key;
	public List<Symbols> symbols;
	public List<Means> means;
	public List<Sent> sent;
	
	public static class Symbols {
		/** 音标 */
		public String ps;
		/** 发音地址 */
		public String pron;
	}

	public static  class Means {
		/** 词性 */
		public String pos;
		/** 解释*/
		public String acceptation;
	}

	public static class Sent {
		/** 英文例句 */
		public String orig;
		/**  例句解释*/
		public String trans;
	}
}




