package com.kay.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypts {

	public final static String encryptToMD5(String pwd) {
		// 用于加密的字符

		try {
			// 使用UTF-8字符集将此 String 编码为 byte序列，
			// 并将结果存储到一个新的 byte数组中
			byte[] input = pwd.getBytes("UTF-8");

			// 获得指定摘要算法的 MessageDigest对象，此处为MD5
			// MessageDigest类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法。
			// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			// MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
			mdInst.update(input);

			// 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
			byte[] md = mdInst.digest();

			// 把密文转换成十六进制的字符串形式
			 String str = bytesToHexString(md);
//			String str = bytesToHexString3(md);

			return str;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("错误的加密方式");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.println("错误的编码方式");
		}
		return null;
	}

	public static void encryptToSHA256ForPrint(String pwd) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(pwd.getBytes("UTF-8"));
			for (byte b : md.digest())
				System.out.format("%02X", b);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String bytesToHexString(byte[] b) {

		char[] md5Str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		//获取加密之后的二进制字节组
		int len = b.length;
		//一个16进制数占4位，一个字节8位
		//n个16进制数转换成一个16进制字符占2*n位
		char[] hexStr = new char[len * 2];
		int n = 0;
		for (int i = 0; i < len; i++) {
			byte t = b[i];
			//每一个字节拆分成高位和低位
			//（同时照顾到了一个字符(EN)8位与一个16进制数4位不匹配的问题）
			//取高位：补0右移4位，并和00001111相与
			hexStr[n++] = md5Str[t >>> 4 & 0xf];
			//取低位：直接和00001111相与
			hexStr[n++] = md5Str[t & 0xf];
		}
		return new String(hexStr);
	}

	public static String bytesToHexString2(byte[] b) {
		//Integer.toHexString(int i);
		//以十六进制（基数 16）无符号整数形式 返回一个整数参数 的 字符串 表示形式
		
		StringBuffer hexStr = new StringBuffer();
		int len = b.length;
		for (int i = 0; i < len; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (len == 1) {
				hex = '0' + hex;
			}
			hexStr.append(hex.toUpperCase());
		}
		return hexStr.toString();
	}

	public static String bytesToHexString3(byte[] b) {
		StringBuffer hexStr = new StringBuffer();
		int len = b.length;
		for (int i = 0; i < len; ++i) {
			hexStr.append(Integer.toHexString((b[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return hexStr.toString().toUpperCase();
	}

	public static String bytesToHexString4(byte[] b) {
		//同2
		StringBuffer hexStr = new StringBuffer();
		if (b == null || b.length <= 0)
			return null;
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2)
				hexStr.append(0);
			hexStr.append(hv.toUpperCase());
		}
		return hexStr.toString();
	}

	public static byte[] hexStringToByte(String hexStr) {
		String HEX_NUMS_STR = "0123456789ABCDEF";
		 /* len为什么是hex.length() / 2 ?
         * 首先，hex是一个字符串，里面的内容是像16进制那样的char数组
         * 用2个16进制数字可以表示1个byte，所以要求得这些char[]可以转化成什么样的byte[]，首先可以确定的就是长度为这个char[]的一半
         */
		if (hexStr == null || hexStr.equals(""))return null;  
        int len = (hexStr.length() / 2);
        hexStr = hexStr.toUpperCase();
        byte[] result = new byte[len];
        char[] hexChars = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
        	//一个int占4个字节(32位)
            int pos = i * 2;
            //public int indexOf(int ch)->返回指定字符在此字符串中第一次出现处的索引
            //使用indexOf方法，将每个16进制字符 转换为 对应大小的数值 如（char(A)->int(10), char(1)->int(1) ）
            //转换之后在后面补4个2进制0，将当前int转换为16进制高位(高的4位)；则下一个int相当于16进制低位(低的4位)；
            //最后将两数相或，合并成一个16进制数字并转位byte型（截去高位只保留最后8位）
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
                    .indexOf(hexChars[pos + 1]));
        }
        return result;

	}

}
