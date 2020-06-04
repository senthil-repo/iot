package com.uk.iot.util;

import com.uk.iot.model.Body;
import com.uk.iot.model.Result;
import org.springframework.http.HttpStatus;

/**
 * Created by Senthil on 04/06/2020.
 */
public class ResultBuilder {
    final private static String SPACE = " ";
    final private static String COLON = " : ";
    public Result buildDataLoadResult(HttpStatus httpStatus, String body) {
        StringBuilder status = new StringBuilder(httpStatus.value()).append(SPACE).append(httpStatus.getReasonPhrase());
        Result result = new Result(status.toString(), buildBody(body));
        return result;
    }

    private Body buildBody(String description) {
        Body body = new Body();
        body.setDescription(description);
        return body;
    }
}
