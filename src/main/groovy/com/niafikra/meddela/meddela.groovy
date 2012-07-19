package com.niafikra.meddela

import com.niafikra.meddela.auth.AuthenticationManager
import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator

/**
 * Author: Boniface Chacha <bonifacechacha@gmail.com>
 * Date: 7/17/12
 * Time: 2:09 PM
 */
class meddela {
    private Logger logger = Logger.getLogger(meddela.class)

    def authenticationManager

    def init() {
        initLog()
        authenticationManager = new AuthenticationManager()
        logger.info("meddella successsfully started ")
    }

    def initLog() {
        String pathToSettings = new StringBuilder(Controller.getAppPathExt())
                .append(File.separator).append("config")
                .append(File.separator).append("log4j.properties")
                .toString();
        PropertyConfigurator.configure(pathToSettings);
    }

    void stop() {
        logger.info("meddella successsfully stopped ")
    }

}
