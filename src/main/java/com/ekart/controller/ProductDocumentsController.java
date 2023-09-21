package com.ekart.controller;

import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.FileFormatNotSupportedException;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.service.ProductDocumentsService;
import com.ekart.util.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ekart.util.AuthenticationHelper.getEmailFromJwt;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class ProductDocumentsController {
    private final ProductDocumentsService productDocumentsService;
    private final JwtService jwtService;

    /**
     * Upload a document for a product.
     *
     * @param productId The productId to which the document is related.
     * @param file      The document file to upload.
     * @param request   The HTTP request.
     * @return ResponseEntity with ApiResponse indicating success or failure.
     * @throws FileFormatNotSupportedException if the file format is not supported.
     * @throws UnAuthorizedUserException      if the user is not authorized.
     * @throws IOException                    if there is an I/O error while processing the file.
     */
    @PostMapping(value = "/{productId}/upload")
    public ResponseEntity<ApiResponse> upload(
            @PathVariable int productId,
            @RequestParam(required = false) MultipartFile file,
            HttpServletRequest request
    ) throws FileFormatNotSupportedException, UnAuthorizedUserException, IOException {
        log.info("Upload category document called in customer controller");
        String emailId = getEmailFromJwt(request);

        // Verify provided userId is available in JWT; throw UnAuthorizedException if different
        AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId(request, jwtService, emailId);

        String path = productDocumentsService.upload(emailId, productId, file);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Document uploaded Successfully", path);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
