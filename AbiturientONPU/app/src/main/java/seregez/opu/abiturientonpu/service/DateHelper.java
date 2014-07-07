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

    public static boolean compareDateFromSettingsWithDateFromWeb(String lastUpdateDate,String serverDate) throws ParseException {

        if (serverDate == null || lastUpdateDate == "") {
            return true;
        }

        try {
            Date   parsedDate   = parseStringToDate(serverDate);
            Date   settingsDate = parseStringToDate(lastUpdateDate);
            long   parsedDateMillis   = parsedDate.getTime();
            long   settingsDateMillis = settingsDate.getTime();
            return !(parsedDateMillis > settingsDateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;

    }

    public static String getDefaultDate(){
        return "01.01.2014 00:00:00";
    }

}
