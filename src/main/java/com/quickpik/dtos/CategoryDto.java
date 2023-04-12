package com.quickpik.dtos;

import java.util.Date;
import org.hibernate.validator.constraints.Length;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDto {
	@Id
	private String categoryId;

	@NotBlank(message = "Please provide a valid title")
	@Length(min = 3, message = "Please add minimum of 3 characters in title")
	private String categoryTitle;

	@NotBlank(message = "Please provide a valid description")
	private String description;
	private String categoryImage;
	private Date createdAt;
	private Date updatedAt;
}
