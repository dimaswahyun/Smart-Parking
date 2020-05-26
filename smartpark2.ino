#include <ESP8266WiFi.h>
#include <ESP8266WiFiAP.h>
#include <ESP8266WiFiGeneric.h>
#include <ESP8266WiFiMulti.h>
#include <ESP8266WiFiScan.h>
#include <ESP8266WiFiSTA.h>
#include <ESP8266WiFiType.h>
#include <PubSubClient.h>
#include <WiFiClient.h>
#include <WiFiClientSecure.h>
#include <WiFiServer.h>
#include <WiFiUdp.h>
#include <Wire.h>
#include <SPI.h>
const char* ssid     = "Redmi";
const char* password = "lolyahmm";
const char* mqtt_server = "192.168.43.147";
WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;
int nodeB = 16;

#define TRIGGER 5
#define ECHO    4
#define LED     2
 
// NodeMCU Pin D1 > TRIGGER | Pin D2 > ECHO
 
void setup() {
  
  Serial.begin (9600);
  pinMode(TRIGGER, OUTPUT);
  pinMode(ECHO, INPUT);
//  pinMode(BUILTIN_LED, OUTPUT);
   pinMode(LED, OUTPUT);

   digitalWrite(LED, LOW);
   WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  
  client.setServer(mqtt_server, 1883); 
}
void callback(char* topic, byte* payload, unsigned int length) {
  payload[length] = '\0';
  String s = String((char*)payload);
if (strcmp(topic, "status7") == 0) {
      String s = String((char*)payload);
      float f = s.toFloat();
      if(s== "on"){
        digitalWrite(LED, HIGH);
      Serial.println("status7 Detected");
    }else{
          digitalWrite(LED, LOW);
//        delay(10);
         
    }
 
  }

}

void reconnect() {
 
  while (!client.connected()) {

    if (client.connect("ESP8266ClientD")) {
      client.subscribe("status7");
      client.setCallback(callback);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}
 
void loop() {
   if (!client.connected()) {
    reconnect();
    client.subscribe("status7");
  }
  //delay(0);
  client.loop();
  
  long duration, distance;
  digitalWrite(TRIGGER, LOW);  
  delayMicroseconds(2); 
  
  digitalWrite(TRIGGER, HIGH);
  delayMicroseconds(10); 
  
  digitalWrite(TRIGGER, LOW);
  duration = pulseIn(ECHO, HIGH);
  distance = (duration/2) / 29.1;
  
  Serial.print(distance);
  Serial.println("Centimeter:");
  delay(100);

  long now = millis();
  if (now - lastMsg > 10) {
    lastMsg = now;
    ++value;
    snprintf (msg, 75, "%ld", distance);

// dtostrf(distance,3,0,msg);
    client.publish("node7", msg);
  }
}
