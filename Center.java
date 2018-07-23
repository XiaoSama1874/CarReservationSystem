package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Center extends Application{

    Company company;

    VehicleControl vehicleControl;
    CustomerControl customerControl;
    ResevationControl resevationControl;
    BorderPane mainPane;
    Button v1=new Button("车辆列表|预定");
    Button c1=new Button("用户列表");
    Button c3=new Button("注册用户");
    Button v3=new Button("注册车辆");
    Button reservations=new Button("所有订单");
    Button exit=new Button("退出系统");

   @Override
    public void start(Stage stage){
        mainPane=new BorderPane();
        company=new Company();
        resevationControl=new ResevationControl(company.getReservations());
        customerControl=new CustomerControl(company.getCustomers());
        vehicleControl=new VehicleControl(company.getVehicles());
//        关闭前保存数据
        stage.setOnCloseRequest((E)->{
            saveData();
        });

       Scene scene=new Scene(mainPane,1000,800);
//       绑定按钮
//       测试数据
       addTextData();
//     添加图片
       ImageView imageView=new ImageView("sample/Background.png");
       imageView.setFitWidth(1000);
       imageView.setFitHeight(30);
       mainPane.setLeft(leftAdministrationPane());
       mainPane.setTop(imageView);
//      显示
       ButtonBind(stage);
       stage.setScene(scene);
       stage.show();
   }

    private void saveData() {
        try {
            FileWriter customersWriter=new FileWriter("C:\\Users\\xiaobin\\IdeaProjects\\carManegementSystem\\src\\sample\\customers",false);
            FileWriter vehicleWriter=new FileWriter("C:\\Users\\xiaobin\\IdeaProjects\\carManegementSystem\\src\\sample\\customers",false);
            ObjectOutputStream reservationsWriter=new ObjectOutputStream(new FileOutputStream("C:\\Users\\xiaobin\\IdeaProjects\\carManegementSystem\\src\\sample\\reservations.dat"));
            for (VehicleControl.Vehicle vehicle :company.getVehicles()
                    ) {
                vehicleWriter.write(vehicle.toString()+"\n");
            }
            for (CustomerControl.Customer customer :company.getCustomers()
                    ) {
                customersWriter.write(customer.toString()+"\n");
            }
            reservationsWriter.writeObject(company.getReservations().toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTextData(){
       company.getCustomers().add(new CustomerControl.Customer("肖斌","13940210572","726581880@qq.com","至尊会员"));
       company.getCustomers().add(new CustomerControl.Customer("撒旦","13940210572","726581880@qq.com","普通用户"));
       company.getVehicles().add(new VehicleControl.Car("京PBS362","玛莎拉蒂",1,"太阳能充电",100,1000));
       company.getVehicles().add(new VehicleControl.Car("粤CKC236","保时捷",2,"火柴",100,2000));
    }
    public void ButtonBind(Stage stage){
//  车辆列表 车辆预定
       v1.setOnAction(e->{
           mainPane.setCenter(vehicleControl.getWholeVehiclePane());
       });
//       顾客列表
        exit.setOnAction(e->{stage.close();saveData();});
       c1.setOnAction(e->mainPane.setCenter(customerControl.customerTableViewPane));
        bindRowOfVehicleTableView();

        bindRowOfReservationTableView(resevationControl.reservationTableView);
        bindRowOfReservationTableView(resevationControl.reservationTableViewForCustomer);
        bindRowOfReservationTableView(resevationControl.reservationTableViewForVehicle);

        bindRowOfCustomerTableView();
//          顾客注册
       c3.setOnAction((e)->{
        try{company.getCustomers().add(customerControl.registerCustomer(stage));
        } catch (Exception ex){
            System.out.println("register process was interuppted nothing returned");
         }
       });
//     展示所有订单
        reservations.setOnAction((e)->{
            mainPane.setCenter(resevationControl.getWholeReservationPane());
        });
       //       车辆注册
       v3.setOnAction((e)->{
        try{company.getVehicles().add(vehicleControl.registerVehicle(stage));
        } catch (Exception ex){
            System.out.println("registration of new Vehicle was canceled");
         }
       });

    }


    public Pane  leftAdministrationPane(){
        Button Header=new Button("公司简介");


        TitledPane vehicle=new TitledPane();
        vehicle.setText("车辆预定系统");

        Button v2=new Button("车辆查看|搜索");


//        添加 预定系统栏目
        VBox box1=new VBox();
        box1.setSpacing(10);
        box1.getChildren().addAll(v1,v2,v3);
        vehicle.setContent(box1);

        TitledPane customer=new TitledPane();
//        用户系统

        Button c2=new Button("用户搜索");

//     添加用户管理系统
        VBox box2=new VBox();
        box2.setSpacing(10);
        box2.getChildren().addAll(c1,c2,c3);
        customer.setContent(box2);

        TitledPane benefit=new TitledPane();
        benefit.setText("业绩统计");
        Button b1=new Button("本月盈利");
        Button b2=new Button("历史统计");
//      添加业绩显示
        VBox box3=new VBox();
        box3.setSpacing(10);
        box3.getChildren().addAll(b1,b2);
        benefit.setContent(box3);


        TitledPane system=new TitledPane();
        system.setText("系统设置");
        Button btn1=new Button("信息修改");
        Button btn2=new Button("预定系统设置");
        Button btn3=new Button("密码更改");
        VBox box4=new VBox();
        box4.setSpacing(10);
        box4.getChildren().addAll(btn1,btn2,btn3);
        system.setContent(box4);



        VBox container=new VBox();
        container.getChildren().addAll(Header,reservations,vehicle,customer,benefit,system,exit);
        container.setSpacing(20);
        container.setPadding(new Insets(10,10,30,10));
        return container;
    }
    public void bindRowOfVehicleTableView(){
        vehicleControl.vehicleTableView.setRowFactory(new javafx.util.Callback<TableView<VehicleControl.Vehicle>, TableRow<VehicleControl.Vehicle>>(){
            @Override
            public TableRow<VehicleControl.Vehicle> call(TableView<VehicleControl.Vehicle> param) {
                return new VehicleTableRow();
            }
        });
    }
    private void bindRowOfCustomerTableView() {
        customerControl.customerTableView.setRowFactory(new Callback<TableView<CustomerControl.Customer>, TableRow<CustomerControl.Customer>>() {
            @Override
            public TableRow<CustomerControl.Customer> call(TableView<CustomerControl.Customer> param) {
                return new CustomerTableRow();
            }
        });
    }
    class CustomerTableRow extends TableRow {
        public CustomerTableRow(){
            super();
            this.setOnMouseClicked(e->{
                if (e.getButton().equals(MouseButton.PRIMARY)&&e.getClickCount()==2&&CustomerTableRow.this.getIndex()<customerControl.customerTableView.getItems().size()){
                    showDetailAboutCustomerPane(customerControl.customerTableView.getSelectionModel().getSelectedItem());
                }
            });
        }
    }
    private void showDetailAboutCustomerPane(CustomerControl.Customer selectedItem){
       ObservableList<ResevationControl.Reservation> tempt=FXCollections.observableArrayList();
        for (ResevationControl.Reservation reservation:company.getReservations()) {
            if (reservation.getCustomer().equals(selectedItem)){
                tempt.add(reservation);
            }
        }
//        显示所有订单
        resevationControl.reservationTableViewForCustomer.setItems(tempt);
        mainPane.setCenter(resevationControl.reservationTableViewForCustomer);
    }

    public void bindRowOfReservationTableView(TableView tableView){
       tableView.setRowFactory(new Callback<TableView<ResevationControl.Reservation>, TableRow<ResevationControl.Reservation>>() {
            @Override
            public TableRow<ResevationControl.Reservation> call(TableView<ResevationControl.Reservation> param) {
                return new ReservationTableRow(tableView);
            }
        });
    }
    class ReservationTableRow extends TableRow {
        public ReservationTableRow(TableView<ResevationControl.Reservation> tableView) {
            super();
            this.setOnMouseClicked(E -> {
                if (E.getButton().equals(MouseButton.PRIMARY) && E.getClickCount() == 2 && ReservationTableRow.this.getIndex() < tableView.getItems().size()) {
                    ResevationControl.Reservation reservation = tableView.getSelectionModel().getSelectedItem();
                    Dialog dialog = new Dialog();
                    GridPane pane = new GridPane();
                    int money = reservation.getVehicle().getFee() * (reservation.getReturnTime().minusHours(reservation.getPickTime().getHour()).getHour());
                    if (LocalTime.now().withNano(0).plusHours(1).isAfter(reservation.getReturnTime())) {
                        int overTimeMoey = (LocalTime.now().withNano(0).minusHours(reservation.getPickTime().getHour())).getHour() * reservation.getVehicle().getFee() * 4;
                        money += overTimeMoey;
                    }
                    pane.addRow(0, new Label("订单号"), new Label(reservation.getReserveID()), new Label("下单时间"), new Label(reservation.getReserveTime().toString()));
                    pane.addRow(1, new Label("顾客"), new Label(reservation.getCustomerName()), new Label("会员级别"), new Label(reservation.getCustomer().getCategory()));
                    pane.addRow(2, new Label("取车日期"), new Label(reservation.getP_pickDate()), new Label("取车时间"), new Label(reservation.getP_pickTime()));
                    pane.addRow(4, new Label("现在时间"), new Label("" + LocalTime.now().withNano(0)), new Label("预计归还时间"), new Label(reservation.getReturnTime().toString()));
                    ;
                    pane.addRow(5, new Label("租用车辆计费/小时", new Label("" + reservation.getVehicle().getFee())));
                    pane.add(new Label("如果超时，超时多于一小时 超时部分将按4被正常费用计费 "), 0, 6, 3, 1);
                    pane.add(new Label("总计 (RMB):" + money), 2, 8, 1, 1);

                    pane.setVgap(30);
                    pane.setHgap(30);
                    ButtonType pay = new ButtonType("付款", ButtonBar.ButtonData.OK_DONE);
                    ButtonType cancle = new ButtonType("关闭", ButtonBar.ButtonData.CANCEL_CLOSE);

                    ImageView imageView=new ImageView(new Image("sample/payReservation.png"));
                    imageView.setFitWidth(300);
                    imageView.setFitHeight(400);
                    dialog.setGraphic(imageView);
                    dialog.setTitle("订单详情");
                    dialog.setHeaderText(null);
                    dialog.getDialogPane().setContent(pane);
                    dialog.getDialogPane().getButtonTypes().setAll(pay, cancle);
                    Optional<ButtonType> result = dialog.showAndWait();
                    Node node = dialog.getDialogPane().lookupButton(pay);
                    node.setDisable(reservation.getStatus().equals("已完成"));
                    if (result.get() == pay) {
                        reservation.setStatus("已完成");
                        mainPane.setCenter(resevationControl.getWholeReservationPane());
                        dialog.close();
                    }

                }
            });
        }
    }

    class VehicleTableRow extends TableRow {
        public VehicleTableRow(){
            super();
            this.setOnMouseClicked(e->{
                if (e.getButton().equals(MouseButton.PRIMARY)&&e.getClickCount()==2&&VehicleTableRow.this.getIndex()<vehicleControl.vehicleTableView.getItems().size()){
                    showDetailAboutVehiclePane(vehicleControl.vehicleTableView.getSelectionModel().getSelectedItem());
                }
            });
        }
    }

    private void showDetailAboutVehiclePane(VehicleControl.Vehicle selectedItem) {
        BorderPane pane=new BorderPane();
        GridPane detailedPane=new GridPane();
        TextField id=new TextField();
        TextField model=new TextField();
        TextField yearUsed=new TextField();
        TextField fee=new TextField();
        ComboBox<String> fuelType=new ComboBox<>();
        ComboBox<String> status=new ComboBox<>();

        id.setText(selectedItem.getVehicleID());
        model.setText(selectedItem.getModelName());
        yearUsed.setText(""+selectedItem.getYearsUsed());
        fee.setText(""+selectedItem.getFee());
        fuelType.setValue(selectedItem.getFuelType());
        status.setValue(selectedItem.getStatus());


        id.setEditable(false);
        model.setEditable(false);
        yearUsed.setEditable(false);
        fee.setEditable(false);
        fuelType.setDisable(true);
        status.setDisable(true);


        Button modify=new Button("编辑");
        Button save=new Button("保存");
        Button delete=new Button("删除");

        modify.setFont(Font.font(15));
        save.setFont(Font.font(15));
        delete.setFont(Font.font(15));

        fuelType.setItems(FXCollections.observableArrayList("930#","93#","97#"));
        status.setItems(FXCollections.observableArrayList("修理中","可使用"));

        detailedPane.addRow(0,new Label("车牌号"),id,new Label("汽车型号"),model);
        detailedPane.addRow(1,new Label("已使用时间"),yearUsed,new Label("价格"),fee);
        detailedPane.addRow(2,new Label("耗油类型"),fuelType,new Label("状态"),status);
        detailedPane.setVgap(20);
        detailedPane.setHgap(40);

        HBox box=new HBox(30);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(modify,save,delete);

//     设置修改
        modify.setOnAction((e)->{
            id.setEditable(true);model.setEditable(true);yearUsed.setEditable(true);fee.setEditable(true);fuelType.setDisable(false);status.setDisable(false);
        });
//        设置保存
        save.setOnAction((e)->{
            selectedItem.setVehicleID(id.getText().trim());
            selectedItem.setFee(Integer.parseInt(fee.getText().trim()));
            selectedItem.setFuelType(fuelType.getValue());
            selectedItem.setYearsUsed(Integer.parseInt(yearUsed.getText().trim()));
            selectedItem.setModelName(model.getText().trim());
            id.setEditable(false);
            model.setEditable(false);
            yearUsed.setEditable(false);
            fee.setEditable(false);
            fuelType.setDisable(true);
            status.setDisable(true);
        });
//        删除
        delete.setOnAction((e)->{
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("确认");
            alert.setHeaderText("被删除的用户将无法恢复");
            alert.setContentText("你确定要删除这名顾客吗？");

            Optional<ButtonType> result=alert.showAndWait();
            if (result.get()==ButtonType.OK){
                company.getVehicles().remove(selectedItem);
                mainPane.setCenter(null);
            }
        });

        ObservableList<ResevationControl.Reservation> tempt=FXCollections.observableArrayList();
//        提供一个面板显示详细信息
//       找出所有与该汽车相关的订单
        for (ResevationControl.Reservation reservation:company.getReservations()) {
            if (reservation.getVehicle().equals(selectedItem)){
                tempt.add(reservation);
            }
        }
//        显示所有订单
        resevationControl.reservationTableViewForVehicle.setItems(tempt);

        Button btn=new Button("预定");
        btn.setPrefSize(100,40);
        btn.setFont(Font.font("Arial",30));

        TextField searchCustomer=new TextField();

//        开始预定
        btn.setOnAction((e)->{
            mainPane.setCenter(getDatePickPane(selectedItem,customerControl.searchCustomer(searchCustomer.getText())));
        });

        VBox vbox=new VBox(20);
        vbox.setPadding(new Insets(20,20,20,20));
        vbox.getChildren().addAll(detailedPane,box,resevationControl.reservationTableViewForVehicle,searchCustomer,btn);
        mainPane.setCenter(vbox);
    }

    public Pane getDatePickPane(VehicleControl.Vehicle vehicle,CustomerControl.Customer customer){

//           获取该汽车被预定过的日期和对应的时间段
        Map<LocalDate,ArrayList<Label>> date=vehicle.getDate();

        DatePicker datePicker=new DatePicker();

        GridPane detailInformationCustomer=customerControl.getDetailInformationPane(customer);

//       默认设置为当天
        datePicker.setValue(LocalDate.now());
//      将已被预定过的天数标记为红色 未被预定过的标记为蓝色。
        datePicker.setDayCellFactory((col)->{
            DateCell cell=new DateCell(){
                @Override
                public void updateItem(LocalDate item,boolean empty){
                    super.updateItem(item,empty);
                    setTooltip(new Tooltip("You can reserve in advance 30 days"));
                    if (item.isAfter(LocalDate.now().plusDays(40))||item.isBefore(LocalDate.now())){
                        setDisable(true);
                        setStyle("-fx-background-color:#ffc0cb");
                    }else{
                        if (date.containsKey(item)){
                            setStyle("-fx-background-color:red");
                            setTooltip(new Tooltip("has ben reserved but "));
                        }else{
                            setStyle("-fx-background-color:green");
                            setTooltip(new Tooltip("nobody reserved"));
                        }
                    }
                }
            };
            return cell;
        });
        ListView<Label> hours=new ListView<>();
        datePicker.valueProperty().addListener((col,o,n)->{
            if (!date.containsKey(n))
                hours.setItems(FXCollections.observableArrayList(getHour()));
            else
                hours.setItems(FXCollections.observableArrayList(date.get(n)));
        });

//      设置  点击  方式
        hours.getSelectionModel().selectedItemProperty().addListener((e,o,n)->{
                    hours.getSelectionModel().clearSelection();
                    ObservableList<Label> currentList=hours.getItems();
                    int maxhour=0;

//                    设置用户可以借用的最长时间。 至尊用户可以最多借用20 小时 其他用户可以最多借用15小时
                    if (customer.getCategory().equals("至尊用户")){
                        maxhour=20;
                    }else {
                        maxhour=15;
                    }
                    try{
                    int a=currentList.indexOf(o);
                    int b=currentList.indexOf(n);
                    System.out.println(a);
                    System.out.println(b);



                    if (b-a+1<maxhour){
                        int min=Math.min(a,b);
                        int max=Math.max(a,b);
                        for (int i = 0; i < currentList.size(); i++) {
                            if (i<=max&&i>=min&&!currentList.get(i).isDisable()){
                                currentList.get(i).setStyle("-fx-background-color:yellow");
                            }
                            else
                                currentList.get(i).setStyle(null);
                        }
                    }
                        hours.getSelectionModel().selectRange(a,b+1);
                    }
                    catch (Exception ex){
                        System.out.println("data pick error  don't need to deal with");
                    }
                }

        );
        Button confirm=new Button("确认");

        datePicker.valueProperty().addListener((e,o,n)->{
            System.out.println(n);
        });
//        默认设置为从当天开始.   并且校准有效时间   校准当天的有效时间
        if (!date.containsKey(LocalDate.now())){
            ArrayList<Label> Hour=getHour();
            setCurrentDayTime(Hour,customer.getCategory(),hours);
            hours.setItems(FXCollections.observableArrayList(Hour));
        }
        else{
            hours.setItems(FXCollections.observableArrayList(date.get(LocalDate.now())));
            setCurrentDayTime(getHour(),customer.getCategory(),hours);
        }
//

        confirm.setOnAction(e->{
            for (Label l:hours.getItems()) {
                if (l.getStyle().contains("-fx-background-color")){
                    l.setDisable(true);
                }
            }
            ObservableList<Label> labels=hours.getSelectionModel().getSelectedItems();
            date.put(datePicker.getValue(),new ArrayList<>(hours.getItems()));
            String pickTime=labels.get(0).getText();
            String returnTime=labels.get(labels.size()-1).getText();
            company.getReservations().add(new ResevationControl.Reservation(vehicle,customer,LocalTime.parse(pickTime),LocalTime.parse(returnTime),datePicker.getValue(),LocalDateTime.now().withNano(0)));
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("预定成功");
            alert.showAndWait();
            mainPane.setCenter(vehicleControl.getWholeVehiclePane());
        });

        GridPane pane=new GridPane();
        pane.setHgap(10);
        pane.setVgap(20);

        TextField pickLocation=new TextField();
        pickLocation.setPrefColumnCount(40);
        confirm.setPrefSize(40,10);

        VBox box=new VBox();
        box.setSpacing(20);
        box.getChildren().addAll(new Label("日期: "),datePicker);
        hours.setPrefWidth(100);

        VBox timeContainer=new VBox(20);
        timeContainer.getChildren().addAll(box,new Label("时间段"),hours,new Label("取车地点"),pickLocation);

        BorderPane container=new BorderPane();
        container.setLeft(detailInformationCustomer);
        container.setCenter(timeContainer);
        container.setBottom(confirm);
        BorderPane.setMargin(detailInformationCustomer,new Insets(100,10,100,10));
        BorderPane.setMargin(timeContainer,new Insets(30,10,30,10));
        BorderPane.setAlignment(confirm,Pos.CENTER);
        BorderPane.setMargin(confirm,new Insets(20,0,20,0));
        return container;
    }

    private void setCurrentDayTime(ArrayList<Label> hour, String category,ListView<Label> hours) {
        int delay=0;
        if (!category.equals("普通用户")){
            delay=6;
        }else{
            delay=2;
        }
//      对不同的客户 设置晚下单时间
        LocalTime currentTime=LocalTime.now().withNano(0);
        LocalTime minTime=LocalTime.now().minusHours(delay);

        for (Label label:
                hour) {
            System.out.println(label.getText());
            if (LocalTime.parse(label.getText()).isBefore(minTime)){
                label.setDisable(true);
            }
        }
    }

    private ArrayList<Label> getHour(){
        int start=4;
        int end=24;

        ArrayList<Label> hour=new ArrayList<>();
        for (int i = start; i <=end; i++) {
            Label label;
            if (i<10){
                label=new Label( "0"+i + ":00");
            }else if(i==24){
                label=new Label("23:59");
            }else{
                label=new Label( i + ":00");
            }
            label.setPrefWidth(100);
            label.setAlignment(Pos.CENTER);
            hour.add(label);
        }
        return hour;
    }
}
