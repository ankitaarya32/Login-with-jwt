package com.example.jwtsecurity.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
@Entity
public class Role {
	//CONSUMER, SELLER;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;
	@Column(unique = true,name = "rolename")
	private String roleName;
	private String description;
	public Role(String roleName, String description) {
		super();
		this.roleName = roleName;
		this.description = description;
	}
	
	public Role(String roleName) {
		super();
		this.roleName = roleName;
		
	}
	public Role() {
		super();
	}

	@Override
	public String toString() {
		return "Role [roleId=" + roleId + ", roleName=" + roleName + ", description=" + description + "]";
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}

class RoleGrantedAuthority implements GrantedAuthority {
	private static final long serialVersionUID = -3408298481881657796L;
	String role;

	public RoleGrantedAuthority(String role) {
		this.role = role;
	}

	@Override
	public String getAuthority() {
		return this.role;
	}
}