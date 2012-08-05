package com.niafikra.meddela.ui.vaadin.dashboard.settings.transport

import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Upload
import com.vaadin.ui.ListSelect
import com.niafikra.meddela.meddela
import groovy.io.FileType
import com.vaadin.ui.Window
import com.vaadin.ui.Panel
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import com.niafikra.meddela.services.TransportManager
import com.vaadin.ui.themes.Reindeer
import com.vaadin.ui.Button
import org.vaadin.peter.buttongroup.ButtonGroup
import com.vaadin.ui.Alignment

/**
 * niafikra engineering

 * Author: Boniface Chacha <boniface.chacha@niafikra.com,bonifacechacha@gmail.com>
 * Date: 8/1/12
 * Time: 6:47 AM
 */
class TransportUploadPanel extends HorizontalLayout implements Upload.FailedListener, Upload.SucceededListener {
    Upload transportUpload;
    ListSelect transportList;
    ConfigurationList transportConfigs
    Button save, remove

    public TransportUploadPanel() {
        transportUpload = new Upload("Upload Transport jar", new UploadReceiver(meddela.transportManager.transportsPath))
        transportList = new ListSelect("Available Transport")
        transportConfigs = new ConfigurationList(null)
        save = new Button("Save")
        remove = new Button("Remove")
        build()
        load()
    }

    def build() {
        setSizeFull()
        setMargin(true)
        setSpacing(true)
        VerticalLayout transListLay = new VerticalLayout()
        transListLay.setSpacing(true)
        transListLay.addComponent(transportList)
        ButtonGroup footer = new ButtonGroup()
        save.setWidth("100px")
        remove.setWidth("100px")
        footer.addButton(save)
        footer.addButton(remove)
        transListLay.addComponent(footer)
        transListLay.setComponentAlignment(footer,Alignment.BOTTOM_CENTER)
        transportList.setSizeFull()
        transListLay.setSizeFull()
        transListLay.setExpandRatio(transportList,1)
        addComponent(transListLay)
        
        VerticalLayout transportInfoLay = new VerticalLayout()
        transportInfoLay.setSizeFull()
        transportInfoLay.setSpacing(true)
        Panel configPanel = new Panel("Transport Plugin Configurations")
        configPanel.setContent(transportConfigs)
        configPanel.setStyleName(Reindeer.PANEL_LIGHT)
        configPanel.setSizeFull()
        transportInfoLay.addComponent(configPanel)
        Panel uploadingPanel = new Panel("Add Transport")
        uploadingPanel.setSizeFull()
        transportUpload.setWidth("50%")
        uploadingPanel.addComponent(transportUpload)
        transportUpload.addListener((Upload.SucceededListener) this)
        transportUpload.addListener((Upload.FailedListener) this)
        transportInfoLay.addComponent(uploadingPanel)
        transportInfoLay.setExpandRatio(configPanel, 2)
        transportInfoLay.setExpandRatio(uploadingPanel, 1)
        addComponent(transportInfoLay)
        setExpandRatio(transportInfoLay, 8)
        setExpandRatio(transListLay, 2)
    }

    def load() {
        meddela.transportManager.listAvailableTransport().each {
            transportList.addItem(it)
        }
    }

    @Override
    void uploadFailed(Upload.FailedEvent event) {
        getWindow().showNotification("Transport Upload Failed!", event.reason.toString(), Window.Notification.TYPE_ERROR_MESSAGE)
    }

    @Override
    void uploadSucceeded(Upload.SucceededEvent event) {
        TransportManager transportManager = meddela.transportManager
        if (event.MIMEType.contains("java-archive")) {
            if (transportManager.installTransportPlugin(event.filename))
                getWindow().showNotification("Transport Plugin installed Successfully!", Window.Notification.TYPE_HUMANIZED_MESSAGE)
            else {
                getWindow().showNotification("Transport Plugin Failed to be installed!", "Jar File does not contain a valid meddela transport", Window.Notification.TYPE_ERROR_MESSAGE)

            }
        } else {
            getWindow().showNotification("Transport Plugin Failed to be installed!", "File is not a valid meddela transport jar", Window.Notification.TYPE_ERROR_MESSAGE)
            FileUtils.deleteQuietly(new File(transportManager.transportsPath + File.separator + event.filename))
        }
    }
}
