package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessTool {
    public static Map<Integer,String> getAllSystemPrecessPIDAndNames(){
        Map<Integer,String> map=new HashMap<>();
        try{
            String[] strs=null;
            String processName;
            int pid;
            String execresult=execCommandOutputToString("tasklist /NH /FO CSV");
            String[] lines=execresult.split("@");
            for(int i=0;i<lines.length;i++){
                strs=lines[i].split(",");
                pid=Integer.parseInt(strs[1].replace("\"",""));
                processName=strs[0].replace("\"","");
                map.put(pid,processName);
            }

        }catch (Exception ex){
           ex.printStackTrace();
        }
        return map;
    }
    public static boolean hasProcessRunByName(String name){
        Map<Integer,String> map= ProcessTool.getAllSystemPrecessPIDAndNames();
        boolean hasProcess=  map.values().stream().anyMatch((a)->{
            int index= a.toUpperCase().indexOf(name.toUpperCase());
            if(index>=0)
                return true;
            return false;
        });
        return hasProcess;
    }
    public static String execCommandOutputToString(String command){
        try{
            Process process= Runtime.getRuntime().
                    exec("cmd /C "+command);
            BufferedReader bufferedReader=new BufferedReader(
                    new InputStreamReader(process.getInputStream(),"GBK"));
            String str=null;
            StringBuffer sb=new StringBuffer();
            while ((str=bufferedReader.readLine())!=null){
                sb.append(str+"@");
            }
            return sb.toString();
        }catch (Exception ex){
            return null;
        }
    }
}
