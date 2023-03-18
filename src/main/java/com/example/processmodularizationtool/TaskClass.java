package com.example.processmodularizationtool;

import org.camunda.bpm.model.bpmn.Query;
import org.camunda.bpm.model.bpmn.builder.AbstractFlowNodeBuilder;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.xml.ModelInstance;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;

import java.util.Collection;

public class TaskClass implements Task {

    private String LaneID;



    public String getLaneID() {
        return this.LaneID;
    }

    public void setLaneID(String ID) {
        this.LaneID = ID;
    }

    public boolean isInLane(String ID) {
        if (this.LaneID.equals(ID)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isCamundaAsync() {
        return false;
    }

    @Override
    public void setCamundaAsync(boolean isCamundaAsync) {

    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public Collection<Documentation> getDocumentations() {
        return null;
    }

    @Override
    public ExtensionElements getExtensionElements() {
        return null;
    }

    @Override
    public void setExtensionElements(ExtensionElements extensionElements) {

    }

    @Override
    public BpmnShape getDiagramElement() {
        return null;
    }

    @Override
    public boolean isForCompensation() {
        return false;
    }

    @Override
    public void setForCompensation(boolean isForCompensation) {

    }

    @Override
    public int getStartQuantity() {
        return 0;
    }

    @Override
    public void setStartQuantity(int startQuantity) {

    }

    @Override
    public int getCompletionQuantity() {
        return 0;
    }

    @Override
    public void setCompletionQuantity(int completionQuantity) {

    }

    @Override
    public SequenceFlow getDefault() {
        return null;
    }

    @Override
    public void setDefault(SequenceFlow defaultFlow) {

    }

    @Override
    public IoSpecification getIoSpecification() {
        return null;
    }

    @Override
    public void setIoSpecification(IoSpecification ioSpecification) {

    }

    @Override
    public Collection<Property> getProperties() {
        return null;
    }

    @Override
    public Collection<DataInputAssociation> getDataInputAssociations() {
        return null;
    }

    @Override
    public Collection<DataOutputAssociation> getDataOutputAssociations() {
        return null;
    }

    @Override
    public Collection<ResourceRole> getResourceRoles() {
        return null;
    }

    @Override
    public LoopCharacteristics getLoopCharacteristics() {
        return null;
    }

    @Override
    public void setLoopCharacteristics(LoopCharacteristics loopCharacteristics) {

    }

    @Override
    public AbstractFlowNodeBuilder builder() {
        return null;
    }

    @Override
    public boolean isScope() {
        return false;
    }

    @Override
    public BpmnModelElementInstance getScope() {
        return null;
    }

    @Override
    public Collection<SequenceFlow> getIncoming() {
        return null;
    }

    @Override
    public Collection<SequenceFlow> getOutgoing() {
        return null;
    }

    @Override
    public Query<FlowNode> getPreviousNodes() {
        return null;
    }

    @Override
    public Query<FlowNode> getSucceedingNodes() {
        return null;
    }

    @Override
    public boolean isCamundaAsyncBefore() {
        return false;
    }

    @Override
    public void setCamundaAsyncBefore(boolean isCamundaAsyncBefore) {

    }

    @Override
    public boolean isCamundaAsyncAfter() {
        return false;
    }

    @Override
    public void setCamundaAsyncAfter(boolean isCamundaAsyncAfter) {

    }

    @Override
    public boolean isCamundaExclusive() {
        return false;
    }

    @Override
    public void setCamundaExclusive(boolean isCamundaExclusive) {

    }

    @Override
    public String getCamundaJobPriority() {
        return null;
    }

    @Override
    public void setCamundaJobPriority(String jobPriority) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public Auditing getAuditing() {
        return null;
    }

    @Override
    public void setAuditing(Auditing auditing) {

    }

    @Override
    public Monitoring getMonitoring() {
        return null;
    }

    @Override
    public void setMonitoring(Monitoring monitoring) {

    }

    @Override
    public Collection<CategoryValue> getCategoryValueRefs() {
        return null;
    }

    @Override
    public DomElement getDomElement() {
        return null;
    }

    @Override
    public ModelInstance getModelInstance() {
        return null;
    }

    @Override
    public ModelElementInstance getParentElement() {
        return null;
    }

    @Override
    public ModelElementType getElementType() {
        return null;
    }

    @Override
    public String getAttributeValue(String attributeName) {
        return null;
    }

    @Override
    public void setAttributeValue(String attributeName, String xmlValue) {

    }

    @Override
    public void setAttributeValue(String attributeName, String xmlValue, boolean isIdAttribute) {

    }

    @Override
    public void setAttributeValue(String attributeName, String xmlValue, boolean isIdAttribute, boolean withReferenceUpdate) {

    }

    @Override
    public void removeAttribute(String attributeName) {

    }

    @Override
    public String getAttributeValueNs(String namespaceUri, String attributeName) {
        return null;
    }

    @Override
    public void setAttributeValueNs(String namespaceUri, String attributeName, String xmlValue) {

    }

    @Override
    public void setAttributeValueNs(String namespaceUri, String attributeName, String xmlValue, boolean isIdAttribute) {

    }

    @Override
    public void setAttributeValueNs(String namespaceUri, String attributeName, String xmlValue, boolean isIdAttribute, boolean withReferenceUpdate) {

    }

    @Override
    public void removeAttributeNs(String namespaceUri, String attributeName) {

    }

    @Override
    public String getTextContent() {
        return null;
    }

    @Override
    public String getRawTextContent() {
        return null;
    }

    @Override
    public void setTextContent(String textContent) {

    }

    @Override
    public void replaceWithElement(ModelElementInstance newElement) {

    }

    @Override
    public ModelElementInstance getUniqueChildElementByNameNs(String namespaceUri, String elementName) {
        return null;
    }

    @Override
    public ModelElementInstance getUniqueChildElementByType(Class<? extends ModelElementInstance> elementType) {
        return null;
    }

    @Override
    public void setUniqueChildElementByNameNs(ModelElementInstance newChild) {

    }

    @Override
    public void replaceChildElement(ModelElementInstance existingChild, ModelElementInstance newChild) {

    }

    @Override
    public void addChildElement(ModelElementInstance newChild) {

    }

    @Override
    public boolean removeChildElement(ModelElementInstance child) {
        return false;
    }

    @Override
    public Collection<ModelElementInstance> getChildElementsByType(ModelElementType childElementType) {
        return null;
    }

    @Override
    public <T extends ModelElementInstance> Collection<T> getChildElementsByType(Class<T> childElementClass) {
        return null;
    }

    @Override
    public void insertElementAfter(ModelElementInstance elementToInsert, ModelElementInstance insertAfterElement) {

    }

    @Override
    public void updateAfterReplacement() {

    }
}
