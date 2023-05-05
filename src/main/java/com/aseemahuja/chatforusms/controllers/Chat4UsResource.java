package com.aseemahuja.chatforusms.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aseemahuja.chatforusms.clients.GoogleBardClientNew;
import com.aseemahuja.chatforusms.entity.*;
import com.aseemahuja.chatforusms.helper.ScrapperHelper;

@RestController
@RequestMapping("/v1/api")
public class Chat4UsResource {
	
	@Autowired
	GoogleBardClientNew googleBardClientNew;
	
	@Autowired
	ScrapperHelper scrapperHelper;
	
	@Autowired
	CacheManager cacheManager;
	
	@PostMapping("/scrap")
	public ResponseEntity<ScrapResponse> scrap(@RequestBody ScrapRequest request){
		ScrapResponse response = new ScrapResponse();
		
		try {
			List<Document> pages = scrapperHelper.getPages(request.getUrl());
			
			response.setTitle(scrapperHelper.getTitle(pages.get(0)));
			response.setUrl(request.getUrl());
			response.setIconUrl(scrapperHelper.getIconUrl(pages.get(0)));
			
			Cache cache = cacheManager.getCache("applicationCache");
			
			cache.put("websiteCache", pages);
			
			
		} catch (IOException e) {
			response.setErrorMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}
	
	@PostMapping("/ask")
	public ResponseEntity<AskResponse> ask(@RequestBody AskRequest request){
		AskResponse response = new AskResponse();
		List<Document> pages = null;
		
		if(request.getQuestion().isEmpty()) {
			response.setError("Question is not asked.");
			 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
		}
		
		Cache cache = cacheManager.getCache("applicationCache");
		
		String content = "";
		try {
			pages =  cache.get("websiteCache", List.class);
			if(null==pages || pages.isEmpty()) {
				response.setError("No saved pages found.");
				 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
			}
			for(Document doc : pages) {
				content+=doc.text();
				
			}
		} catch (Exception e) {
			pages = null;
		}
		
		String ques = (request.getQuestion().endsWith("?"))? request.getQuestion(): (request.getQuestion()+ "?");
		
		String question = "I have a raw text of a website, " + ques + " " + pages.get(0).text();
		
		response.setAnswer(googleBardClientNew.ask(question));
		
		
		return new ResponseEntity<>(response, HttpStatus.OK); 
	}

}
