/*
 * Copyright 2021. Eduardo Programador
 * All rights reserved.
 * www.eduardoprogramador.com
 * consultoria@eduardoprogramador.com
 *
 * */

package com.eduardoprogramador.dujava.network;

import com.eduardoprogramador.dujava.DuException;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;

public class Internet {
    private NetBuilder.OnGetResponseListener onGetResponseListener;
    private NetBuilder.OnPostResponseListener onPostResponseListener;
    private NetBuilder.OnServerListener onServerListener;
    private NetBuilder.OnSocketRawListener onSocketRawListener;
    private final int DISPLAY = 1;
    private final int DISPLAY_NAME = 0;
    private final int MAC = 2;

    private Internet() {

    }

    public static Internet getInstance() {
        return new Internet();
    }

    public boolean isInternetEnabled() {
        try {
            URL url = new URL("http://example.com");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.connect();
            httpURLConnection.disconnect();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isPortOpen(String ip,int port) {

        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.setSoTimeout(3000);
            socket.connect(socketAddress);
            socket.close();

            return true;

        } catch (IOException ex) {
            return false;
        }
    }

    public boolean getRequest(String www, boolean isHttps, int port, String path) {

        if(isHttps) {

            try {

                URL url = new URL("https",www,port,path);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {

                    if(onGetResponseListener != null)
                        onGetResponseListener.onGetResponse(line);

                }

                bufferedReader.close();
                httpsURLConnection.disconnect();
                return true;

            } catch (Exception ex) {
                return false;
            }

        } else {

            try {

                URL url = new URL("http",www,port,path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {

                    if(onGetResponseListener != null)
                        onGetResponseListener.onGetResponse(line);
                }

                bufferedReader.close();
                httpURLConnection.disconnect();
                return true;

            } catch (Exception ex) {
                return false;
            }


        }


    }

    public boolean postRequest(String www, boolean isHttps, int port, String path, String[] keys, String[] values) {

        if(isHttps) {

            try {

                URL url = new URL("https",www,port,path);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset= UTF-8");

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < keys.length; i++) {
                    stringBuilder.append(URLEncoder.encode(keys[i],"UTF-8") + "=" + URLEncoder.encode(values[i],"UTF-8"));
                    if(i < (keys.length - 1)) {
                        stringBuilder.append("&");
                    }
                }

                byte[] out = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

                httpsURLConnection.setFixedLengthStreamingMode(out.length);
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                outputStream.write(out);
                outputStream.flush();

                httpsURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {

                    if(onPostResponseListener != null)
                        onPostResponseListener.onPostResponse(line);

                }

                bufferedReader.close();
                httpsURLConnection.disconnect();
                return true;

            } catch (Exception ex) {
                return false;
            }

        } else {

            try {

                URL url = new URL("http",www,port,path);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset= UTF-8");

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < keys.length; i++) {
                    stringBuilder.append(URLEncoder.encode(keys[i],"UTF-8") + "=" + URLEncoder.encode(values[i],"UTF-8"));
                    if(i < (keys.length - 1)) {
                        stringBuilder.append("&");
                    }
                }

                byte[] out = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);

                httpURLConnection.setFixedLengthStreamingMode(out.length);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(out);
                outputStream.flush();

                httpURLConnection.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {

                    if(onPostResponseListener != null)
                        onPostResponseListener.onPostResponse(line);


                }

                bufferedReader.close();
                httpURLConnection.disconnect();
                return true;

            } catch (Exception ex) {
                return false;
            }


        }



    }

    public boolean rawRequest(String host, int port) {

        try {
            Socket socket = new Socket();
            socket.setSoTimeout(3000);
            SocketAddress socketAddress = new InetSocketAddress(host,port);
            socket.connect(socketAddress);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            if(onSocketRawListener != null) {
                onSocketRawListener.onRawInput(socket,inputStream);
                onSocketRawListener.onRawOutput(socket,outputStream);
            }


            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    public boolean sendFile(String ip, int port, String sourceFilePath) {

        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            FileInputStream fileInputStream = new FileInputStream(sourceFilePath);
            byte[] bytes = new byte[1];
            int count = 0;

            while ((count = fileInputStream.read(bytes)) > 0) {
                dataOutputStream.write(bytes);
                dataOutputStream.flush();
            }

            fileInputStream.close();
            socket.close();

            return true;

        } catch (IOException ex) {
            return false;
        }

    }

    public boolean receiveFile(String serverIp, int port, String outputPath) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            SocketAddress socketAddress = new InetSocketAddress(serverIp, port);
            serverSocket.bind(socketAddress);

            if(onServerListener != null)
                onServerListener.onWait(serverIp,port);

            Socket socket = serverSocket.accept();

            if(onServerListener != null)
                onServerListener.onClientArrived(socket);

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
            byte[] bytes = new byte[1];
            int count = 0;

            while (true) {
                while ((count = dataInputStream.read(bytes)) > 0) {
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                }

                fileOutputStream.close();
                break;
            }

            socket.close();
            serverSocket.close();

            if(onServerListener != null)
                onServerListener.onFileReceived();

            return true;


        } catch (IOException ex) {
            return false;
        }


    }

    public boolean startServer(String serverIp, int port) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(serverIp, port));

            if(onServerListener != null)
                onServerListener.onWait(serverIp,port);

            Socket socket = serverSocket.accept();

            if(onServerListener != null) {
                onServerListener.onClientArrived(socket);
                onServerListener.onJobStart(socket.getInputStream(),socket.getOutputStream(),socket);
            }


            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    public void setOnGetResponseListener(NetBuilder.OnGetResponseListener listener) {
        this.onGetResponseListener = listener;
    }

    public void setOnPostResponseListener(NetBuilder.OnPostResponseListener listener) {
        this.onPostResponseListener = listener;
    }

    public String startDns(String srcHost) throws DuException {
        try {
            return InetAddress.getByName(srcHost).getHostAddress();
        } catch (Exception ex) {
            throw new DuException("Du Exception: Fail to resolve address: " + srcHost);
        }
    }

    public ArrayList<ArrayList> getNetworkParams() throws DuException {
        try {

            ArrayList<ArrayList> res = new ArrayList<>();
            ArrayList<String> subRes = new ArrayList<>();
            ArrayList<String> ips = new ArrayList<>();

            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                String displayName = networkInterface.getDisplayName();
                String name = networkInterface.getName();
                subRes.add(displayName);
                subRes.add(name);

                byte[] bytes = networkInterface.getHardwareAddress();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    int n = bytes[i] & 0xff;
                    if(i == 0)
                        stringBuilder.append(Integer.toHexString(n));
                    else
                        stringBuilder.append(":" + Integer.toHexString(n));
                }

                String mac = stringBuilder.toString();
                subRes.add(mac);

                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();

                    String hostAddress = inetAddress.getHostAddress();
                    String hostName = inetAddress.getHostName();
                    ips.add(hostAddress);
                    ips.add(hostName);

                }

                res.add(subRes);
                res.add(ips);
            }

            return res;

        } catch (Exception ex) {
            throw new DuException("Du Exception: Fail to retrive networking hardware information");
        }
    }

    public void setOnSocketRawListener(NetBuilder.OnSocketRawListener listener) {
        this.onSocketRawListener = listener;
    }

    public void setOnServerListener(NetBuilder.OnServerListener listener) {
        this.onServerListener = listener;
    }

}
