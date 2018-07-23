package sample;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.IDN;
import java.util.Optional;

public class CustomerControl {
//    声明变量
    TableView<Customer> customerTableView;
    ObservableList<Customer> customers;
    BorderPane customerTableViewPane;
//    构造函数
    public CustomerControl(ObservableList<Customer> customers) {
        this.customers=customers;
        customerTableViewPane=getCustomerViewPane(customers);
    }

//    得到顾客信息面板
    public BorderPane getCustomerViewPane(ObservableList<Customer> customers){
        customerTableView=new TableView<>();
        Pagination pagination=new Pagination();
        TableColumn number=new TableColumn("序号");
//        TableColumn CustomerID=new TableColumn("会员号");
        TableColumn Name=new TableColumn("姓名");
        TableColumn CustomerID=new TableColumn("会员号");
        TableColumn Tel=new TableColumn("电话");
        TableColumn Mail=new TableColumn("邮件");
        TableColumn Category=new TableColumn("身份");

//        CustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        Tel.setCellValueFactory(new PropertyValueFactory<>("Tel"));
        Mail.setCellValueFactory(new PropertyValueFactory<>("Mail"));
        Category.setCellValueFactory(new PropertyValueFactory<>("Category"));



        customerTableView.getColumns().addAll(number,CustomerID,Name,Tel,Mail,Category);
        customerTableView.setPrefSize(500,600);
        customerTableView.setItems(customers);
        GridPane function=new GridPane();
        function.setAlignment(Pos.CENTER);
        function.setPadding(new Insets(30,20,10,10));
        function.setHgap(10);
        function.setVgap(10);


        ToggleGroup group=new ToggleGroup();

        RadioButton showCar=new RadioButton();
        RadioButton showVan=new RadioButton();
        RadioButton showAll=new RadioButton();

        showCar.setToggleGroup(group);
        showVan.setToggleGroup(group);
        showAll.setToggleGroup(group);

        Label label=new Label();
        TextField searchFied=new TextField();
        searchFied.setPromptText("输入姓名或会员号进行搜索");

        Button btn=new Button("搜索");
        btn.setPrefWidth(100);
        searchFied.setPrefColumnCount(50);

        searchFied.textProperty().addListener((e,o,n)->{
            btn.setDisable(n.isEmpty());
        });
        Label Title=new Label("用户管理");
        Title.setFont(Font.font("Arial",25));

        function.add(Title,0,0,3,1);
        GridPane.setHalignment(Title,HPos.CENTER);
        HBox tempt1=new HBox();
        tempt1.setSpacing(20);
        tempt1.getChildren().addAll(showCar,new Label("至尊用户"),showVan,new Label("会员用户"),showAll,new Label("一般用户"));
        function.addRow(1,tempt1);
        function.addRow(2,searchFied,btn);

        BorderPane container=new BorderPane();
        container.setTop(function);
        container.setCenter(customerTableView);
        container.setBottom(pagination);
        BorderPane.setMargin(pagination,new Insets(0,0,40,0));


//                添加分页
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                showList(param);
                return customerTableView;
            }
        });

//                序列行自动增加
        number.setCellFactory((col)->{
            TableCell<VehicleControl.Vehicle,String> cell=new TableCell<>(){
                @Override
                public void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);
                    this.setText(null);
                    this.setGraphic(null);
                    if (!empty){
                        int rowIndex=this.getIndex()+1;
                        this.setText(String.valueOf(rowIndex));
                    }
                }
            };
            return cell;
        });
//               处理双击选中事件  此时显示有关该订单的详细信息;

        customerTableView.setRowFactory(param -> new TableRowControl());
        return container;

    }
    public Customer searchCustomer(String customerID){
        for (Customer customer:
                customers) {
            if (customer.getCustomerId().equals(customerID)){
                return customer;
            }
        }
        return null;
    }
    class TableRowControl extends TableRow{
            public TableRowControl(){
                super();
                this.setOnMouseClicked(e->{
                    if (e.getButton().equals(MouseButton.PRIMARY)&&e.getClickCount()==2&& CustomerControl.TableRowControl.this.getIndex()<customerTableView.getItems().size()){
                        showDetailAboutCustomer(customerTableView.getSelectionModel().getSelectedItem());
                    }
                });
            }
    }
    public GridPane getDetailInformationPane(Customer customer){
        GridPane pane=new GridPane();
        pane.addRow(0,new Label("会员号:"),new Label(customer.getCustomerId()));
        pane.addRow(1,new Label("姓名:"),new Label(customer.getName()));
        pane.addRow(2,new Label("身份:"),new Label(customer.getCategory()));
       pane.addRow(3,new Label("电话号码:"),new Label(customer.getTel()));
       pane.addRow(4,new Label("电子邮件:"),new Label(customer.getMail()));
       pane.setVgap(20);
       pane.setHgap(20);
       return pane;

    }
    private void showDetailAboutCustomer(Customer selectedItem) {

    }

    private void showList(Integer param) {

    }

    public Customer registerCustomer(Stage stage) throws Exception{
        GridPane pane=new GridPane();
        TextField name=new TextField();
        TextField tel=new TextField();
        TextField mail=new TextField();
        ComboBox<String> categoryBox=new ComboBox<>();
        categoryBox.setItems(FXCollections.observableArrayList("至尊会员","高级会员","普通会员"));
        categoryBox.setValue("至尊会员");
        Dialog dialog=new Dialog();
        dialog.setTitle("用户注册");
//        主界面
        dialog.initOwner(stage);
        dialog.setHeaderText(null);

//        添加图片
        Image photo=new Image("sample/registerCustomer.jpg");
        ImageView image=new ImageView(photo);
        image.setFitWidth(300);
        image.setFitHeight(200);
        dialog.setGraphic(image);

        pane.setPadding(new Insets(20,20,20,20));
        pane.setHgap(10);
        pane.setVgap(20);
        pane.addRow(0,new Label("姓名"),name);
        pane.addRow(1,new Label("电话号码"),tel);
        pane.addRow(2,new Label("电子邮件"),mail);
        pane.addRow(3,new Label("会员级别"),categoryBox);

        dialog.getDialogPane().setContent(pane);
//       设置按钮
        ButtonType close=new ButtonType("取消",ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType regiseter=new ButtonType("注册", ButtonBar.ButtonData.OK_DONE);
//      添加确认按钮
        dialog.getDialogPane().getButtonTypes().setAll(FXCollections.observableArrayList(close,regiseter));
        Node confirmButton=dialog.getDialogPane().lookupButton(regiseter);
        confirmButton.setDisable(true);
        name.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(name.getText().isEmpty()||tel.getText().isEmpty()||mail.getText().isEmpty());
        });
        tel.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(name.getText().isEmpty()||tel.getText().isEmpty()||mail.getText().isEmpty());
        });
        mail.textProperty().addListener((e,o,n)->{
            confirmButton.setDisable(name.getText().isEmpty()||tel.getText().isEmpty()||mail.getText().isEmpty());
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
            System.out.println(name);
            return new CustomerControl.Customer(name.getText(),tel.getText(),mail.getText(),categoryBox.getValue());
        }
        throw new Exception();
    }

    public static class Customer{
//        会员起始号码
        static String ID="100000";
    private SimpleStringProperty Name;
    private SimpleStringProperty CustomerID;
//    private SimpleStringProperty CustomerID;
    private SimpleStringProperty Tel;
    private SimpleStringProperty Mail;
    private SimpleStringProperty Category;
    public  Customer(String name,String tele,String mail,String category){
//        每 次 递 增 1
        ID=""+(Integer.parseInt(ID)+1);
        this.CustomerID=new SimpleStringProperty(ID);
        this.Name=new SimpleStringProperty(name);
        this.Tel=new SimpleStringProperty(tele);
        this.Mail=new SimpleStringProperty(mail);
        this.Category=new SimpleStringProperty(category);
    }
    public String getCustomerId(){
        return CustomerID.get();
    }
    public String getTel(){
        return Tel.get();
    }
    public String getMail(){
        return Mail.get();
    }
    public String getCategory(){
        return Category.get();
    }
    public String getName(){
        return Name.get();
    }
    public void setCustomerID(String id){
        CustomerID.set(id);
    }

    public void setName(String name){
        Name.set(name);
    }
    public void  setTel(String tel){
        Tel.set(tel);
    }
    public void setMail(String mail){
        Mail.set(mail);
    }
    public void setCategory(String category){
        Category.set(category);
    }
    @Override
    public String toString(){
//        每一行储存姓名,电话号码，邮件,会员级别
        return getName()+","+getTel()+","+getMail()+","+getCategory();
    }
}
}