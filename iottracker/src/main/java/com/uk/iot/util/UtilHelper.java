package com.uk.iot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Senthil on 05/06/2020.
 */
public class UtilHelper {

    public static String getDateTime(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }
}
