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

        //String pathName = "/Users/maxl/Downloads/How2Travel (Kopie).bpmn";
        String pathName = "/Users/maxl/Downloads/How2Travel (Kopie)-3.bpmn";

        Modularizer modularizer = new Modularizer(pathName);


        //modularizer.addKnowHowDependencies();

        //modularizer.printDependencies();

        //modularizer.lanePrint();

        modularizer.getTasksInOrder();






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