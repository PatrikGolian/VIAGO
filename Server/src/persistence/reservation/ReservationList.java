package persistence.reservation;

import model.entities.reservation.Reservation;

import java.util.List;

public class ReservationList
{
  public List<Reservation> reservations;

  public ReservationList(List<Reservation> reservations)
  {
    this.reservations = reservations;
  }
}
