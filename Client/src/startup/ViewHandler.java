package startup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import networking.addnew.AddNewVehicleClient;
import networking.addnew.SocketAddNewVehicleClient;
import networking.adminallvehicles.AdminAllVehiclesClient;
import networking.adminallvehicles.SocketAllVehiclesClient;
import networking.authentication.AuthenticationClient;
import networking.authentication.SocketAuthenticationClient;
import networking.myvehicles.MyVehiclesClient;
import networking.myvehicles.SocketMyVehiclesClient;
import networking.reservation.ReservationClient;
import networking.reservation.SocketReservationClient;
import networking.studentaccount.SocketStudentAccountClient;
import networking.studentaccount.StudentAccountClient;
import networking.user.SocketUsersClient;
import networking.user.UsersClient;
import ui.addnew.AddNewController;
import ui.addnew.AddNewVM;
import ui.adminaccount.UserFx;
import ui.adminallvehicles.AdminAllVehiclesController;
import ui.adminallvehicles.AdminAllVehiclesVM;
import ui.common.Controller;
import ui.login.LoginController;
import ui.login.LoginVM;
import ui.myvehicles.MyVehiclesController;
import ui.myvehicles.MyVehiclesVM;
import ui.popup.MessageType;
import ui.popup.PopupController;
import ui.register.RegisterController;
import ui.register.RegisterVM;
import ui.reservation.ReservationController;
import ui.reservation.ReservationVM;
import ui.studentaccount.StudentAccountController;
import ui.studentaccount.StudentAccountVM;
import ui.adminaccount.ViewUsersController;
import ui.adminaccount.ViewUsersVM;

import java.io.IOException;

public class ViewHandler
{
  private static Stage stage;

  public ViewHandler(Stage stage)
  {
    this.stage = stage;
  }

  public void start()
  {
    showView(ViewType.LOGIN);
    stage.show();
    stage.setResizable(false);
    stage.getIcons().add(
        new Image(getClass().getResourceAsStream("../resources/icon-06.png")));
  }

  public static void showView(ViewType viewToShow)
  {
    try
    {
      switch (viewToShow)
      {
        case REGISTER -> openRegisterView();
        case LOGIN -> openLoginView();
        case ADDNEW -> openAddNewView();
        case RESERVATION -> openReservationView();
        case STUDENTACCOUNT -> openStudentAccountView();
        case MYVEHICLES -> openMyVehiclesView();
        case VIEWUSERS -> openUsersOverview();
        case ADMINALLVEHICLES -> openAdminAllVehicles();
        default -> throw new RuntimeException("View not found.");
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void popupMessage(MessageType type, String message)
  {
    Stage stage = new Stage();
    stage.setMinWidth(300);
    stage.setMinHeight(200);
    stage.setResizable(false);


    PopupController controller = new PopupController(stage, type, message);

    FXMLLoader fxmlLoader = new FXMLLoader(
        ViewHandler.class.getResource("../ui/popup/Popup.fxml"));
    fxmlLoader.setControllerFactory(ignore -> controller);

    try
    {
      Scene scene = new Scene(fxmlLoader.load());
      stage.setTitle("Error");
      stage.setScene(scene);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    stage.show();
  }

  public static void popupMessage(MessageType type, String message, UserFx user,
      UsersClient userService)
  {
    Stage stage = new Stage();
    stage.setMinWidth(300);
    stage.setMinHeight(200);
    stage.setResizable(false);

    PopupController controller = new PopupController(stage, type, message, user,
        userService);

    FXMLLoader fxmlLoader = new FXMLLoader(
        ViewHandler.class.getResource("../ui/popup/Popup.fxml"));
    fxmlLoader.setControllerFactory(ignore -> controller);

    try
    {
      Scene scene = new Scene(fxmlLoader.load());
      stage.setTitle("Error");
      stage.setScene(scene);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    stage.show();
  }

  private static void openLoginView() throws IOException
  {
    AuthenticationClient service = new SocketAuthenticationClient();
    LoginVM vm = new LoginVM(service);
    LoginController controller = new LoginController(vm);
    String viewTitle = "viaGO - Login";
    String viewSubPath = "login/Login.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openRegisterView() throws IOException
  {
    AuthenticationClient service = new SocketAuthenticationClient();
    RegisterVM vm = new RegisterVM(service);
    RegisterController controller = new RegisterController(vm);
    String viewTitle = "viaGO - Register";
    String viewSubPath = "register/Register.fxml";
    openView(viewTitle, viewSubPath, controller);

  }

  private static void openAddNewView() throws IOException
  {
    AddNewVehicleClient addNew = new SocketAddNewVehicleClient();
    AddNewVM vm = new AddNewVM(addNew);
    AddNewController controller = new AddNewController(vm);
    String viewTitle = "viaGO - Add New";
    String viewSubPath = "addnew/AddNew.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openReservationView() throws IOException
  {
    ReservationClient reservationClient = new SocketReservationClient();
    ReservationVM vm = new ReservationVM(reservationClient);
    ReservationController controller = new ReservationController(vm);
    String viewTitle = "viaGO - Reservation";
    String viewSubPath = "reservation/Reservation.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openStudentAccountView() throws IOException
  {
    StudentAccountClient studentAccountClient = new SocketStudentAccountClient();
    StudentAccountVM vm = new StudentAccountVM(studentAccountClient);
    StudentAccountController controller = new StudentAccountController(vm);
    String viewTitle = "viaGO - Student Account";
    String viewSubPath = "studentaccount/StudentAccount.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openMyVehiclesView() throws IOException
  {
    MyVehiclesClient myVehiclesClient = new SocketMyVehiclesClient();
    MyVehiclesVM vm = new MyVehiclesVM(myVehiclesClient);
    MyVehiclesController controller = new MyVehiclesController(vm);
    String viewTitle = "viaGO - My Vehicles";
    String viewSubPath = "myvehicles/MyVehicles.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openUsersOverview() throws IOException
  {
    UsersClient client = new SocketUsersClient();
    ViewUsersVM vm = new ViewUsersVM(client);
    ViewUsersController controller = new ViewUsersController(vm);
    String viewTitle = "viaGO - Overview";
    String viewSubPath = "adminaccount/ViewUsers.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openAdminAllVehicles() throws IOException
  {
    AdminAllVehiclesClient client = new SocketAllVehiclesClient();
    AdminAllVehiclesVM vm = new AdminAllVehiclesVM(client);
    AdminAllVehiclesController controller = new AdminAllVehiclesController(vm);
    String viewTitle = "viaGO - All Vehicles";
    String viewSubPath = "adminallvehicles/AdminAllVehicles.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openView(String viewTitle, String viewSubPath,
      Controller controller) throws IOException
  {
    FXMLLoader fxmlLoader = new FXMLLoader(
        ViewHandler.class.getResource("../ui/" + viewSubPath));
    fxmlLoader.setControllerFactory(ignore -> controller);

    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle(viewTitle);
    stage.setScene(scene);
  }
}
