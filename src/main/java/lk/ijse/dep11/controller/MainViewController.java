package lk.ijse.dep11.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Employee;

import java.util.ArrayList;

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
