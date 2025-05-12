/*package services.vehicle;

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
      //VehicleService service = new VehicleServiceImpl();
      var list = service.getVehiclesOverview();
      assertNotNull(list);
      list.forEach(System.out::println);
    }
    catch (Exception e)
    {
      fail("Exception occurred: " + e.getMessage());
    }
  }
}*/

package services.vehicle;

import dtos.vehicle.AddNewBikeRequest;
import dtos.vehicle.AddNewEBikeRequest;
import dtos.vehicle.AddNewScooterRequest;
import model.entities.vehicles.Bike;
import model.entities.vehicles.EBike;
import model.entities.vehicles.Scooter;
import model.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.vehicle.VehicleDao;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceImplTest {

  private VehicleDao mockDao;
  private VehicleServiceImpl service;

  @BeforeEach
  void setUp() {
    mockDao = mock(VehicleDao.class);
    service = new VehicleServiceImpl(mockDao);
  }

  @Test
  void addNewBike_validRequest_callsDaoAddWithBike() throws SQLException {
    var req = new AddNewBikeRequest(
         1,
         "bike",
         "BrandCo",
         "X100",
         "good",
         "Blue",
         15.0,
         "Mountain",
         "owner@example.com",
         "Available"
    );

    service.addNew(req);

    // verify that we handed off a Bike instance to the DAO
    verify(mockDao, times(1)).add(any(Bike.class));
  }

  @Test
  void addNewEBike_validRequest_callsDaoAddWithEBike() throws SQLException {
    var req = new AddNewEBikeRequest(
        2,
        "e-bike",
        "eBrand",
        "E200",
        "new",
        "Green",
        25.5,
        30,
        80,
        "mountain",
        "ebike-owner@example.com",
        "Available"
    );

    service.addNew(req);

    verify(mockDao, times(1)).add(any(EBike.class));
  }

  @Test
  void addNewScooter_validRequest_callsDaoAddWithScooter() throws SQLException {
    var req = new AddNewScooterRequest(
        3,
        "scooter",
        "ScootCo",
        "S300",
        "used",
        "Red",
        12.0,
        20,
        50,
        "scooter-owner@example.com",
        "Available"
    );

    service.addNew(req);

    verify(mockDao, times(1)).add(any(Scooter.class));
  }

  @Test
  void addNewBike_invalidBrand_throwsValidationException() {
    // brand contains digits ----> invalid
    var req = new AddNewBikeRequest(
        4,
        "bike",
        "Brand123",
        "ModelX",
        "good",
        "Black",
        10.0,
        "Road",
        "owner@example.com",
        "Available"
    );

    ValidationException ex = assertThrows(ValidationException.class, () -> {
      service.addNew(req);
    });
    assertTrue(ex.getMessage().contains("Brand can contain only letters"));
    verifyNoInteractions(mockDao);
  }
}
