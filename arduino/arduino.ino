#include <LinkedList.h>

const int stopButton = 9 ; 
const int startButton = 7 ;

const int greenLed = 12 ;
const int redLed = 13 ;

const int MAX_LIMIT = 100 ;

const int micro = 5 ;


void setup() {
  Serial.begin(9600);

  //partie bouton start
  pinMode(startButton, INPUT_PULLUP);
  pinMode(greenLed, OUTPUT);

  //partie bouton stop
  pinMode(stopButton, INPUT_PULLUP);
  pinMode(redLed, OUTPUT);

  //pin micro;
  pinMode(micro, INPUT);

}

int microValue ;
bool startRec ;
bool stopRec ;

void loop() {
  microValue = analogRead(micro);
  eventListener();
  startRec ; 
  stopRec ; 
}


void eventListener(){
  int start = digitalRead(startButton);
  int stop = digitalRead(stopButton);

  //Serial.print("Etat bouton start : ");
  //Serial.println(start);

  //Serial.print("Etat bouton stop : ");
  //Serial.println(stop);

 

  if(start == 1 && stop == 0){
    digitalWrite(greenLed, HIGH);
    digitalWrite(redLed, LOW);

    // une fois que le bouton start est enfoncé on doit envoyer 1111 seule message précisant le début de l'enregistrement ; 
    if(!startRec){
      Serial.println(1111); // 1111 correspond au code de début d'enregistrement ; 
      startRec = true;
    }
    
    startRec = true;
    Serial.println(microValue);
    delay(500);
  }

  if(start == 0 && stop == 0){
    digitalWrite(greenLed, LOW);
    digitalWrite(redLed, HIGH);
    // une fois l'enregistrement fini, envoi du signal 1000 afin de préciser au serveur que l'envoi est fini et qu'il peut traiter les données ;
    Serial.println(1000);
    delay(500);
  }
  if(start == 0 && stop == 1){
    
    if(!stopRec){
      Serial.println(2222);
      stopRec = true ;
    }
    stopRec = true ; 
    delay(500);
  }

  /*
  if(startState == HIGH || start == HIGH && stop == LOW){
    // allumage des leds correspondantes ;
    digitalWrite(greenLed,HIGH);
    digitalWrite(redLed, LOW);
    startState = HIGH;
    start = HIGH ; 
    stop = LOW ;  
    delay(500);
    //Serial.println(microValue);

    //LinkedList<int>* data = new LinkedList<int>(); //  création liste contenant les données capté par le micro ;
    // data->add(microValue);

    // for(int i = 0 ; i < data->size() ; i++){
    //   Serial.println(data->get(i));
    // }
  }*/

}

/*
int sort(LinkedList<int>* listeATrier){
  int mini = 200 ;
  int maxi = 100 ;
  for(int i = 0 ; i < listeATrier->size() ; i++){
    if(listeATrier->get(i) < mini){
      mini = listeATrier->get(i);
    }
    if(listeATrier->get(i) > maxi){
      maxi = listeATrier->get(i);
    }
  }
  return mini, maxi;
}*/

/*
void startRec(bool state){


  LinkedList<int>* data = new LinkedList<int>();

  if(state){
    data->add(microValue);
    delay(500);
  }

  else{
    for(int i = 0 ; i < data->size() ; i++){
      Serial.println(data->get(i));
    }
    delay(500);
    Serial.println("fin enregistrement");
  }

}*/





