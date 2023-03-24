package com.quickpik.payload;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sourav Choudhary
 * <p>
 * This is API payload which will be send in case of any violation at server
 * whether it is related to resource not found or server side validation. The response
 * will include custom message, status code and timestamp
 * */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@JsonPropertyOrder({"timestamp","status","message"})
public class ApiResponse {
	private String message;
	private int status;
	private long timestamp;
}
