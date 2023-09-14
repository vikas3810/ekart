package com.ekart.serviceImpl;

import com.ekart.exception.FileFormatNotSupportedException;
import com.ekart.model.CategoryDocuments;
import com.ekart.model.Product;
import com.ekart.model.ProductDocuments;
import com.ekart.model.User;
import com.ekart.repo.ProductDocumentsRepo;
import com.ekart.repo.ProductRepo;
import com.ekart.repo.UserRepo;
import com.ekart.service.ProductDocumentsService;
import com.ekart.util.ExcelFileHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductDocumentsServiceImpl implements ProductDocumentsService {
    private final ProductDocumentsRepo productDocumentsRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    @Value("${file.upload-dir}")
    String pathDir;
    @Override
    public String upload(String emailId, int productId, MultipartFile file) throws FileFormatNotSupportedException, IOException {
        log.info("customer document upload called in CustomerDocumentsServiceImpl");
        User user = userRepo.findByEmailId(emailId).orElseThrow(ResourceNotFoundException::new);
        Product product = productRepo.findById(productId).orElseThrow(ResourceNotFoundException::new);
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
        //build and save customer_document object
        ProductDocuments productDocuments = ProductDocuments.builder()
                .product(product)
                .documentFormat(fileFormat)
                .path(path)
                .isActive(true)
                .createdBy(user.getEmailId())
                .createdDate(LocalDate.now())
                .modifiedBy(user.getEmailId())
                .modifiedDate(LocalDate.now())

                .build();
        ProductDocuments savedProductDocument = productDocumentsRepo.save(productDocuments);
        return savedProductDocument.getPath();
    }

    @Override
    public List<ProductDocuments> getProductByProductId(int productId) {
        return productDocumentsRepo.findByProductProductIdAndIsActive(productId,true);
    }
}
