package com.niafikra.meddela

import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator
import com.niafikra.meddela.services.ObjectDatabase

/**
 * This class provides a facade to  the services that
 * provide the meddella functionality.
 *
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/17/12
 * Time: 2:09 PM
 */

class meddela {
    private static Logger log = Logger.getLogger(meddela.class)
    static ObjectDatabase database  // objectdatabase can be used to manipulate meddela's local db
    static String appPath           // this path is set by the object that initializes medella

    /**
     * Initialize medella
     *
     * @param pathToApp is the path to where medella is running from
     * @return true if medella starts succesfully false otherwise
     */
    static boolean init(String pathToApp) {
        appPath = pathToApp

        try {
            initLog()
            database = new ObjectDatabase();

            log.info("meddella successsfully started ")
            return true
        } catch (Exception ex) {
            log.info("meddella failed to start", ex)
            return false
        }
    }

    static def initLog() {

        String pathToSettings = new StringBuilder(Controller.getAppPathExt())
                .append(File.separator).append("config")
                .append(File.separator).append("log4j.properties")
                .toString();

        PropertyConfigurator.configure(pathToSettings);

    }

    /**
     * Shutdown medella
     */
    static void stop() {
        database.close()
        log.info("meddella successsfully stopped ")
    }
}
