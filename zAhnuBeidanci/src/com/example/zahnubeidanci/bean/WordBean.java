package com.example.zahnubeidanci.bean;

import java.util.List;

public class WordBean {
	/** 单词名称 */
	public String word_name;
	public int is_CRI;
	public List<Symbols> symbols;

	public class Symbols {

		public String ph_other;
		/** 英氏音标 */
		public String ph_en;
		/** 英氏发音  */
		public String ph_en_mp3;
		public String ph_tts_mp3;
		public String ph_am;
		public String ph_am_mp3;
		public List<Parts> parts;

		public class Parts {
			/** 形式 */
			public String part;
			/** 解释 */
			public List<String> means;
		}
	}

	public List<String> items;
	
	public Exchange exchange;
	/** 动词分词形式*/
	public class Exchange {

		public String word_er;
		public String word_est;
		public List<String> word_pl;

		public List<String> word_third;

		public List<String> word_past;

		public List<String> word_done;

		public List<String> word_ing;

	}
}