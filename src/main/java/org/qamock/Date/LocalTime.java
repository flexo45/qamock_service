package org.qamock.date;

import java.util.Date;

public class LocalTime {
    public static Date now(){
        Date date = new Date();
        return new Date(date.getTime() - 1 * 60 * 60 * 1000);
    }
}
