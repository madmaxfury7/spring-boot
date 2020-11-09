package com.madmax.campaign.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	private String username;
	private String password;
	private String fullname;
	private String phoneno;
	private String email;
	private String address;
	private String experience;

}
