/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbobrov85.uploadfilesbutton;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Resource;
import javax.swing.ImageIcon;
import org.netbeans.api.project.FileOwnerQuery;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.EditableProperties;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.util.actions.CallbackSystemAction;

@ActionID(
    category = "Project",
    id = "ru.sbobrov85.uploadfilesbutton.UploadFilesButton"
)

@ActionRegistration(
//    iconBase = "ru/sbobrov85/uploadfilesbutton/upload-files-16.png",
    lazy = false,
    displayName = "#CTL_UploadFilesButton"
)

@ActionReferences({
    @ActionReference(
        path = "Menu/Source",
        position = 9100,
        separatorBefore = 9050
    ),
    @ActionReference(path = "Toolbars/Remote", position = 3100),
    @ActionReference(path = "Shortcuts", name = "DO-U")
})

@Messages("CTL_UploadFilesButton=UploadFiles on Save")

public final class UploadFilesButton extends CallbackSystemAction {

    @Resource
    private static final String
        ICON_OFF_16 = "ru/sbobrov85/uploadfilesbutton/upload-files-off-16.png";

    @Resource
    private static final String
        ICON_16 = "ru/sbobrov85/uploadfilesbutton/upload-files-16.png";

    private Project currentProject;

    public static final String MANUALLY_STATE = "MANUALLY";

    public static final String ON_SAVE_STATE = "ON_SAVE";


    public void setCurrentProject() {
        Lookup lookup = Utilities.actionsGlobalContext();

        FileObject fileObject = lookup.lookup(FileObject.class);
        if (fileObject != null) {
            currentProject = FileOwnerQuery.getOwner(fileObject);
        } else {
            currentProject = lookup.lookup(Project.class);
        }
    }

    protected String getEditableProperty(String key) {
        String result = null;
        FileObject projectProps = currentProject.getProjectDirectory()
            .getFileObject(AntProjectHelper.PRIVATE_PROPERTIES_PATH);

        try {
            EditableProperties editableProps = loadProperties(projectProps);
            result = editableProps.getProperty(key);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        return result;
    }

    protected void setEditableProperty(String key, String value) {
        FileObject projectProps = currentProject.getProjectDirectory()
            .getFileObject(AntProjectHelper.PRIVATE_PROPERTIES_PATH);

        try {
            EditableProperties editableProps = loadProperties(projectProps);
            editableProps.setProperty(key, value);

            storeProperties(projectProps, editableProps);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public boolean checkEnabled() {
        setCurrentProject();
        boolean isEnabled = false;

        if (currentProject != null) {
            String prop = getEditableProperty("run.as");
            if ("REMOTE".equals(prop)) {
                isEnabled = true;
            }
        }

        return isEnabled;
    }

    protected String toggleUploadState(String state) {
        String resultState;

        switch (state) {
            case MANUALLY_STATE:
                resultState = ON_SAVE_STATE;
                break;

            case ON_SAVE_STATE:
                resultState = MANUALLY_STATE;
                break;

            default:
                resultState = MANUALLY_STATE;
        }

        return resultState;
    }

    private static EditableProperties loadProperties(FileObject propsFO) throws IOException {
        InputStream propsIS = propsFO.getInputStream();
        EditableProperties props = new EditableProperties(true);
        try {
            props.load(propsIS);
        } finally {
            propsIS.close();
        }
        return props;
    }

    public static void storeProperties(FileObject propsFO, EditableProperties props) throws IOException {
        FileLock lock = propsFO.lock();
        try {
            OutputStream os = propsFO.getOutputStream(lock);
            try {
                props.store(os);
            } finally {
                os.close();
            }
        } finally {
            lock.releaseLock();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (checkEnabled()) {
            setEditableProperty(
                "remote.upload",
                toggleUploadState(
                    getEditableProperty("remote.upload")
                )
            );
            setIcon(new ImageIcon());
        }
    }

    @Override
    public String getName() {
        return Bundle.CTL_UploadFilesButton();
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected String iconResource() {
        String iconPath = currentProject != null && "MANUALLY".equals(getEditableProperty("remote.upload"))  ?
            ICON_16 : ICON_OFF_16;
        return iconPath;
    }
}
