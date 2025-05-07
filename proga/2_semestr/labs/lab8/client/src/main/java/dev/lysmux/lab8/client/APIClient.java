package dev.lysmux.lab8.client;

import dev.lysmux.lab8.client.network.Connection;
import dev.lysmux.lab8.client.network.TCPConnection;
import dev.lysmux.lab8.client.ui.scene.SceneManager;
import dev.lysmux.lab8.common.collection.model.*;
import dev.lysmux.lab8.common.dto.Auth;
import dev.lysmux.lab8.common.dto.ProfileData;
import dev.lysmux.lab8.common.dto.Request;
import dev.lysmux.lab8.common.dto.Response;
import javafx.scene.control.Alert;
import lombok.Getter;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APIClient {
    private static APIClient instance;

    private String login;
    private String password;

    private Connection connection;

    @Getter
    private final Collection<LabWork> collection = new HashSet<>();

    private ProfileData profileData;

    private APIClient() {}

    public static APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    public boolean login(String login, String password) {
        Response response = request(new Request("login", new Object[]{login, password}));
        if (response.success()) {
            this.login = login;
            this.password = password;
            getProfileData();
        }

        return response.success();
    }

    public boolean register(String login, String password) {
        Response response = request(new Request("register", new Object[]{login, password}));
        if (response.success()) {
            this.login = login;
            this.password = password;
            getProfileData();
        }

        return response.success();
    }

    public boolean isLogin() {
        return login != null && password != null;
    }

    public void logout() {
        login = null;
        password = null;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void changePassword(String password) {
        request(new Request(new Auth(login, this.password), "changePassword", new Object[]{password}));
        this.password = password;
    }

    public ProfileData getProfileData() {
        if (profileData == null) {
            Response response = request(new Request(new Auth(login, password), "getProfileData", new Object[]{}));
            profileData = (ProfileData) response.objects().getFirst();
        }

        return profileData;
    }

    public long getOwnedItemsCount() {
        return collection.stream().filter(
                item -> item.getOwnerId() == profileData.id()
        ).count();
    }

    public void refreshCollection() {
        Response response = request(new Request(new Auth(login, password), "show", new Object[]{}));
        collection.clear();
        if (response.objects() != null) collection.addAll((Collection<LabWork>) response.objects());
    }


    public void removeCollectionItem(int id) {
        request(new Request(new Auth(login, password), "remove_by_id", new Object[]{id}));

    }

    public void updateCollectionItem(LabWork item) {
        request(new Request(new Auth(login, password), "update", new Object[]{item}));
    }

    public void addCollectionItem(LabWork item) {
        request(new Request(new Auth(login, password), "add", new Object[]{item}));
    }

    public void addRandom(int count) {
        request(new Request(new Auth(login, password), "add_random", new Object[]{count}));
    }

    public void removeLower(int value) {
        request(new Request(new Auth(login, password), "remove_lower", new Object[]{value}));
    }

    public void removeGreater(int value) {
        request(new Request(new Auth(login, password), "remove_greater", new Object[]{value}));
    }


    public void clearCollection() {
        request(new Request(new Auth(login, password), "clear", new Object[]{}));
    }

    public boolean connect(String serverAddress, int serverPort) {
        try {
            connection = new TCPConnection(serverAddress, serverPort);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public Response request(Request request) {
        try {
            connection.send(SerializationUtils.serialize(request));
            Response response = SerializationUtils.deserialize(connection.read());
            System.out.println(response.text());
            return response;
        } catch (IOException e) {
            e.printStackTrace();

            SceneManager.getInstance().switchTo("server-settings");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Нет ответа от сервера");
            alert.setHeaderText(null);
            alert.setContentText("Нет ответа от сервера");
            alert.show();

            connection = null;
            return null;
        }
    }
}
