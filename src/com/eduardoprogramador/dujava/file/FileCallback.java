/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava.file;

public interface FileCallback {

    public void onReadingZipEntries(String name, long time, long size, int position, int total);

    public void onZipExtracting();

    public void onZipExtractingProgress(int position, int total);

    public void onZipExtractingFinish();

    public void onZipCreating(int position, int total);
}
