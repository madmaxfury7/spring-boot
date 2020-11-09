package com.madmax.campaign.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Connection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long connectionid;
	
	@NotBlank(message="Name is required!!")
	@Size(min=3,max=20,message="Minimum 3 and Maximum 20 characters are required!!")
	private String fullname;
	
	@NotBlank(message="Email is required!!")
	private String email;
	
	@NotBlank(message="Phone Number is required!!")
	private String phoneno;
	
	@NotBlank(message="Degree is required!!")
	private String degree;
	
	@NotBlank(message="Address is required!!")
	private String address;
	
	@NotBlank(message="Work is required!!")
	private String work;
	
	@ManyToOne
	private User user;
	
	@ManyToMany(mappedBy = "connections", fetch = FetchType.LAZY)
	private List<Campaign> campaigns=new ArrayList<Campaign>();
}
