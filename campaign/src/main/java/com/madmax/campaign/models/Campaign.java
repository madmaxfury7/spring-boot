package com.madmax.campaign.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Campaign {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long campaignid;
	
	@NotBlank(message="Campaign Title is required!!")
	@Size(min=3,max=20,message="Title must be between 3 and 20 characters")
	private String title;
	
	private String description;
	
	private String phasedescription;
	
	@NotBlank(message="Specify Start Date of Campaign!!")
	private String startdate;
	
	private String deadline;
	private String status;
	private Boolean isactive;
	private String image;
	@ManyToOne
	private User user;
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name="campaign_connection",
				joinColumns = @JoinColumn(name="campaignid"),
				inverseJoinColumns = @JoinColumn(name="connectionid"))
	private List<Connection> connections=new ArrayList<Connection>();
}
