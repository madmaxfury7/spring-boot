package com.madmax.campaign.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.madmax.campaign.models.Campaign;
import com.madmax.campaign.models.Connection;
import com.madmax.campaign.models.User;
import com.madmax.campaign.repository.CampaignRepository;
import com.madmax.campaign.repository.ConnectionRepository;
import com.madmax.campaign.repository.UserRepository;
import com.madmax.campaign.utils.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConnectionRepository connectionRepository;
	
	@Autowired
	private CampaignRepository campaignRepository;
	
	//method for adding common data for response
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String username=principal.getName();
		//Get user by username
		User user=userRepository.getUserByUsername(username);
		System.out.println("USER "+user);
		model.addAttribute("user", user);
	}
	
	//Dashboard handler
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal)
	{
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard";
	}
	
	//Add connection handler
	@GetMapping("/add-connection")
	public String openAddConnectionForm(Model model)
	{
		model.addAttribute("title", "Add Connection");
		model.addAttribute("connection", new Connection());
		return "normal/add_connection_form";
	}
	
	//Processing Add Connection form
	@PostMapping("/process-connection")
	public String processConnection(@Valid @ModelAttribute Connection connection, BindingResult bindingResult ,Principal principal,Model model,HttpSession session)
	{
		try {
			
			if (bindingResult.hasErrors())
			{
				System.out.println("ERROR "+bindingResult.toString());
				model.addAttribute("connection", connection);
				return "normal/add_connection_form";
			}
			
			String username=principal.getName();
			User user=this.userRepository.getUserByUsername(username);
			connection.setUser(user);
			user.getConnections().add(connection);
			this.userRepository.save(user);
			System.out.println("DATA "+connection);
			model.addAttribute("connection", connection);
			session.setAttribute("message", new Message("Connection added Successfully!! Add more?","alert-success"));
			return "normal/add_connection_form";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("connection", connection);
			session.setAttribute("message", new Message("Something Went Wrong!! "+e.getMessage(),"alert-danger"));
			return "normal/add_connection_form";
		}
		
		
	}
	
	//start campaign handler
	@GetMapping("/start-campaign")
	public String openStartCampaignForm(Model model)
	{
		model.addAttribute("title", "Start Campaign");
		model.addAttribute("campaign", new Campaign());
		return "normal/start_campaign_form";
	}
	
	//processing start campaign form
	@PostMapping("/process-campaign")
	public String processCampaign(@Valid @ModelAttribute Campaign campaign, BindingResult bindingResult ,@RequestParam("profileimage") MultipartFile file, Principal principal,Model model,HttpSession session)
	{
		try {
			if (bindingResult.hasErrors())
			{
				System.out.println("ERROR "+bindingResult.toString());
				model.addAttribute("campaign", campaign);
				return "normal/start_campaign_form";
			}
			
			String username=principal.getName();
			User user=this.userRepository.getUserByUsername(username);
			
			//processing and uploading campaign poster
			if (file.isEmpty())
			{
				System.out.println("No Campaign Poster Provided");
				campaign.setImage("default.jpg");
			}
			else
			{
				campaign.setImage(file.getOriginalFilename());
				File saveFile=new ClassPathResource("static/img").getFile();
				
				Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Camapign Poster uploaded");
			}
			
			campaign.setPhasedescription("Starting Phase");
			if (campaign.getDeadline().length()==0)
			{
				campaign.setDeadline("Not Sure");
			}
			campaign.setIsactive(true);
			if (campaign.getStatus().length()==0)
			{
				campaign.setStatus("Just started");
			}
			user.getCampaigns().add(campaign);
			campaign.setUser(user);
			this.userRepository.save(user);
			
			System.out.println("DATA "+campaign);
			model.addAttribute("campaign", campaign);
			session.setAttribute("message", new Message("New Campaign Successfully started!! Head to campaign Section to view.","alert-success"));
			return "normal/start_campaign_form";
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
			model.addAttribute("campaign", campaign);
			session.setAttribute("message", new Message("Something Went Wrong!! "+e.getMessage(),"alert-danger"));
			return "normal/start_campaign_form";
		}
	}
	
	//show connections handler
	//per-page=3[n]
	//current-page=0[current]
	@GetMapping("/show-connections/{page}")
	public String showConnetions(@PathVariable("page")int page, Model model,Principal principal)
	{
		model.addAttribute("title", "Show Connections Page");
		String username=principal.getName();
		User user=this.userRepository.getUserByUsername(username);
		
		Pageable pageable=PageRequest.of(page, 10);
		Page<Connection> connections=this.connectionRepository.findConnectionsByUser(user.getUserid(),pageable);
		model.addAttribute("connections", connections);
		model.addAttribute("currentpage", page);
		model.addAttribute("totalpages", connections.getTotalPages());
		return "normal/show_connections";
	}
	
	//show campaign page
	//per-page=3[n]
	//current-page=0[current]
	@GetMapping("/show-campaigns/{page}")
	public String showCampaigns(@PathVariable("page")int page, Model model, Principal principal)
	{
		model.addAttribute("title", "Show Campaigns Page");
		String username=principal.getName();
		User user=this.userRepository.getUserByUsername(username);
		Pageable pageable=PageRequest.of(page, 3);
		Page<Campaign> campaigns=this.campaignRepository.findCampaignsByUser(user.getUserid(), pageable);
		model.addAttribute("campaigns", campaigns);
		model.addAttribute("currentpage", page);
		int totalpages=campaigns.getTotalPages();
		if (totalpages==0)
			totalpages=1;
		model.addAttribute("totalpages", totalpages);
		System.out.println("Total pages "+campaigns.getTotalPages());
		return "normal/show_campaigns";
	}
 }
