package Models.utilities;

import java.util.Scanner;

public abstract class MyScanner {
    private static Scanner scanner;

    private MyScanner(){
        
    }

    public static Scanner getScanner(){
        if(scanner == null){
            return scanner = new Scanner(System.in);
        }
        return scanner;
    }
    
}
