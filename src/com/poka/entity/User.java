/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.poka.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author poka
 */
@Entity
@Table(name="USERINFO")  
public class User implements Serializable{
        private Integer id;
	private String username;
	private String password;
	private String realName;
	private Integer age;
	
	public User(){
		
	}
        
        public User(String username,String password){
		this.username = username;
                this.password = password;
	}
        
        
	@Transient()      
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}	
        @Id()
	@Column(name = "USERCODE")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
        @Column(name = "USERPASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
        @Column(name = "USERNAME")
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
        @Transient()      
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
}
