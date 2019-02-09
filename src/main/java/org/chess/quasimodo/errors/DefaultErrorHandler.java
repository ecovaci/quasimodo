package org.chess.quasimodo.errors;

import java.text.MessageFormat;

import org.chess.quasimodo.application.Constants;
import org.chess.quasimodo.message.Message;
import org.chess.quasimodo.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;


@Component ("errorHandler")
public class DefaultErrorHandler implements ErrorHandler {
    
	@Autowired
	private MessageHandler messageHandler;
	
	@Override
	public void handleError(Throwable t) {
        if (t != null) {
        	if (t instanceof BusinessException) {
        		messageHandler.showErrorMessages(t.getMessage());
        	} else if (t instanceof AppException) {
        		messageHandler.showErrorMessage(t.getMessage(), t.getCause());
        	} else if (t instanceof FatalException) {
        		messageHandler.showErrorMessages(MessageFormat.format(Message.ERROR_FATAL, t.getMessage()));
        		System.exit(Constants.ERR_CODE_RUNTIME);
        	} else {
        		messageHandler.showErrorFullTrace(t);
        	}
        }
	}

}
