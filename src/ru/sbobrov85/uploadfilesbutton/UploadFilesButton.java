/*
 * Copyright (C) 2017 sbobrov85 <sbobrov85@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
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
    @ActionReference(path = "Toolbars/File", position = 500),
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
    menuItem = super.getMenuPresenter();
    menuItem.addActionListener(this);

    return menuItem;
  }
}
