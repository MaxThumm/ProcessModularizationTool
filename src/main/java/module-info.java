module com.example.processmodularizationtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires camunda.bpmn.model;
    requires camunda.xml.model;


    opens com.example.processmodularizationtool to javafx.fxml;
    exports com.example.processmodularizationtool;
}