package org.a22ivancp.tareatontisima;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.a22ivancp.tareatontisima.datamodel.Tarefa;
import org.a22ivancp.tareatontisima.datamodel.TarefaFicheiro;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ListView<Tarefa> listViewTarea;
    @FXML
    private Label dateValue;
    @FXML
    private Button toolAdd;
    @FXML
    private TextArea areaDetalles;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    private SortedList<Tarefa> listSorted ;
    private FilteredList<Tarefa> listFilter ;
    @FXML
    private ToggleButton toolFilter;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatorData();
        listContextMenu = new ContextMenu();
        toolAdd.setOnAction(this::mostrarDialogoNovaTarefa);
        toolFilter.setOnAction(actionEvent -> {
            ToggleButton button = (ToggleButton) actionEvent.getSource();
            if (button.isSelected()) {
                listFilter = listSorted.filtered(tarefa -> {
                    return Objects.equals(tarefa.getLimitDate(), LocalDate.now());
                });
                listViewTarea.setItems(listFilter);
                if (listViewTarea.getSelectionModel().getSelectedItem()==null)
                    listViewTarea.getSelectionModel().selectFirst();
            }else
                listViewTarea.setItems(listSorted);
        });

        // Crea un elemento do menú para a opción borra
        MenuItem deleteMenuItem = new MenuItem("Delete");

        // engade a deleteMenuItem un listener para que ao activar o menú
        // se execute o código que obteña a tarefa seleccionada e
        // se invoque o método borrarTarefa(tarefa)
        deleteMenuItem.setOnAction(actionEvent -> {
            Tarefa t = listViewTarea.getSelectionModel().getSelectedItem();
            if (t != null){
                borrarTarefa(t);
            }
        });

        // Crea un elemento do menú para a opción editar
        MenuItem editMenuItem = new MenuItem("Edit");

        // engade a deleteMenuItem un listener para que ao activar o menú
        // se execute o código que obteña a tarefa seleccionada e
        // se invoque o método borrarTarefa(tarefa)
        editMenuItem.setOnAction(actionEvent -> {
            Tarefa t = listViewTarea.getSelectionModel().getSelectedItem();
            if (t != null){
                editTarefa(t);
            }
        });

        // engadir o elemento ao menú contextual
        listContextMenu.getItems().addAll(deleteMenuItem,editMenuItem);
        // Establecer valor del label de la fecha:
        listViewTarea.getSelectionModel().selectedItemProperty().addListener((observableValue, originalTarefa, newTarefa) -> {
            if (newTarefa != null){
                areaDetalles.setText(newTarefa.getDetalles());
                dateValue.setText(newTarefa.getLimitDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            }
        });
        listViewTarea.setCellFactory(new Callback<ListView<Tarefa>, ListCell<Tarefa>>() {
            @Override
            public ListCell<Tarefa> call(ListView<Tarefa> p) {
                // variable para eliminar repitidos y colorear dependiendo fecha
                ListCell<Tarefa> cela = new ListCell<>() {
                    // este método execútase cada vez que se repinta unha cela
                    @Override
                    protected void updateItem(Tarefa t, boolean empty) {
                        // execútase o método da clase ancestra para manter o estilo
                        // e comportamento por defecto
                        super.updateItem(t, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(t.getDescription());
                            if (t.getLimitDate().equals(LocalDate.now()))
                                setTextFill(Color.GREEN);
                            else if (t.getLimitDate().isBefore(LocalDate.now()))
                                setTextFill(Color.RED);
                            else
                                setTextFill(Color.BLUE);
                        }
                    }
                };
                // añadir el menu contextual
                cela.emptyProperty().addListener(((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cela.setContextMenu(null);
                    } else {
                        cela.setContextMenu(listContextMenu);
                    }
                }));
                return cela;
            }
        });
        listViewTarea.getSelectionModel().selectFirst();


    }

    private void editTarefa(Tarefa t) {
        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.initOwner(mainBorderPane.getScene().getWindow());

        // engade o código para establecer o título “Engadir nova tarefa” ao diálogo
        dialog.setTitle("Editar tarefa");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("novaTarefaDialogo.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Non se puido cargar o diálogo");
            e.printStackTrace();
            return;
        }
        NovaTarefaDialogController controllerDialog = fxmlLoader.getController();

        // meter Opciones anteriores:
        controllerDialog.description.setText(t.getDescription());
        controllerDialog.detalles.setText(t.getDetalles());
        controllerDialog.limitDate.getEditor().setText(t.getLimitDate().toString());

        // engadir os botóns
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Tarefa tarefa = controllerDialog.editarTarea(t);
            listViewTarea.getSelectionModel().select(tarefa);
        } else {
            System.out.println("Cancel pressed");
        }
    }

    private void borrarTarefa(Tarefa tarefa) {
        // Crea unha alerta de CONFIRMATION
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // establece o título da alerta ao texto “Borrar tarefa”.
        alert.setTitle("Borrar tarefa");

        // No header mostra o texto “Borrar tarefa: <descrición tarefa seleccionada>”
        alert.setHeaderText("Borrar tarefa: "+tarefa);

        // Na rexión content mostra o texto:
        // ‘Desexa eliminar a tarefa? Pulse Ok para confirmar ou Cancel en caso contrario’
        alert.setContentText("Desexa eliminar a tarefa? Pulse Ok para confirmar ou Cancel en caso contrario");

        Optional<ButtonType> resultado = alert.showAndWait();
        if (resultado.isPresent() && (resultado.get() == ButtonType.OK)) {
            TarefaFicheiro.getFicheiroInstance().borrarTarefa(tarefa);
        }
    }

    private void generatorData() {
        listSorted = new SortedList<Tarefa>(TarefaFicheiro.getFicheiroInstance().getList());
        listSorted.setComparator((tarefa, t1) -> {
            if (tarefa.getLimitDate().isAfter(t1.getLimitDate()))
                return 1;
            if (tarefa.getLimitDate()==t1.getLimitDate())
                return 0;
            else
                return -1;
        });
        listViewTarea.getItems().setAll(listSorted);
    }

    @FXML
    public void mostrarDialogoNovaTarefa(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();

        // por defecto, un diálogo é modal
        // polo que non permitirá interaccionar con outras ventá
        dialog.initOwner(mainBorderPane.getScene().getWindow());

        // engade o código para establecer o título “Engadir nova tarefa” ao diálogo
        dialog.setTitle("Engadir nova tarefa");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("novaTarefaDialogo.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Non se puido cargar o diálogo");
            e.printStackTrace();
            return;
        }

        // engadir os botóns
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            NovaTarefaDialogController controllerDialog = fxmlLoader.getController();
            Tarefa tarefa = controllerDialog.procesarResultado();
            listViewTarea.getSelectionModel().select(tarefa);
        } else {
            System.out.println("Cancel pressed");
        }
    }
    @FXML
    public void cerrarApp(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Estas seguro de que quieres salir?");
        alert.setTitle("Salida");
        Optional<ButtonType> b = alert.showAndWait();
        if (b.get().equals(ButtonType.OK)) {
            System.out.println("Le diste a ok");
            System.exit(0);
        }else System.out.println("Le diste a cancel");

    }
    @FXML
    public void suprimirTarefa(KeyEvent event) {
        if (event.getCode()== KeyCode.DELETE){
            Tarefa t = listViewTarea.getSelectionModel().getSelectedItem();
            if (t != null){
                borrarTarefa(t);
            }
        }
    }
}