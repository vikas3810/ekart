package com.ekart.controller;

import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.*;
import com.ekart.jwt.JwtService;
import com.ekart.service.CategoryDocumentsService;
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
@RequestMapping("/category")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class CategoryDocumentsController {
    private final CategoryDocumentsService categoryDocumentsService;
private final JwtService jwtService;

    @PostMapping(value = "/{categoryId}/upload")

    public ResponseEntity<ApiResponse> upload(
                                              @PathVariable int categoryId,
                                              @RequestParam(required = false) MultipartFile file,
                                              HttpServletRequest request
    ) throws EntityAlreadyExistException, FileFormatNotSupportedException, DocumentAlreadyExistsException, InvalidCategoryTypeException, UnAuthorizedUserException, IOException {
        log.info("Upload category document called in customer controller");
        String emailId = getEmailFromJwt(request);

        AuthenticationHelper.compareJwtEmailIdAndCustomerEmailId(request, jwtService,emailId);
        String path = categoryDocumentsService.upload(emailId, categoryId,file);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Document uploaded Successfully", path);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
