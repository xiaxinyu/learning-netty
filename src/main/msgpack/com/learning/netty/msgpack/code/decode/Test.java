package com.learning.netty.msgpack.code.decode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

public class Test {
	public static void main(String[] args) throws IOException {
		List<String> data = new ArrayList<String>();
		data.add("spring");
		data.add("summer");
		data.add("winter");
		data.add("autumn");

		//code
		MessagePack msgPack = new MessagePack();
		byte[] raw = msgPack.write(data);
		for (int i = 0; i < raw.length; i++) {
			System.out.print(raw[i]);
		}
		System.out.println();
		System.out.println(binary(raw, 2));

		//decode
		List<String> dst = msgPack.read(raw, Templates.tList(Templates.TString));
		System.out.println(dst.get(0));
		System.out.println(dst.get(1));
		System.out.println(dst.get(2));
	}
	
    /** 
     * 将byte[]转为各种进制的字符串 
     * @param bytes byte[] 
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}
}
