package com.ai.api.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.ai.commons.JsonUtil;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("restriction")
public class Base64Tester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// String path = "E:/test.docx";
		// String path = "E:/test.xlsx";
		// String path = "E:/test.pdf";
//		String path = "E:/test.xls";
//		File file = new File(path);
//		FileInputStream inputFile = new FileInputStream(file);
//		byte[] buffer = new byte[(int) file.length()];
//		inputFile.read(buffer);
//		inputFile.close();
//		String result = Base64.encode(buffer);
		
		String resultStr = "[{orderId=D61D546821E747058386B909F7248E65, serviceType=1, serviceTypeText=PSI, status=40, statusText=test, supplierName=test, refNumber=R-Cloud-1602137, clientReference=222, productNames=red light, poNumbers=asfas, serviceDate=19-Oct-2016, bookingDate=10-Oct-2016, confirmBySupplier=null, isSupplierConfirmed=false}]";
		List<SimpleOrderSearchBean> tempList = JsonUtil.mapToObject(resultStr, new TypeReference<List<SimpleOrderSearchBean>>(){});

		String str = "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";
		byte[] code = Base64.decode(str);
		OutputStream os = new FileOutputStream("E:\\roy.xlsx");
		os.write(code);
		os.flush();
		os.close();
//		System.out.println(result);
		// return new BASE64Encoder().encode(buffer);

	}

}
