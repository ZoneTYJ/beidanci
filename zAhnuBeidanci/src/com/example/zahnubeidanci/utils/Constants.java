package com.example.zahnubeidanci.utils;

public class Constants {
//	public static final String BASE_URL="http://10.0.2.2:8080/beidanci";
	public static final String BASE_URL="http://10.0.2.2:8080/beidanciJson";
	/** 版本更新字符串 */
	public static final String VERSION_UPDATE="VersionUpdate";
	/** 版本更新检查URL地址 */
	public static final String UPDATE_URL=BASE_URL+"/VersionInfo.json";
	/** 用户打卡天数 */
	public static final String UserPunchCount="UserPunchCount";
	/** 用户学习的次数 */
	public static final String UserLearnCount="UserLearnCount";
	/** 用户名称 */
	public static final String UserName="UserName";
	/** 用户密码 */
	public static final String UserPass="UserPass";
	/** 用户uid */
	public static final String UserUid="UserUid";
	/** 获取用户信息的URL地址 */
	public static final String USERINFO_URL=BASE_URL+"/UserInfo.json";
	/**网络请求登录URL地址 */
	public static final String USERLOGIN_URL=BASE_URL+"/LoginRe.json";
	/**网络请求注册URL地址 */
	public static final String USERREGISTER_URL=BASE_URL+"/LoginRe.json";
	/**网络请求查单词URL地址 */
	public static final String SEARCHWORD_URL="http://dict-co.iciba.com/api/dictionary.php";
	/**网络请求查单词金山Key */
	public static final String SEARCH_KEY="247706E3DD42D6E96E5626FFF3126C6B";
	/**网络请求用户背诵单词URL地址 */
	public static final String RECITE_USERINFO_URL=BASE_URL+"/recite_userinfo.json";
	/**网络请求用户检测单词URL地址 */
	public static final String EXAM_USERINFO_URL=BASE_URL+"/exam_userinfo.json";
	/**网络添加生词本URL */
	public static final String ADDWORD_URL=BASE_URL+"/LoginRe.json";
	/**网络请求记忆词组URL */
	public static final String RECITEARRYS_URL=BASE_URL+"/recitearrys.json"; ///servlet/ReciteArrayServlet
	/**网络请求更新单词状态URL */
	public static final String UPDATEDIC_URL=BASE_URL+"";  //屏蔽中
	/**网络请求听写更新单词状态URL */
	public static final String UPDATE_DICTATION_URL=BASE_URL+"";  //屏蔽中
	/**网络请求打卡URL */
	public static final String SUCESSDIC_URL = BASE_URL+"/LoginRe.json";
	/**网络请求听写的单词URL */
	public static final String DICTION_URL = BASE_URL+"/dicitiondic.json";
	/**网络请求历史记录URL */
	public static final String HISTORY_URL = BASE_URL+"/history.json";
	
	
}
