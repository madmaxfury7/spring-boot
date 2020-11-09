package com.madmax.campaign.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.madmax.campaign.models.Campaign;
import com.madmax.campaign.models.User;
import com.madmax.campaign.repository.CampaignRepository;
import com.madmax.campaign.repository.UserRepository;

@Controller
@RequestMapping("/user/campaign")
public class CampaignController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	//adding common data to response
	@ModelAttribute
	private void addCommonData(Model model, Principal principal)
	{
		String username=principal.getName();
		User user=userRepository.getUserByUsername(username);
		System.out.println("USER "+user);
		model.addAttribute("user", user);
	}
	
	//Handler for showing particular campaign
	@GetMapping("/{campaignid}")
	public String showCampaignDetail(@PathVariable("campaignid")long campaignid,Model model,Principal principal)
	{
		System.out.println("Campaignid "+campaignid);
		Optional<Campaign> campiagnOptional=this.campaignRepository.findById(campaignid);
		Campaign campaign=campiagnOptional.get();
		
		//Checking whether the same user is viewing this
		String username=principal.getName();
		User user=this.userRepository.getUserByUsername(username);
		
		if (user.getUserid()==campaign.getUser().getUserid())
		{
			model.addAttribute("campaign", campaign);
			model.addAttribute("title", campaign.getTitle());
		}
		return "normal/campaign_home";
	}
}
