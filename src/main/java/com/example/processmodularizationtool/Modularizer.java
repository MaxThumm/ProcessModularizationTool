package com.example.processmodularizationtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    private int[][] laneDependencies;
    private int[][] timeDependencies;
    private int[][] inputDependencies;
    private int[][] dataDependencies;


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
        laneDependencies = new int[tasks.size()][tasks.size()];
        timeDependencies = new int[tasks.size()][tasks.size()];
        inputDependencies = new int[tasks.size()][tasks.size()];
        dataDependencies = new int[tasks.size()][tasks.size()];
        resetAllDependencies();
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

    public void resetLaneDependencies() {
        for (int i = 0; i < laneDependencies.length; i++) {
            for (int j = 0; j < laneDependencies.length; j++) {
                if (i == j) {
                    laneDependencies[i][j] = -1;
                }
                else {
                    laneDependencies[i][j] = 0;
                }
            }
        }
    }

    public void resetTimeDependencies() {
        for (int i = 0; i < timeDependencies.length; i++) {
            for (int j = 0; j < timeDependencies.length; j++) {
                if (i == j) {
                    timeDependencies[i][j] = -1;
                }
                else {
                    timeDependencies[i][j] = 0;
                }
            }
        }
    }

    public void resetInputDependencies() {
        for (int i = 0; i < inputDependencies.length; i++) {
            for (int j = 0; j < inputDependencies.length; j++) {
                if (i == j) {
                    inputDependencies[i][j] = -1;
                }
                else {
                    inputDependencies[i][j] = 0;
                }
            }
        }
    }

    public void resetDataDependencies() {
        for (int i = 0; i < dataDependencies.length; i++) {
            for (int j = 0; j < dataDependencies.length; j++) {
                if (i == j) {
                    dataDependencies[i][j] = -1;
                }
                else {
                    dataDependencies[i][j] = 0;
                }
            }
        }
    }

    public void resetAllDependencies() {
        resetDependencies();
        resetLaneDependencies();
        resetTimeDependencies();
        resetInputDependencies();
        resetDataDependencies();
    }

    public void combineDependencies() {
        for (int i = 0; i < dependencies.length; i++) {
            for (int j = 0; j < dependencies.length; j++) {
                dependencies[i][j] = laneDependencies[i][j] + timeDependencies[i][j] + inputDependencies[i][j] + dataDependencies[i][j];
            }
        }
    }

    public void exportLaneDependencies(String fileName) {
        String[][] laneDependencyStrings = new String[laneDependencies.length + 1][laneDependencies.length + 1];
        for (int i = 0; i < laneDependencyStrings.length; i++) {
            for (int j = 0; j < laneDependencyStrings.length; j++) {
                if (i == 0) {
                    if (j == 0) {
                        laneDependencyStrings[i][j] = "X";
                    }
                    else {
                        laneDependencyStrings[i][j] = tasks.get(j - 1).getName();
                    }
                }
                else if (j == 0) {
                    laneDependencyStrings[i][j] = tasks.get(i - 1).getName();
                }
                else {
                    laneDependencyStrings[i][j] = String.valueOf(laneDependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        writer.exportCsv(laneDependencyStrings, fileName);
    }

    public void exportTimeDependencies(String fileName) {
        String[][] timeDependencyStrings = new String[timeDependencies.length + 1][timeDependencies.length + 1];
        for (int i = 0; i < timeDependencyStrings.length; i++) {
            for (int j = 0; j < timeDependencyStrings.length; j++) {
                if (i == 0) {
                    if (j == 0) {
                        timeDependencyStrings[i][j] = "X";
                    }
                    else {
                        timeDependencyStrings[i][j] = tasks.get(j - 1).getName();
                    }
                }
                else if (j == 0) {
                    timeDependencyStrings[i][j] = tasks.get(i - 1).getName();
                }
                else {
                    timeDependencyStrings[i][j] = String.valueOf(timeDependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        writer.exportCsv(timeDependencyStrings, fileName);
    }

    public void exportInputDependencies(String fileName) {
        String[][] inputDependencyStrings = new String[inputDependencies.length + 1][inputDependencies.length + 1];
        for (int i = 0; i < inputDependencyStrings.length; i++) {
            for (int j = 0; j < inputDependencyStrings.length; j++) {
                if (i == 0) {
                    if (j == 0) {
                        inputDependencyStrings[i][j] = "X";
                    }
                    else {
                        inputDependencyStrings[i][j] = tasks.get(j - 1).getName();
                    }
                }
                else if (j == 0) {
                    inputDependencyStrings[i][j] = tasks.get(i - 1).getName();
                }
                else {
                    inputDependencyStrings[i][j] = String.valueOf(inputDependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        writer.exportCsv(inputDependencyStrings, fileName);
    }

    public void exportDataDependencies(String fileName) {
        String[][] dataDependencyStrings = new String[dataDependencies.length + 1][dataDependencies.length + 1];
        for (int i = 0; i < dataDependencyStrings.length; i++) {
            for (int j = 0; j < dataDependencyStrings.length; j++) {
                if (i == 0) {
                    if (j == 0) {
                        dataDependencyStrings[i][j] = "X";
                    }
                    else {
                        dataDependencyStrings[i][j] = tasks.get(j - 1).getName();
                    }
                }
                else if (j == 0) {
                    dataDependencyStrings[i][j] = tasks.get(i - 1).getName();
                }
                else {
                    dataDependencyStrings[i][j] = String.valueOf(dataDependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        writer.exportCsv(dataDependencyStrings, fileName);
    }

    public void exportCombinedDependencies(String fileName) {
        combineDependencies();
        String[][] dependencyStrings = new String[dependencies.length + 1][dependencies.length + 1];
        for (int i = 0; i < dependencyStrings.length; i++) {
            for (int j = 0; j < dependencyStrings.length; j++) {
                if (i == 0) {
                    if (j == 0) {
                        dependencyStrings[i][j] = "X";
                    }
                    else {
                        dependencyStrings[i][j] = tasks.get(j - 1).getName();
                    }
                }
                else if (j == 0) {
                    dependencyStrings[i][j] = tasks.get(i - 1).getName();
                }
                else {
                    dependencyStrings[i][j] = String.valueOf(dependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        writer.exportCsv(dependencyStrings, fileName);
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

    public void printLaneDependencies() {
        for (int i = 0; i < laneDependencies.length; i++) {
            System.out.print(tasks.get(i).getName() + ":    ");
            for (int j = 0; j < laneDependencies.length; j++) {
                System.out.print("T" + j + ": " + laneDependencies[i][j] + " | ");
            }
            System.out.println();
        }
    }

    public void printTimeDependencies() {
        for (int i = 0; i < timeDependencies.length; i++) {
            System.out.print(tasks.get(i).getName() + ":    ");
            for (int j = 0; j < timeDependencies.length; j++) {
                System.out.print("T" + j + ": " + timeDependencies[i][j] + " | ");
            }
            System.out.println();
        }
    }

    public void printInputDependencies() {
        for (int i = 0; i < inputDependencies.length; i++) {
            System.out.print(tasks.get(i).getName() + ":    ");
            for (int j = 0; j < inputDependencies.length; j++) {
                System.out.print("T" + j + ": " + inputDependencies[i][j] + " | ");
            }
            System.out.println();
        }
    }

    public void printDataDependencies() {
        for (int i = 0; i < dataDependencies.length; i++) {
            System.out.print(tasks.get(i).getName() + ":    ");
            for (int j = 0; j < dataDependencies.length; j++) {
                System.out.print("T" + j + ": " + dataDependencies[i][j] + " | ");
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
    /*public boolean checkKnowHowSpecificity(Task n, Task m) {
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
    }*/

    public int checkKnowHowSpecificity(Lane l, Task n, Task m, int counter) {
        boolean hasKnowHowSpecificity = false;
        Collection<FlowNode> flowNodes = l.getFlowNodeRefs();
        boolean nInside = false;
        boolean mInside = false;
        for (FlowNode f:flowNodes) {
            //nInside = false;
            //mInside = false;
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
        if (hasKnowHowSpecificity == true) {
            return counter;
        }
        else {
            return 0;
        }
    }

    public void addKnowHowDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j) {
                    laneDependencies[i][j] = laneDependencies[i][j] + laneCheck(tasks.get(i), tasks.get(j));
                }
            }
        }
    }


    public void checkInformationDependency(Task n, Task m) {
        boolean hasInformationDependency = false;
        
    }


    //public void checkLane

    public void checkLaneDependencies() {
        for (Participant p: participants) {

        }
    }

    /*public void lanePrint() {
        for (Participant p:participants) {
            System.out.println(p.getName() + ":");
            Process process = p.getProcess();
            Collection<LaneSet> laneSets = process.getLaneSets();
            for (LaneSet laneSet:laneSets) {
                Collection<Lane> lanes1 = laneSet.getLanes();
                if (lanes1.size() > 1) {
                    for (Lane lane:lanes1) {
                        System.out.println("(ChildLane of " + p.getName() + ") " + lane.getName() + ": ");
                        checkTasks(lane);
                        checkChildLanes(lane);
                    }
                }
                else {
                    checkTasks(lanes1.iterator().next());
                }
            }
            System.out.println();
        }
    }*/

    public int laneCheck(Task n, Task m) {
        int counter = 1;
        int dependency = 0;
        boolean inSameLane;
        for (Participant p:participants) {
            Process process = p.getProcess();
            Collection<LaneSet> laneSets = process.getLaneSets();
            for (LaneSet laneSet:laneSets) {
                Collection<Lane> lanes1 = laneSet.getLanes();
                if (lanes1.size() > 1) {
                    counter++;
                    for (Lane lane:lanes1) {
                        dependency = dependency + checkChildLanes(lane, n, m, counter);
                        dependency = dependency + checkKnowHowSpecificity(lane, n, m, counter);
                    }
                }
                else {
                    dependency = dependency + checkKnowHowSpecificity(lanes1.iterator().next(), n, m, counter);
                }
            }
        }
        return dependency;
    }

    /*public void checkChildLanes(Lane lane, int counter) {
        LaneSet laneSet = lane.getChildLaneSet();
        if (laneSet != null) {
            Collection<Lane> lanes1 = laneSet.getLanes();
            for (Lane l:lanes1) {
                System.out.println("(ChildLane of " + lane.getName() + ") " + l.getName() + ": ");
                checkTasks(l);
                checkChildLanes(l);
            }
        }

    }*/

    public int checkChildLanes(Lane lane, Task n, Task m, int counter) {
        counter ++;
        int dependency = 0;
        LaneSet laneSet = lane.getChildLaneSet();
        if (laneSet != null) {
            Collection<Lane> lanes1 = laneSet.getLanes();
            for (Lane l:lanes1) {
                dependency = dependency + checkChildLanes(l, n, m, counter);
                dependency = dependency + checkKnowHowSpecificity(l, n, m, counter);
            }
        }
        return dependency;

    }

    public void addLaneDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j) {
                    laneDependencies[i][j] = checkLaneDependency(tasks.get(i), tasks.get(j));
                }
            }
        }
    }

    public int checkLaneDependency (Task n, Task m) {
        int dependency = 0;
        boolean inSameLane;
        for (Participant p:participants) {
            Process process = p.getProcess();
            Collection<LaneSet> laneSets = process.getLaneSets();
            for (LaneSet laneSet:laneSets) {
                ArrayList<Task> tasksInside = new ArrayList<>();
                Collection<Lane> lanes1 = laneSet.getLanes();
                for (Lane lane:lanes1) {
                    dependency = dependency + laneDependencyCheck(lane, n, m, tasksInside);
                }
            }
        }
        return dependency;
    }

    public int laneDependencyCheck(Lane lane, Task n, Task m, ArrayList<Task> tasksInside) {
        int counter = 1;
        getTasksInside(lane, tasksInside);

        if (tasksInside.contains(n) && tasksInside.contains(m)) {
            ArrayList<Task> insideChildLane = new ArrayList<>();
            LaneSet laneSet = lane.getChildLaneSet();
            if (laneSet != null) {
                for (Lane l:laneSet.getLanes()) {
                    counter = counter + laneDependencyCheck(l, n, m, insideChildLane);
                }
            }
            return counter;
        }
        else {
            return 0;
        }
    }

    public void getTasksInside(Lane lane, ArrayList<Task> tasksInside) {
        for (FlowNode flowNode:lane.getFlowNodeRefs()) {
            if (flowNode instanceof Task) {
                tasksInside.add((Task) flowNode);
            }
        }
        LaneSet laneSet = lane.getChildLaneSet();
        if (laneSet != null) {
            Collection<Lane> lanes1 = laneSet.getLanes();
            for (Lane l:lanes1) {
                getTasksInside(l, tasksInside);
            }
        }
    }

    public void printLaneContent() {
        for (Lane lane: lanes) {
            ArrayList<Task> tasksInside = new ArrayList<>();
            getTasksInside(lane, tasksInside);
            if (tasksInside.size() > 0) {
                for (Task task:tasksInside) {
                    System.out.println(task.getName());
                }
            }
            else {
                System.out.println("Lane leer");
            }
        }
    }

    /*public void checkTasks(Lane lane, int counter) {
        Collection<FlowNode> flowNodes = lane.getFlowNodeRefs();
        if(flowNodes.size() > 0) {
            for (FlowNode f:flowNodes) {
                if(f instanceof Task) {
                    System.out.print(f.getName() + ", ");
                }
            }
            System.out.println();
        }
    }*/

    public void checkTasks(Lane lane, int counter) {
        Collection<FlowNode> flowNodes = lane.getFlowNodeRefs();
        if(flowNodes.size() > 0) {
            for (FlowNode f:flowNodes) {
                if(f instanceof Task) {
                    System.out.print(f.getName() + ", ");
                }
            }
            System.out.println();
        }
    }

    public List<List> getTasksInOrder() {
        ArrayList<List> listList = new ArrayList<>();
        for (Participant participant:participants) {
            listList.add(getTasksFromPool(participant));
        }
        return listList;
    }

    public ArrayList<Task> getTasksFromPool(Participant participant) {
        System.out.print(participant.getName() + ": ");
        Collection<FlowElement> flowElements = participant.getProcess().getFlowElements();
        /*for (FlowElement f:flowElements) {
            System.out.println(f.getName());
        }*/
        StartEvent start = null;
        for (FlowElement flowElement:flowElements) {
            if(flowElement instanceof StartEvent) {
                start = (StartEvent) flowElement;
                System.out.print("(StartEvent: " + start.getName() + ") ");
            }
        }
        ArrayList<Task> tasks1 = new ArrayList<>();       //getFollowingTasks(start);
        List<FlowNode> following = start.getSucceedingNodes().list();
        int counter = 0;
        while (following.size() > counter) {
            int i = following.size();
            while (counter < i) {                //for (FlowNode flowNode:following) {
                if(following.get(counter) instanceof Task && tasks1.contains(following.get(counter)) == false) {
                    System.out.print(following.get(counter).getName() + ", ");
                    tasks1.add((Task)following.get(counter));
                }
                for (FlowNode f:following.get(counter).getSucceedingNodes().list()) {
                    if (following.contains(f) == false) {
                        following.add(i, f);
                    }
                }
                //following = flowNode.getSucceedingNodes().list();

                i = following.size();
                counter++;
            }
        }

        /*for(Task task:tasks1) {
            System.out.print(task.getName() + ", ");
        }*/

        /*List<Task> tasks1 = start.getSucceedingNodes().filterByType(Task.class).list();
        for(Task task:tasks1) {
            System.out.print(task.getName() + ", ");
        }*/
        System.out.println();
        System.out.println();
        return tasks1;
    }

    /*public ArrayList<Task> getFollowingTasks(FlowNode flowNode) {
        ArrayList<Task> tasks1 = new ArrayList<>();
        List<FlowNode>following = getNextTask(flowNode);
        while (following.size() > 0) {
            for (FlowNode f:following) {

                    //if(flowNode instanceof Task && tasks1.contains((Task) flowNode) == false) {
                if(flowNode instanceof Activity) {
                    System.out.print(f.getName() + ", ");
                    tasks1.add((Task)f);
                }
                following = getNextTask(f);


            }
        }


        /*if(following.isEmpty() == false) {
            for (FlowNode f:following) {
                getFollowingTasks(f);
            }
        }
        return tasks1;
    }*/

    /*public List<FlowNode> getNextTask(FlowNode flowNode) {
        List<FlowNode> following = flowNode.getSucceedingNodes().list();
        return following;
    }*/

    public void getTasksOrdered() {
        Participant participant = null;
        for (Participant p:participants) {
            if (p.getName().equals("Nutzer")) {
                participant = p;
            }
        }
        System.out.println(participant.getName());
        getTasksFromPool(participant);
    }

    public void checkTimeDependency() {
        Collection<ModelElementInstance> flowNodeInstances = modelInstance.getModelElementsByType(modelInstance.getModel().getType(FlowNode.class));
        ArrayList<FlowNode> allFlowNodes = new ArrayList<>();
        for (ModelElementInstance x:flowNodeInstances) {
            allFlowNodes.add((FlowNode) x);
        }
        ArrayList<Task> timeDependentTasks = new ArrayList<>();
        for (FlowNode flowNode:allFlowNodes) {
            if (flowNode instanceof ParallelGateway && flowNode.getPreviousNodes().list().size() > 1) {
                if (getFirstTaskFollowing(flowNode) != null) {
                    timeDependentTasks.add(getFirstTaskFollowing(flowNode));
                }
                getTasksAfterGateway(flowNode, timeDependentTasks);
            }
        }

        for (int i = 0; i < timeDependentTasks.size(); i++) {
            int counter = 0;
            String taskToRemove = null;
            for (int j = 0; j < timeDependentTasks.size(); j++) {
                if (i != j && timeDependentTasks.get(i).getId().equals(timeDependentTasks.get(j).getId())) {
                    taskToRemove = timeDependentTasks.get(j).getId();
                    //timeDependentTasks.remove(j);
                    //j--;
                }
            }
            if (taskToRemove != null) {
                for (int n = 0; n < timeDependentTasks.size(); n++) {
                    if (timeDependentTasks.get(n).getId().equals(taskToRemove)) {
                        timeDependentTasks.remove(n);
                        n--;
                    }
                }
            }
        }

        for (int i = 0; i < tasks.size(); i++) {
            for (Task n:timeDependentTasks) {
                if (n.getId().equals(tasks.get(i).getId())) {
                    for (int j = 0; j < tasks.size(); j++) {
                        for (Task m:timeDependentTasks) {
                            if (m.getId().equals(tasks.get(j).getId()) && i != j) {
                                timeDependencies[i][j] = 1;
                            }
                        }
                    }
                }
            }
        }
    }

    /*public void getTasksAfterGateway(FlowNode flowNode) {
        ArrayList<Gateway> gateways = new ArrayList<>();
        ArrayList<Task> tasksBetweenGateways = new ArrayList<>();
        if (flowNode  instanceof Task) {
            tasksBetweenGateways.add((Task) flowNode);
        }
        for (FlowNode f:flowNode.getPreviousNodes().list()) {
            if (flowNode.getPreviousNodes().list)
            if (f instanceof Task) {
                tasksBetweenGateways.add((Task) f);
            }
            else if (f instanceof Gateway && gateways.contains(f) == true) {
                break;
            }
            else if (f instanceof Gateway && gateways.contains(f) == false) {
                gateways.add((Gateway) f);
            }

        }
    }*/

    public void getTasksAfterGateway(FlowNode flowNode, ArrayList<Task> tasksBetweenGateways) {
        if (flowNode.getPreviousNodes().list().size() != 0) {
            for (FlowNode f:flowNode.getPreviousNodes().list()) {
                if (f instanceof Task) {
                    int counter = 0;
                    for (Task t:tasksBetweenGateways) {
                        if (f.getId().equals(t.getId())) {
                            counter++;
                        }
                    }
                    if (counter > 20) {
                        continue;
                    }
                    tasksBetweenGateways.add((Task) f);
                }
                getTasksAfterGateway(f, tasksBetweenGateways);
            }
        }
    }

    public Task getFirstTaskFollowing(FlowNode flowNode) {
        Task firstTask = null;
        if (flowNode.getSucceedingNodes().list().size() > 0) {
            for (FlowNode f:flowNode.getSucceedingNodes().list()) {
                if (f instanceof Task) {
                    firstTask = (Task) f;
                }
                else if (f instanceof CatchEvent) {
                    continue;
                }
                else {
                    firstTask = getFirstTaskFollowing(f);
                }
            }
        }
        return firstTask;
    }

    public boolean checkDocumentDependency(Task n, Task m) {
        boolean hasDocumentDependency = false;
        for (DataAssociation associationN:n.getDataInputAssociations()) {
            for (DataAssociation associationM:m.getDataInputAssociations()) {
                for (ItemAwareElement sourceN:associationN.getSources()) {
                    for (ItemAwareElement sourceM:associationM.getSources()) {
                        if(sourceN instanceof DataStoreReference && sourceM instanceof DataStoreReference && ((DataStoreReference) sourceN).getName().equals(((DataStoreReference) sourceM).getName())) {
                            hasDocumentDependency = true;
                        }
                        else if (sourceN instanceof DataObjectReference && sourceM instanceof DataObjectReference && ((DataObjectReference) sourceN).getName().equals(((DataObjectReference) sourceM).getName())) {
                            hasDocumentDependency = true;
                        }
                    }
                }
            }
        }
        return hasDocumentDependency;
    }

    public void addDocumentDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j && checkDocumentDependency(tasks.get(i), tasks.get(j)) == true) {
                    inputDependencies[i][j] = 1;
                }
            }
        }
    }

    public boolean checkDataDependency(Task n, Task m) {
        boolean hasDataDependency = false;
        for (DataAssociation associationN:n.getDataOutputAssociations()) {
            for (DataAssociation associationM:m.getDataInputAssociations()) {
                for (ItemAwareElement sourceM:associationM.getSources()) {
                    //System.out.println("Target output " + n.getName() + ": (" + associationN.getTarget().getClass() + ") " + associationN.getTarget().getId());
                    //System.out.println("Source input " + m.getName() + ": (" + sourceM.getClass() + ") " + sourceM.getId());
                    if (associationN.getTarget() instanceof DataObjectReference && sourceM instanceof DataObjectReference && associationN.getTarget().getId().equals(sourceM.getId())) {
                        hasDataDependency = true;
                    }
                }
            }
        }
        for (DataAssociation associationN:n.getDataInputAssociations()) {
            for (DataAssociation associationM:m.getDataOutputAssociations()) {
                for (ItemAwareElement sourceN:associationN.getSources()) {
                    //System.out.println("Target output " + m.getName() + ": (" + associationM.getTarget().getClass() + ") " + associationM.getTarget().getId());
                    //System.out.println("Source input " + n.getName() + ": (" + sourceN.getClass() + ") " + sourceN.getId());
                    if (associationM.getTarget() instanceof DataObjectReference && sourceN instanceof DataObjectReference && associationM.getTarget().getId().equals(sourceN.getId())) {
                        hasDataDependency = true;
                    }
                }
            }
        }
        return hasDataDependency;
    }

    public void addDataDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j && checkDataDependency(tasks.get(i), tasks.get(j)) == true) {
                    dataDependencies[i][j] = 1;
                }
            }
        }
    }



}
