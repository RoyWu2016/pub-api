package com.ai.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("restriction")
public class Base64Tester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// String path = "E:/test.docx";
		// String path = "E:/test.xlsx";
		// String path = "E:/test.pdf";
		String path = "E:/test.png";
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		String result = Base64.encode(buffer);

		System.out.println(result);
		// return new BASE64Encoder().encode(buffer);

	}

}
