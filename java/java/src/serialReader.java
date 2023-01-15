import com.fazecast.jSerialComm.SerialPort;
import java.io.*;
import java.util.*;

public class serialReader{

    protected final static int ARDUINOUP = 1000 ;

    protected final static int STOP = 2222 ;

    protected final static int START = 1111 ;

    protected LinkedList<Integer> data ;

    protected final static String portName = "COM5" ;

    protected static boolean startRec = false;

    protected static boolean stopRec = false ;


    protected SerialPort comPort ;
    public serialReader(){
        this.data = new LinkedList<Integer>(); // création liste qui va contenir les données recues
        this.comPort = SerialPort.getCommPort(portName);
    }


    public void start(){
        // permet de lire les données arrivant sur le port série COM5
        this.comPort.openPort(); // ouvre le port série
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
                    sendArray();
                    break;
                }
            }
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }
        inputData.close();
        comPort.closePort();
    }


    public LinkedList<Integer> sendArray(){
        return this.data ;
    }
}