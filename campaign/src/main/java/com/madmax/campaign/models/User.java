package com.madmax.campaign.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userid;
	
	@NotBlank(message="Username is required!!")
	@Size(min=2,max=16,message = "Minimum 2 and Maximum 16 characters are allowed!!")
	private String username;
	
	@NotBlank(message="Password is required!!")
	private String password;
	
	@NotBlank(message="Name is required!!")
	@Size(min=2,max=20,message = "Minimum 3 and Maximum 20 characters are allowed!!")
	private String fullname;
	
	@NotBlank(message="Email is required!!")
	@Column(unique=true)
	private String email;
	
	@NotBlank(message="Phone Number is required!!")
	@Column(unique=true)
	private String phoneno;
	
	@NotBlank(message="Experience is required!!")
	private String experience;
	
	@NotBlank(message="Address is required!!")
	private String address;
	
	private String about;
	private String role;
	private String imageurl;
	private Boolean isactive;
	@OneToMany(targetEntity = Campaign.class, cascade = CascadeType.ALL,mappedBy = "user")
	private List<Campaign> campaigns=new ArrayList<Campaign>();
	@OneToMany(targetEntity = Connection.class, cascade = CascadeType.ALL,mappedBy = "user")
	private List<Connection> connections=new ArrayList<Connection>();
}
