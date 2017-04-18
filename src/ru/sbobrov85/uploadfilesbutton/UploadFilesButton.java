/*
 * License.
 */
package ru.sbobrov85.uploadfilesbutton;

import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallbackSystemAction;
import ru.sbobrov85.uploadfilesbutton.classes.ContextProperties;
import ru.sbobrov85.uploadfilesbutton.classes.StateHelper;

@ActionID(
    category = "Project",
    id = "ru.sbobrov85.uploadfilesbutton.UploadFilesButton"
)

@ActionRegistration(
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

@Messages("CTL_UploadFilesButton=Toggle auto upload")

public final class UploadFilesButton extends CallbackSystemAction {
    private JMenuItem menuItem;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (StateHelper.isEnabled()) {
            ContextProperties props = new ContextProperties();
            props.setProperty(
                "remote.upload",
                StateHelper.toggle(
                    props.getProperty("remote.upload")
                )
            );
            setIcon(new ImageIcon());
            menuItem.setIcon(new ImageIcon(getClass().getResource(StateHelper.getIcon())));
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
      return StateHelper.getIcon();
    }

  @Override
  public JMenuItem getMenuPresenter() {
    menuItem = new JMenuItem(
        getName(),
        getIcon()
    );
    menuItem.addActionListener(this);

    return menuItem;
  }
}
