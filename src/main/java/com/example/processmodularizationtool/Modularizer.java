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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Modularizer {

    private File file; //source file
    private BpmnModelInstance modelInstance; //Instance of BPMN model

    private Collection<ModelElementInstance> participantInstances; //Collection of participant instances
    private Collection<ModelElementInstance> laneInstances; //Collection of lane instances
    private Collection<ModelElementInstance> taskInstances; //Collection of task instances

    private Collection<ModelElementInstance> messageFlowInstances;

    private ArrayList<Participant> participants; //ArrayList of participants
    private ArrayList<Lane> lanes; //ArrayList of lanes
    private ArrayList<Task> tasks; //ArrayList of tasks

    private ArrayList<MessageFlow> messageFlows;

    private int[][] dependencies; //Dependency matrix for all Tasks


    public Modularizer(String pathName) {
        file = new File(pathName); // read a model from a file
        modelInstance = Bpmn.readModelFromFile(file); //create model instance from loaded .bpmn file
        System.out.println("Read file " + pathName);

        participantInstances = modelInstance.getModelElementsByType(modelInstance.getModel().getType(Participant.class)); //create collection of participant instances
        laneInstances = modelInstance.getModelElementsByType(modelInstance.getModel().getType(Lane.class)); //create collection of lane instances
        taskInstances = modelInstance.getModelElementsByType(modelInstance.getModel().getType(Task.class)); //create collection of task instances
        messageFlowInstances = modelInstance.getModelElementsByType(modelInstance.getModel().getType(MessageFlow.class));

        participants = new ArrayList<>();
        lanes = new ArrayList<>();
        tasks = new ArrayList<>();
        messageFlows = new ArrayList<>();

        createParticipantList();
        createLaneList();
        createTaskList();
        createMessageFlowList();

        dependencies = new int[tasks.size()][tasks.size()]; //initiate dependency matrix with length and width of number of tasks
        resetDependencies();
    }

    /**
     * Create an Array List with Tasks from task instances
     */
    public void createTaskList() {
        for (ModelElementInstance t:taskInstances) {
            Task task = (Task) t;
            tasks.add(task);
        }
    }

    /**
     *
     */
    public void createLaneList() {
        for (ModelElementInstance l:laneInstances) {
            Lane lane = (Lane) l;
            lanes.add(lane);
        }
    }

    public void createParticipantList() {
        for (ModelElementInstance p:participantInstances) {
            Participant participant = (Participant) p;
            participants.add(participant);
        }
    }

    public void createMessageFlowList() {
        for (ModelElementInstance m:messageFlowInstances) {
            MessageFlow messageFlow = (MessageFlow) m;
            messageFlows.add(messageFlow);
        }
    }

    public void resetDependencies() {
        for (int i = 0; i < dependencies.length; i++) {
            for (int j = 0; j < dependencies.length; j++) {
                if (i == j) {
                    dependencies[i][j] = -1;
                }
                else {
                    dependencies[i][j] = 0;
                }
            }
        }
    }

    public void printDependencies() {
        for (int i = 0; i < dependencies.length; i++) {
            System.out.print(tasks.get(i).getName() + ":    ");
            for (int j = 0; j < dependencies.length; j++) {
                System.out.print("T" + j + ": " + dependencies[i][j] + " | ");
            }
            System.out.println();
        }
    }

    public boolean isInLane(Lane lane, FlowNode flowNode) {
        Collection<FlowNode> flowNodes = lane.getFlowNodeRefs();
        boolean inLane = false;
        for (FlowNode f:flowNodes) {
            if (f.getId().equals(flowNode.getId())) {
                inLane = true;
            }
            else {
                inLane = false;
            }
        }
        return inLane;
    }


    /**
     * Checks for shared know how specificity between two tasks
     * @param n first task to check for shared know how specificity
     * @param m first task to check for shared know how specificity
     * @return boolean: true if tasks n & m share know how specificity
     */
    public boolean checkKnowHowSpecificity(Task n, Task m) {
        boolean hasKnowHowSpecificity = false;
        for (Lane l:lanes) {
            Collection<FlowNode> flowNodes = l.getFlowNodeRefs();
            boolean nInside = false;
            boolean mInside = false;
            for (FlowNode f:flowNodes) {
                if (f.getId().equals(n.getId())) {
                    nInside = true;
                }
                if (f.getId().equals(m.getId())) {
                    mInside = true;
                }
            }
            if (nInside && mInside) {
                hasKnowHowSpecificity = true;
            }
        }
        return hasKnowHowSpecificity;
    }

    public void addKnowHowDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j && checkKnowHowSpecificity(tasks.get(i), tasks.get(j))) {
                    dependencies[i][j]++;
                }
            }
        }
    }


    public void checkInformationDependency(Task n, Task m) {
        boolean hasInformationDependency = false;
        
    }




}
