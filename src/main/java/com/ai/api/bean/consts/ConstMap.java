package com.ai.api.bean.consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ai.api.bean.EmployeeBean;
import com.ai.api.bean.EmployeeGroup;
import com.ai.api.bean.EmployeeRole;
import com.ai.commons.beans.audit.api.ApiEmployeeBean;

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
	public final static Set<String> DOC_TYPE = new HashSet<String>();
	public final static Set<String> STATUS = new HashSet<String>();

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

		DOC_TYPE.add("ACCESS_MAP");
		DOC_TYPE.add("CHECKLIST_TEST");
		DOC_TYPE.add("CHECKLIST_EXPECTED_DEFECT");
		DOC_TYPE.add("BUS_LIC");
		DOC_TYPE.add("EXPORT_LIC");
		DOC_TYPE.add("ROHS_CERT");
		DOC_TYPE.add("TAX_CERT");
		DOC_TYPE.add("ISO_CERT");
		DOC_TYPE.add("OTHER_DOC");
		DOC_TYPE.add("ORDER_ATT");
		DOC_TYPE.add("AUDIT_PREVIEW_DOC");
		DOC_TYPE.add("PROD_REPORT_PICTURE");

		STATUS.add("15");
		STATUS.add("17");
		STATUS.add("20");
		STATUS.add("22");
		STATUS.add("23");
		STATUS.add("25");
		STATUS.add("30");
		STATUS.add("40");
		STATUS.add("50");
		STATUS.add("60");
	}

	public static ApiEmployeeBean convert2ApiEmployeeBean(EmployeeBean source) {
		ApiEmployeeBean apiEmployeeBean = new ApiEmployeeBean();
		apiEmployeeBean.setDepartmentId(source.getDepartmentId());
		apiEmployeeBean.setDepartmentName(source.getDepartmentName());
		apiEmployeeBean.setEmail(source.getEmail());
		apiEmployeeBean.setEmployeeType(source.getEmployeeType());
		apiEmployeeBean.setFirstName(source.getFirstName());
		apiEmployeeBean.setLastName(source.getLastName());
		apiEmployeeBean.setUserId(source.getUserId());
		apiEmployeeBean.setPhone(source.getPhone());
		apiEmployeeBean.setPosition(source.getPosition());
		apiEmployeeBean.setStatus(source.getStatus());

		List<EmployeeRole> allRoles = new ArrayList<EmployeeRole>();
		if (null != source.getRoles()) {
			allRoles = source.getRoles();
			colletModuleName(apiEmployeeBean.getRoles(), source.getRoles());
		}

		if (null != source.getGroups()) {
			for (EmployeeGroup each : source.getGroups()) {
				if (null != each.getRoles()) {
					allRoles.addAll(each.getRoles());
					colletModuleName(apiEmployeeBean.getRoles(), each.getRoles());
				}
			}
		}
		convert2Roles(apiEmployeeBean.getRoles(), allRoles);

		return apiEmployeeBean;

	}

	private static void colletModuleName(Map<String, HashSet<String>> map, List<EmployeeRole> roles) {
		// TODO Auto-generated method stub
		for (EmployeeRole role : roles) {
			HashSet<String> hs = new HashSet<String>();
			map.put(role.getModuleName(), hs);
		}
	}

	private static void convert2Roles(Map<String, HashSet<String>> result, List<EmployeeRole> sources) {
		// TODO Auto-generated method stub
		if(null == sources) {
			return;
		}
		for (EmployeeRole role : sources) {
			for(String key : result.keySet()) {
				if(key.equals(role.getModuleName())){
					result.get(key).add(role.getDisplayName());
				}
			}
		}
	}
}
