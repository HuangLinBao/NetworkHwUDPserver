package com.net;
import java.io.*;
import java.net.*;
import java.util.*;

public class Main {

    public static void main(String[] args)throws Exception  {
        String courseName;//course name that will be sent to client
        //the following code is just reading data from the file and storing them in a HashMap
        File file = new File("/home/ayyy/Courses.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        HashMap<String,String> map = new HashMap<String,String >();
        String line = null;
        while ((line = br.readLine()) != null) {

            // split the line by :
            String[] parts = line.split("->");

            // first part is name, second is number
            String number = parts[0].trim();
            String name = parts[1].trim();

            // put name, number in HashMap if they are
            // not empty
            if (!name.equals("") && !number.equals(""))
                map.put(number, name);
        }
        if (br != null) {
            br.close();
        }
        //HashMap storation ends here
        DatagramSocket serverSocket = new DatagramSocket(8080);//UDP server connected through port 8080

        byte[] receiveData = new byte[1024];//packet arrays
        byte[] sendData ;

        while(true)
        {

            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);//UDP server receives packet
            serverSocket.receive(receivePacket);
            String courseNumber = new String(receivePacket.getData());
            courseNumber = courseNumber.trim();//create the string to store the packet in
            if(map.containsKey(courseNumber)){
                courseName = map.get(courseNumber);//check if the course is there
            }
            else {
                courseName = "404 Not found";// if the course wasn't there
            }
            InetAddress IPAddress = receivePacket.getAddress();//get the IP of the client

            int port = receivePacket.getPort();//get the port

            String capitalizedSentence = courseName.toUpperCase();//capitalize output

            sendData = capitalizedSentence.getBytes();//turn string into packet

            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress,
                            port);// send packet

            serverSocket.send(sendPacket);
        }


    }
}
