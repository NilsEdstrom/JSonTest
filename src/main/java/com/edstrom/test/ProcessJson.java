package com.edstrom.test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProcessJson {
    protected static String JSON_STRING;
    protected DocumentContext docContext;


    static {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("test.json")) {
            if (is == null) {
                System.out.println("Unable to find file test.json");
                System.exit(1);
            }
            try {
                try (InputStreamReader isr = new InputStreamReader(is);
                     BufferedReader reader = new BufferedReader(isr)) {
                    JSON_STRING = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProcessJson() {
        docContext = JsonPath.parse(JSON_STRING);
    }

    void retrieveStackViewItem(final String path) {
//        Filter filter = Filter.filter(Criteria.where("class").eq("StackView"));
        Object array =  docContext.read(path);
        if (array == null) return;
        if (array instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray)array;
            for (int  i = 0; i < jsonArray.size(); i++) {
                Object theMap = jsonArray.get(i);
                if (theMap instanceof Map) {
                    Map<String, Object> valueMap = (Map)theMap;
                    StringBuffer sb = new StringBuffer();
                    sb.append('{');
                    for (String key : valueMap.keySet()) {
                        if (sb.length() > 1) {
                            sb.append(',');
                        }
                        sb.append('"');
                        sb.append(key);
                        sb.append('"');
                        sb.append(':');
                        sb.append(" \"");
                        sb.append(valueMap.get(key));
                        sb.append('"');
                    }
                    sb.append('}');
                    System.out.println(sb.toString());
                }
            }
        } else {
            System.out.println(array);
        }

     }

     /*
     I am sure there must exist a filter that allows me to select items that have an array element 'container',
     but I was not able find one.
      */
     void retrieveClassNames(final String path) {
         Object array =  docContext.read(path);
         if (array == null) return;
         if (array instanceof JSONArray) {
             JSONArray jsonArray = (JSONArray)array;
             for (int  i = 0; i < jsonArray.size(); i++) {
                 Object theMap = jsonArray.get(i);
                 if (theMap instanceof JSONArray) {
                     JSONArray arr = (JSONArray)theMap;
                     for (int j = 0; j < arr.size(); j++) {
                         Object obj = arr.get(j);
                         if (obj instanceof String) {
                             String item = (String)obj;
                             if (item.equals("container")) {
                                 System.out.println(arr);
                             }
                         }
                     }
                  }
             }
         } else {
             System.out.println(array);
         }
     }

     void retrieveVideoMode(String path) {
         Object array =  docContext.read(path);
         if (array == null) return;
         if (array instanceof JSONArray) {
             JSONArray jsonArray = (JSONArray)array;
             System.out.println(jsonArray.get(0));
         }

     }


     public static void main(String[] args) {
        ProcessJson processJson = new ProcessJson();
         Scanner in = new Scanner(System.in);
         System.out.println("Please enter a command");
         String s = null;
         while((s = in.nextLine()) != null) {
             System.out.println("You entered string " + s);
             if (s.contains("StackView")) {
                 processJson.retrieveStackViewItem("$..subviews[?(@['class'] == 'StackView')]");
             } else if (s.contains("container")) {
                 processJson.retrieveClassNames("$..classNames");
             } else if (s.contains("videoMode")) {
                 processJson.retrieveVideoMode("$..control[?(@.identifier == 'videoMode')]");
             }
         }

     }


}
