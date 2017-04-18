/*
 * License.
 */
package ru.sbobrov85.uploadfilesbutton.classes;

import javax.annotation.Resource;

public final class StateHelper {

    @Resource
    private static final String
        ICON_OFF_16 = "/ru/sbobrov85/uploadfilesbutton/upload-files-off-16.png";

    @Resource
    private static final String
        ICON_16 = "/ru/sbobrov85/uploadfilesbutton/upload-files-16.png";

    public static final String MANUALLY_STATE = "MANUALLY";

    public static final String ON_SAVE_STATE = "ON_SAVE";

    public final static boolean isEnabled() {
        boolean isEnabled = false;

        String runAs = new ContextProperties().getProperty("run.as");
        if ("REMOTE".equals(runAs)) {
            isEnabled = true;
        }

        return isEnabled;
    }

    public final static boolean check() {
        return ON_SAVE_STATE.equals(
            new ContextProperties().getProperty("remote.upload")
        );
    }

    public final static String toggle(String state) {
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

    public final static String getIcon() {
        return check() ? ICON_16 : ICON_OFF_16;
    }
}
