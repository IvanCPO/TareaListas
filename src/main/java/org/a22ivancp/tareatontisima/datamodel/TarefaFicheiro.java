package org.a22ivancp.tareatontisima.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TarefaFicheiro {
    private static TarefaFicheiro instance;

    private String nameFile;
    private ObservableList<Tarefa> list;
    private DateTimeFormatter formato;

    private TarefaFicheiro(){
        nameFile = "tarefas.txt";
        formato = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    }
    public static TarefaFicheiro getFicheiroInstance(){
        if (instance == null){
            synchronized (TarefaFicheiro.class){
                if (instance == null)
                    instance = new TarefaFicheiro();
            }
        }
        return instance;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public ObservableList<Tarefa> getList() {
        return list;
    }

    public void setList(ObservableList<Tarefa> list) {
        this.list = list;
    }

    public void engadirTarefa(Tarefa tarefa) {
        this.list.add(tarefa);
    }

    public void borrarTarefa(Tarefa tarefa) {
        this.list.remove(tarefa);
    }

    public void replaceTarefa(Tarefa original, Tarefa newTarefa){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)==original)
                list.set(i,newTarefa);
        }
    }

    public DateTimeFormatter getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = DateTimeFormatter.ofPattern(formato);
    }

    public void lerTarefas(){
        list = FXCollections.observableArrayList();
        Path path = Paths.get(nameFile);

        try (BufferedReader br = Files.newBufferedReader(path)){
            String input;
            while ((input = br.readLine()) != null) {
                String[] palabras = input.split("\t");
                String descricion = palabras[0];
                String detalles = palabras[1];
                String dataString = palabras[2];
                LocalDate data = LocalDate.parse(dataString,formato);
                Tarefa tarefa = new Tarefa(descricion, detalles, data);
                list.add(tarefa);
            }
        } catch (IOException e){
            System.err.println("SYSTEM BROKE --- FATAL ERROR");
            System.out.println(e.getMessage());
        }
    }
    public void gardarTarefas() {
        Path path = Paths.get(nameFile);
        try (BufferedWriter bw = Files.newBufferedWriter(path)){
            Iterator<Tarefa> iterator = list.iterator();
            while (iterator.hasNext()) {
                Tarefa tarefa = iterator.next();
                bw.write(String.format("%s\t%s\t%s",
                        tarefa.getDescription(),
                        tarefa.getDetalles(),
                        tarefa.getLimitDate().format(formato)));
                bw.newLine();
            }
        }catch (IOException e){
            System.err.println("SYSTEM BROKE --- FATAL ERROR");
            System.out.println(e.getMessage());
        }
    }
}
