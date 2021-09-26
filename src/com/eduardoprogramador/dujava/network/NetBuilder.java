/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava.network;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetBuilder {

    public static interface OnGetResponseListener {
        public void onGetResponse(String lines);
    }

    public static interface OnPostResponseListener {
        public void onPostResponse(String lines);
    }

    public static interface OnSocketRawListener {
        public void onRawInput(Socket socket, InputStream inputStream);

        public void onRawOutput(Socket socket, OutputStream outputStream);
    }

    public static interface OnServerListener {
        public void onWait(String ip, int port);

        public void onClientArrived(Socket client);

        public void onJobStart(InputStream inputStream, OutputStream outputStream, Socket client);

        public void onFileReceived();
    }
}
