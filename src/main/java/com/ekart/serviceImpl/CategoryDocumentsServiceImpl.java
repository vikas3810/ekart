package com.ekart.serviceImpl;

import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.FileFormatNotSupportedException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.model.Category;
import com.ekart.model.CategoryDocuments;
import com.ekart.model.User;
import com.ekart.repo.CategoryDocumentsRepo;
import com.ekart.repo.CategoryRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.CategoryDocumentsService;
import com.ekart.util.ExcelFileHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryDocumentsServiceImpl implements CategoryDocumentsService {
    private final CategoryRepo categoryRepo;
    private final CategoryDocumentsRepo categoryDocumentsRepo;
    private final UserRepo userRepo;
    @Value("${file.upload-dir}")
    String pathDir;

    @Override
    public String upload(String emailId,int categoryId, MultipartFile file)
            throws IOException, FileFormatNotSupportedException, DocumentAlreadyExistsException, InvalidCategoryTypeException {
        log.info("customer document upload called in CustomerDocumentsServiceImpl");
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        // Category validation
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

//		String pathDir = "D:/images/";
        String fileName = file.getOriginalFilename();
        String path = pathDir + File.separator + fileName;
//        assert fileName != null;
        String fileFormat = ExcelFileHelper.getExtension(fileName);
        //check required file format
        if (!fileFormat.equalsIgnoreCase("jpg") && !fileFormat.equalsIgnoreCase("jpeg") && !fileFormat.equalsIgnoreCase("png")) {
            throw new FileFormatNotSupportedException("File Format Not Supported");
        }
        //file save to destination
        file.transferTo(new File(path));
        //build and save category document object
        CategoryDocuments cdd = CategoryDocuments.builder()
                .category(category)
                .documentFormat(fileFormat)
                .path(path)
                .isActive(true)
                .createdBy(user.getEmailId())
                .createdDate(LocalDate.now())
                .modifiedBy(user.getEmailId())
                .modifiedDate(LocalDate.now())
                .build();
        CategoryDocuments customerDocuments = categoryDocumentsRepo.save(cdd);
        return customerDocuments.getPath();
    }

    @Override
    public String delete(int customerId, String type) throws InvalidCategoryTypeException {
        log.info("customer validation");
//        if (!customerRepo.existsById(customerId)) {
//            throw new ResourceNotFoundException("Customer not found");
//        }
//        // get document type
//        DocumentType documentType = getDocumentType(type);
//        CustomerDocuments customerDocument = customerDocumentRepo.findByCustomerCustomerIdAndDocumentTypeAndIsActive(customerId,
//                documentType, true);
//        if (customerDocument == null) {
//            throw new ResourceNotFoundException("Document not found");
//        }
//        customerDocument.setActive(false);
//        customerDocumentRepo.save(customerDocument);
        return "document deleted";
    }

    @Override
    public byte[] download(int categoryId, String documentType) throws IOException, InvalidCategoryTypeException {
        return new byte[0];
    }

    @Override
    public List<CategoryDocuments> getCategoryByCategoryId(int categoryId) {
        return categoryDocumentsRepo.findByCategoryCategoryIdAndIsActive(categoryId,true);
    }
}
