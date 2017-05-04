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
package ru.sbobrov85.uploadfilesbutton.classes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.support.ant.AntProjectHelper;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.EditableProperties;
import org.openide.util.Lookup;
import org.openide.util.Utilities;

/**
 * Get/Set project private properties.
 * @see <https://blogs.oracle.com/geertjan/entry/setting_netbeans_properties_programatically>
 */
public final class ContextProperties {

  /**
   * Contains private properties FileObject.
   */
  private FileObject projectProps;

  /**
   * Constructor.
   * Set current private properties FileObject for context.
   */
  public ContextProperties() {
    Project currentProject;
    projectProps = null;

    Lookup lookup = Utilities.actionsGlobalContext();
    FileObject fileObject = lookup.lookup(FileObject.class);
    if (fileObject != null) {
      currentProject = FileOwnerQuery.getOwner(fileObject);
    } else {
      currentProject = lookup.lookup(Project.class);
    }

    if (currentProject != null) {
      projectProps = currentProject.getProjectDirectory()
        .getFileObject(AntProjectHelper.PRIVATE_PROPERTIES_PATH);
    }
  }

  /**
   * Load editable properties from private folder.
   * @param propsFO private properties FileObject.
   * @return EditableProperties instance.
   * @throws IOException on load errors.
   * @throws NullPointerException on propsFO null.
   */
  protected static EditableProperties load(final FileObject propsFO)
    throws IOException, NullPointerException {
    InputStream propsIS = propsFO.getInputStream();
    EditableProperties props = new EditableProperties(true);
    try {
      props.load(propsIS);
    } finally {
      propsIS.close();
    }
    return props;
  }

  /**
   * Store editable properties.
   * @param propsFO private properties FileObject.
   * @param props EditableProperties instance.
   * @throws IOException on store error.
   * @throws NullPointerException if propsFO is null.
   */
  protected static void store(
    final FileObject propsFO,
    final EditableProperties props
  ) throws IOException, NullPointerException {
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

  /**
   * Get property by key.
   * @param key property name.
   * @return property value.
   */
  public String getProperty(final String key) {
    String result = null;

    try {
      EditableProperties editableProps = load(projectProps);
      result = editableProps.getProperty(key);
    } catch (IOException | NullPointerException ex) {
      //todo log
    }

    return result;
  }

  /**
   * Set property value by key.
   * @param key property key.
   * @param value property new value.
   */
  public void setProperty(final String key, final String value) {
    try {
      EditableProperties editableProps = load(projectProps);
      editableProps.setProperty(key, value);
      store(projectProps, editableProps);
    } catch (IOException | NullPointerException ex) {
      //todo log
    }
  }
}
