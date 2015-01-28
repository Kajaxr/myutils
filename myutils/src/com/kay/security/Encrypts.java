package com.kay.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypts {

	public final static String encryptToMD5(String pwd) {
		// ���ڼ��ܵ��ַ�

		try {
			// ʹ��UTF-8�ַ������� String ����Ϊ byte���У�
			// ��������洢��һ���µ� byte������
			byte[] input = pwd.getBytes("UTF-8");

			// ���ָ��ժҪ�㷨�� MessageDigest���󣬴˴�ΪMD5
			// MessageDigest��ΪӦ�ó����ṩ��ϢժҪ�㷨�Ĺ��ܣ��� MD5 �� SHA �㷨��
			// ��ϢժҪ�ǰ�ȫ�ĵ����ϣ�����������������С�����ݣ�������̶����ȵĹ�ϣֵ��
			MessageDigest mdInst = MessageDigest.getInstance("MD5");

			// MessageDigest����ͨ��ʹ�� update�����������ݣ� ʹ��ָ����byte�������ժҪ
			mdInst.update(input);

			// ժҪ����֮��ͨ������digest����ִ�й�ϣ���㣬�������
			byte[] md = mdInst.digest();

			// ������ת����ʮ�����Ƶ��ַ�����ʽ
			 String str = bytesToHexString(md);
//			String str = bytesToHexString3(md);

			return str;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("����ļ��ܷ�ʽ");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.println("����ı��뷽ʽ");
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
		//��ȡ����֮��Ķ������ֽ���
		int len = b.length;
		//һ��16������ռ4λ��һ���ֽ�8λ
		//n��16������ת����һ��16�����ַ�ռ2*nλ
		char[] hexStr = new char[len * 2];
		int n = 0;
		for (int i = 0; i < len; i++) {
			byte t = b[i];
			//ÿһ���ֽڲ�ֳɸ�λ�͵�λ
			//��ͬʱ�չ˵���һ���ַ�(EN)8λ��һ��16������4λ��ƥ������⣩
			//ȡ��λ����0����4λ������00001111����
			hexStr[n++] = md5Str[t >>> 4 & 0xf];
			//ȡ��λ��ֱ�Ӻ�00001111����
			hexStr[n++] = md5Str[t & 0xf];
		}
		return new String(hexStr);
	}

	public static String bytesToHexString2(byte[] b) {
		//Integer.toHexString(int i);
		//��ʮ�����ƣ����� 16���޷���������ʽ ����һ���������� �� �ַ��� ��ʾ��ʽ
		
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
		//ͬ2
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
		 /* lenΪʲô��hex.length() / 2 ?
         * ���ȣ�hex��һ���ַ������������������16����������char����
         * ��2��16�������ֿ��Ա�ʾ1��byte������Ҫ�����Щchar[]����ת����ʲô����byte[]�����ȿ���ȷ���ľ��ǳ���Ϊ���char[]��һ��
         */
		if (hexStr == null || hexStr.equals(""))return null;  
        int len = (hexStr.length() / 2);
        hexStr = hexStr.toUpperCase();
        byte[] result = new byte[len];
        char[] hexChars = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
        	//һ��intռ4���ֽ�(32λ)
            int pos = i * 2;
            //public int indexOf(int ch)->����ָ���ַ��ڴ��ַ����е�һ�γ��ִ�������
            //ʹ��indexOf��������ÿ��16�����ַ� ת��Ϊ ��Ӧ��С����ֵ �磨char(A)->int(10), char(1)->int(1) ��
            //ת��֮���ں��油4��2����0������ǰintת��Ϊ16���Ƹ�λ(�ߵ�4λ)������һ��int�൱��16���Ƶ�λ(�͵�4λ)��
            //���������򣬺ϲ���һ��16�������ֲ�תλbyte�ͣ���ȥ��λֻ�������8λ��
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
                    .indexOf(hexChars[pos + 1]));
        }
        return result;

	}

}
