package com.example.sanat.dressexchangev03;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//Create ASYNC Inner Class
public class ServerComm extends AsyncTask<String, String, String> {

    //ASYNC INNER CLASS
    @Override
    protected void onPreExecute() {
        // Runs on UI thread- Any code you wants
        // to execute before web service call. Put it here.
        // Eg show progress dialog
        Log.d("Request Object", "PreExecute Start");
    }
    //ASYNC INNER CLASS METHODS
    @Override
    protected String doInBackground(String... params) {
        // Runs in background thread
        Log.d("Request Object", "doInBackground Start");
        String result = "";//your web service request;
        Log.d("Request Object", "Begin connection setup");
        try {
            InetAddress serverAddress = InetAddress.getByName("192.168.1.70");
            Socket connect = new Socket(serverAddress, 9876);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(connect.getOutputStream())), true);
            DataOutputStream dataout = new DataOutputStream(new BufferedOutputStream(connect.getOutputStream()));
            //OutputStreamWriter stream = new OutputStreamWriter(connect.getOutputStream());
            Log.d("Request Object", "Sending...");

            switch (params[0])
            {
                case "signUp":
                    out.println(params[0] + ";" + params[1]  + ";" + params[2] + ";" + params[3]);
                    break;
                case "login":
                    out.println(params[0] + ";" + params[1] + ";" + params[2]);
                    break;
                case "liked":
                    out.println(params[0] + ";" + params[1]);
                case "refresh":
                    out.println(params[0] + ";" + params[1]);
                    break;
                case "add":
                    //stream.write(lol, 0, lol.length());
                    //stream.write(lol, 0, lol.length());
                    out.println(params[0] + ";" + params[1] + ";" + params[2] + ";" + params[3] + ";" + params[4]);

                    //dataout.writeChars(lol);

                    break;
                case "likes":
                    out.println(params[0] + ";" + params[1] + ";" + params[2]);
                case "pic":
                    out.println(params[0]);
                case "mine":
                    out.println(params[0] + ";" + params[1]);
                default:
                    out.println("Nothing");
                    break;
            }
            dataout.flush();
            Log.d("Request Object", "Receiving...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            result = reader.readLine();
            Log.d("Request Object", "Closing Readers and Writers");
            reader.close();
            out.close();
            connect.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    //ASYNC INNER CLASS METHODS
    @Override
    protected void onProgressUpdate(String... values) {
        Log.d("Request Object", "Progress Update Start");
        super.onProgressUpdate(values);
    }
    //ASYNC INNER CLASS METHODS
    @Override
    protected void onPostExecute(String resp) {
        Log.d("Request Object", "PostExecute Start");
        // runs in UI thread - You may do what you want with response
        // Eg Cancel progress dialog - Use result
    }

}
//End Details of new ASYNC class