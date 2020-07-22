package com.geoly.app.config;

import com.geoly.app.models.StatusMessage;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

public class GeolyAPI {

    public static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery){
        List<Object> values = jooqQuery.getBindValues();
        for(int i = 0; i < values.size(); i++){
            hibernateQuery.setParameter(i + 1, values.get(i));
        }
    }

    public static List catchException(Exception e){
        Sentry.capture(e);
        e.printStackTrace();
        return Collections.singletonList(new ResponseEntity<>(StatusMessage.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
