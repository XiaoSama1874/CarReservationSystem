package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VehicleControl {
    ObservableList<Vehicle> vehicles;
    TableView<Vehicle> vehicleTableView;
    Pane vehicleViewPane;
    Pane functionPane;

    public VehicleControl(ObservableList<Vehicle> list) {
        this.vehicles = list;
        this.vehicleTableView = getVehicleTableView();
        this.vehicleViewPane = getVehicleViewPane(list);
        this.functionPane=getVehicleFunctionPane();
        getWholeVehiclePane();
    }
    public Pane getWholeVehiclePane(){
        VBox box=new VBox(10);
        functionPane=getVehicleFunctionPane();
        vehicleViewPane=getVehicleViewPane(vehicles) ;
        box.getChildren().addAll(functionPane,vehicleViewPane);
        return box;
    }

    public TableView<Vehicle> getVehicleTableView() {
        vehicleTableView = new TableView<>();
        TableColumn vehicleID = new TableColumn("编号");
        TableColumn modelName = new TableColumn("型号");
        TableColumn yearsUsed = new TableColumn("已使用年限");
        TableColumn fuelType = new TableColumn("燃油类型");
        TableColumn Status = new TableColumn("状态");
        TableColumn fee = new TableColumn("费用");
        TableColumn number = new TableColumn("序号");

        vehicleID.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleID"));
        modelName.setCellValueFactory(new PropertyValueFactory<>("modelName"));
        yearsUsed.setCellValueFactory(new PropertyValueFactory<>("yearsUsed"));
        fuelType.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        fee.setCellValueFactory(new PropertyValueFactory<>("fee"));



        vehicleTableView.getColumns().addAll(number, vehicleID, modelName, yearsUsed, fuelType, fee, Status);
        vehicleTableView.setPrefSize(500, 600);
        vehicleTableView.setItems(vehicles);

//                序列行自动增加
        number.setCellFactory((col) -> {
            TableCell<Vehicle, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty) {
                        int rowIndex = this.getIndex() + 1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
            return cell;
        });
        return vehicleTableView;
    }

    //    功能函数
    public Pane getVehicleFunctionPane() {
        GridPane function = new GridPane();
        function.setAlignment(Pos.CENTER);
        function.setPadding(new Insets(30, 20, 10, 10));
        function.setHgap(10);
        function.setVgap(10);
//            checkBox for filter
        RadioButton showRepair = new RadioButton("修理中");
        RadioButton showAvalible = new RadioButton("可使用");
        ToggleGroup group1=new ToggleGroup();
        showAvalible.setToggleGroup(group1);
        showRepair.setToggleGroup(group1);

//


        ToggleGroup group = new ToggleGroup();
        RadioButton showCar = new RadioButton();
        RadioButton showVan = new RadioButton();
        RadioButton showAll = new RadioButton();
        showAll.setSelected(true);
//      绑定函数 显示修理中的车辆
        showRepair.selectedProperty().addListener((e)->{
            ObservableList<Vehicle> tempt=FXCollections.observableArrayList();
            for (Vehicle vehicle :
                    vehicles) {
                if (vehicle.getStatus().equals("可使用")){
                    tempt.add(vehicle);
                }
            }
            vehicleTableView.setItems(tempt);
        });
//      绑定函数 显示修理中的车辆
        showAvalible.selectedProperty().addListener((e)->{
            ObservableList<Vehicle> tempt=FXCollections.observableArrayList();
            for (Vehicle vehicle :
                    vehicles) {
                if (vehicle.getStatus().equals("修理中")){
                    tempt.add(vehicle);
                }
            }
            vehicleTableView.setItems(tempt);
        });
//      绑定函数 显示可以使用的车辆

//    绑定函数 只看车;
        showCar.selectedProperty().addListener((e)->{
            ObservableList<Vehicle> cars=FXCollections.observableArrayList();
            for (Vehicle vehicle :
                    vehicles) {
                if (vehicle instanceof Car){
                    cars.add(vehicle);
                }
            }
            vehicleTableView.setItems(cars);
        });
//      绑定函数显示所有的货车
        showVan.selectedProperty().addListener((e)->{
            ObservableList<Vehicle> vans=FXCollections.observableArrayList();
            for (Vehicle vehicle :
                    vehicles) {
                if (vehicle instanceof Van){
                    vans.add(vehicle);
                }
            }
            vehicleTableView.setItems(vans);
        });
//       绑定函数显示全部的车
        showAll.selectedProperty().addListener((e)->{
            vehicleTableView.setItems(vehicles);
        });


        showCar.setToggleGroup(group);
        showVan.setToggleGroup(group);
        showAll.setToggleGroup(group);

        Label label = new Label();
        TextField searchFied = new TextField();
        searchFied.setPromptText("输入车牌号进行搜索");

        Button btn1 = new Button("搜索");
        Button btn2 = new Button("重置");
        btn1.setPrefWidth(100);
        searchFied.setPrefColumnCount(50);

        searchFied.textProperty().addListener((e, o, n) -> {
            btn1.setDisable(n.isEmpty());
        });
//        绑定搜索
        btn1.setOnAction((e)->{
            for (Vehicle vehicle:vehicles
                 ) {
                if (searchFied.getText().equals(vehicle.getVehicleID())){
                    vehicleTableView.setItems(FXCollections.observableArrayList(vehicle));
                }
            }
        });
//        绑定重置按钮
        btn2.setOnAction((e)->{
            vehicleTableView.setItems(vehicles);
            searchFied.setText("");
            showAll.setSelected(true);
        });

        Label Title = new Label("车辆管理");
        Title.setFont(Font.font("Arial", 25));

        function.add(Title, 0, 0, 3, 1);
        GridPane.setHalignment(Title, HPos.CENTER);
        HBox tempt1 = new HBox();
        tempt1.setSpacing(20);
        HBox tempt2 = new HBox();
        tempt2.setSpacing(40);
        tempt1.getChildren().addAll(showCar, new Label("汽车"), showVan, new Label("卡车"), showAll, new Label("全部汽车"));
        tempt2.getChildren().addAll(showRepair, showAvalible);
        function.addRow(1, tempt2);
        function.addRow(2, tempt1);
        function.addRow(3, searchFied, btn1,btn2);

        return function;
    }

    //        面板
    public BorderPane getVehicleViewPane(ObservableList<Vehicle> items) {

        Pagination pagination = new Pagination();

        //                添加分页
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                showList(param);
                return vehicleTableView;
            }
        });

        BorderPane container = new BorderPane();
        container.setCenter(vehicleTableView);
        container.setBottom(pagination);
        BorderPane.setMargin(pagination, new Insets(0, 0, 40, 0));

        return container;
    }
    public VehicleControl.Vehicle registerVehicle(Stage stage) throws Exception{
        GridPane pane=new GridPane();
//        车牌 使用时间 价格 容量
        TextField model=new TextField();
        TextField yearUsed=new TextField();
        TextField fee=new TextField();
        TextField capacity=new TextField();
//       卡车还是汽车
        ComboBox<String> typeBox=new ComboBox<>();
        typeBox.setItems(FXCollections.observableArrayList("汽车","货车"));
        typeBox.setValue("汽车");
//        烧油类型
        ComboBox<String> fuelTypeBox=new ComboBox<>();
        fuelTypeBox.setItems(FXCollections.observableArrayList("90#","93#","97#"));
        fuelTypeBox.setValue("90#");

        Dialog dialog=new Dialog();
        dialog.setTitle("新车登记");
//        主界面
        dialog.initOwner(stage);
        dialog.setHeaderText(null);

//        添加图片
        Image photo=new Image("sample/registerVehicle.jpg");
        ImageView image=new ImageView(photo);
        image.setFitWidth(300);
        image.setFitHeight(300);
        dialog.setGraphic(image);

        pane.setPadding(new Insets(20,20,20,20));
        pane.setHgap(10);
        pane.setVgap(20);
        pane.addRow(0,new Label("类型"),typeBox);
        pane.addRow(1,new Label("汽车/货车车辆型号"),model);
        pane.addRow(2,new Label("使用年限"),yearUsed);
        pane.addRow(3,new Label("燃油类型"),fuelTypeBox);
        pane.addRow(4,new Label("每小时价格"),fee);
        pane.addRow(5,new Label("负载量/引擎容量 ："),capacity);

        dialog.getDialogPane().setContent(pane);
//       设置按钮
        ButtonType close=new ButtonType("取消",ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType regiseter=new ButtonType("注册", ButtonBar.ButtonData.OK_DONE);
//      添加确认按钮
        dialog.getDialogPane().getButtonTypes().setAll(FXCollections.observableArrayList(close,regiseter));
        Node confirmButton=dialog.getDialogPane().lookupButton(regiseter);
        confirmButton.setDisable(true);
        model.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(model.getText().isEmpty()||yearUsed.getText().isEmpty()||fee.getText().isEmpty());
        });
        yearUsed.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(model.getText().isEmpty()||yearUsed.getText().isEmpty()||fee.getText().isEmpty());
        });
        fee.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(model.getText().isEmpty()||yearUsed.getText().isEmpty()||fee.getText().isEmpty());
        });
        capacity.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(model.getText().isEmpty()||yearUsed.getText().isEmpty()||fee.getText().isEmpty());
        });

//        设置按钮触发
        Optional<ButtonType> result=dialog.showAndWait();
        if (result.get()==close){
            dialog.close();
        }else {
//            显示注册成功
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("注册");
            alert.setHeaderText(null);
            alert.setContentText("注册成功");
            alert.showAndWait();
            dialog.close();
            if (typeBox.getValue().equals("汽车")){
                System.out.println(model.getText());
                System.out.println(yearUsed.getText());
                System.out.println(fuelTypeBox.getValue());
                System.out.println(fee.getText());
                System.out.println(capacity.getText());
                return new Car("123",model.getText().trim(),Integer.parseInt(yearUsed.getText().trim()),fuelTypeBox.getValue(),Integer.parseInt(fee.getText().trim()),Integer.parseInt(capacity.getText()));
            }
            else{
                return new Van("123",model.getText().trim(),Integer.parseInt(yearUsed.getText().trim()),fuelTypeBox.getValue(),Integer.parseInt(fee.getText().trim()),Integer.parseInt(capacity.getText()));}
        }
        throw new Exception();
    }

    //  显示第 param 页的数据
    private void showList(Integer param) {

    }

    public static class Vehicle {
        private Map<LocalDate, ArrayList<Label>> date = new HashMap<>();
        private final SimpleStringProperty vehicleID;
        private final SimpleStringProperty modelName;
        private final SimpleIntegerProperty yearsUsed;
        private final SimpleStringProperty fuelType;
        private final SimpleStringProperty status;
        private final SimpleIntegerProperty fee;

        public Vehicle(String vehicleID, String modelName, int yearsUsed, String fuelType, int fee) {
            this.vehicleID = new SimpleStringProperty(vehicleID);
            this.modelName = new SimpleStringProperty(modelName);
            this.yearsUsed = new SimpleIntegerProperty(yearsUsed);
            this.fuelType = new SimpleStringProperty(fuelType);
            this.status = new SimpleStringProperty("可用");
            this.fee = new SimpleIntegerProperty(fee);
        }

        public String getVehicleID() {
            return vehicleID.get();
        }

        public void setVehicleID(String vehicleID) {
            this.vehicleID.set(vehicleID);
        }

        public String getModelName() {
            return modelName.get();
        }

        public void setModelName(String modelName) {
            this.modelName.set(modelName);
        }

        public int getYearsUsed() {
            return yearsUsed.get();
        }

        public void setYearsUsed(int yearsUsed) {
            this.yearsUsed.set(yearsUsed);
        }

        public String getFuelType() {
            return fuelType.get();
        }


        public void setFuelType(String fuelType) {
            this.fuelType.set(fuelType);
        }

        public String getStatus() {
            return status.get();
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public int getFee() {
            return fee.get();
        }

        public void setFee(int fee) {
            this.fee.set(fee);
        }

        public Map<LocalDate, ArrayList<Label>> getDate() {
            return date;
        }

        public void setDate(Map<LocalDate, ArrayList<Label>> date) {
            this.date = date;
        }
        @Override
        public String toString(){
            //            货车，车牌号，使用年份，车型，燃油类型，价格,状态,最大载重;
            return getVehicleID()+","+getModelName()+","+getYearsUsed()+","+getFuelType()+","+getFee()+","+getStatus();
        }
    }

    public static class Van extends Vehicle {
        private SimpleIntegerProperty maximumLoadCapacity;

        public Van(String vehicleID, String modelName, int yearsUsed, String fuelType, int fee, int maximumLoadCapacity) {
            super(vehicleID, modelName, yearsUsed, fuelType, fee);
            this.maximumLoadCapacity = new SimpleIntegerProperty(maximumLoadCapacity);
        }

        public void setMaximumLoadCapacity(int maximumLoadCapacity) {
            this.maximumLoadCapacity.set(maximumLoadCapacity);
        }

        public int getMaximumLoadCapacity() {
            return maximumLoadCapacity.get();
        }
        @Override
        public String toString(){
            //            货车，车牌号，使用年份，车型，燃油类型，价格,状态,最大载重;
            return "货车"+","+super.toString()+","+getMaximumLoadCapacity();
        }
    }

    public static class Car extends Vehicle {
        private SimpleIntegerProperty engineCapacity;

        public Car(String vehicleID, String modelName, int yearsUsed, String fuelType, int fee, int engineCapacity) {
            super(vehicleID, modelName, yearsUsed, fuelType, fee);
            this.engineCapacity = new SimpleIntegerProperty(engineCapacity);
        }

        public void setEngineCapacity(int engineCapacity) {
            this.engineCapacity.set(engineCapacity);
        }

        public int getEngineCapacity() {
            return engineCapacity.get();
        }
        @Override
        public String toString(){
//            汽车，车牌号，车型，使用年份，燃油类型，价格,状态,引擎容量;
            return "汽车"+","+super.toString()+","+getEngineCapacity();
        }
    }
}
