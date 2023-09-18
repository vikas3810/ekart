package com.ekart.exception;


import com.ekart.dto.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
		String validationErrors = ex.getBindingResult().getFieldError().getDefaultMessage();
		return ResponseEntity.badRequest().body(validationErrors);
	}
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse> handleIoJsonWebTokenExpiredException(ExpiredJwtException ex) {
		log.info("handleIoJsonWebTokenExpiredException fired");
		ApiResponse apiResponse = new ApiResponse(400,"something went wrong",ex.getMessage());
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(JwtExpiredException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiResponse> handleJwtExpiredException(JwtExpiredException ex){
		log.info("handleJwtExpiredException fired");
		ApiResponse apiResponse = new ApiResponse(400,"something went wrong",ex.getMessage());
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
	}
		@ExceptionHandler(InvalidCredentials.class)
		@ResponseStatus(code = HttpStatus.BAD_REQUEST)
		public ResponseEntity<ApiResponse> handleIcsdException(InvalidCredentials ex){
			ApiResponse apiResponse = new ApiResponse(400,"something went wrong",ex.getMessage());
			return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
		}


	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IncompleteDataException.class)
	public ResponseEntity<ApiResponse> handleIncompleteDataException(IncompleteDataException ex,
																		  WebRequest webRequest)
	{
		log.info("handleIncompleteDataException fired");
		return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	    @ResponseStatus(HttpStatus.BAD_REQUEST)
	    @ExceptionHandler(EntityAlreadyExistException.class)
	    public ResponseEntity<ApiResponse> handleEntityAlreadyExistsException(EntityAlreadyExistException ex,
	                                                                          WebRequest webRequest) 
	    {
	    	log.info("handleEntityAlreadyExistsException fired");
	        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
	                HttpStatus.BAD_REQUEST);
	    }

	    @ExceptionHandler(ResourceNotFoundException.class)
	    @ResponseStatus(HttpStatus.NOT_FOUND)
	    public ResponseEntity<ApiResponse> handleResourecNotFoundExecption(ResourceNotFoundException ex, WebRequest webRequest) 
	    {
	    	log.info("handleResourecNotFoundExecption fired");
	        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
	                HttpStatus.BAD_REQUEST);
	    }

	@ExceptionHandler(UnAuthorizedUserException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse> handleUnAuthorizedUserException(UnAuthorizedUserException ex, WebRequest webRequest)
	{
		log.info("handleUnAuthorizedUserException fired");
		return new ResponseEntity<>(new ApiResponse(HttpStatus.UNAUTHORIZED.value(),  ex.getMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(FileFormatNotSupportedException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse> handleFileFormatNotSupportedException(FileFormatNotSupportedException ex, WebRequest webRequest)
	{
		log.info("FileFormatNotSupportedException fired");
		return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(DocumentAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse> handleDocumentAlreadyExistsException(DocumentAlreadyExistsException ex, WebRequest webRequest)
	{
		log.info("DocumentAlreadyExistsException fired");
		return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(InSufficientBalanceException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse> handleInSufficientBalanceException(InSufficientBalanceException ex, WebRequest webRequest)
	{
		log.info("InSufficientBalanceException fired");
		return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidCategoryTypeException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ApiResponse> handleInvalidDocumentTypeException(InvalidCategoryTypeException ex, WebRequest webRequest)
	{
		log.info("InvalidCategoryTypeException fired");
		return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}


}
