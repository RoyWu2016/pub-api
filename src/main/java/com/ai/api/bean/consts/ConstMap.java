package com.ai.api.bean.consts;

import java.util.HashMap;
import java.util.Map;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.bean.consts
 * <p>
 * Creation Date   : 2016/7/4 15:35
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public class ConstMap {

	public final static Map<String, String> bucketMap = new HashMap();
	public final static Map<String, String> serviceTypeMap = new HashMap();

	public static enum DOC_TYPE {
		ACCESS_MAP, CHECKLIST_TEST, CHECKLIST_EXPECTED_DEFECT, BUS_LIC, EXPORT_LIC, ROHS_CERT, TAX_CERT, ISO_CERT, OTHER_DOC, ORDER_ATT, AUDIT_PREVIEW_DOC
	}

	static {
		bucketMap.put("ACCESS_MAP", "access-map");
		bucketMap.put("BUS_LIC", "supplier-certs");
		bucketMap.put("EXPORT_LIC", "supplier-certs");
		bucketMap.put("ROHS_CERT", "supplier-certs");
		bucketMap.put("TAX_CERT", "supplier-certs");
		bucketMap.put("ISO_CERT", "supplier-certs");
		bucketMap.put("OTHER_DOC", "supplier-certs");
		bucketMap.put("GI_COORDINATION", "dm-general-instruction");
		bucketMap.put("GI_SAMPLE_REF", "dm-general-instruction");
		bucketMap.put("GI_SAMPLE_PROD", "dm-general-instruction");
		bucketMap.put("GI_PROTOCAL", "dm-general-instruction");
		bucketMap.put("GI_INSP_RPT", "dm-general-instruction");
		bucketMap.put("GI_LAB_TEST", "dm-general-instruction");
		bucketMap.put("ORDER_ATT", "order-attachments");
		bucketMap.put("CHECKLIST_TEST", "checklist-attachements");
		bucketMap.put("CHECKLIST_EXPECTED_DEFECT", "checklist-attachements");
		bucketMap.put("AUDIT_PREVIEW_DOC", "audit-docs");

		serviceTypeMap.put("psi", "1");
		serviceTypeMap.put("ipc", "2");
		serviceTypeMap.put("dupro", "3");
		serviceTypeMap.put("clc", "4");
		serviceTypeMap.put("pm", "6");
		serviceTypeMap.put("ma", "5"); // Manufacturing Audit, formerly factory
										// audit
		serviceTypeMap.put("lt", "11");
		serviceTypeMap.put("ea", "9"); // Ethical Audit, formerly social audit
		serviceTypeMap.put("ctpat", "5,51");
		serviceTypeMap.put("stra", "9,91");
	}
}
