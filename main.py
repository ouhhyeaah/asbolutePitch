import os
import pymysql

username = "admin" ;
password = "admin" ;

def createDatabase():
    serv = pymysql.connect(
        host="localhost",
        user=username,
        passwd=password,
    )
    serv.query("CREATE DATABASE sae ;");
    db = pymysql.connect(
        host="localhost",
        user=username,
        passwd=password,
        db="sae",
    )
    reqPartitions ="""
            CREATE TABLE IF NOT EXISTS `partitions` (
                `i` int(10) NOT NULL AUTO_INCREMENT,
                `notes` varchar(50) NOT NULL,
                `duree` int(50) NOT NULL,
                `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                PRIMARY KEY (`i`)
                ) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
        """

    db.query(reqPartitions);

    reqUsers = """
        CREATE TABLE IF NOT EXISTS `users` (
            `login` varchar(50) NOT NULL,
            `password` varchar(100) NOT NULL
        ) ENGINE=MyISAM DEFAULT CHARSET=latin1;
    """

    db.query(reqUsers);
    print("Base de donnée crée");

def dropDatabase():
    serv = pymysql.connect(
        host="localhost",
        user="admin",
        passwd="admin",
    )
    serv.query("DROP DATABASE sae ;");
    print("Base de donnée supprimé");



def afficheMenu(choixActions):
    print ("Choix possibles :")
    for ch  in choixActions:
        print ("{} : {}".format(choixActions.index(ch)+1, ch[0] ))
    print ("{} : {}".format( len(choixActions)+1 , "Quitter" ))



if(__name__ == "__main__"):
    listeChoix = [ 
             ("Créer la base ",createDatabase),
             ("Supprimer la base ",dropDatabase),
            ]

    while True :
        afficheMenu(listeChoix)
        try :
            choix = int(input("Votre Choix ? : "))
            if ( choix == len(listeChoix) + 1 ):
                    break
            elif 1 <= choix and choix <= len(listeChoix):
                label, fct = listeChoix[choix-1] # recuperer adresse fct associee
                fct()
            else :
                print ("*** Choix non valide, recommencez!")
        except IndexError as e:
            print ('*** Choix non valide, recommencez!')
        except ValueError as e :
            print ('*** Entrez un entier SVP')



