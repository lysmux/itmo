package dev.lysmux.lab8.client.ui.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SceneManager {
    private static SceneManager instance;

    private Stage primaryStage;
    private String currentScene;
    private Locale locale;
    @Getter
    private Theme currentTheme;

    private static final Preferences preferences = Preferences.userNodeForPackage(SceneManager.class);
    private static final String PREF_LOCALE = "app_locale";
    private static final String PREF_THEME = "app_theme";

    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final HashMap<String, String> scenesData = new HashMap<>();

    private SceneManager() {
        this.locale = Locale.forLanguageTag(preferences.get(PREF_LOCALE, Locale.getDefault().toLanguageTag()));
        this.currentTheme = Theme.valueOf(preferences.get(PREF_THEME, Theme.LIGHT.name()));
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void loadScene(String name, String fxmlPath) throws IOException {
        Scene scene = loadFXMLScene(fxmlPath);
        scenes.put(name, scene);
        scenesData.put(name, fxmlPath);
    }

    public void switchTo(String name) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.locale", locale);
        if (primaryStage == null) throw new IllegalStateException("Primary stage is not set");

        Scene scene = scenes.get(name);
        if (scene == null) throw new IllegalArgumentException("No scene found with name: " + name);

        applyTheme(scene);
        currentScene = name;
        primaryStage.setTitle(bundle.getString(name + ".title"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void reloadCurrentScene() {
        if (currentScene != null) {
            switchTo(currentScene);
        }
    }

    public void reloadScenes() {
        for (var entry : scenesData.entrySet()) {
            try {
                Scene scene = loadFXMLScene(entry.getValue());
                scenes.put(entry.getKey(), scene);
            } catch (IOException ignored) {
            }
        }
    }

    public void switchLocale(Locale newLocale) {
        this.locale = newLocale;
        preferences.put(PREF_LOCALE, newLocale.toLanguageTag());
        reloadScenes();
        reloadCurrentScene();
    }

    public void switchTheme(Theme theme) {
        this.currentTheme = theme;
        preferences.put(PREF_THEME, theme.name());
        applyTheme(primaryStage.getScene());
    }


    private Scene loadFXMLScene(String fxmlPath) throws IOException {
        Parent root = loadFXML(fxmlPath).load();
        return new Scene(root);
    }

    private FXMLLoader loadFXML(String fxmlPath) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.locale", locale);
        return new FXMLLoader(getClass().getResource(fxmlPath), bundle);
    }

    public void applyTheme(Scene scene) {
        if (scene == null) return;

        scene.getStylesheets().clear();
        String cssPath = switch (currentTheme) {
            case LIGHT -> "/ui/light.css";
            case DARK -> "/ui/dark.css";
        };

        var cssURL = getClass().getResource(cssPath);
        if (cssURL != null) {
            scene.getStylesheets().add(cssURL.toExternalForm());
        }
    }

    public <T> T openPopup(String name, String fxmlPath) throws IOException {
        FXMLLoader loader = loadFXML(fxmlPath);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        applyTheme(scene);

        Stage newStage = new Stage();
        newStage.setTitle(name);
        newStage.setScene(scene);

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.show();

        return loader.getController();
    }
}
