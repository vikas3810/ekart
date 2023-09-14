package com.ekart.exception;

import java.io.Serial;

public class EntityAlreadyExistException extends Exception {


	@Serial
	private static final long serialVersionUID = 1L;

		
		public EntityAlreadyExistException()
		{
			
		}
		public EntityAlreadyExistException(String msg)
		{
			super(msg);
		}
		
		
	

}
