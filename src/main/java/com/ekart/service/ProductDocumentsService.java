package com.ekart.service;

import com.ekart.exception.FileFormatNotSupportedException;
import com.ekart.model.CategoryDocuments;
import com.ekart.model.ProductDocuments;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ProductDocumentsService {
    String upload(String emailId, int productId, MultipartFile file) throws FileFormatNotSupportedException, IOException;

    List<ProductDocuments> getProductByProductId(int productId);
}
