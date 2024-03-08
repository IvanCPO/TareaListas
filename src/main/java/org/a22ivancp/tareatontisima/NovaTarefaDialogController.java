package org.a22ivancp.tareatontisima;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.a22ivancp.tareatontisima.datamodel.Tarefa;
import org.a22ivancp.tareatontisima.datamodel.TarefaFicheiro;

import java.time.LocalDate;

public class NovaTarefaDialogController {
    @FXML
    public DatePicker limitDate;
    @FXML
    public TextField description;
    @FXML
    public TextArea detalles;

    @FXML
    public void initialize() {
    }

    public Tarefa procesarResultado() {
        String desc = description.getText();
        String det = detalles.getText();

        String[] valuedate = limitDate.getEditor().getText().split("/");
        System.out.println(valuedate.length);
        LocalDate date = LocalDate.of(Integer.parseInt(valuedate[2]),Integer.parseInt(valuedate[1]),Integer.parseInt(valuedate[0]));

        Tarefa tarefa = new Tarefa(desc,det,date);
        TarefaFicheiro.getFicheiroInstance().engadirTarefa(tarefa);
        return tarefa;
    }

    public Tarefa editarTarea(Tarefa t) {
        String desc = description.getText();
        String det = detalles.getText();

        String[] valuedate;
        LocalDate date;
        if (limitDate.getEditor().getText().contains("-")) {
            valuedate = limitDate.getEditor().getText().split("-");
            date = LocalDate.of(Integer.parseInt(valuedate[0]), Integer.parseInt(valuedate[1]), Integer.parseInt(valuedate[2]));
        }else {
            valuedate = limitDate.getEditor().getText().split("/");
            date = LocalDate.of(Integer.parseInt(valuedate[2]), Integer.parseInt(valuedate[1]), Integer.parseInt(valuedate[0]));
        }
        Tarefa tarefa = new Tarefa(desc,det,date);
        TarefaFicheiro.getFicheiroInstance().replaceTarefa(t,tarefa);
        return tarefa;
    }
}