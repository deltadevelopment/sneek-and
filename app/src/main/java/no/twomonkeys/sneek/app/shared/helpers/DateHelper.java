package no.twomonkeys.sneek.app.shared.helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 26/09/16 by simenlie
 * Copyright 2MONKEYS AS
 */

public class DateHelper {

    public static boolean hasExpired(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Log.v("date", "date " + date);
            Date date1 = formatter.parse(date);

            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(date1); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 24); // adds one hour
            date1 = cal.getTime();

            Date date2 = new Date();

            if (date1.compareTo(date2) < 0) {
                return true;
            }

        } catch (ParseException e) {
            return false;
        }

        return false;
    }

    public static boolean dateLaterThan(Date date, int seconds) {
        Date dateNow = new Date();
        long secondsDiff = (dateNow.getTime() - date.getTime()) / 1000;
        Log.v("DIFF is", "DIFF " + secondsDiff);
        return true;
    }

    public static String shortTime(String dateString) {
        String finalString = "00.00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date dateFromString = formatter.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFromString);
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);
            String hoursString = hours < 10 ? "0" + hours : hours + "";
            String minuteString = minutes < 10 ? "0" + minutes : minutes + "";
            finalString = hoursString + "." + minuteString;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return finalString;
    }

    public static String dateNowInString() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(new Date());
    }

    public static boolean isSameTimeWithDates(Date dateOne, Date dateTwo) {

        long difference = dateOne.getTime() - dateTwo.getTime();
        long seconds = difference / 1000;
        long minutes = seconds / 60;
        // Log.v("MIn","minutes is " + minutes + " " + dateOne.toString()+ " " + dateTwo.toString());
        if (minutes > 5) {
            return false;
        }
        return true;
    }


    public static Date dateForString(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSameDayWithDates(Date dateOne, Date dateTwo) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(dateOne);
        cal2.setTime(dateTwo);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        return sameDay;
    }

    public static String shortTimeAfter(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1 = formatter.parse(dateString);
            String outputString;

            long diff = date1.getTime() - new Date().getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds < 0){
                return "expired_txt";
            }

            if (seconds > 60) {
                if (minutes > 60) {
                    if (hours > 24) {
                        outputString = days + " days";
                    } else {
                        outputString = hours + " hours";
                    }
                } else {
                    outputString = minutes + " minutes";
                }
            } else {
                outputString = seconds + " seconds";
            }

            return outputString;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String shortTimeSince(String date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.v("date", "date " + date);
        try {
            Date date1 = formatter.parse(date);
            String outputString;

            long diff = new Date().getTime() - date1.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (seconds > 60) {
                if (minutes > 60) {
                    if (hours > 24) {
                        outputString = days + "d";
                    } else {
                        outputString = hours + "h";
                    }
                } else {
                    outputString = minutes + "m";
                }
            } else {
                outputString = seconds + "s";
            }

            return outputString;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Pretty dates

    public static String prettyStringFromDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = formatter.parse(dateString);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date1);
            int day = cal2.get(Calendar.DAY_OF_WEEK);
            int month = cal2.get(Calendar.MONTH);
            int hour = cal2.get(Calendar.HOUR);
            int minutes = cal2.get(Calendar.MINUTE);


            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String monthName = month_date.format(cal2.getTime());

            String hourString = hour < 10 ? hour + "0" : hour + "";
            String minuteString = minutes < 10 ? minutes + "0" : minutes + "";

            String finalString = day + " " + monthName + " " + hourString + "." + minuteString;

            return finalString;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String prettyMonthYear(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = formatter.parse(dateString);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date1);
            int day = cal2.get(Calendar.DAY_OF_MONTH);
            int month = cal2.get(Calendar.MONTH);
            int year = cal2.get(Calendar.YEAR);
            int hour = cal2.get(Calendar.HOUR);
            int minutes = cal2.get(Calendar.MINUTE);


            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            String monthName = month_date.format(cal2.getTime());


            Date dateNow = new Date();
            long diffInMs = dateNow.getTime() - date1.getTime();

            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);

            int seconds = (int) diffInSec;
            int minutes2 = seconds / 60;
            int hours = minutes2 / 60;
            int days = hours / 24;

            if (days < 6) {
                return prettyDayOfWeek(dateString);
            }

            String finalString = monthName + " " + day + ", " + year;

            return finalString;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String prettyDayOfWeek(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            Date date1 = formatter.parse(dateString);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date1);
            int weekDay = cal2.get(Calendar.DAY_OF_WEEK);
            int hour = cal2.get(Calendar.HOUR);
            int minutes = cal2.get(Calendar.MINUTE);

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            String dayName = dayFormat.format(cal2.getTime());

            if (isSameDayWithDates(date1, new Date())) {
                dayName = "Today";
            }

            return dayName;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


}
