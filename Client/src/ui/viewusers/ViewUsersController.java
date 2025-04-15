package ui.viewusers;

import javafx.scene.control.*;
import ui.common.Controller;

public class ViewUsersController implements Controller
{
    private final ViewUsersVM vm;
    public Button promoteButton;
    public Button blacklistButton;
    public Button changePasswordButton;
    public TableView<UserFx> table;
    public TableColumn<UserFx, String> firstNameColumn;
    public TableColumn<UserFx, String> lastNameColumn;
    public TableColumn<UserFx, String> emailColumn;
    public TableColumn<UserFx, Boolean> blacklistedColumn;

    public ViewUsersController(ViewUsersVM vm)
    {
        this.vm = vm;
    }

    public void initialize()
    {
        vm.loadUsers();
        promoteButton.visibleProperty().bind(vm.showPromoteButtonProperty());
        promoteButton.disableProperty().bind(vm.disablePromoteButtonProp());

        blacklistButton.visibleProperty().bind(vm.showBlacklistButtonProperty());
        blacklistButton.disableProperty().bind(vm.disableBlacklistButtonProp());

        changePasswordButton.disableProperty().bind(vm.disableChangePasswordButtonProperty());

        table.setItems(vm.getUsersList());
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        vm.getSelectedIndexProperty().bind(table.getSelectionModel().selectedIndexProperty());

        firstNameColumn.setCellValueFactory(param -> param.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(param -> param.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(param -> param.getValue().emailProperty());
        blacklistedColumn.setCellValueFactory(param -> param.getValue().isBlacklistedProperty());
    }

    public void onPromote()
    {
        vm.promote();
    }

    public void onBlacklist()
    {
        vm.blacklist();
    }

    public void onChangePassword()
    {

    }

    public void onSort(SortEvent<TableView<UserFx>> tableViewSortEvent)
    {
        // not sure what this can do.. will investigate
    }
}
