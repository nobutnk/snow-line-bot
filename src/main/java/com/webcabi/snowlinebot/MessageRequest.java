package com.webcabi.snowlinebot;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String message;
}
