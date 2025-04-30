package model.entities.reservation;

import model.Date;

public class Reservation
{
  private int vehicleId;
  private String vehicleType;
  private String ownerEmail;
  private String reservedByEmail;
  private Date startDate;
  private Date endDate;
  private double price;

  public Reservation(int vehicleId, String ownerEmail, String reservedByEmail, Date startDate, Date endDate, double price)
  {
    this.vehicleId = vehicleId;
    this.ownerEmail = ownerEmail;
    this.reservedByEmail = reservedByEmail;
    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
  }

  public int getVehicleId()
  {
    return vehicleId;
  }

  public String getVehicleType()
  {
    return vehicleType;
  }

  public String getOwnerEmail()
  {
    return ownerEmail;
  }

  public String getReservedByEmail()
  {
    return reservedByEmail;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public double getPrice()
  {
    return price;
  }

  @Override
  public String toString()
  {
    return "Reservation{" +
        "vehicleId=" + vehicleId + '\'' +
        ", ownerEmail='" + ownerEmail + '\'' +
        ", reservedByEmail='" + reservedByEmail + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", price=" + price +
        '}';
  }
}
