/***************************************************************************
 *
 * This document contains confidential and proprietary information 
 * subject to non-disclosure agreements with AsiaInspection. This 
 * information shall not be distributed or copied without written 
 * permission from the AsiaInspection.
 *
 ***************************************************************************/

package com.ai.api.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/***************************************************************************
 * <PRE>
 *  Project Name    : user-service
 * 
 *  Package Name    : com.ai.userservice.pojo
 * 
 *  File Name       : User.java
 * 
 *  Creation Date   : Jun 18, 2014
 * 
 *  Author          : Hysun He
 * 
 *  Purpose         : Value Object(VO) mapping to table UR_DEPARTMENT
 * 
 * 
 *  History         : 
 *   - Jun 23, 2014 by Hysun He: Change the format of [Creation Date] from 
 *     Chinese special format to English format.
 *   - Jul 24, 2014 by Hysun He: Removed [Department] field according the
 *     new table structure.
 * </PRE>
 ***************************************************************************/
public class EmployeeBean implements Serializable {
	private static final long serialVersionUID = -1062429376485547945L;

	private String userId;
	private String firstName;
	private String lastName;
	private Date joinDate;
	private String departmentId;
	private String departmentName;
	private String position;
	private String employeeType;
	private String email;
	private String phone;
	private String login;
	private String password;
	private String status = "Active"; // default
	private Timestamp createTime;
	private Timestamp updateTime;

	private List<EmployeeGroup> groups;
	private List<EmployeeRole> roles;
	
	// add by Alva Xie start, for hold user input criteria 
	private List<String> criteriaStatusList; 
	private List<String> criteriaDepartmentIdList;
	private List<String> criteriaGroupIdList;
	private List<String> criteriaRoleIdList;
	// add by Alva Xie end
	
	// add by Alva Xie for PND start
	private List<EmployeeBean> reportTos;
	private String modifiedBy;
	private Timestamp modifiedDate;
	// add by Alva Xie for PND end
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}

	/**
	 * @param departmentId
	 *            the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	/**
	 * @return the departmentName
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * @param departmentName
	 *            the departmentName to set
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the joinDate
	 */
	public Date getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate
	 *            the joinDate to set
	 */
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the employeeType
	 */
	public String getEmployeeType() {
		return employeeType;
	}

	/**
	 * @param employeeType
	 *            the employeeType to set
	 */
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the createTime
	 */
	public Timestamp getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the updateTime
	 */
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the groups
	 */
	public List<EmployeeGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<EmployeeGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the roles
	 */
	public List<EmployeeRole> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<EmployeeRole> roles) {
		this.roles = roles;
	}

	
	public List<String> getCriteriaStatusList() {
		return criteriaStatusList;
	}
	
	public void setCriteriaStatusList(List<String> criteriaStatusList) {
		this.criteriaStatusList = criteriaStatusList;
	}
	
	public List<String> getCriteriaDepartmentIdList() {
		return criteriaDepartmentIdList;
	}

	public void setCriteriaDepartmentIdList(List<String> criteriaDepartmentIdList) {
		this.criteriaDepartmentIdList = criteriaDepartmentIdList;
	}
	
	public List<String> getCriteriaGroupIdList() {
		return criteriaGroupIdList;
	}

	public void setCriteriaGroupIdList(List<String> criteriaGroupIdList) {
		this.criteriaGroupIdList = criteriaGroupIdList;
	}
	
	public List<String> getCriteriaRoleIdList() {
		return criteriaRoleIdList;
	}

	public void setCriteriaRoleIdList(List<String> criteriaRoleIdList) {
		this.criteriaRoleIdList = criteriaRoleIdList;
	}


	public List<EmployeeBean> getReportTos() {
		return reportTos;
	}

	public void setReportTos(List<EmployeeBean> reportTos) {
		this.reportTos = reportTos;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EmployeeBean other = (EmployeeBean) obj;
		if (userId == null) {
			if (other.userId != null) {
				return false;
			}
		} else if (!userId.equals(other.userId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", joinDate=" + joinDate
				+ ", departmentId=" + departmentId + ", departmentName="
				+ departmentName + ", position=" + position + ", employeeType="
				+ employeeType + ", email=" + email + ", phone=" + phone
				+ ", login=" + login + ", password=" + password + ", status="
				+ status + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", groups=" + groups + ", roles=" + roles
				+ ", criteriaStatusList=" + criteriaStatusList
				+ ", criteriaDepartmentIdList=" + criteriaDepartmentIdList
				+ ", criteriaGroupIdList=" + criteriaGroupIdList
				+ ", criteriaRoleIdList=" + criteriaRoleIdList + ", reportTos="
				+ reportTos + ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + "]";
	}



}
