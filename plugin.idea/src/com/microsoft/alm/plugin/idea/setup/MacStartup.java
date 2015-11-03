// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.idea.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This class runs a Mac application bundle that will install the protocol handler on a Mac
 */
public class MacStartup {
    private static final Logger logger = LoggerFactory.getLogger(MacStartup.class);
    protected static final String VSOI_APP_PLIST = "/vsoi.app/";

    public static void startup() {
        try {
            // find location of vsoi.app
            final String vsoiAppPath = MacStartup.class.getResource(VSOI_APP_PLIST).getPath();
            // run vsoi.app which will register the vsoi protocol handler for a Mac
            Runtime.getRuntime().exec(new String[]{"open", vsoiAppPath});
        } catch (RuntimeException e) {
           logger.warn("A RuntimeException was caught while trying to execute vsoi.app: {}", e.getMessage());
        } catch (IOException e) {
            logger.warn("An IOException was caught while trying to execute vsoi.app: {}", e.getMessage());
        } catch (Exception e) {
            logger.warn("An Exception was caught while trying to find and execute vsoi.app: {}", e.getMessage());
        }
    }
}