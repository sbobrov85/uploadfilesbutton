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

import javax.annotation.Resource;

/**
 * Action state helper.
 */
public final class StateHelper {

  /**
   * Contains const for resource folder base path.
   */
  private static final String
    RESOURCE_FOLDER = "/ru/sbobrov85/uploadfilesbutton/resources/";

  /**
   * Contains filename for off image.
   */
  @Resource
  private static final String ICON_OFF_16 = "upload-files-off.png";

  /**
   * Contains filename for base image.
   */
  @Resource
  private static final String ICON_16 = "upload-files.png";

  /**
   * Contains const for "manually" config string.
   */
  public static final String MANUALLY_STATE = "MANUALLY";

  /**
   * Contains const for "on save" config string.
   */
  public static final String ON_SAVE_STATE = "ON_SAVE";

  /**
   * Hide default constructor.
   */
  private StateHelper() {
  }

  /**
   * Checking if remote settings is possible.
   * @return true if possible, false another.
   */
  public static boolean isEnabled() {
    boolean isEnabled = false;

    String runAs = new ContextProperties().getProperty("run.as");
    if (runAs != null && "REMOTE".equals(runAs)) {
      isEnabled = true;
    }

    return isEnabled;
  }

  /**
   * Checking context property state.
   * @return true if on save, false another.
   */
  public static boolean check() {
    return ON_SAVE_STATE.equals(
        new ContextProperties().getProperty("remote.upload")
    );
  }

  /**
   * Toggle context state.
   * @param state state string from config.
   * @return toggled config string state.
   */
  public static String toggle(final String state) {
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

  /**
   * Get icon path for action.
   * @return icon path.
   */
  public static String getIcon() {
    String iconBaseName;

    if (check()) {
      iconBaseName = ICON_16;
    } else {
      iconBaseName = ICON_OFF_16;
    }

    return iconBaseName + RESOURCE_FOLDER;
  }
}
