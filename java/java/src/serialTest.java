import com.fazecast.jSerialComm.SerialPort;
import java.io.*;
import java.util.*;

public class serialTest{


    public static void main(String[] args) {

        int ARDUINOUP = 1000 ;

        int STOP = 2222 ;

        int START = 1111 ;

        boolean startRec = false;

        boolean stopRec = false ;

        LinkedList<Integer> data = new LinkedList<>() ; // LinkedList contenant les données recue ;
        // permet de lire les données arrivant sur le port série COM5
        SerialPort comPort = SerialPort.getCommPort("COM5");


        comPort.openPort(); // ouvre le port série
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); // initialise le timeout

        InputStream in = comPort.getInputStream(); // récupère le flux d'entrée

        Scanner inputData = new Scanner(in); // crée un scanner pour lire le flux d'entrée


        try{
            while(inputData.hasNextInt()) { // tant qu'il y a des lignes à lire
                Integer line = inputData.nextInt(); // lit la ligne
                //System.out.println(line);

                if(line == START){
                    startRec = true ;
                    System.out.println("Début enregistrement");

                }
                if(line == ARDUINOUP){
                    System.out.println("Pas de donnée envoyé");
                }

                if(line == STOP){
                    System.out.println("Fin enregistrement");
                    System.out.println("Durée enregistrement = " + ((data.size()-2)*0.5) + " secondes ") ;
                    stopRec = true;
                }
                if(startRec){
                    data.add(line); // ajoute la ligne à la liste
                }
                if(stopRec){
                    javaDatabase db = new javaDatabase(data);
                    break;
                }
            }
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }
        System.out.println(data);
        comPort.closePort();
        inputData.close();
    }
}