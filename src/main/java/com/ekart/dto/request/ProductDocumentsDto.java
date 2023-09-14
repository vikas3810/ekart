package com.ekart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocumentsDto {
    private int productId;
    //    @NotBlank(message = "document Type should not be null")
//    private DocumentType documentType;
    private MultipartFile multipartFile;
}
