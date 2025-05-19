package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.*;

/**
 * A class representing a date with day, month, year, hour and minute.
 *
 * @author Patrik Golian
 * @version 1.0
 */
public class Date implements Serializable
{
  private int day;
  private int month;
  private int year;
  private int hour;
  private int minute;

  /**
   * No-argument constructor initializing the current date and time.
   */
  public Date()
  {
    LocalDate currentDate = LocalDate.now();
    LocalTime now = LocalTime.now();
    this.day = currentDate.getDayOfMonth();
    this.month = currentDate.getMonthValue();
    this.year = currentDate.getYear();
    this.hour = now.getHour();
    this.minute = now.getMinute();

  }

  /**
   * Three-argument constructor setting the minute and hour to 0.
   *
   * @param day   the date's day
   * @param month the date's month
   * @param year  the date's year
   */
  public Date(int day, int month, int year)
  {
    this.day = day;
    this.month = month;
    this.year = year;
    minute = 0;
    hour = 0;
  }

  /**
   * Five-argument constructor.
   *
   * @param day    the date's day
   * @param month  the date's month
   * @param year   the date's year
   * @param hour   the date's hour
   * @param minute the date's minute
   */
  public Date(int day, int month, int year, int hour, int minute)
  {
    this.day = day;
    this.month = month;
    this.year = year;
    this.hour = hour;
    this.minute = minute;
  }

  public LocalDate toLocalDate()
  {
    return LocalDate.of(year, month, day);
  }

  public java.sql.Date toSQLDate()
  {
    return java.sql.Date.valueOf(toLocalDate());
  }

  /**
   * Creates a Date object representing current date and time.
   *
   * @return a Date object initialized to current date and time
   */
  public static Date today()
  {
    LocalDate currentDate = LocalDate.now();
    LocalTime now = LocalTime.now();
    int currentday = currentDate.getDayOfMonth();
    int currentmonth = currentDate.getMonthValue();
    int currentyear = currentDate.getYear();
    int currenthour = now.getHour();
    int currentminute = now.getMinute();
    return new Date(currentday, currentmonth, currentyear, currenthour,
        currentminute);
  }

  /**
   * Gets the date's day.
   *
   * @return the date's day
   */
  public int getDay()
  {
    return day;
  }

  /**
   * Gets the date's month.
   *
   * @return the date's month
   */
  public int getMonth()
  {
    return month;
  }

  /**
   * Gets the date's year.
   *
   * @return the date's year
   */
  public int getYear()
  {
    return year;
  }

  /**
   * Gets the date's hour.
   *
   * @return the date's hour
   */
  public int getHour()
  {
    return hour;
  }

  /**
   * Gets the date's minute.
   *
   * @return the date's minute
   */
  public int getMin()
  {
    return minute;
  }

  /**
   * Sets the date's hour.
   *
   * @param hour what the date's hour will be set to
   */
  public void setHour(int hour)
  {
    this.hour = hour;
  }

  /**
   * Sets the date's minute.
   *
   * @param minute what the date's minute will be set to
   */
  public void setMin(int minute)
  {
    this.minute = minute;
  }

  /**
   * Sets the date's day.
   *
   * @param day what the date's day will be set to
   */
  public void setDay(int day)
  {
    this.day = day;
  }

  /**
   * Sets the date's month.
   *
   * @param month what the date's month will be set to
   */
  public void setMonth(int month)
  {
    this.month = month;
  }

  /**
   * Sets the date's year.
   *
   * @param year what the date's year will be set to
   */
  public void setYear(int year)
  {
    this.year = year;
  }

  /**
   * Calculates the number of days between the startDate and endDate.
   *
   * @param startDate the start date
   * @param endDate   the end date
   * @return the number of days between start and end date
   * @throws IllegalArgumentException if the end date is before start date
   */
  public static int calculatePeriod(Date startDate, Date endDate)
  {
    if (endDate.isBefore(startDate))
    {
      throw new IllegalArgumentException(
          "Check-out date must be after check-in date.");
    }
    int totalDays = startDate.daysInMonth() - startDate.getDay();

    if (startDate.getYear() == endDate.getYear())
    {
      if (startDate.getMonth() == endDate.getMonth())
      {
        if (startDate.getDay() == endDate.getDay())
        {
          return 0;
        }
        return endDate.getDay() - startDate.getDay();
      }

      int checkMonth = startDate.getMonth() + 1;
      for (int i = endDate.getMonth() - startDate.getMonth() - 1; i > 0; i--)
      {
        totalDays += daysInMonth(checkMonth, startDate.getYear());
        checkMonth++;
      }
      return totalDays + endDate.getDay();
    }

    int compare = startDate.getMonth() + 1;
    for (int i = 12; i >= compare; i--)
    {
      totalDays += daysInMonth(i, startDate.getYear());
    }

    int difference = endDate.getYear() - startDate.getYear();
    if (difference > 1)
    {
      int compares = startDate.getYear() + 1;
      int getvalue = endDate.getYear();
      for (int i = compares; i < getvalue; i++)
      {
        if (isLeapYear(i))
        {
          totalDays += 366;
        }
        totalDays += 365;
      }
    }
    int getendvalue = endDate.getMonth();
    for (int i = 1; i < getendvalue; i++)
    {
      totalDays += daysInMonth(i, endDate.getYear());
    }

    return totalDays + endDate.getDay();
  }

  /**
   * Checks if the year is a leap year.
   *
   * @return true if the year is leap year, else false
   */
  private boolean isLeapYear()
  {
    if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Checks if the given year is a leap year.
   *
   * @param year the year to check
   * @return true if the year is leap year, else false
   */
  public static boolean isLeapYear(int year)
  {
    return year % 400 == 0 || (year % 100 != 0 && year % 4 == 0);
  }

  /**
   * Calculates the number of days in the  month.
   *
   * @return the number of days in the month
   */
  public int daysInMonth()
  {
    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
        || month == 10 || month == 12)
    {
      return 31;
    }

    else if (month == 4 || month == 6 || month == 9 || month == 11)
    {
      return 30;
    }

    else if (month == 2)
    {
      if (isLeapYear())
      {
        return 29;
      }
      else
      {
        return 28;
      }
    }
    else
    {
      return 0;
    }
  }

  /**
   * Calculates the number of days in specific month and year.
   *
   * @param month the given month
   * @param year  the given year
   * @return the number of days in that month
   */
  public static int daysInMonth(int month, int year) // (1 complexity overall)
  {
    if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
        || month == 10 || month == 12)
    {
      return 31;
    }

    else if (month == 4 || month == 6 || month == 9 || month == 11)
    {
      return 30;
    }

    else if (month == 2)
    {
      if (isLeapYear(year))
      {
        return 29;
      }
      else
      {
        return 28;
      }
    }
    else
    {
      return 0;
    }
  }

  /**
   * Checks if the current date is before another date.
   *
   * @param date2 the date to compare with
   * @return true if the current date is before the given date, else false
   */
  public boolean isBefore(Date date2)
  {
    if (year < date2.year)
    {
      return true;
    }
    else if (year == date2.year)
    {
      if (month < date2.month)
      {
        return true;
      }

      else if (month == date2.month)
      {
        if (day < date2.day)
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Creates a copy of the current Date object.
   *
   * @return A new Date object with the same values as the current object
   */
  public Date copy()
  {
    return new Date(day, month, year, hour, minute);
  }

  /**
   * Compares day, month, year, hour and minute of two dates.
   *
   * @param obj the object to compare with
   * @return true if the given object is equal to this date
   */
  public boolean equals(Object obj)
  {
    if (obj == null || getClass() != obj.getClass())
    {
      return false;
    }
    Date other = (Date) obj;
    return this.day == other.day &&
        this.month == other.month &&
        this.year == other.year;
  }

  /**
   * Returns a string representation of the date.
   *
   * @return a string representation of the date in the format: month/day/year -- hour:minute
   */
  public String toString()
  {
    return day + "/" + month + "/" + year;
  }

}
