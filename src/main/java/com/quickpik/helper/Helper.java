package com.quickpik.helper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import com.quickpik.dtos.PageableResponse;

/**
 * 
 * This class contains a helper method to convert a Spring Data Page of entities
 * into a pageable response of DTOs using the ModelMapper library for mapping.
 * The method is generic and can be used for any entity-DTO mapping.
 * 
 * @author Sourav Choudhary
 */

public class Helper {

	/**
	 * Converts a Spring Data Page of entities into a pageable response of DTOs.
	 *
	 * @param page The Spring Data Page of entities to be converted.
	 * @param type The class of the DTO to be mapped.
	 * @return A PageableResponse object containing the mapped DTOs and page
	 *         information.
	 */
	
	// U, V are generic types
	public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type) {
		// Get the list of entities from the Spring Data Page
		List<U> entity = page.getContent();
		
		// Map the entities to their corresponding DTOs using ModelMapper, and collect them into a list.
		List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type))
				.collect(Collectors.toList());

		// Create a new PageableResponse object and set its properties.
		PageableResponse<V> response = new PageableResponse<>();
		response.setContent(dtoList);
		response.setPageNumber(page.getNumber());
		response.setPageSize(page.getSize());
		response.setTotalElements(page.getTotalElements());
		response.setTotalPages(page.getTotalPages());
		response.setLastPage(page.isLast());

		// Return the new PageableResponse object with the mapped DTOs
		return response;
	}
}
