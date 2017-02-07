#include LiquidCrystal.h
#include stdlib.h

#define REDLITE 3
#define GREENLITE 5
#define BLUELITE 6

LiquidCrystal lcd(7, 8, 9, 10, 11, 12);

void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);

  pinMode(REDLITE, OUTPUT);
  pinMode(GREENLITE, OUTPUT);
  pinMode(BLUELITE, OUTPUT);
  
  Serial.println(Arduino ready-);
}

void loop() {
  serialComms(';');
}

void serialComms(char delim) {
  String rawStr = null;
  String commands[] = {echo, lcdBacklight, lcdPrint, clearLcd};
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
    Serial.println(LCD cleared);
  }
  else {
    Serial.println(No comand on the arduino for  + command + ntWith parameters  + params);
  }
}





parsing structs
typedef struct intStr{
  String str;
  int num;
} intStr;







parsing functinos

boolean checkNext(String str, int i) {
  if(str[i] == ';'  str[i] == '(') {
    return true;
  }else if(str[i] == ' ') {
    return checkNext(str, i++);
  }else{
    return false;
  }
}

String parseCommand(String &str) {
  String command = (str.length()  0)null;
  for(int i = 0; i  str.length(); i++) {
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
  String params = null;
  for(int i = 0; i  str.length(); i++) {
    if(str[i] == '(' && openP == 0) {
      openP = i;
    }else if(str[i] == ')' && closeP == 0) {
      closeP = i;
    }
  }
  if(openP == closeP-1) return params;
  params = ;
  for(int i = openP+1; i  closeP; i++) {
    params+= str[i];
  }
  return params;
}

struct intStr parseNextParamInt(String str) {
  intStr ret;
  int paramInt, comma = 0;
  String trunc = ;
  char num[3] = {48, 48, 48};3 digit integer because arduino is 255 max val. 48 is '0'
  for(int i = 0, j = 0; i  str.length(); i++) {
    if(str[i] == ',') {
      comma = i;
      break;
    }else if(str[i] != ' ') {
      num[j] = str[i];
      j++;
    }
  }
  if(comma == 0) comma = str.length();last param in a string
  for(int i = comma+1; i  str.length(); i++) {remove leading comma and space
    if(str[i] == ' ' && i == comma+1) continue;
    trunc += str[i];
  }
  ret.str = trunc;
  ret.num = atoi(num);
  return ret;
}

int countParams(String str) {
  if(str == null) return 0;
  int params = 1;
  for(int i = 0; i  str.length(); i++) {
    if(str[i] == ',') params++;
  }
  return params;
}









functions that are called by the input handler

void echo(String str) {
  Serial.println(str);
}

void LCDBrightness(String params) {ex 20, 30, 255
  int paramNum = countParams(params), paramInt;
  intStr paramIntStr;
  if(paramNum != 3) return;
  int ints[3];
  for(int i = 0; i  paramNum; i++) {
    paramIntStr = parseNextParamInt(params);
    params = paramIntStr.str;
    ints[i] = paramIntStr.num;
  }
  setBacklight(ints[0], ints[1], ints[2]);
}

void LCDPrint(String params) {(x, y, Str)
  int paramNum = countParams(params), paramInt, ints[2];
  intStr paramIntStr;
  if(paramNum != 3) return;
  for(int i = 0; i  2; i++) {
    paramIntStr = parseNextParamInt(params);
    params = paramIntStr.str;
    ints[i] = paramIntStr.num;
  }

  char printStr[100];
  sprintf(printStr, Printing to LCD- x%d, y%d, str, ints[0], ints[1]);
  Serial.println(printStr + params);
  
  lcd.setCursor(ints[0], ints[1]);x2, y16
  lcd.print(params);
}





other functinos
void setBacklight(int r, int g, int b) {
  char printStr[100];
  sprintf(printStr, Setting LCD background to- R%d, G%d, B%d, r, g, b);
  Serial.println(printStr);
  
  analogWrite(REDLITE, r);
  analogWrite(GREENLITE, g);
  analogWrite(BLUELITE, b);
}

