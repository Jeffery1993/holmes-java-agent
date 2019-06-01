package com.jeffery.holmes.server.consts;

import java.io.File;

/**
 * Constants for path.
 */
public interface PathConsts {

    String USER_HOME = System.getProperty("user.home");
    String DATA_DIR = USER_HOME + File.separator + "holmes" + File.separator + "data";

}
