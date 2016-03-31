package com.example.zahnubeidanci.bean;

import java.util.List;

import com.example.zahnubeidanci.bean.DictXmlInfo.Symbols;


public class DictionInfo {
	public String key;
	public String RightMean;
	public List<WrongDic> wrongdic;
	public List<Symbols> symbols;
	
	
	public class WrongDic{
		public String key;
		public String mean;
	}
}
