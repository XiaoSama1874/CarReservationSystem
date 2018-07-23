package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import javafx.scene.image.ImageView;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class ResevationControl {
    public TableView<Reservation> reservationTableView;
    public TableView<Reservation> reservationTableViewForVehicle;
    public TableView<Reservation> reservationTableViewForCustomer;
    public ObservableList<Reservation> reservations;
    BorderPane reservationViewPane;

    ResevationControl(ObservableList<Reservation> reservations) {

//                生成
        this.reservations = reservations;
        this.reservationTableView = getReservationTableView();
        this.reservationTableViewForCustomer=getReservationTableView();
        this.reservationTableViewForVehicle=getReservationTableView();
        this.reservationViewPane = getReservationViewPane();
    }

    //    public BorderPane addReservation(CustomerControl.Customer customer , VehicleControl.Vehicle vehicle){
//
//    }
    public VBox getWholeReservationPane() {
        VBox box = new VBox(20);
        Pane functionAbilityPane = getReservationFunctionPane();
        reservationViewPane = getReservationViewPane();
        box.getChildren().addAll(functionAbilityPane, reservationViewPane);
        return box;
    }

    private TableView getReservationTableView() {
        TableView<Reservation> reservationTableView = new TableView<>();
        TableColumn reserveID = new TableColumn("订单编号");
        TableColumn customerName = new TableColumn("顾客");
        TableColumn vehicleID = new TableColumn("车辆编号");
        TableColumn pickTime = new TableColumn("取车时间");
        TableColumn pickDate = new TableColumn("日期");
        TableColumn returnTime = new TableColumn("还车时间");
        TableColumn status = new TableColumn("状态");
        TableColumn number = new TableColumn("序号");

        reserveID.setCellValueFactory(new PropertyValueFactory<VehicleControl.Vehicle, String>("reserveID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        vehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
        pickTime.setCellValueFactory(new PropertyValueFactory<>("p_pickTime"));
        pickDate.setCellValueFactory(new PropertyValueFactory<>("p_pickDate"));
        returnTime.setCellValueFactory(new PropertyValueFactory<>("p_returnTime"));
        status.setCellValueFactory(new PropertyValueFactory<>("Status"));

        reservationTableView.getColumns().addAll(reserveID, vehicleID, customerName, pickTime, pickDate, returnTime, status);
        reservationTableView.setPrefSize(500, 300);
        number.setCellFactory((col) -> {
            TableCell<VehicleControl.Vehicle, String> cell = new TableCell<>() {
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
        reservationTableView.setItems(reservations);
//        reservationTableView.setRowFactory(new Callback<TableView<ResevationControl.Reservation>, TableRow<ResevationControl.Reservation>>() {
//            @Override
//            public TableRow<ResevationControl.Reservation> call(TableView<ResevationControl.Reservation> param) {
//                return new ReservationTableRow();
//            }
//        });

        return reservationTableView;
    }

//    class ReservationTableRow extends TableRow {
//        public ReservationTableRow() {
//            super();
//            this.setOnMouseClicked(E -> {
//                if (E.getButton().equals(MouseButton.PRIMARY) && E.getClickCount() == 2 && ReservationTableRow.this.getIndex() < reservationTableView.getItems().size()) {
//                    ResevationControl.Reservation reservation = reservationTableView.getSelectionModel().getSelectedItem();
//                    Dialog dialog = new Dialog();
//                    GridPane pane = new GridPane();
//                    int money = reservation.getVehicle().getFee() * (reservation.getReturnTime().minusHours(reservation.getPickTime().getHour()).getHour());
//                    if (LocalTime.now().withNano(0).plusHours(1).isAfter(reservation.getReturnTime())) {
//                        int overTimeMoey = (LocalTime.now().withNano(0).minusHours(reservation.getPickTime().getHour())).getHour() * reservation.getVehicle().getFee() * 4;
//                        money += overTimeMoey;
//                    }
//                    pane.addRow(0, new Label("订单号"), new Label(reservation.getReserveID()), new Label("下单时间"), new Label(reservation.getReserveTime().toString()));
//                    pane.addRow(1, new Label("顾客"), new Label(reservation.getCustomerName()), new Label("会员级别"), new Label(reservation.getCustomer().getCategory()));
//                    pane.addRow(2, new Label("取车日期"), new Label(reservation.getP_pickDate()), new Label("取车时间"), new Label(reservation.getP_pickTime()));
//                    pane.addRow(4, new Label("现在时间"), new Label("" + LocalTime.now().withNano(0)), new Label("预计归还时间"), new Label(reservation.getReturnTime().toString()));
//                    ;
//                    pane.addRow(5, new Label("租用车辆计费/小时", new Label("" + reservation.getVehicle().getFee())));
//                    pane.add(new Label("如果超时，超时多于一小时 超时部分将按4被正常费用计费 "), 0, 6, 3, 1);
//                    pane.add(new Label("总计 (RMB):" + money), 2, 8, 1, 1);
//
//                    pane.setVgap(30);
//                    pane.setHgap(30);
//                    ButtonType pay = new ButtonType("付款", ButtonBar.ButtonData.OK_DONE);
//                    ButtonType cancle = new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//                    ImageView imageView=new ImageView(new Image("sample/payReservation.png"));
//                    imageView.setFitWidth(300);
//                    imageView.setFitHeight(400);
//                    dialog.setGraphic(imageView);
//                    dialog.setTitle("订单详情");
//                    dialog.setHeaderText(null);
//                    dialog.getDialogPane().setContent(pane);
//                    dialog.getDialogPane().getButtonTypes().setAll(pay, cancle);
//                    Optional<ButtonType> result = dialog.showAndWait();
//                    Node node = dialog.getDialogPane().lookupButton(pay);
//                    node.setDisable(reservation.getStatus().equals("已完成"));
//                    if (result.get() == pay) {
//                        reservation.setStatus("已完成");
//                        dialog.close();
//                    }
//
//                }
//            });
//        }
//    }

    private BorderPane getReservationViewPane() {

        Pagination pagination = new Pagination();
//                添加分页
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                showList(param);
                return reservationTableView;
            }
        });

//                序列行自动增加

//               处理双击选中事件  此时显示有关该订单的详细信息;

//        reservationTableView.setRowFactory(new Callback<TableView<Reservation>, TableRow<Reservation>>() {
//            @Override
//            public TableRow<Reservation> call(TableView<Reservation> param) {
//                return new TableRowControl();
//            }
//        });

        BorderPane container = new BorderPane();
        container.setCenter(reservationTableView);
        container.setBottom(pagination);
        BorderPane.setMargin(pagination, new Insets(0, 0, 40, 0));
        return container;
    }

    //  显示第 param 页的数据
    private void showList(Integer param) {

    }

    class TableRowControl extends TableRow {
        public TableRowControl() {
            super();
            this.setOnMouseClicked(e -> {
                if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2 && TableRowControl.this.getIndex() < reservationTableView.getItems().size()) {
                    showDetailAboutReservationPane(reservationTableView.getSelectionModel().getSelectedItem());
                }
            });
        }
    }

    private void showDetailAboutReservationPane(Reservation selectedItem) {

    }

    public Pane getReservationFunctionPane() {
        GridPane function = new GridPane();
        function.setAlignment(Pos.CENTER);
        function.setPadding(new Insets(30, 20, 10, 10));
        function.setHgap(10);
        function.setVgap(10);

//            checkBox for filter
        CheckBox showRepair = new CheckBox("未完成订单");
        CheckBox showAvalible = new CheckBox("已完成订单");

        ToggleGroup group = new ToggleGroup();

        RadioButton showCar = new RadioButton();
        RadioButton showVan = new RadioButton();
        RadioButton showAll = new RadioButton();

        showCar.setToggleGroup(group);
        showVan.setToggleGroup(group);
        showAll.setToggleGroup(group);

        Label label = new Label();
        TextField searchFied = new TextField();
        searchFied.setPromptText("输入订单编号进行搜索");

        Button btn = new Button("搜索");
        btn.setPrefWidth(100);
        searchFied.setPrefColumnCount(50);

        searchFied.textProperty().addListener((e, o, n) -> {
            btn.setDisable(n.isEmpty());
        });

        Label Title = new Label("订单管理");
        Title.setFont(Font.font("Arial", 25));

        function.add(Title, 0, 0, 3, 1);
        GridPane.setHalignment(Title, HPos.CENTER);
        HBox tempt2 = new HBox();
        tempt2.setSpacing(40);
        tempt2.getChildren().addAll(showRepair, showAvalible);
        function.addRow(1, tempt2);
        function.addRow(2, searchFied, btn);

        return function;
    }

    public static class Reservation implements Serializable {

        static String ID = "10000";
        private VehicleControl.Vehicle vehicle;
        private CustomerControl.Customer customer;

        private LocalTime pickTime;
        private LocalTime returnTime;
        private LocalDate pickDate;

        private LocalDateTime reserveTime;

        private SimpleStringProperty reserveID;
        private SimpleStringProperty vehicleID;
        private SimpleStringProperty customerName;
        private SimpleStringProperty p_pickTime;
        private SimpleStringProperty p_pickDate;
        private SimpleStringProperty p_returnTime;
        private SimpleStringProperty Status;

        public Reservation(VehicleControl.Vehicle vehicle, CustomerControl.Customer customer, LocalTime pickTime, LocalTime returnTime, LocalDate pickDate, LocalDateTime reserveTime) {
            this.vehicle = vehicle;
            this.customer = customer;
            this.pickTime = pickTime;
            this.returnTime = returnTime;
            this.pickDate = pickDate;
            this.reserveTime = reserveTime;
            ID = "" + (Integer.parseInt(ID) + 1);
            this.reserveID = new SimpleStringProperty(ID);
            this.vehicleID = new SimpleStringProperty(vehicle.getVehicleID());
            this.customerName = new SimpleStringProperty(customer.getName());
            this.Status = new SimpleStringProperty("未完成");

            this.p_pickDate = new SimpleStringProperty(pickDate.toString());
            this.p_pickTime = new SimpleStringProperty(pickTime.toString());
            this.p_returnTime = new SimpleStringProperty(returnTime.toString());

        }


        public void setVehicle(VehicleControl.Vehicle vehicle) {
            this.vehicle = vehicle;
        }

        public VehicleControl.Vehicle getVehicle() {
            return vehicle;
        }


        public void setCustomer(CustomerControl.Customer customer) {
            this.customer = customer;
        }


        public void setPickTime(LocalTime pickTime) {
            this.pickTime = pickTime;
        }


        public void setReturnTime(LocalTime returnTime) {
            this.returnTime = returnTime;
        }


        public void setPickDate(LocalDate pickDate) {
            this.pickDate = pickDate;
        }


        public void setReserveTime(LocalDateTime reserveTime) {
            this.reserveTime = reserveTime;
        }

        public String getReserveID() {
            return reserveID.get();
        }


        public void setReserveID(String reserveID) {
            this.reserveID.set(reserveID);
        }

        public String getVehicleID() {
            return vehicleID.get();
        }


        public void setVehicleID(String vehicleID) {
            this.vehicleID.set(vehicleID);
        }

        public String getCustomerName() {
            return customerName.get();
        }


        public void setCustomerName(String customerName) {
            this.customerName.set(customerName);
        }

        public String getP_pickTime() {
            return p_pickTime.get();
        }


        public void setP_pickTime(String p_pickTime) {
            this.p_pickTime.set(p_pickTime);
        }

        public String getP_pickDate() {
            return p_pickDate.get();
        }


        public void setP_pickDate(String p_pickDate) {
            this.p_pickDate.set(p_pickDate);
        }

        public String getStatus() {
            return Status.get();
        }

        public void setStatus(String status) {
            this.Status.set(status);
            System.out.println(status);
        }

        public CustomerControl.Customer getCustomer() {
            return customer;
        }

        public LocalTime getPickTime() {
            return pickTime;
        }

        public LocalTime getReturnTime() {
            return returnTime;
        }

        public LocalDateTime getReserveTime() {
            return reserveTime;
        }

        public String getP_returnTime() {
            return p_returnTime.get();
        }

        public void setP_returnTime(String p_returnTime) {
            this.p_returnTime.set(p_returnTime);
        }
    }
}
