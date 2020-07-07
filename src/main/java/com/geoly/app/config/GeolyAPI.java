package com.geoly.app.config;

import javax.persistence.Query;
import java.util.List;

public class GeolyAPI {

    public static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery){
        List<Object> values = jooqQuery.getBindValues();
        for(int i = 0; i < values.size(); i++){
            hibernateQuery.setParameter(i + 1, values.get(i));
            System.out.println(values.get(i).toString());
        }
    }
}
