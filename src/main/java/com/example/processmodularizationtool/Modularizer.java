package com.example.processmodularizationtool;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Modularizer {

    private File file; //source file
    private BpmnModelInstance modelInstance; //Instance of BPMN model

    private Collection<ModelElementInstance> participantInstances; //Collection of participant instances
    private Collection<ModelElementInstance> laneInstances; //Collection of lane instances
    private Collection<ModelElementInstance> taskInstances; //Collection of task instances

    private Collection<ModelElementInstance> messageFlowInstances;

    private ArrayList<Participant> participants; //ArrayList of participants
    private ArrayList<Task> tasks; //ArrayList of tasks


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
        taskInstances = modelInstance.getModelElementsByType(modelInstance.getModel().getType(Task.class)); //create collection of task instances

        participants = new ArrayList<>();
        tasks = new ArrayList<>();

        createParticipantList();
        createOrderedTaskList();

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


    public void createParticipantList() {
        for (ModelElementInstance p:participantInstances) {
            Participant participant = (Participant) p;
            participants.add(participant);
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

    /**
     *
     * @param fileName
     */
    public void exportLaneDependencies(String fileName) {
        /*
        Two-dimensional String-Array with both length and width of the
        dependency-matix +1 to have an extra lane and column for the task
        names
         */
        String[][] laneDependencyStrings = new String
                [laneDependencies.length + 1][laneDependencies.length + 1];
        //Iteration through the whole String-Array
        for (int i = 0; i < laneDependencyStrings.length; i++) {
            for (int j = 0; j < laneDependencyStrings.length; j++) {
                //First column
                if (i == 0) {
                    if (j == 0) {
                        laneDependencyStrings[i][j] = "X";
                    }
                    /*
                    For each lane, the first column contains the task name.
                     All carriage returns or new lines are replaced with a
                     space for coherence.
                     */
                    else {
                        String string = tasks.get(j - 1).getName();
                        string = string.replaceAll("\r\n", " ");
                        string = string.replaceAll("\n", " ");
                        string = string.replaceAll("\r", " ");
                        laneDependencyStrings[i][j] = string;
                    }
                }
                //First lane
                /*
                For each column, the first lane contains the task name.
                 All carriage returns or new lines are replaced with a
                 space for coherence.
                 */
                else if (j == 0) {
                    String string = tasks.get(i - 1).getName();
                    string = string.replaceAll("\r\n", " ");
                    string = string.replaceAll("\n", " ");
                    string = string.replaceAll("\r", " ");
                    laneDependencyStrings[i][j] = string;
                }
                /*
                Every field apart from the first lane and column ist filled
                 in with the according laneDependency value transformed to
                 a String.
                 */
                else {
                    laneDependencyStrings[i][j] =
                            String.valueOf(laneDependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        //Export of the newly generated String-Array as CSV-File
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
                        String string = tasks.get(j - 1).getName();
                        string = string.replaceAll("\r\n", " ");
                        string = string.replaceAll("\n", " ");
                        string = string.replaceAll("\r", " ");
                        timeDependencyStrings[i][j] = string;
                    }
                }
                else if (j == 0) {
                    String string = tasks.get(i - 1).getName();
                    string = string.replaceAll("\r\n", " ");
                    string = string.replaceAll("\n", " ");
                    string = string.replaceAll("\r", " ");
                    timeDependencyStrings[i][j] = string;
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
                        String string = tasks.get(j - 1).getName();
                        string = string.replaceAll("\r\n", " ");
                        string = string.replaceAll("\n", " ");
                        string = string.replaceAll("\r", " ");
                        inputDependencyStrings[i][j] = string;
                    }
                }
                else if (j == 0) {
                    String string = tasks.get(i - 1).getName();
                    string = string.replaceAll("\r\n", " ");
                    string = string.replaceAll("\n", " ");
                    string = string.replaceAll("\r", " ");
                    inputDependencyStrings[i][j] = string;
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
                        String string = tasks.get(j - 1).getName();
                        string = string.replaceAll("\r\n", " ");
                        string = string.replaceAll("\n", " ");
                        string = string.replaceAll("\r", " ");
                        dataDependencyStrings[i][j] = string;
                    }
                }
                else if (j == 0) {
                    String string = tasks.get(i - 1).getName();
                    string = string.replaceAll("\r\n", " ");
                    string = string.replaceAll("\n", " ");
                    string = string.replaceAll("\r", " ");
                    dataDependencyStrings[i][j] = string;
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
                        String string = tasks.get(j - 1).getName();
                        string = string.replaceAll("\r\n", " ");
                        string = string.replaceAll("\n", " ");
                        string = string.replaceAll("\r", " ");
                        dependencyStrings[i][j] = string;
                    }
                }
                else if (j == 0) {
                    String string = tasks.get(i - 1).getName();
                    string = string.replaceAll("\r\n", " ");
                    string = string.replaceAll("\n", " ");
                    string = string.replaceAll("\r", " ");
                    dependencyStrings[i][j] = string;
                }
                else {
                    dependencyStrings[i][j] = String.valueOf(dependencies[i - 1][j - 1]);
                }
            }
        }

        CsvWriter writer = new CsvWriter();
        writer.exportCsv(dependencyStrings, fileName);
    }


    /**
     * Storing data dependencies between all possible combinations of two Tasks in dataDependencies Array
     */
    public void addLaneDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j) {
                    laneDependencies[i][j] =
                            checkLaneDependency(tasks.get(i), tasks.get(j));
                }
            }
        }
    }


    /**
     * Checks whether tasks n and m have lane dependency and returns the
     * degree of lane dependency between n and m
     * @param n Task
     * @param m Task
     * @return Integer: degree of lane dependency between n and m
     */
    public int checkLaneDependency (Task n, Task m) {
        int dependency = 0;

        //Iterating through each participant to check for lane dependency
        for (Participant p:participants) {
            Process process = p.getProcess();
            if (process == null) {
                System.out.println(p.getName() + " skipped");
                continue;
            }
            /*
            List of all tasks inside the pool of the participant of the
            current iteration
             */
            ArrayList<Task> tasksInsidePool = new ArrayList<>();
            Collection<LaneSet> laneSets = process.getLaneSets();
            for (LaneSet laneSet:laneSets) {
                if (laneSet != null) {
                    /*
                    Iterating through all lanes in the pool and adding all
                    tasks within all lanes and sub-lanes to the list
                     */
                    for (Lane lane:laneSet.getLanes()) {
                        getTasksInside(lane, tasksInsidePool);
                    }
                    /*
                    If both tasks are in the list and, hence, in the same
                    pool, iterating through all lanes and checking, whether
                     both tasks are also in the same lane
                     */
                    if (tasksInsidePool.contains(n) &&
                            tasksInsidePool.contains(m)) {
                        dependency = 1;
                        if (laneSet.getLanes().size() > 1) {
                            for (Lane lane:laneSet.getLanes()) {
                                dependency = dependency +
                                        laneDependencyCheck(lane, n, m);
                            }
                        }
                    }
                }
            }
        }
        return dependency;
    }


    /**
     * Checking whether n & m are within lane or its subLanes
     * @param lane
     * @param n
     * @param m
     * @return 0 if n & m are not within lane or its subLanes; 1 if n & m
     * are within lane or its subLanes + value of return for subLanes
     */
    public int laneDependencyCheck(Lane lane, Task n, Task m) {
        int counter = 0;

        //List of all Tasks within lane
        ArrayList<Task> insideThisLane = new ArrayList<>();
        //Adding all Tasks inside lane to List
        getTasksInside(lane, insideThisLane);
        //Checking whether n & m are in List
        if (insideThisLane.contains(n) && insideThisLane.contains(m)) {
            counter = 1;

            //Recursive call of this method for subLanes
            LaneSet childLaneSet = lane.getChildLaneSet();
            if (childLaneSet != null) {
                for (Lane l:childLaneSet.getLanes()) {
                    counter = counter + laneDependencyCheck(l, n, m);
                }
            }
        }
        return counter;
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



    public void createOrderedTaskList (){
        for (Participant participant:participants) {
            if (participant.getProcess() != null) {
                getTasksFromPool(participant);
            }
        }
    }

    public void getTasksFromPool(Participant participant) {
        //System.out.print(participant.getName() + ": ");
        Collection<FlowElement> flowElements = participant.getProcess().getFlowElements();

        StartEvent start = null;
        for (FlowElement flowElement:flowElements) {
            if(flowElement instanceof StartEvent) {
                start = (StartEvent) flowElement;
                //System.out.print("(StartEvent: " + start.getName() + ") ");
            }
        }
        //ArrayList<Task> tasks1 = new ArrayList<>();       //getFollowingTasks(start);
        List<FlowNode> following = start.getSucceedingNodes().list();
        int counter = 0;
        while (following.size() > counter) {
            int i = following.size();
            while (counter < i) {                //for (FlowNode flowNode:following) {
                if(following.get(counter) instanceof Task && tasks.contains(following.get(counter)) == false) {
                    //System.out.print(following.get(counter).getName() + ", ");
                    tasks.add((Task)following.get(counter));
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

        List<Task> tasks1 = start.getSucceedingNodes().filterByType(Task.class).list();


    }



    /**
     * Checking for all time dependencies within the model, adding the value
     * 1 to the time dependency matrix for each combination of two tasks that
     * are time dependend.
     */
    public void checkTimeDependency() {
        Collection<ModelElementInstance> flowNodeInstances =
                modelInstance.getModelElementsByType(modelInstance.getModel()
                        .getType(FlowNode.class));
        //List of all FlowNodes within the model
        ArrayList<FlowNode> allFlowNodes = new ArrayList<>();
        for (ModelElementInstance x:flowNodeInstances) {
            allFlowNodes.add((FlowNode) x);
        }

        //Iterating through all FlowNodes
        for (FlowNode flowNode:allFlowNodes) {
            //List for set of time dependent Tasks
            ArrayList<Task> timeDependentTasks = new ArrayList<>();
            /*
            Checking for merging parallelGateway; if it has more than one
            previous node it is a merging gateway
             */
            if (flowNode instanceof ParallelGateway &&
                    flowNode.getPreviousNodes().list().size() > 1) {
                /*
                List of visited Tasks to prevent StackOverFlowErrors
                because of Task loops
                 */
                ArrayList<FlowNode> visited = new ArrayList<>();
                /*
                If there is a Task in direct succession of the
                parallelGateway, it is added to the List of time dependent
                Tasks
                 */
                if (getFirstTaskFollowing(flowNode) != null) {
                    visited.add(getFirstTaskFollowing(flowNode));
                    timeDependentTasks.add(getFirstTaskFollowing(flowNode));
                }

                /*
                List of all possible FlowNodes that might be the source of
                all paths merged by the parallelGateway
                 */
                ArrayList<FlowNode> sourceNodes =
                        getPossibleSourceNodes(flowNode);

                /*
                Identifying all Tasks between the merging parallelGateway
                and the source of all incoming paths
                 */
                for (FlowNode f:flowNode.getPreviousNodes().list()) {
                    getTasksBeforeGateway(f, timeDependentTasks, visited,
                            sourceNodes);
                }
            }

            /*
            Storing time dependencies between all possible combinations of
            two Tasks in timeDependencies Array
             */
            for (int i = 0; i < tasks.size(); i++) {
                for (int j = 0; j < tasks.size(); j++) {
                    if (i != j && timeDependentTasks.contains(tasks.get(i))
                            && timeDependentTasks.contains(tasks.get(j))) {
                        timeDependencies[i][j] = 1;
                    }
                }
            }
        }
    }


    /**
     *
     * @param flowNode FlowNode
     * @return
     */
    public ArrayList<FlowNode> getPossibleSourceNodes(FlowNode flowNode) {
        /*List of FlowNodes visited by iterating through all incoming paths
         of flowNode
         */
        ArrayList<FlowNode> visitedByAll = new ArrayList<>();
        /*
        List of FlowNodes that have been visited by all iterations through
        incoming paths of flowNode and hence qualify to be source nodes
         */
        ArrayList<FlowNode> possibleSourceNodes = new ArrayList<>();

        /*
        if flowNode has previous FlowNodes iterating through all incoming
        paths
         */
        if (flowNode.getPreviousNodes().list().size() != 0) {
            for (int i = 0; i < flowNode.getPreviousNodes().list().size(); i++) {
                //List of FlowNodes already visited in this iteration
                ArrayList<FlowNode> visited = new ArrayList<>();
                visited.add(flowNode.getPreviousNodes().list().get(i));
                /*
                Calling Method that adds all previous FlowNodes to visited
                List
                 */
                addPreviousFlowNodes(flowNode.getPreviousNodes()
                        .list().get(i), visited);

                /*
                Iterating through all FlowNodes in visited List; if not
                contained in visitedByAll adding to visitedByAll;
                otherwise adding FlowNode to possibleSourceNodes
                 */
                for(FlowNode f:visited) {
                    if(visitedByAll.contains(f)) {
                        possibleSourceNodes.add(f);
                        continue;
                    }
                    visitedByAll.add(f);
                }

                /*
                Iterating through all FlowNodes within possibleSourceNodes;
                All FlowNodes in possibleSourceNodes that have not been
                visited in this iteration of previous FLowNodes are removed
                 from possibleSourceNodes
                 */
                for (int j = 0; j < possibleSourceNodes.size(); j++) {
                    if (visited.contains(possibleSourceNodes.get(j)) == false) {
                        possibleSourceNodes.remove(possibleSourceNodes.get(j));
                    }
                }
            }
        }
        return possibleSourceNodes;
    }


    /**
     * Iteratively and recursively visits all FlowNodes prior (in sequence) to flowNode and adds them to visited List
     * @param flowNode FlowNode
     * @param visited ArrayList of FlowNodes
     */
    public void addPreviousFlowNodes(FlowNode flowNode, ArrayList<FlowNode> visited) {
        //if flowNode has previous FlowNodes iterating through all previous FlowNodes f
        if (flowNode.getPreviousNodes().list().size() != 0) {
            for (FlowNode f:flowNode.getPreviousNodes().list()) {
                /*
                if f has not been visited yet it is added to visited List and this method is called recursively for f;
                otherwise recursion is stopped within current iteration
                 */
                if (visited.contains(f)) {
                    continue;
                }
                visited.add(f);

                addPreviousFlowNodes(f, visited);
            }
        }
    }



    /**
     * Identifies all Tasks between flowNode and any FlowNode within sourceNodes List and adds them to
     * tasksBetweenGateways List
     * @param flowNode FlowNode
     * @param tasksBetweenGateways ArrayList of Tasks
     * @param visited ArrayList of FlowNodes
     * @param sourceNodes ArrayList of FlowNodes
     */
    public void getTasksBeforeGateway(FlowNode flowNode, ArrayList<Task> tasksBetweenGateways, ArrayList<FlowNode> visited, ArrayList<FlowNode> sourceNodes) {
        //Checking if flowNode has not been visited yes and if flowNode is not contained within sourceNodes List
        if (visited.contains(flowNode) == false && sourceNodes.contains(flowNode) == false) {
            //flowNode is added to visited List
            visited.add(flowNode);
            //if flowNode is a Task it is added to tasksBetweenGateways List
            if (flowNode instanceof Task) {
                tasksBetweenGateways.add((Task) flowNode);
            }

            /*
            if flowNode has previous FlowNodes iterating through all previous FlowNodes f and recursively calling this
            method for f
             */
            if (flowNode.getPreviousNodes().list().size() != 0) {
                for (FlowNode f:flowNode.getPreviousNodes().list()) {
                    getTasksBeforeGateway(f, tasksBetweenGateways, visited, sourceNodes);
                }
            }
        }
    }

    /**
     * Checks for first Task that (in sequence) is succeeding (not necessarily directly) flowNode without any
     * CatchEvent or Gateway between flowNode and said Task
     * @param flowNode FlowNode
     * @return first Task after flowNode (without any CatchEvent or Gateway inbetween); null if no according Task found
     */
    public Task getFirstTaskFollowing(FlowNode flowNode) {
        Task firstTask = null;
        /*
        if flowNode has succeeding FlowNodes iterating through all succeeding FlowNodes f
        */
        if (flowNode.getSucceedingNodes().list().size() > 0) {
            for (FlowNode f:flowNode.getSucceedingNodes().list()) {
                //if f is a Task it is the firstTask succeeding flowNode
                if (f instanceof Task) {
                    firstTask = (Task) f;
                }
                //skipping this iteration if f is a CatchEvent
                else if (f instanceof CatchEvent) {
                    continue;
                }
                //skipping this iteration if f is a Gateway
                else if (f instanceof Gateway) {
                    continue;
                }
                /*
                if first Task succeeding flowNode has not been found and there was no CatchEvent or Gateway this method
                is called recursively for f
                 */
                else {
                    firstTask = getFirstTaskFollowing(f);
                }
            }
        }
        return firstTask;
    }

    /**
     * Checking whether n & m have document dependency
     * @param n Task
     * @param m Task
     * @return true if n & m have document dependency; otherwise false
     */
    public boolean checkDocumentDependency(Task n, Task m) {
        boolean hasDocumentDependency = false;
        //Iterating through all DataInputAssociations of n
        for (DataAssociation associationN:n.getDataInputAssociations()) {
            //Iterating through all DataInputAssociations of m
            for (DataAssociation associationM:m.getDataInputAssociations()) {
                //Iterating through all sources of DataInputAssociations of n
                for (ItemAwareElement sourceN:associationN.getSources()) {
                    //Iterating through all sources of DataInputAssociations of m
                    for (ItemAwareElement sourceM:associationM.getSources()) {
                        /*
                        Checking whether the sources of the
                        DataInputAssociations of n & m are both
                        DataStoreReferences and have the same name
                         */
                        if(sourceN instanceof DataStoreReference &&
                                sourceM instanceof DataStoreReference &&
                                ((DataStoreReference) sourceN).getName()
                                        .equals(((DataStoreReference) sourceM)
                                                .getName())) {
                            hasDocumentDependency = true;
                        }
                        /*
                        Checking whether the sources of the
                        DataInputAssociations of n & m are both
                        DataObjectReferences and have the same name
                         */
                        else if (sourceN instanceof DataObjectReference
                                && sourceM instanceof DataObjectReference &&
                                ((DataObjectReference) sourceN).getName()
                                        .equals(((DataObjectReference) sourceM)
                                                .getName())) {
                            hasDocumentDependency = true;
                        }
                    }
                }
            }
        }
        return hasDocumentDependency;
    }


    /**
     * Storing document dependencies between all possible combinations of
     * two Tasks in documentDependencies Array
     */
    public void addDocumentDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j && checkDocumentDependency(tasks.get(i),
                        tasks.get(j)) == true) {
                    inputDependencies[i][j] = 1;
                }
            }
        }
    }

    /**
     * Checking whether n & m have data dependency
     * @param n Task
     * @param m Task
     * @return true if n needs the data output of m as input or vice versa;
     * false otherwise
     */
    public boolean checkDataDependency(Task n, Task m) {
        boolean hasDataDependency = false;
        //Iterating through all DataOutputAssociations of n
        for (DataAssociation associationN:n.getDataOutputAssociations()) {
            //Iterating through all DataInputAssociations of m
            for (DataAssociation associationM:m.getDataInputAssociations()) {
                //Iterating through all sources of DataInputAssociations of m
                for (ItemAwareElement sourceM:associationM.getSources()) {
                    /*
                    Checking whether the target of the
                    DataOutputAssociation of n and the source of the
                    DataInputAssociation of m are both DataObjectReferences
                     and whether they have the same name
                    */
                    if (associationN.getTarget() instanceof DataObjectReference
                            && sourceM instanceof DataObjectReference &&
                            ((DataObjectReference) associationN.getTarget())
                                    .getName().equals(((DataObjectReference) sourceM)
                                            .getName())) {
                        hasDataDependency = true;
                    }
                }
            }
        }

        //Same procedure as above but with m & n switching places
        for (DataAssociation associationN:n.getDataInputAssociations()) {
            for (DataAssociation associationM:m.getDataOutputAssociations()) {
                for (ItemAwareElement sourceN:associationN.getSources()) {
                    if (associationM.getTarget() instanceof DataObjectReference
                            && sourceN instanceof DataObjectReference
                            && ((DataObjectReference) associationM.getTarget())
                            .getName().equals(((DataObjectReference) sourceN)
                                    .getName())) {
                        hasDataDependency = true;
                    }
                }
            }
        }

        return hasDataDependency;
    }

    /**
     * Storing data dependencies between all possible combinations of two
     * Tasks in dataDependencies Array
     */
    public void addDataDependencies() {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (i != j && checkDataDependency(tasks.get(i),
                        tasks.get(j)) == true) {
                    dataDependencies[i][j] = 1;
                }
            }
        }
    }



}
