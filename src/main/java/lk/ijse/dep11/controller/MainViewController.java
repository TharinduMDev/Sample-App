package lk.ijse.dep11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Employee;

import java.util.ArrayList;
import java.util.List;

public class MainViewController {
    public AnchorPane root;
    public TextField txtID;
    public TextField txtName;
    public TextField txtContact;
    public TableView<Employee> tblEmployee;
    public Button btnSave;
    public Button btnDelete;
    public Button btnAddNewEmployee;

    public ArrayList<Employee> employeesList = new ArrayList<>();
    public TextField txtSearch;

    public ObservableList<Employee> employeesObservablList;
    public  void initialize(){
        tblEmployee.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblEmployee.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblEmployee.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));

        for(Control control: new Control[]{txtName,txtContact,btnDelete,btnSave}){
            control.setDisable(true);
        }
        btnAddNewEmployee.requestFocus();

        //employeesList = readEmployee();
        employeesList.add(new Employee("E-001","Shan","+9477-6589898"));
        employeesObservablList = FXCollections.observableList(employeesList);
        tblEmployee.setItems(employeesObservablList);

    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if(!isDataValid()){
            return;
        }
        Employee newEmployee = new Employee(txtID.getText(), txtName.getText(), txtContact.getText());
        employeesList.add(newEmployee);
        tblEmployee.refresh();
        txtName.clear();
        txtContact.clear();
        btnAddNewEmployee.fire();
    }
    boolean isDataValid(){
        if(!txtName.getText().matches("[A-Za-z ]+")){
            txtName.requestFocus();
            txtName.selectAll();
            return false;
        }
        if(!txtContact.getText().matches("[+]94\\d{2}-\\d{7}")){
            txtContact.requestFocus();
            txtContact.selectAll();
            return  false;
        }
        for (Employee employee: getEmployeeList()) {
            if(employee.equals(tblEmployee.getSelectionModel().getSelectedItem())){
                new Alert(Alert.AlertType.ERROR,"This Employee already Exist").show();
                return false;
            }
            if ( employee.getContact().equals(txtContact.getText())){
                new Alert(Alert.AlertType.ERROR,"Contact number already exist").show();
                txtContact.requestFocus();
                txtContact.selectAll();
                return false;
            }
        }
        return true;
    }
    public List<Employee> getEmployeeList(){
        return tblEmployee.getItems();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnAddNewEmployeeOnAction(ActionEvent actionEvent) {
        txtName.setDisable(false);
        txtContact.setDisable(false);
        btnSave.setDisable(false);
        txtName.requestFocus();
        txtID.setText(getID());
    }
    String getID(){
        if(employeesList.isEmpty())return "E-001";
        return String.format("E-%03d",Integer.parseInt(employeesList.get(employeesList.size()-1).getId().substring(2))+1);
    }

    public void tblEmployeeOnDragDetected(MouseEvent event) {
    }

    public void tblEmployeeOnDragOver(DragEvent dragEvent) {
    }

    public void tblEmployeeOnDragDropped(DragEvent dragEvent) {
    }

}
