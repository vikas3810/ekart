package com.ekart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDocumentsDto {
    private int categoryId;
    private MultipartFile multipartFile;
}
