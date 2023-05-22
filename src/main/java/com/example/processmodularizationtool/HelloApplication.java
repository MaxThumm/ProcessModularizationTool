package com.example.processmodularizationtool;

import javafx.application.Application;
import javafx.stage.Stage;
import javax.swing.*;


public class HelloApplication extends Application {




    public static void main(String[] args) {

        String pathName = JOptionPane.showInputDialog("Please enter the path name of the file desired .bpmn file.\r\nIf the file is stored in the folder of this program (/ProcessModularizationTool) entering the file name is sufficient.");

        while (pathName.endsWith(".bpmn") == false) {
            pathName = JOptionPane.showInputDialog("No .bpmn file entered!\r\nPlease enter the path name of the file desired .bpmn file. (e.g. 'C:/testFile.bpmn')\r\nIf the file is stored in the folder of this program (/ProcessModularizationTool) entering the file name is sufficient.");
            if (pathName.endsWith(".bpmn")) {
                break;
            }
        }

        String exportName = JOptionPane.showInputDialog("Please enter export file name prefix.\r\n(Could be a part of the input file name or any destinct identifier)");
        while (exportName.equals("")) {
            exportName = JOptionPane.showInputDialog("Export file name cannot be empty!\r\nPlease enter export file name prefix.\r\n(Could be a part of the input file name or any destinct identifier)");
        }

        Modularizer modularizer = new Modularizer(pathName);

        modularizer.addLaneDependencies();
        modularizer.checkTimeDependency();
        modularizer.addDocumentDependencies();
        modularizer.addDataDependencies();

        modularizer.exportTimeDependencies(exportName + "_timeDependencies.csv");
        modularizer.exportLaneDependencies(exportName + "_laneDependencies.csv");
        modularizer.exportDataDependencies(exportName + "_dataDependencies.csv");
        modularizer.exportInputDependencies(exportName + "_documentDependencies.csv");
        modularizer.exportCombinedDependencies(exportName + "_combinedDependencies.csv");

        System.exit(0);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}