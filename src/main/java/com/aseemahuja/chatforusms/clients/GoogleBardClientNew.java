package com.aseemahuja.chatforusms.clients;

import org.springframework.stereotype.Service;

import com.pkslow.ai.GoogleBardClient;

@Service
public class GoogleBardClientNew {
	
	private GoogleBardClient googleBardClient;
	
	public GoogleBardClientNew () {
		googleBardClient = new GoogleBardClient("Vwhj7eyCKjWN7F3FCsLoEXKx4ql8D_vPfRoGsyiDt9-hVXnZ9S4k_0ORcSvG19MtbW8bSQ.");
	}
	
	public String train(String content) {
		return googleBardClient.ask(content).chosenAnswer();
	}
	
	public String ask(String content) {
		return googleBardClient.ask(content).chosenAnswer();
	}

}
