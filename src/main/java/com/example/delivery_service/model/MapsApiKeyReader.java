package com.example.delivery_service.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MapsApiKeyReader {
     public static String readKey(){
         File file = new File("C:\\Users\\David\\Documents\\GOOGLE_MAP_API_KEY.txt");

         try {
             BufferedReader br = new BufferedReader(new FileReader(file));
             return br.readLine();
         }
         catch (Exception ex){
             return "";
         }
     }
}
