#include <LiquidCrystal.h>
#include <Adafruit_NeoPixel.h>
#include <stdlib.h>

#define REDLITE 3
#define GREENLITE 5
#define BLUELITE 6

#define PIN 46
#define LED_COUNT 45
#define START_LED 's'
#define END_LED 'e'
#define TOT_LEN 4 + (3*LED_COUNT)

#define LED_NUM 6

LiquidCrystal lcd(7, 8, 9, 10, 11, 12);
Adafruit_NeoPixel strip = Adafruit_NeoPixel(LED_COUNT, PIN, NEO_GRB + NEO_KHZ800);

int totLen = TOT_LEN;
char states[LED_COUNT][3];//2d array LED_COUNT x 3
char message[TOT_LEN];

int leds[LED_NUM] = {1, 2, 3, 4, 5, 6};

int globTotTime = 0, globTime = 0;

//parsing structs
typedef struct intStr{
  String str;
  int num;
} intStr;

typedef struct doubleStr{
  String str;
  int num;
} doubleStr;

typedef struct paramStr{
  String param;
  String str;
} paramStr;




void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);

  pinMode(REDLITE, OUTPUT);
  pinMode(GREENLITE, OUTPUT);
  pinMode(BLUELITE, OUTPUT);
  
  Serial.println("Arduino ready->");

  strip.begin();

  for(int i = 0; i < LED_NUM; i++) {//set stand alone leds to output
    pinMode(leds[i], OUTPUT);
  }
  
  for(int i = 0; i < LED_COUNT; i++) {//clear all states
    states[i][0] = 0;
    states[i][1] = 0;
    states[i][2] = 0;
  }

  //clearLEDs();
  strip.show();
}


void loop() {
  serialComms(';');
  updateTimer();
}

void serialComms(char delim) {
  String rawStr = "null";
  String commands[] = {"echo", "lcdBacklight", "lcdPrint", "clearLcd", "clearLeds", "setLedStrip", "countDown", "countDownTwo", "realGameTimer", "quit", "setTimer", "setLed"};
  if(Serial.available()) rawStr = Serial.readStringUntil(delim);
  else return;
  String command = parseCommand(rawStr);
  String params = parseParamStr(rawStr);
  if(command == commands[0]) {
    echo(params);
  }else if(command == commands[1]) {
    LCDBrightness(params);
  }else if(command == commands[2]) {
    LCDPrint(params);
  }else if(command == commands[3]) {
    lcd.clear();
    Serial.println("LCD cleared");
  }else if(command == commands[4]) {
    //clearLEDs();
  }else if(command == commands[5]) {
    setLED(params);
  }else if(command == commands[6]) {
    countDown(params);
  }else if(command == commands[7]) {
    countDownTwo(params);
  }else if(command == commands[8]) {
    gameTimer();
  }else if(command == commands[9]) {
    Serial.println("not in a loop");
  }else if(command == commands[10]) {
    realGameTimer(params);
  }else if(command == commands[11]) {
    setSingleLED(params);
  }else {
    Serial.println("No comand on the arduino for: "  + command + "\n\tWith parameters: "  + params);
  }
}










//parsing functinos

boolean checkNext(String str, int i) {
  if(str[i] == ';' || str[i] == '(') {
    return true;
  }else if(str[i] == ' ') {
    return checkNext(str, i++);
  }else{
    return false;
  }
}

String parseCommand(String &str) {
  String command = (str.length() > 0)?"":"null";
  for(int i = 0; i < str.length(); i++) {
    if(checkNext(str, i)) {
      return command;
    }else if(str[i] == ' ') {
      continue;
    }
    command += str[i];
  }
  return command;
}

String parseParamStr(String str) {
  int openP = 0, closeP = 0;
  String params = "null";
  for(int i = 0; i < str.length(); i++) {
    if(str[i] == '(' && openP == 0) {
      openP = i;
    }else if(str[i] == ')' && closeP == 0) {
      closeP = i;
    }
  }
  if(openP == closeP-1) return params;
  params = "";
  for(int i = openP+1; i < closeP; i++) {
    params+= str[i];
  }
  return params;
}

void lShiftCharArr(char arr[], int len) {
  char temp[len];
  for(int i = 0; i < len; i++){
    temp[i - 1 % len] = arr[i];
  }
  arr = temp;
}

struct paramStr parseNextParam(String str) {
  paramStr ret;
  int comma = str.length();
  for(int i = 0; i < str.length(); i++) {
    if(str[i] == ',') {
      comma = i+1;
      break;
    }
    ret.param[i] = str[i];
  }
  for(int i = comma, j = 0; i < str.length(); i++, j++) {
    ret.str[i] = str[i];
  }
  return ret;
}

struct intStr parseNextParamInt(String str) {
  intStr ret;
  int paramInt, comma = str.length();
  String trunc = "";
  char num[3] = {48, 48, 48};//3 digit integer because arduino is 255 max val. 48 is '0'

  for(int i = 0; i < str.length(); i++) {
    /*if(isdigit(str[i])) {
      lShiftCharArr(num, 3);
      num[2] = str[i];
      Serial.print("' " + str[i]);
    }*/
    if(str[i] == ',') {
      comma = i;
      break;
    }
  }

  for(int i = comma, j = 2; i >= 0; i--) {
    if(isdigit(str[i])) {
      num[j] = str[i];
      j--;
    }
  }

  /*char printStr[100];
  sprintf(printStr, "Num: %d, %d, %d", num[0], num[1], num[2]);
  Serial.println(printStr);*/

  //truncate str
  if(comma == 0) comma = str.length();//last param in a string
  for(int i = comma+1; i < str.length(); i++) {//remove leading comma and space
    if(str[i] == ' ' && i == comma+1) continue;
    trunc += str[i];
  }
  ret.str = trunc;
  ret.num = atoi(num);
  return ret;
}

struct doubleStr parseNextParamDouble(String str) {
  doubleStr ret;
  paramStr paramSt = parseNextParam(str);
  char charBuf[50];
  int len = 50;
  
  ret.str = paramSt.str;
  paramSt.param.toCharArray(charBuf, len);//convert to char buffer
  ret.num = strtod(charBuf, NULL);//str to double

  return ret;
}

int countParams(String str) {
  if(str == "null") return 0;
  int params = 1;
  for(int i = 0; i < str.length(); i++) {
    if(str[i] == ',') params++;
  }
  return params;
}



boolean checkQuit() {
  if(Serial.available()) {
    String raw = Serial.readStringUntil(';');
    String quitStr = parseCommand(raw);
    if(quitStr == "quit") {
      Serial.println("Recieved quit command");
      return true;
    }else {
      return false;
    }
  }
}







//functions that are called by the input handler

void echo(String str) {
  Serial.println("str: " + str);
}

void LCDBrightness(String params) {//ex 20, 30, 255
  int paramNum = countParams(params), paramInt;
  intStr paramIntStr;
  if(paramNum != 3) return;
  int ints[3];
  for(int i = 0; i < paramNum; i++) {
    paramIntStr = parseNextParamInt(params);
    params = paramIntStr.str;
    ints[i] = paramIntStr.num;
  }
  setBacklight(ints[0], ints[1], ints[2]);
}

void LCDPrint(String params) {//(x, y, Str)
  int paramNum = countParams(params), paramInt, ints[2];
  intStr paramIntStr;
  if(paramNum != 3) return;
  for(int i = 0; i < 2; i++) {
    paramIntStr = parseNextParamInt(params);
    params = paramIntStr.str;
    ints[i] = paramIntStr.num;
  }

  char printStr[100];
  sprintf(printStr, "Printing to LCD- x%d, y%d, str: ", ints[0], ints[1]);
  Serial.println(printStr + params);
  
  lcd.setCursor(ints[0]%16, ints[1]%2);//x<2, y<16
  lcd.print(params);
}



void clearLEDs() {
  for (int i=0; i<LED_COUNT; i++) {
    strip.setPixelColor(i, 0);
    states[i][0] = 0;
    states[i][1] = 0;
    states[i][2] = 0;
  }
}

void setLED(String params) {//ex:(5, 0xFF, 0xE1, 0xF3)

  int paramNum = countParams(params), paramInt;
  intStr paramIntStr;
  if(paramNum != 4) return;
  int ints[4];
  for(int i = 0; i < paramNum; i++) {
    if(checkQuit()) return;
    paramIntStr = parseNextParamInt(params);
    params = paramIntStr.str;
    ints[i] = paramIntStr.num;
  }

  int n = ints[0];
  int r = ints[1];
  int g = ints[2];
  int b = ints[3];
  
  strip.setPixelColor(n, r, g, b);
  states[n][0] = r;
  states[n][1] = g;
  states[n][2] = b;
  strip.show();//if lag, put this after a large number of setPixelColor() 's
}

void countDown(String params) {

  Serial.println("countDown: " + params);

  int paramNum = countParams(params);
  intStr seconds;
  if(paramNum != 1) return;
  seconds = parseNextParamInt(params);
  
  for(int j = seconds.num; j>0; j--) {
    if(checkQuit()) return;
    delay(1000);
    if(j<seconds.num && j>(seconds.num/3)*2) {
      for(int i=0; i<LED_COUNT; i++) {
        if(checkQuit()) return;
        char printStr[20];
         sprintf(printStr, "%d, 0, 255, 0", i);
         setLED(printStr);
      }
    }
     if(j<(seconds.num/3)*2 && j> seconds.num/3) {
       for(int i=0; i<LED_COUNT; i++) {
        if(checkQuit()) return;
         char printStr[20];
         sprintf(printStr, "%d, 0, 0, 255", i);
         setLED(printStr);
       }
     }
     if(j<seconds.num/3) {
       for(int i=0; i<LED_COUNT; i++) {
        if(checkQuit()) return;
         char printStr[20];
         sprintf(printStr, "%d, 255, 0, 0", i);
         setLED(printStr);
       }
     }
  }

  
}

void realGameTimer(String params) {//(time, total time)
  int paramNum = countParams(params);
  if(paramNum != 2) return;
  doubleStr seconds;
  double curTime, totalTime;
  seconds = parseNextParamDouble(params);
  curTime = seconds.num;
  seconds = parseNextParamDouble(seconds.str);
  totalTime = seconds.num;

  globTime = curTime;
  globTotTime = totalTime;
  updateTimer();
}

void countDownTwo(String params) {
  int paramNum = countParams(params);
  intStr seconds;
  if(paramNum != 1) return;
  seconds = parseNextParamInt(params);
  double sec = seconds.num;
  
  for(int LED = LED_COUNT; LED >= 0; LED--) {
    if(checkQuit()) return;
    //delay((seconds.num/LED_COUNT)*1000);
    states[LED][0] = 0x00;
    states[LED][1] = 0xFF;
    states[LED][2] = 0x00;
  }
  drawStates();
  for(int LED = LED_COUNT; LED >= 0; LED--) {
    if(checkQuit()) return;
    delay((sec/LED_COUNT)*1000);
    states[LED][0] = 0xFF;
    states[LED][1] = 0x00;
    states[LED][2] = 0x00;
    drawStates();
  }
}

void gameTimer() {
  double seconds = 2.5*60;
  double t1 = 12/seconds, t2 = (1.9*60)/seconds;
  for(int LED = LED_COUNT; LED >= 0; LED--) {
    if(checkQuit()) return;
    //delay((seconds/LED_COUNT)*1000);
    if(LED < t1*LED_COUNT) {
      states[LED][2] = 0xFF;
      states[LED][0] = 0x00;
      states[LED][1] = 0x00;
    }
    if(LED >= t1*LED_COUNT && LED < t2*LED_COUNT) {
      states[LED][1] = 0xFF;
      states[LED][0] = 0x00;
      states[LED][2] = 0x00;
    }
    if(LED >= t2*LED_COUNT) {
      states[LED][0] = 0xFF;
      states[LED][1] = 0xFF;
      states[LED][2] = 0x00;
    }
  }
  drawStates();
  for(int LED = 0; LED < LED_COUNT; LED++) {
    if(checkQuit()) return;
    delay((seconds/LED_COUNT)*1000);
    char printStr[20];
         sprintf(printStr, "%d, 255, 0, 0", LED);
         setLED(printStr);
  }
}

void drawStates() {
  for(int i = 0; i < LED_COUNT; i++) {
    if(checkQuit()) return;
    strip.setPixelColor(i+1, states[i][0], states[i][1], states[i][2]); //i+1 because I think that is where the first LED in indexed
  }
  strip.show();
}

void setSingleLED(String str) {//(LED#, LEDState(1, 0))
  intStr LED = parseNextParamInt(str);
  intStr LEDState = parseNextParamInt(LED.str);
  digitalWrite(leds[LED.num%LED_NUM], LEDState.num%2);
}


//dependance/other functinos

void updateTimer() {
  int t = (globTime / globTotTime) * LED_COUNT;
  for(int i = 0; i < LED_COUNT; i++) {
    if(i < t) {
      states[i][1] = 255;
    }else if(i >= t) {
      states[i][0] = 255;
    }
  }
  drawStates();
}

void setBacklight(int r, int g, int b) {
  r %= 256;
  g %= 256;
  b %= 256;
  char printStr[100];
  sprintf(printStr, "Setting LCD background to- R%d, G%d, B%d", r, g, b);
  Serial.println(printStr);
  
  analogWrite(REDLITE, r);
  analogWrite(GREENLITE, g);
  analogWrite(BLUELITE, b);
}
