package seregez.opu.abiturientonpu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yevgen on 21.06.14.
 */
public class DateHelper {

    public static String parseDateToString(Date date){
        return date.toString();
    }

    public static Date parseStringToDate(String dateString) throws ParseException {

        Date date;
        SimpleDateFormat simpleDateFormat   = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        date                                =   simpleDateFormat.parse(dateString);
        return date;

    }

    public static boolean compareDateFromSettingsWithDateFromWeb(String updateTime,String uploadedDate) throws ParseException {

        try {
            Date   parsedDate   = parseStringToDate(uploadedDate);
            Date   settingsDate = parseStringToDate(updateTime);
            long   parsedDateMillis   = parsedDate.getTime();
            long   settingsDateMillis = settingsDate.getTime();
            return parsedDateMillis > settingsDateMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;

    }

}
