package com.example.processmodularizationtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;


public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {

        /*
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter file path");

        String pathName = scanner.nextLine();  // Read user input
         */

        //String pathName = "/Users/maxl/Library/CloudStorage/OneDrive-UniversitätSt.Gallen/Dokumente/Universität/Master/Masterarbeit/05 Evaluation/01 BPMN Modelle/KSB/Diagram_2023-04-13 14-51-52/Diagram_2023-04-13 14-51-52.bpmn";
        String pathName = "/Users/maxl/Library/CloudStorage/OneDrive-UniversitätSt.Gallen/Dokumente/Universität/Master/Masterarbeit/05 Evaluation/01 BPMN Modelle/Beispiel/Simplified emergency care process.bpmn";

        Modularizer modularizer = new Modularizer(pathName);


        //modularizer.addKnowHowDependencies();

        modularizer.addLaneDependencies();

        //modularizer.printDependencies();

        //modularizer.lanePrint();

        //modularizer.getTasksInOrder();

        modularizer.checkTimeDependency();
        //modularizer.printTimeDependencies();

        modularizer.addDocumentDependencies();
        //modularizer.printInputDependencies();

        modularizer.addDataDependencies();
        //modularizer.exportDataDependencies("How2Travel (Kopie)-6_dataDependencies.csv");

        /*modularizer.exportTimeDependencies("Diagram_2023-04-13 14-51-52_time-dependencies.csv");
        modularizer.exportLaneDependencies("Diagram_2023-04-13 14-51-52_lane-dependencies.csv");
        modularizer.exportDataDependencies("Diagram_2023-04-13 14-51-52_data-dependencies.csv");
        modularizer.exportInputDependencies("Diagram_2023-04-13 14-51-52_input-dependencies.csv");

        modularizer.exportCombinedDependencies("Diagram_2023-04-13 14-51-52_dependencies.csv");*/

        modularizer.exportTimeDependencies("Simplified emergency care process_timeDependencies.csv");
        modularizer.exportLaneDependencies("Simplified emergency care process_laneDependencies.csv");
        modularizer.exportDataDependencies("Simplified emergency care process_dataDependencies.csv");
        modularizer.exportInputDependencies("Simplified emergency care process_inputDependencies.csv");
        modularizer.exportCombinedDependencies("Simplified emergency care process_dependencies.csv");








        /*
        ModelElementType taskType = modelInstance.getModel().getType(Task.class);
        Collection<ModelElementInstance> taskInstances = modelInstance.getModelElementsByType(taskType);
        System.out.println(taskInstances.size());

        ModelElementType participantType = modelInstance.getModel().getType(Participant.class);
        Collection<ModelElementInstance> participantInstances = modelInstance.getModelElementsByType(participantType);
        System.out.println(participantInstances.size());

        ArrayList<Task> tasks = new ArrayList<>();
        for (ModelElementInstance t:taskInstances) {
            tasks.add(createTask(t));
        }

        System.out.println(tasks.size());

        ModelElementType laneType = modelInstance.getModel().getType(Lane.class);
        Collection<ModelElementInstance> laneInstances = modelInstance.getModelElementsByType(laneType);
        System.out.println(laneInstances.size());

        int[][] dependencies = new int[tasks.size()][tasks.size()];

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j && checkKnowHowSpecificity(tasks.get(i), tasks.get(j), laneInstances)) {
                    dependencies[i][j]++;
                }
            }
        }

        int counter = 0;
        for (Task task:tasks) {
            counter++;
            System.out.println("Task " + counter + ": Name '" + task.getName() + "' | ID '" + task.getId() + "'");
        }
         */
    }


}