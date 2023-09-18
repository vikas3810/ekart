package com.ekart.service;

import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.FileFormatNotSupportedException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.model.Category;
import com.ekart.model.CategoryDocuments;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryDocumentsService {
    String upload(String emailId,int categoryId, MultipartFile file) throws IOException, EntityAlreadyExistException, FileFormatNotSupportedException, DocumentAlreadyExistsException, InvalidCategoryTypeException;
    String delete(int categoryId, String type) throws InvalidCategoryTypeException;
    byte[] download(int categoryId, String documentType) throws IOException, InvalidCategoryTypeException;

    List<CategoryDocuments> getCategoryImagesByCategoryId(int categoryId);
}
