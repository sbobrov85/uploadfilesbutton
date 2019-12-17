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
package io.github.sbobrov85.nb.uploadfilesbutton;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle.Messages;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.CallbackSystemAction;
import io.github.sbobrov85.nb.uploadfilesbutton.classes.ContextProperties;
import io.github.sbobrov85.nb.uploadfilesbutton.classes.StateHelper;
import org.openide.util.Lookup.Result;
import org.openide.util.NbBundle;

/**
 * Described action id through annotation.
 */
@ActionID(
    category = "Project",
    id = "io.github.sbobrov85.nb.uploadfilesbutton.UploadFilesButton"
)

/**
 * Action registration through annotation.
 */
@ActionRegistration(
    lazy = false,
    displayName = "#CTL_UploadFilesButton"
)

@ActionReferences({
    @ActionReference(
        path = "Menu/Source",
        //CHECKSTYLE:OFF
        position = 9100,
        separatorBefore = 9050
        //CHECKSTYLE:ON
    ),
    //CHECKSTYLE:OFF
    @ActionReference(path = "Toolbars/File", position = 500),
    //CHECKSTYLE:ON
    @ActionReference(path = "Shortcuts", name = "DO-U")
})

@Messages("CTL_UploadFilesButton=Toggle auto upload")

public final class UploadFilesButton extends CallbackSystemAction
  implements LookupListener {

  /**
   * Contains menu item instance.
   */
  private JMenuItem menuItem;

  /**
   * Contains actions button instance.
   */
  private Component button;

  /**
   * Adding this class as lookup listener for context.
   * @param type context type.
   */
  protected void addLookupListener(final Class type) {
    Lookup lookup = Utilities.actionsGlobalContext();
    Result lookupResult = lookup.lookupResult(type);
    if (lookupResult != null) {
      lookupResult.addLookupListener(this);
    }
  }

  /**
   * Constructor.
   */
  public UploadFilesButton() {
    addLookupListener(Project.class);
    addLookupListener(FileObject.class);
  }

  /**
   * Get menu item image icon instance.
   * @return image icon instance.
   */
  protected ImageIcon getMenuItemImageIcon() {
    return new ImageIcon(getClass().getResource(StateHelper.getIcon()));
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public void actionPerformed(final ActionEvent e) {
    if (StateHelper.isEnabled()) {
      ContextProperties props = new ContextProperties();
      props.setProperty(
        "remote.upload",
        StateHelper.toggle(
            props.getProperty("remote.upload")
        )
      );
      setIcon(new ImageIcon());
      menuItem.setIcon(getMenuItemImageIcon());
    }
  }

  @Override
  public String getName() {
    return NbBundle.getMessage(
      this.getClass(),
      "Toggle-Message-Localized"
    );
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
    menuItem.setAccelerator((KeyStroke) getProperty(ACCELERATOR_KEY));
    menuItem.addActionListener(this);

    return menuItem;
  }

  @Override
  public Component getToolbarPresenter() {
    button = super.getToolbarPresenter();
    return button;
  }

  @Override
  public void resultChanged(final LookupEvent ev) {
    setIcon(new ImageIcon());
    boolean enabled = StateHelper.isEnabled();
    if (menuItem != null) {
      menuItem.setEnabled(enabled);
      menuItem.setIcon(getMenuItemImageIcon());
    }
    if (button != null) {
      button.setEnabled(enabled);
    }
  }
}
