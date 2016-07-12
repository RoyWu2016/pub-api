package com.ai.api.bean;

import java.io.Serializable;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.bean
 * <p>
 * Creation Date   : 2016/7/11 11:43
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


public class UserForToken implements Serializable {
private static final long serialVersionUID=-1062429376485547945L;

private String userId;
private String firstName;
private String lastName;
private String departmentId;
private String departmentName;
private String position;
private String employeeType;
private String email;
private String phone;
private String login;
private String password;



/**
 * @return the userId
 */
public String getUserId(){
        return userId;
        }

/**
 * @param userId
 * the userId to set
 */
public void setUserId(String userId){
        this.userId=userId;
        }

/**
 * @return the firstName
 */
public String getFirstName(){
        return firstName;
        }

/**
 * @param firstName
 *            the firstName to set
 */
public void setFirstName(String firstName){
        this.firstName=firstName;
        }

/**
 * @return the lastName
 */
public String getLastName(){
        return lastName;
        }

/**
 * @param lastName
 *            the lastName to set
 */
public void setLastName(String lastName){
        this.lastName=lastName;
        }

/**
 * @return the departmentId
 */
public String getDepartmentId(){
        return departmentId;
        }

/**
 * @param departmentId
 *            the departmentId to set
 */
public void setDepartmentId(String departmentId){
        this.departmentId=departmentId;
        }

/**
 * @return the departmentName
 */
public String getDepartmentName(){
        return departmentName;
        }

/**
 * @param departmentName
 *            the departmentName to set
 */
public void setDepartmentName(String departmentName){
        this.departmentName=departmentName;
        }

/**
 * @return the position
 */
public String getPosition(){
        return position;
        }

/**
 * @param position
 *            the position to set
 */
public void setPosition(String position){
        this.position=position;
        }

/**
 * @return the employeeType
 */
public String getEmployeeType(){
        return employeeType;
        }

/**
 * @param employeeType
 *            the employeeType to set
 */
public void setEmployeeType(String employeeType){
        this.employeeType=employeeType;
        }

/**
 * @return the email
 */
public String getEmail(){
        return email;
        }

/**
 * @param email
 *            the email to set
 */
public void setEmail(String email){
        this.email=email;
        }

/**
 * @return the phone
 */
public String getPhone(){
        return phone;
        }

/**
 * @param phone
 *            the phone to set
 */
public void setPhone(String phone){
        this.phone=phone;
        }

/**
 * @return the login
 */
public String getLogin(){
        return login;
        }

/**
 * @param login
 *            the login to set
 */
public void setLogin(String login){
        this.login=login;
        }

/**
 * @return the password
 */
public String getPassword(){
        return password;
        }

/**
 * @param password
 *            the password to set
 */
public void setPassword(String password){
        this.password=password;
        }


}
