package run;

import utility.Collector;
import utility.Commander;


import java.io.IOException;
import java.util.Scanner;

public class Main {
    //execute_script C:\Users\Asus\IdeaProjects\prog5maven\src\main\java\run\script.txt

    public static void main(String[] args) throws IOException {
        String inPath = "C:\\Users\\Asus\\IdeaProjects\\prog5maven\\src\\main\\java\\json\\source_code.json";
        String outPath = "C:\\Users\\Asus\\IdeaProjects\\prog5maven\\src\\main\\java\\json\\out.json";
        //String in = "C:\\Users\\Asus\\IdeaProjects\\programming5\\src\\main\\java\\json\\"+args[0];
        //String out = "C:\\Users\\Asus\\IdeaProjects\\programming5\\src\\main\\java\\json\\"+args[1];
        Collector coll = new Collector(inPath,outPath);//args[0],args[1]
        Commander com = new Commander(coll);
        com.interactiveMod();

    }
}
