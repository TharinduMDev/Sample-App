package lk.ijse.dep11.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.tm.Employee;

import java.io.*;
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

        employeesList = readEmployee();
        employeesObservablList = FXCollections.observableList(employeesList);
        tblEmployee.setItems(employeesObservablList);

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((o,old,current) ->{
        txtID.setText(current.getId());
        txtName.setText(current.getName());
        txtContact.setText(current.getContact());
        btnDelete.setDisable(false);
        });

        Platform.runLater(()->{
            root.getScene().getWindow().setOnCloseRequest(e ->{
                txtSearch.clear();
                saveEmployees();
            });
        });
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String current) {
                if (current == null) return;
                filterText(current);
            }
        });
    }

    void filterText(String current){
        ArrayList<Employee> filterEmployee = new ArrayList<>();
        ObservableList<Employee> employees = FXCollections.observableList(filterEmployee);
        tblEmployee.setItems(employeesObservablList);
        for (Employee employee: tblEmployee.getItems()) {
            if(employee.getId().toLowerCase().startsWith(current.toLowerCase()) || employee.getName().toLowerCase().startsWith(current.toLowerCase()) || employee.getContact().toLowerCase().startsWith(current.toLowerCase())){
                filterEmployee.add(employee);
                tblEmployee.setItems(employees);
                tblEmployee.refresh();
            }
        }

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
        employeesList.remove(tblEmployee.getSelectionModel().getSelectedItem());
        tblEmployee.refresh();
        txtName.clear();
        txtContact.clear();
        txtID.clear();
        btnDelete.setDisable(true);
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
    public  void saveEmployees(){
        File file = new File("Employee_Details.dep");
        try {
            FileOutputStream fis = new FileOutputStream(file);
            BufferedOutputStream bis = new BufferedOutputStream(fis);
            ObjectOutputStream oos = new ObjectOutputStream(bis);
            oos.writeObject(employeesList);
            oos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Employee> readEmployee(){
        File file = new File("Employee_Details.dep");
        if(!file.exists()) return new ArrayList<Employee>();
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                ObjectInputStream oos = new ObjectInputStream(bis);
                try {
                    return (ArrayList<Employee>) oos.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
