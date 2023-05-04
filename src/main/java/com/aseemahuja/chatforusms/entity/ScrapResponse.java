package com.aseemahuja.chatforusms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ScrapResponse {
	
	String status;
	String title;
	String url;
	String iconUrl;
	
	

}
