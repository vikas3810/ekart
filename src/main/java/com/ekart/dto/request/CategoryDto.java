package com.ekart.dto.request;

import com.ekart.model.CategoryType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotNull(message = "Category Name should not be null")
    private CategoryType categoryType;
    @NotNull(message = "slug should not be null")
    private String slug;
    @NotNull(message = "description should not be null")
    private String description;


}
