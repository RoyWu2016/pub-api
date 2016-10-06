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
import java.sql.Timestamp;
import java.util.List;

/***************************************************************************
 * <PRE>
 *  Project Name    : user-service
 * 
 *  Package Name    : com.ai.userservice.pojo
 * 
 *  File Name       : Group.java
 * 
 *  Creation Date   : Jun 18, 2014
 * 
 *  Author          : Hysun He
 * 
 *  Purpose         : Value Object(VO) mapping to table UR_GROUP.
 * 
 * 
 *  History         : 
 *   - Jun 23, 2014 by Hysun He: Change the format of [Creation Date] from 
 *     Chinese special format to English format.
 * 
 * </PRE>
 ***************************************************************************/
public class EmployeeGroup implements Serializable {
	private static final long serialVersionUID = 5262584908896336582L;

	private String groupId;
	private String groupName;
	private String description;
	private Timestamp createTime;
	private Timestamp updateTime;

	private List<EmployeeRole> roles;
	private String departmentId;
	private String departmentName;

	public EmployeeGroup() {
	}

	public EmployeeGroup(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * @param groupId
	 *            the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the group
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroupName(String group) {
		this.groupName = group;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
		EmployeeGroup other = (EmployeeGroup) obj;
		if (groupId == null) {
			if (other.groupId != null) {
				return false;
			}
		} else if (!groupId.equals(other.groupId)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", groupName=" + groupName
				+ ", description=" + description + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", roles=" + roles
				+ ", departmentId=" + departmentId + ", departmentName="
				+ departmentName + "]";
	}


	
}
