import java.util.LinkedList;



public class Main {


    public static void main(String[] args){

        serialReader portSerie = new serialReader(); // création d'un objet lisant sur le port série ;

        portSerie.start();  // lancement de la lecture ;

        LinkedList<Integer> array =  portSerie.sendArray(); // récuperration de la liste contant les données recu sur le port série ;

        javaDatabase db = new javaDatabase(array); //  objet permettant l'interraction avec la base de données ;

        System.out.println("Liste valeur non filtrées : " + db.data);

        System.out.println("Liste des valeur filtrées  : " + db.filterData()) ;

        System.out.println("Valeur Minimale = "+db.findMaxiAndMini()[0]); // le mini est le premier du tableau retourné
        System.out.println("Valeur Maximale = "+db.findMaxiAndMini()[1]); // le maxi est le deuxieme


        //for(int i = 1 ; i < 9 ; i++)System.out.println("Echelon "  + i  + " : "+db.scaleData()[i-1]);

        // insertion des données dans la base de données ;
        db.insertData(db.transformDataIntoString());

    }
}
