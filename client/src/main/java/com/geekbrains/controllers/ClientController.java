package com.geekbrains.controllers;


import com.geekbrains.*;
import com.geekbrains.message.*;
import com.geekbrains.message.impl.*;
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
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.Paths.get;

public class ClientController implements Initializable {


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
    public Label regNoConnection_label;
    public Label authNoConnection_label;
    public ChoiceBox<File> listHards_box;
    public Button deleteItemsServer_button;
    public Button createNewFolderClient_button;
    public Button createNewFolderServer_button;
    public Button deleteItemsClient_button;
    public TextField nameFolderServer_field;
    public TextField nameFolder_field;
    public Button nameFolder_button;
    public Pane nameFolder_panel;
    public Pane nameFolderServer_panel;
    public Button nameFolderServer_button;
    public PasswordField changeOldPassword_field;
    public TextField changeLogin_field;
    public PasswordField changeNewPassword_field;
    public Label changeEmptyField_label;
    public Button changeDeleteAccount_button;
    public Label changePasswordNotChang_label;
    public AnchorPane deleteAccount_panel;
    public Button delete_button;
    public Button deleteReturn_button;
    public TextField deleteAccLogin_field;
    public PasswordField deleteAccPassword_field;
    public Label deleteEmptyField_label;
    public Label deleteErrorField_label;
    private Path baseDir;
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private final String DEFAULT_DIR = "user.home";
    private Path locationPath;
    private final File[] file = File.listRoots();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        authPanel.setVisible(true);
        listHards_box.getItems().addAll(file);
        listHards_box.setValue(file[0]);


// Client ViewTable

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
        sizeFilesServer.setCellFactory(column -> new TableCell<File, Long>() {
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
        });

        serverFiles.getColumns().
                add(sizeFilesServer);

        TableColumn<File, File> modFilesServer = new TableColumn<>("Change");
        modFilesServer.setCellValueFactory(e -> new SimpleObjectProperty<>(
                e.getValue()));
        modFilesServer.setPrefWidth(120);
        modFilesServer.setCellFactory(column ->
                new TableCell<File, File>() {
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
                });

        serverFiles.getColumns().
                add(modFilesServer);
        try {

            baseDir = get(System.getProperty(DEFAULT_DIR));
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
                            client.setVisible(false);
                            client.setVisible(true);
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
                filePathServer_label.setText(locationPath.toString());
                if (e.getClickCount() == 2) {
                    File file = serverFiles.getSelectionModel()
                            .getSelectedItem();
                    if (file.isDirectory()) {
                        filePathServer_label.setText(locationPath.toString());
                        try {
                            locationPath = locationPath.resolve(file.getName());
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
            Thread thread = new Thread(this::listener);
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            System.err.println("No connection");

        }


    }


    private void listener() {
        try {
            while (true) {
                AbstractMessage msg = (AbstractMessage) is.readObject();
                switch (msg.getMessageType()) {
                    case FILE : commandFile((FileMessage) msg); break;
                    case FILES_LIST : commandFilesList((FilesList) msg);break;
                    case AUTHENTICATION_ERROR : commandAuthenticationError();break;
                    case AUTHENTICATION_OK : commandAuthenticationOk((AuthenticationOK) msg);break;
                    case REGISTRATION_ERROR : commandRegistrationError((RegistrationError) msg);break;
                    case CHANGE_PASSWORD_OK : commandChangePasswordOk();break;
                    case CHANGE_PASSWORD_ERROR : commandChangePasswordError();break;
                    case DELETE_ACCOUNT_OK : commandDeleteAccountOk();break;
                    case DELETE_ACCOUNT_ERROR : commandDeleteAccountError();break;
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR ABSTRACT MESSAGE: ------>>>" + e + "<<<------");
        }
    }

    private void commandDeleteAccountError() {
        deleteEmptyField_label.setVisible(false);
        deleteErrorField_label.setVisible(true);
        deleteAccLogin_field.clear();
        deleteAccPassword_field.clear();
    }

    private void commandDeleteAccountOk() {
        deleteErrorField_label.setVisible(false);
        deleteEmptyField_label.setVisible(false);
        deleteAccLogin_field.clear();
        deleteAccPassword_field.clear();
        deleteAccount_panel.setVisible(false);
        client.setVisible(false);
        regPanel.setVisible(false);
        authPanel.setVisible(true);
        incorrect_label.setVisible(false);
    }

    private void commandChangePasswordError() {
        changeEmptyField_label.setVisible(false);
        changePasswordNotChang_label.setVisible(true);
        changeLogin_field.clear();
        changeNewPassword_field.clear();
        changeOldPassword_field.clear();
    }

    private void commandChangePasswordOk() {
        changeEmptyField_label.setVisible(false);
        changePasswordNotChang_label.setVisible(false);
        changeLogin_field.clear();
        changeNewPassword_field.clear();
        changeOldPassword_field.clear();
        changePswPanel.setVisible(false);
        client.setVisible(true);
    }

    private void commandRegistrationError(RegistrationError msg) {
        loginEmpty_label.setVisible(false);
        passwordMatch_label.setVisible(false);
        loginAlready_label.setVisible(true);
        loginAlready_label.setText(msg.getInfo());
        regPassword_field1.clear();
        regPassword_field2.clear();
        regLogin_field.clear();
    }

    private void commandAuthenticationOk(AuthenticationOK msg) {
        authPanel.setVisible(false);
        client.setVisible(true);
        locationPath = get(msg.getLocationPath());
    }

    private void commandAuthenticationError() {
        incorrect_label.setVisible(true);
        authLogin_field.clear();
        authPassword_field.clear();
    }

    private void commandFilesList(FilesList msg) {
        Platform.runLater(() ->
                fillServerView(msg.getFiles()));
    }

    private void commandFile(FileMessage msg) throws IOException {
        Files.write(
                baseDir.resolve(msg.getFileName()),
                msg.getBytes()
        );
        Platform.runLater(() -> {
            try {
                fillClientView(getClientFiles());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void singIn(ActionEvent actionEvent) {
        try {
            os.writeObject(new Authentication(
                    authLogin_field.getText(),
                    authPassword_field.getText()));
            authLogin_field.clear();
            authPassword_field.clear();
        } catch (IOException | NullPointerException e) {
            authNoConnection_label.setVisible(true);
            System.err.println("No connection");
        }
    }

    private void fillServerView(List<File> list) {
        filePathServer_label.setText(locationPath.toString());
        serverFiles.getItems().clear();
        serverFiles.getItems().addAll(list);
    }

    private void fillClientView(List<FileInfo> list) {
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
        return Arrays.asList(Objects.requireNonNull(files));
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


    public void authSingUp(ActionEvent actionEvent) {
        authLogin_field.clear();
        authPassword_field.clear();
        authPanel.setVisible(false);
        regPanel.setVisible(true);
        regNoConnection_label.setVisible(false);
        loginAlready_label.setVisible(false);
        passwordMatch_label.setVisible(false);
        loginEmpty_label.setVisible(false);

    }


    public void registration(ActionEvent actionEvent) {
        if (regPassword_field1.getText().equals(regPassword_field2.getText()) &&
                !(regPassword_field1.getText().isEmpty()) &&
                !(regLogin_field.getText().isEmpty())) {
            try {
                os.writeObject(new Registration(regLogin_field.getText(), regPassword_field1.getText()));
            } catch (IOException | RuntimeException e) {
                regNoConnection_label.setVisible(true);
                loginAlready_label.setVisible(false);
                passwordMatch_label.setVisible(false);
                loginEmpty_label.setVisible(false);
                System.err.println("No connection");
            }
        } else if (!regPassword_field1.getText().equals(regPassword_field2.getText())) {
            regNoConnection_label.setVisible(false);
            loginAlready_label.setVisible(false);
            passwordMatch_label.setVisible(true);
            loginEmpty_label.setVisible(false);
            regPassword_field1.clear();
            regPassword_field2.clear();
        } else if (regLogin_field.getText().isEmpty()) {
            regNoConnection_label.setVisible(false);
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

    public void folderUpClient(ActionEvent actionEvent) throws IOException {
        Path parents = get(filePathClient_label.getText()).getParent();
        if (parents != null) {
            baseDir = parents;
            fillClientView(getClientFiles());
        }
    }

    public void folderUpServer(ActionEvent actionEvent) {
        Path parent = get(filePathServer_label.getText()).getParent().getParent();
        if (parent != null) {
            try {
                locationPath = get(filePathServer_label.getText()).getParent();
                os.writeObject(new RequestFolder(locationPath.toString()));
                fillServerView(getServerFiles(locationPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void returnToReg(ActionEvent actionEvent) {
        regLogin_field.clear();
        regPassword_field1.clear();
        regPassword_field2.clear();
        authNoConnection_label.setVisible(false);
        regPanel.setVisible(false);
        authPanel.setVisible(true);

    }


    public void navigationDisk(ActionEvent mouseEvent) {
        if (!listHards_box.getSelectionModel().isEmpty()) {
            baseDir = listHards_box.getSelectionModel().getSelectedItem().toPath();
            try {
                fillClientView(getClientFiles());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public void deleteItemsClient(ActionEvent actionEvent) {
        if (clientFiles.getSelectionModel().getSelectedItem() != null) {
            try {
                Cleaner.delete(new File(String.valueOf(baseDir.resolve(clientFiles.getSelectionModel()
                        .getSelectedItem().getFileName()))));
                fillClientView(getClientFiles());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createNewFolderServer(ActionEvent actionEvent) {
        String nameFolder = nameFolderServer_field.getText();
        if (nameFolderServer_field.getText().isEmpty()) {
            nameFolder = "New Folder";
        }
        try {
            os.writeObject(new CreateFolder(nameFolder));
            nameFolderServer_field.clear();
            nameFolderServer_panel.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void visibleNamePanel() {
        nameFolder_panel.setVisible(true);
    }

    public void createFolderClient(ActionEvent actionEvent) {
        nameFolder_panel.setVisible(true);
        String name = nameFolder_field.getText();
        if (name.isEmpty()) {
            name = "new folder";
        }
        new File(baseDir + "/" + name).mkdirs();
        try {
            fillClientView(getClientFiles());
            nameFolder_field.clear();
            nameFolder_panel.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteItemsServer(ActionEvent actionEvent) {
        if (serverFiles.getSelectionModel().getSelectedItem() != null) {
            try {
                os.writeObject(new DeleteFile(serverFiles.getSelectionModel()
                        .getSelectedItem().getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void visibleNamePanelServer(ActionEvent actionEvent) {
        nameFolderServer_panel.setVisible(true);
    }

    public void changePassword(ActionEvent actionEvent) {
        if (changeLogin_field.getText().isEmpty() || changeNewPassword_field.getText().isEmpty() || changeOldPassword_field.getText().isEmpty()) {
            changeEmptyField_label.setVisible(true);
            changeLogin_field.clear();
            changeNewPassword_field.clear();
            changeOldPassword_field.clear();
        }
        try {
            os.writeObject(new ChangePassword(changeNewPassword_field.getText(), changeOldPassword_field.getText(), changeLogin_field.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteAccount(ActionEvent actionEvent) {

        if (deleteAccLogin_field.getText().isEmpty() || deleteAccPassword_field.getText().isEmpty()) {
            deleteEmptyField_label.setVisible(true);
            deleteAccLogin_field.clear();
            deleteAccPassword_field.clear();
        }

        try {
            os.writeObject(new DeleteAccount(deleteAccLogin_field.getText(), deleteAccPassword_field.getText()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onDeletePanel(ActionEvent actionEvent) {
        client.setVisible(false);
        deleteAccount_panel.setVisible(true);
        deleteErrorField_label.setVisible(false);
        deleteEmptyField_label.setVisible(false);
    }
}



