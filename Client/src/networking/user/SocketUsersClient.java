package networking.user;

import dtos.Request;
import dtos.reservation.ReservationReserveRequest;
import dtos.studentAuth.ChangeUserRequest;
import dtos.studentAuth.GetPasswordRequest;
import dtos.user.BlacklistUserRequest;
import dtos.user.UserDataDto;
import dtos.vehicle.DeleteVehicleRequest;
import dtos.vehicle.VehicleOwnerRequest;
import javafx.collections.ObservableList;
import networking.SocketService;

import java.util.List;

public class SocketUsersClient implements UsersClient
{
    @Override
    public List<UserDataDto> getUsers()
    {
        Request request = new Request("users", "view_users", null);
        return (List<UserDataDto>) SocketService.sendRequest(request);
    }


    @Override
    public void blacklist(BlacklistUserRequest blacklistRequest)
    {
        Request request = new Request("users", "blacklist", blacklistRequest);
        SocketService.sendRequest(request);

        VehicleOwnerRequest r1 = new VehicleOwnerRequest(blacklistRequest.email());
        Request request1 = new Request("yourVehicles", "delete_allVehicle",r1);
        SocketService.sendRequest(request1);

        ReservationReserveRequest r2 = new ReservationReserveRequest(blacklistRequest.email());
        Request request2 = new Request("reservation", "delete_allReservation",r2);
        SocketService.sendRequest(request2);
    }
    @Override
    public void blackListReason(BlacklistUserRequest blacklistRequest)
    {
        Request request = new Request("users", "blackListReason", blacklistRequest);
        SocketService.sendRequest(request);
    }
    @Override
    public String getBlackListReason(BlacklistUserRequest blacklistRequest)
    {
        return (String) SocketService.sendRequest(new Request("users","getBlackListReason", blacklistRequest));
    }

    @Override public void changeUser(ChangeUserRequest user)
    {
        Request request = new Request("users", "changeUser", user);
        SocketService.sendRequest(request);

    }

    @Override public String getPassword(GetPasswordRequest email)
    {
        Request request = new Request("users","getPassword", email);

        return (String) SocketService.sendRequest(request);
    }
}
