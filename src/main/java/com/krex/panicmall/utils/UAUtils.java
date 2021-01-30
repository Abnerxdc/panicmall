package com.krex.panicmall.utils;

import java.util.Random;

/**
 * 
 * @author cz__wp (Ailk No.)
 * @version 1.0
 * @since 2018年8月20日
 * @category com.ailk.iot.caught.pc.util
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 *
 */
public class UAUtils
{
	/**
	 * 获取PC端UA
	 * @return
	 */
	public static String getPCUA()
	{
		String ua = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; MALCJS; rv:11.0) like Gecko";
		if ((new Random()).nextInt(5) == 0)
		{
			ua = "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)";
		}
		else if ((new Random()).nextInt(5) == 1)
		{
			ua = "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)";
		}
		else if ((new Random()).nextInt(5) == 2)
		{
			ua = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
		}
		else if ((new Random()).nextInt(5) == 3)
		{
			ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22";
		}
		else
		{
			ua = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; MALCJS; rv:11.0) like Gecko";
		}
		return ua;
	}
	
	
	/**
	 * 获取手机端UA
	 * @return
	 */
	public static String  getPhoneUA()
	{
		return "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36";
	}
}
