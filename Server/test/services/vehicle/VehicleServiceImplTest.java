package services.vehicle;

import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.entities.vehicles.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;

class VehicleServiceImplTest
{

  @Mock private VehicleService service;

  @InjectMocks private VehicleServiceImpl vehicleService = mock(
      VehicleServiceImpl.class);

  @BeforeEach void setUp() throws Exception
  {
//    Mockito
//        .when(service.fetchAllVehicles())
//        .thenReturn(List.of(
//            new Bike(1, "Type", "Brand", "Model",
//                "Condition", "Color", 20,
//                "bikeType","owner@via.dk","Available"),
//            new EBike(2, "Type", "Brand", "Model",
//                "Condition","Color",60,"bikeType",
//                50, 1000, "owner@via.dk", "Available"),
//            new Scooter(3, "Type", "Brand", "Model",
//                "Condition", "Color", 30, 20,
//                30,"owner@via.dk", "Available")
//        ));
  }

  @Test public void testGetVehiclesOverview()
  {
    try
    {
      VehicleService service = new VehicleServiceImpl();
      var list = service.getVehiclesOverview();
      assertNotNull(list);
      list.forEach(System.out::println);
    }
    catch (Exception e)
    {
      fail("Exception occurred: " + e.getMessage());
    }
  }
}