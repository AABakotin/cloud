package com.geekbrains.client.controllers;


import com.geekbrains.client.FileInfo;
import com.geekbrains.model.*;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientController implements Initializable {


    public Button download_button;
    public Button upload_button;
    public Button delete_button;
    public AnchorPane authPanel;
    public AnchorPane regPanel;
    public AnchorPane changePswPanel;
    public TableView<FileInfo> clientFiles;
    public TableView<File> serverFiles;
    public AnchorPane client;
    public Button authSingUp_button;
    public TextField authLogin_field;
    public PasswordField authPassword_field;
    public Button authSingIn_button;
    public TextField regLogin_field;
    public PasswordField regPassword_field1;
    public Button regReturn_button;
    public PasswordField regPassword_field2;
    public Button regSignIn_button;
    public Button changeReturn_button;
    public PasswordField changePassword_field1;
    public PasswordField changePassword_field2;
    public Button changeOK_button;
    public Button FolderBackClient_button;
    public Button upLoadFiles_button;
    public Button downloadFiles_button;
    public Button changePassword_button;
    public Label filePathClient_label;
    public Label filePathServer_label;
    public Button FolderBackServer_button;
    public Label incorrect_label;
    public Label loginAlready_label;
    public Label passwordMatch_label;
    public Label loginEmpty_label;
    private Path baseDir;
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private final String DEFAULT_DIR = "user.home";
    private Path locationPath;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authPanel.setVisible(true);


// Client ViewTable

//        TableColumn<FileInfo, String> typeFilesClient = new TableColumn<>("Type");
//        typeFilesClient.setCellValueFactory(e ->
//                new SimpleStringProperty(e.getValue()
//                        .getType()));
//        typeFilesClient.setPrefWidth(40);
//        typeFilesClient.setCellFactory(column -> {
//            return new TableCell<FileInfo, String>() {
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else if (item.equals("File")) {
//                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("file:/png/file.png")));
//                        ImageView imageView = new ImageView(image);
//                        setGraphic(imageView);
//                    } else if (item.equals("DIR")) {
//                        setGraphic(new ImageView(new Image("file:/png/folder.png")));
//                    }
//                }
//            };
//        });
//
//        clientFiles.getColumns().add(typeFilesClient);

        TableColumn<FileInfo, String> nameFilesClient = new TableColumn<>("Name");
        nameFilesClient.setCellValueFactory(e -> new

                SimpleStringProperty(e.getValue().

                getFileName()));
        nameFilesClient.setPrefWidth(130);

        clientFiles.getColumns().
                add(nameFilesClient);

        TableColumn<FileInfo, Long> sizeFilesClient = new TableColumn<>("Size");
        sizeFilesClient.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().getItemSize()));
        sizeFilesClient.setPrefWidth(100);
        sizeFilesClient.setCellFactory(column ->
        {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1) {
                            text = "<folder>";
                        }
                        setText(text);
                    }
                }
            };
        });

        clientFiles.getColumns().
                add(sizeFilesClient);


        TableColumn<FileInfo, String> modFilesClient = new TableColumn<>("Change");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        modFilesClient.setCellValueFactory(e -> new
                SimpleStringProperty(e.getValue().
                getData().
                format(formatter)));
        modFilesClient.setPrefWidth(120);

        clientFiles.getColumns().
                add(modFilesClient);


// Server ViewTable

//        TableColumn<File, File> typeFilesServer = new TableColumn<>("Type");
//        typeFilesServer.setCellValueFactory(e ->
//                new SimpleObjectProperty<File>(e.getValue()));
//        typeFilesServer.setPrefWidth(40);
//        typeFilesServer.setCellFactory(column ->
//
//        {
//            return new TableCell<File, File>() {
//                protected void updateItem(File item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else if (item.equals("File")) {
//                        setGraphic(new ImageView(new Image("file:/png/file.png")));
//                    } else if (item.equals("DIR")) {
//                        setGraphic(new ImageView(new Image("file:/png/folder.png")));
//                    }
//                }
//            };
//        });
//
//        serverFiles.getColumns().
//
//                add(typeFilesServer);

        TableColumn<File, String> nameFilesServer = new TableColumn<>("Name");
        nameFilesServer.setCellValueFactory(e -> new
                SimpleStringProperty(e.getValue().
                getName()));
        nameFilesServer.setPrefWidth(130);

        serverFiles.getColumns().
                add(nameFilesServer);


        TableColumn<File, Long> sizeFilesServer = new TableColumn<>("Size");
        sizeFilesServer.setCellValueFactory(e -> new SimpleObjectProperty<>(e.getValue().

                length()));
        sizeFilesServer.setPrefWidth(100);
        sizeFilesServer.setCellFactory(column -> {
            return new TableCell<File, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1) {
                            text = "folder";
                        }
                        setText(text);
                    }
                }
            };
        });

        serverFiles.getColumns().
                add(sizeFilesServer);

        TableColumn<File, File> modFilesServer = new TableColumn<>("Change");
        modFilesServer.setCellValueFactory(e -> new SimpleObjectProperty<>(
                e.getValue()));
        modFilesServer.setPrefWidth(120);
        modFilesServer.setCellFactory(column ->
        {
            return new TableCell<File, File>() {
                @Override
                protected void updateItem(File item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        Date modif = new Date(item.lastModified());
                        String modifFile = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(modif);
                        setText(modifFile);
                    }
                }
            };
        });

        serverFiles.getColumns().
                add(modFilesServer);


        try {
            baseDir = Paths.get(System.getProperty(DEFAULT_DIR));
            filePathClient_label.setText(baseDir.toString());
            clientFiles.getItems().addAll(getClientFiles());
            clientFiles.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    FileInfo fileInfo = clientFiles.getSelectionModel()
                            .getSelectedItem();
                    Path path = baseDir.resolve(fileInfo.getFileName());
                    if (fileInfo.isDirectory()) {
                        baseDir = path;
                        filePathClient_label.setText(baseDir.toString());
                        try {
                            fillClientView(getClientFiles());
                        } catch (IOException ex) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Access denied");
                            alert.showAndWait();
                        }
                    } else {
                        File selectedFile = new File(path.toString());
                        try {
                            Desktop.getDesktop().open(selectedFile);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            });


            serverFiles.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    File file = serverFiles.getSelectionModel()
                            .getSelectedItem();
                    Path path = locationPath.resolve(file.getName());
                    filePathServer_label.setText(path.toString());
                    if (file.isDirectory()) {
                        locationPath = path;
                        filePathServer_label.setText(locationPath.toString());
                        try {
                            fillServerView(getServerFiles(locationPath));
                            os.writeObject(new RequestFolder(locationPath.toString()));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        File file1 = new File(file.toString());
                        try {
                            Desktop.getDesktop().open(file1);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });


            Socket socket = new Socket("localhost", 8189);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            is = new ObjectDecoderInputStream(socket.getInputStream());
            Thread thread = new Thread(this::read);
            thread.setDaemon(true);
            thread.start();
        } catch (
                IOException e) {
            e.printStackTrace();
        }


    }


    private void read() {
        try {
            while (true) {
                AbstractMessage msg = (AbstractMessage) is.readObject();
                switch (msg.getMessageType()) {
                    case FILE_MESSAGE:
                        FileMessage fileMessage = (FileMessage) msg;
                        Files.write(
                                baseDir.resolve(fileMessage.getFileName()),
                                fileMessage.getBytes()
                        );
                        Platform.runLater(() -> {
                            try {
                                fillClientView(getClientFiles());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    case FILES_LIST:
                        FilesList files = (FilesList) msg;
                        Platform.runLater(() ->
                                fillServerView(files.getFiles()));
                        break;
                    case AUTHENTICATION_ERROR:
                        incorrect_label.setVisible(true);
                        authLogin_field.clear();
                        authPassword_field.clear();
                        break;
                    case AUTHENTICATION_OK:
                        AuthenticationOK authenticationOK = (AuthenticationOK) msg;
                        authPanel.setVisible(false);
                        client.setVisible(true);
                        locationPath = Paths.get(authenticationOK.getLocationPath());
                        break;
                    case REGISTRATION_ERROR:
                        loginEmpty_label.setVisible(false);
                        passwordMatch_label.setVisible(false);
                        loginAlready_label.setVisible(true);
                        regPassword_field1.clear();
                        regPassword_field2.clear();
                        regLogin_field.clear();
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void singIn(ActionEvent actionEvent) {
        try {
            os.writeObject(new Authentication(
                    authLogin_field.getText(),
                    authPassword_field.getText()));
            authLogin_field.clear();
            authPassword_field.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillServerView(List<File> list) {
        serverFiles.getItems().clear();
        serverFiles.getItems().addAll(list);
    }

    private void fillClientView(List<FileInfo> list) {
        filePathClient_label.setText(null);
        clientFiles.getItems().clear();
        clientFiles.getItems().addAll(list);
        filePathClient_label.setText(baseDir.toString());
    }

    private List<FileInfo> getClientFiles() throws IOException {
        return Files.list(baseDir)
                .map(FileInfo::new)
                .collect(Collectors.toList());

    }

    private List<File> getServerFiles(Path path) throws IOException {
        filePathServer_label.setText(path.toString());
        File file = new File(path.toString());
        File[] files = file.listFiles();
        return Arrays.asList(files);
    }


    public void upload(ActionEvent e) throws IOException {
        FileInfo file = clientFiles.getSelectionModel().getSelectedItem();
        Path filePath = baseDir.resolve(file.getFileName());
        os.writeObject(new FileMessage(filePath));
    }

    public void exit() {
        Platform.exit();
    }


    public void downLoad(ActionEvent e) throws IOException {
        File file = serverFiles.getSelectionModel().getSelectedItem();
        os.writeObject(new FileRequest(file.getName()));
    }


    public void deleteFile(ActionEvent actionEvent) throws IOException {
        FileInfo fileInfo = clientFiles.getSelectionModel()
                .getSelectedItem();
        if (!fileInfo.isDirectory()) {
            Files.delete(baseDir.resolve(fileInfo.getFileName()));
        }
        fillClientView(getClientFiles());
    }

    public void authSingUp(ActionEvent actionEvent) {
        authPanel.setVisible(false);
        regPanel.setVisible(true);

    }

    private void showContent(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Oops");
        alert.setHeaderText(info);
        alert.showAndWait();

    }

    public void registration(ActionEvent actionEvent) {
        if (regPassword_field1.getText().equals(regPassword_field2.getText()) &&
                !(regPassword_field1.getText().isEmpty()) &&
                !(regLogin_field.getText().isEmpty())) {
            try {
                os.writeObject(new Registration(regLogin_field.getText(), regPassword_field1.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!regPassword_field1.getText().equals(regPassword_field2.getText())) {
            loginAlready_label.setVisible(false);
            passwordMatch_label.setVisible(true);
            loginEmpty_label.setVisible(false);
            regPassword_field1.clear();
            regPassword_field2.clear();
        } else if (regLogin_field.getText().isEmpty()) {
            loginAlready_label.setVisible(false);
            passwordMatch_label.setVisible(false);
            loginEmpty_label.setVisible(true);
        }
    }

    public void changePass(ActionEvent actionEvent) {
        client.setVisible(false);
        changePswPanel.setVisible(true);

    }

    public void returnToClient(ActionEvent actionEvent) {
        changePswPanel.setVisible(false);
        client.setVisible(true);
    }

    public void folderUP(ActionEvent actionEvent) throws IOException {
        Path pathUp = Paths.get(filePathClient_label.getText()).getParent();
        if (pathUp.compareTo(Paths.get(System.getProperty(DEFAULT_DIR))) >= 0) {
            baseDir = pathUp;
            fillClientView(getClientFiles());
        }
    }
}


