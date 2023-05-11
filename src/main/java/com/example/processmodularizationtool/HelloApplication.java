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

        //String pathName = "/Users/maxl/Library/CloudStorage/OneDrive-UniversitätSt.Gallen/Dokumente/Universität/Master/Masterarbeit/05 Evaluation/01 BPMN Modelle/KSB/Diagram_2023-04-13 14-50-56/Diagram_2023-04-13 14-50-56.bpmn";
        //String pathName = "/Users/maxl/Library/CloudStorage/OneDrive-UniversitätSt.Gallen/Dokumente/Universität/Master/Masterarbeit/05 Evaluation/01 BPMN Modelle/KSGB/Operation stationar (AC).bpmn";
        //String pathName = "/Users/maxl/Library/CloudStorage/OneDrive-UniversitätSt.Gallen/Dokumente/Universität/Master/Masterarbeit/05 Evaluation/02 Funktional/How2Travel/How2Travel (Kopie)-7.bpmn";
        String pathName = "How2Travel (Kopie)-7.bpmn";

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

        /*modularizer.exportTimeDependencies("Diagram_2023-04-13 14-50-56_time-dependencies.csv");
        modularizer.exportLaneDependencies("Diagram_2023-04-13 14-50-56_lane-dependencies.csv");
        modularizer.exportDataDependencies("Diagram_2023-04-13 14-50-56_data-dependencies.csv");
        modularizer.exportInputDependencies("Diagram_2023-04-13 14-50-56_input-dependencies.csv");
        modularizer.exportCombinedDependencies("Diagram_2023-04-13 14-50-56_dependencies.csv");*/

        /*modularizer.exportTimeDependencies("Operation stationar (AC)_timeDependencies.csv");
        modularizer.exportLaneDependencies("Operation stationar (AC)_laneDependencies.csv");
        modularizer.exportDataDependencies("Operation stationar (AC)_dataDependencies.csv");
        modularizer.exportInputDependencies("Operation stationar (AC)_inputDependencies.csv");
        modularizer.exportCombinedDependencies("Operation stationar (AC)_dependencies.csv");*/

        modularizer.exportTimeDependencies("How2Travel (Kopie)-7_timeDependencies.csv");
        modularizer.exportLaneDependencies("How2Travel (Kopie)-7_laneDependencies.csv");
        modularizer.exportDataDependencies("How2Travel (Kopie)-7_dataDependencies.csv");
        modularizer.exportInputDependencies("How2Travel (Kopie)-7_inputDependencies.csv");
        modularizer.exportCombinedDependencies("How2Travel (Kopie)-7_dependencies.csv");




    }


}