/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class Utilities {
    private Process process;
    private ProcessListenner processListenner;

    public void print(String msg) {
        System.out.println(msg);
    }

    public long getTimeInMilliseconds() {
        return System.currentTimeMillis();
    }

    public String getDate() {
        return new Date().toString();
    }

    public String getUserName() {
        return System.getProperty("user.name");
    }

    public String getUserDir() {
        return System.getProperty("user.dir");
    }

    public String getUserHomeDir() {
        return System.getProperty("user.home");
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }

    public String getOsVersion() {
        return System.getProperty("os.version");
    }

    public String getOsArch() {
        return System.getProperty("os.arch");
    }

    public void goToWebPage(String url) throws DuException {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException ex) {
            throw new DuException("Du Exception: Fail to browse URL");
        }
    }

    public void sendEmail(String email) throws DuException {
        try {
            Desktop.getDesktop().mail(URI.create("mailto:" + email));
        } catch (IOException ex) {
            throw new DuException("Du Exception: Fail to send email");
        }
    }

    public boolean startProcess (String[] commands) {
        try {
            Process process = Runtime.getRuntime().exec(commands);
            if(processListenner != null) {
                processListenner.onInput(process.getInputStream());
                processListenner.onOutput(process.getOutputStream());
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean stopProcess() {
        try {
            process.destroy();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void setProcessListenner(ProcessListenner processListenner) {
        this.processListenner = processListenner;
    }


}
