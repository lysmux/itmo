package dev.lysmux.lab8.client.ui.main;

import dev.lysmux.lab8.client.APIClient;
import dev.lysmux.lab8.client.ui.scene.SceneManager;
import dev.lysmux.lab8.client.ui.util.NestedPropertyValueFactory;
import dev.lysmux.lab8.common.collection.model.LabWork;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    public Button clearBtn;
    public VBox rootPane;
    public MenuItem addRandomBtn;
    public MenuItem removeGreaterBtn;
    public MenuItem removeLowerBtn;
    public MenuItem executeScriptBtn;
    public TabPane tabPane;
    @FXML
    private Pane visualPane;
    @FXML
    private CheckBox displayOnlyMy;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private Button addItemBtn;
    @FXML
    private MenuItem editItem;
    @FXML
    private MenuItem deleteItem;

    @FXML
    private TableView<LabWork> table;
    @FXML
    private TableColumn<LabWork, Integer> idCol;
    @FXML
    private TableColumn<LabWork, Integer> ownerIdCol;
    @FXML
    private TableColumn<LabWork, String> nameCol;
    @FXML
    private TableColumn<LabWork, Long> minimalPointCol;
    @FXML
    private TableColumn<LabWork, String> difficultyCol;
    @FXML
    private TableColumn<LabWork, String> creationDateCol;
    @FXML
    private TableColumn<LabWork, Integer> coordinatesXCol;
    @FXML
    private TableColumn<LabWork, Double> coordinatesYCol;
    @FXML
    private TableColumn<LabWork, String> authorNameCol;
    @FXML
    private TableColumn<LabWork, String> authorBirthdayCol;
    @FXML
    private TableColumn<LabWork, Long> authorWeightCol;
    @FXML
    private TableColumn<LabWork, Float> authorLocationYCol;
    @FXML
    private TableColumn<LabWork, Integer> authorLocationXCol;
    @FXML
    private TableColumn<LabWork, String> authorLocationNameCol;

    private ResourceBundle bundle;

    private final ObservableList<LabWork> data = FXCollections.observableArrayList();
    private final FilteredList<LabWork> filteredData = new FilteredList<>(data);
    private final SortedList<LabWork> sortedData = new SortedList<>(filteredData);

    private final APIClient client = APIClient.getInstance();

    private final HashMap<Integer, Color> colorMap = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bundle = resourceBundle;

        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ownerIdCol.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        minimalPointCol.setCellValueFactory(new PropertyValueFactory<>("minimalPoint"));
        difficultyCol.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        coordinatesXCol.setCellValueFactory(new NestedPropertyValueFactory<>("coordinates.x"));
        coordinatesYCol.setCellValueFactory(new NestedPropertyValueFactory<>("coordinates.y"));
        authorNameCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.name"));
        authorBirthdayCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.birthday"));
        authorWeightCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.weight"));
        authorLocationYCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.location.y"));
        authorLocationXCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.location.x"));
        authorLocationNameCol.setCellValueFactory(new NestedPropertyValueFactory<>("author.location.name"));

        table.setRowFactory(tv -> {
            TableRow<LabWork> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && !row.isEmpty()) {
                    LabWork rowData = row.getItem();
                    openEditor(rowData, item -> {
                        client.updateCollectionItem(item);
                        refreshTable();
                    });
                }

                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    if (row.getItem().getOwnerId() != client.getProfileData().id()) return;
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });

            return row;
        });

        displayOnlyMy.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (!newValue) return true;
                return item.getOwnerId() == client.getProfileData().id();
            });
        });

        addItemBtn.setOnAction(event -> openEditor(null, item -> {
            client.addCollectionItem(item);
            refreshTable();
        }));
        editItem.setOnAction(event -> {
            LabWork selectedRow = table.getSelectionModel().getSelectedItem();
            openEditor(selectedRow, item -> {
                client.updateCollectionItem(item);
                refreshTable();
            });
        });
        deleteItem.setOnAction(event -> {
            LabWork selectedRow = table.getSelectionModel().getSelectedItem();
            client.removeCollectionItem(selectedRow.getId());
            refreshTable();
        });

        clearBtn.setOnAction(event -> {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    bundle.getString("main.clearCollection.question"),
                    ButtonType.YES,
                    ButtonType.NO
            );
            alert.setTitle(bundle.getString("main.clearCollection.title"));
            alert.setHeaderText(null);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                client.clearCollection();
                refreshTable();
            }
        });

        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene == null) return;

            newScene.windowProperty().addListener((obs, oldWindow, newWindow) -> {
                if (newWindow == null) return;

                refreshTable();
            });
        });

        addRandomBtn.setOnAction(event -> {
            Integer count = requestNumber();
            if (count == null) return;
            client.addRandom(count);
            refreshTable();
        });

        removeGreaterBtn.setOnAction(event -> {
            Integer count = requestNumber();
            if (count == null) return;
            client.removeGreater(count);
            refreshTable();
        });

        removeLowerBtn.setOnAction(event -> {
            Integer count = requestNumber();
            if (count == null) return;
            client.removeLower(count);
            refreshTable();
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getId().equals("visualTab")) {
                updateVisual();
            }
        });

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            if (client.isConnected()) refreshTable();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateVisual() {
        visualPane.getChildren().clear();
        data.forEach(item -> {
            if (!colorMap.containsKey(item.getOwnerId())) {
                var random = new Random();
                var r = random.nextDouble();
                var g = random.nextDouble();
                var b = random.nextDouble();
                if (Math.abs(r - g) + Math.abs(r - b) + Math.abs(b - g) < 0.6) {
                    r += (1 - r) / 1.4;
                    g += (1 - g) / 1.4;
                    b += (1 - b) / 1.4;
                }
                colorMap.put(item.getOwnerId(), Color.color(r, g, b));
            }

            StackPane stackPane = new StackPane();


            double size = Math.log10(item.getMinimalPoint() + 1) * 10 + 10;
            Circle circle = new Circle(size, colorMap.get(item.getOwnerId()));

            double x = Math.abs(item.getCoordinates().getX()) * 100;
            double y = Math.abs(item.getCoordinates().getY()) * 100;

            stackPane.setLayoutX(visualPane.getWidth() / 2);
            stackPane.setLayoutY(visualPane.getHeight() / 2);

            Label label = new Label(item.getId().toString());
            stackPane.getChildren().addAll(circle, label);

            stackPane.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    openEditor(item, n -> {
                        client.updateCollectionItem(n);
                        refreshTable();
                    });
                }
            });

            visualPane.getChildren().addAll(stackPane);

            animateToCoordinates(stackPane, x, y);
        });
    }

    private Integer requestNumber() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("enterNumber.title"));
        dialog.setHeaderText(bundle.getString("enterNumber.message"));
        dialog.setContentText(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Integer.parseInt(result.get());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        bundle.getString("error.notANumber"),
                        ButtonType.OK
                );
                alert.setTitle(bundle.getString("error.title"));
                alert.setHeaderText(null);
                alert.show();
            }
        }
        return null;
    }

    private void refreshTable() {
        client.refreshCollection();
        data.setAll(client.getCollection());

        table.getColumns().forEach(column -> {
            Button filterBtn = new Button("*");
            filterBtn.getStyleClass().add("filter-button");

            ContextMenu menu = createFilterMenu(column);
            filterBtn.setOnMouseClicked(e -> menu.show(filterBtn, e.getScreenX(), e.getScreenY()));
            column.setGraphic(filterBtn);
        });
    }

    private void animateToCoordinates(StackPane pane, double targetX, double targetY) {
        TranslateTransition transition = new TranslateTransition(
                Duration.millis(1000),
                pane
        );

        transition.setFromX(0);
        transition.setFromY(0);
        transition.setToX(targetX - pane.getLayoutX()); // Разница между центром и целевой позицией
        transition.setToY(targetY - pane.getLayoutX());

        // Эффект "плавного старта и остановки"
        transition.setAutoReverse(false);
        transition.play();
    }

    private void openEditor(LabWork item, Consumer<LabWork> onComplete) {
        try {
            ItemViewController controller = SceneManager.getInstance().openPopup(
                    bundle.getString("collection.title"),
                    "/ui/main/item-view.fxml"
            );
            controller.setItem(item);
            controller.setReadOnly(item != null && client.getProfileData().id() != item.getOwnerId());
            controller.setOnComplete(onComplete);
        } catch (Exception ignored) {
        }
    }

    private ContextMenu createFilterMenu(TableColumn<LabWork, ?> column) {
        ContextMenu menu = new ContextMenu();

        TextField searchField = createSearchField(column, menu);
        CustomMenuItem searchItem = new CustomMenuItem(searchField);
        searchItem.setHideOnClick(false);
        menu.getItems().add(searchItem);

        addSortOptions(menu, column);
        addFilterOptions(menu, column);
        addSelectionButtons(menu, column);

        return menu;
    }

    private TextField createSearchField(TableColumn<LabWork, ?> column, ContextMenu menu) {
        TextField searchField = new TextField();

        searchField.setPromptText(bundle.getString("main.sorting.search"));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lowerVal = newVal.toLowerCase();

            getCheckBoxItems(menu).forEach(cb -> {
                boolean visible = cb.getText().toLowerCase().startsWith(lowerVal);
                cb.setVisible(newVal.isEmpty() || visible);
            });

            filteredData.setPredicate(person -> {
                Object cellData = column.getCellData(person);
                if (cellData == null) return false;
                return newVal.isEmpty() || cellData.toString().toLowerCase().startsWith(lowerVal);
            });
        });

        return searchField;
    }

    private void addSortOptions(ContextMenu menu, TableColumn<LabWork, ?> column) {
        MenuItem sortAsc = new MenuItem(bundle.getString("main.sorting.asc"));
        sortAsc.setOnAction(e -> {
            column.setSortType(TableColumn.SortType.ASCENDING);
            table.getSortOrder().setAll(column);
        });

        MenuItem sortDesc = new MenuItem(bundle.getString("main.sorting.desc"));
        sortDesc.setOnAction(e -> {
            column.setSortType(TableColumn.SortType.DESCENDING);
            table.getSortOrder().setAll(column);
        });

        menu.getItems().addAll(sortAsc, sortDesc, new SeparatorMenuItem());
    }

    private void addFilterOptions(ContextMenu menu, TableColumn<LabWork, ?> column) {
        Set<String> uniqueValues = column.getTableView().getItems().stream()
                .map(column::getCellData)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toCollection(TreeSet::new)); // Сортировка

        for (String value : uniqueValues) {
            CheckBox checkBox = new CheckBox(value);
            checkBox.setSelected(true);
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> applyMultiFilter(column, menu));

            CustomMenuItem item = new CustomMenuItem(checkBox);
            item.setHideOnClick(false);
            menu.getItems().add(item);
        }
    }

    private void addSelectionButtons(ContextMenu menu, TableColumn<LabWork, ?> column) {
        Button selectAll = new Button(bundle.getString("main.sorting.all"));
        selectAll.setOnAction(e -> {
            getCheckBoxItems(menu).forEach(cb -> cb.setSelected(true));
            applyMultiFilter(column, menu);
        });

        Button clearAll = new Button(bundle.getString("main.sorting.clear"));
        clearAll.setOnAction(e -> {
            getCheckBoxItems(menu).forEach(cb -> cb.setSelected(false));
            getTextField(menu).clear();
            applyMultiFilter(column, menu);
        });

        HBox buttonBox = new HBox(5, selectAll, clearAll);
        buttonBox.setPadding(new Insets(5));
        CustomMenuItem buttonsItem = new CustomMenuItem(buttonBox);
        buttonsItem.setHideOnClick(false);
        menu.getItems().add(buttonsItem);
    }

    private List<CheckBox> getCheckBoxItems(ContextMenu menu) {
        return menu.getItems().stream()
                .filter(item -> item instanceof CustomMenuItem)
                .map(item -> (CustomMenuItem) item)
                .map(CustomMenuItem::getContent)
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox) node)
                .collect(Collectors.toList());
    }

    private TextField getTextField(ContextMenu menu) {
        return menu.getItems().stream()
                .filter(item -> item instanceof CustomMenuItem)
                .map(item -> (CustomMenuItem) item)
                .map(CustomMenuItem::getContent)
                .filter(node -> node instanceof TextField)
                .map(node -> (TextField) node)
                .findFirst().orElse(null);
    }

    private void applyMultiFilter(TableColumn<LabWork, ?> column, ContextMenu menu) {
        Set<String> selectedValues = getCheckBoxItems(menu).stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toSet());

        if (selectedValues.isEmpty()) {
            filteredData.setPredicate(person -> false);
        } else {
            filteredData.setPredicate(person -> {
                Object value = column.getCellData(person);
                return value != null && selectedValues.contains(value.toString());
            });
        }
    }
}



