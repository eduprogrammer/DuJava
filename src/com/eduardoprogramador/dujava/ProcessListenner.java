/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava;

import java.io.InputStream;
import java.io.OutputStream;

public interface ProcessListenner {

    public void onInput(InputStream inputStream);

    public void onOutput(OutputStream outputStream);

}
