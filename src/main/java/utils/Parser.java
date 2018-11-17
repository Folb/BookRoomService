package utils;

import controllers.IndexController;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class Parser {

    private static final Logger LOG = Logger.getLogger(IndexController.class.getName());

    public static Integer parseInt(String type, String i) {
        Integer parsedInt = null;

        try {
            parsedInt = Integer.parseInt(i);
        } catch (NumberFormatException e) {
            LOG.warning("Could not parse " + i + "to int.");
        }

        return parsedInt;
    }

    public static LocalDateTime parseDate(String type, String date) {
        String[] ds = date.split("-");
        Integer year = parseInt("year", ds[0]);
        Integer month = parseInt("month", ds[1]);
        Integer day = parseInt("day", ds[2]);

        LocalDateTime parsedDate = null;

        if (year != null && month != null && day != null) {
            if (type.equals("start")) {
                parsedDate = LocalDateTime.now()
                        .withYear(year)
                        .withMonth(month)
                        .withDayOfMonth(day)
                        .withHour(0)
                        .withMinute(0);
            } else {
                parsedDate = LocalDateTime.now()
                        .withYear(year)
                        .withMonth(month)
                        .withDayOfMonth(day)
                        .withHour(23)
                        .withMinute(59);
            }
        } else {
            LOG.warning("Some of the dates cannot be parsed, returning null");
        }

        return parsedDate;

    }

}
