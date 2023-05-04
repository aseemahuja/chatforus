package com.aseemahuja.chatforusms.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class AskRequest {
	
	String question;

}
