package com.example.demo.controller;

import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

public class ResidentPropertyEditorFactory implements Callback<PropertySheet.Item, PropertyEditor<?>> {

    private final ResidentListController residentListController;

    public ResidentPropertyEditorFactory(ResidentListController residentListController) {
        this.residentListController = residentListController;
    }

    @Override
    public PropertyEditor<?> call(PropertySheet.Item item) {
        PropertyEditor<?> propertyEditor = new DefaultPropertyEditorFactory().call(item);
        propertyEditor.getEditor().focusedProperty().addListener(event -> residentListController.enableUpdateButton());
        return propertyEditor;
    }
}
