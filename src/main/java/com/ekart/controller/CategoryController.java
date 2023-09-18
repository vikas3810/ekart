package com.ekart.controller;

import com.ekart.dto.request.CategoryDto;
import com.ekart.dto.response.ApiResponse;
import com.ekart.exception.DocumentAlreadyExistsException;
import com.ekart.exception.EntityAlreadyExistException;
import com.ekart.exception.InvalidCategoryTypeException;
import com.ekart.exception.UnAuthorizedUserException;
import com.ekart.jwt.JwtService;
import com.ekart.model.Category;
import com.ekart.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ekart.util.AuthenticationHelper.*;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final JwtService jwtService;

    @PostMapping("/addCategory")

    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid CategoryDto categoryDto,
                                                HttpServletRequest request
    ) throws EntityAlreadyExistException, InvalidCategoryTypeException, UnAuthorizedUserException, DocumentAlreadyExistsException {
        log.info("addCategory called in CategoryController controller ");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //Check emailId belong to admin or not (contains @ekart)
        adminAuthorized(emailId);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);

        int authenticationResponse = categoryService.addCategory(categoryDto,emailId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "category is created successfully", authenticationResponse);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @PostMapping("/{categoryId}/deleteCategory")

    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable int categoryId,
                                                     HttpServletRequest request
    ) throws UnAuthorizedUserException {
        log.info("inside deleteCategory method controller");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //Check emailId belong to admin or not (contains @ekart)
        adminAuthorized(emailId);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);

        String result = categoryService.delete(categoryId);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "Category is deleted successfully", result);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }


    @PostMapping("/{categoryId}/updateCategory")

    public ResponseEntity<ApiResponse> updateCategory(@PathVariable int categoryId,
                                                     HttpServletRequest request,
                                                     @RequestBody @Valid CategoryDto categoryDto
    ) throws UnAuthorizedUserException, InvalidCategoryTypeException {
        log.info("inside updateProduct method controller");
        //get email from JWT(request)
        String emailId = getEmailFromJwt(request);

        //Check emailId belong to admin or not (contains @ekart)
        adminAuthorized(emailId);

        //check authorization
        compareJwtEmailIdAndCustomerEmailId(request,jwtService,emailId);

        String result = categoryService.update(categoryId,emailId,categoryDto);
        ApiResponse apiresponse = new ApiResponse(HttpStatus.CREATED.value(), "Category is updated successfully", result);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllCategory")

    public ResponseEntity<ApiResponse> getAllCategory() {
        log.info("display all category ");
        List<Category> list = categoryService.dispAllCategory();
        ApiResponse apiresponse = new ApiResponse(HttpStatus.OK.value(), "List of category", list);
        return new ResponseEntity<>(apiresponse, HttpStatus.OK);
    }

}
