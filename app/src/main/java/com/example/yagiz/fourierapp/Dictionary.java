package com.example.yagiz.fourierapp;

//This class translates commands in psuedo language to common language

import java.util.ArrayList;

public class Dictionary {

    public Dictionary() {
    }

    public String translateCodes(ArrayList<String> codes){

        char [] codesAsChar = new char[codes.size()+1];


        //The fist character is p symbolizing programming mode
        codesAsChar[0] = 'p';

        for (int i=0;i<codes.size();i++)
            codesAsChar[i+1]=translateString(codes.get(i));

        return new String(codesAsChar);
    }

    public char translateString(String pseudo){

        //initialize the character
        char pseudoAsChar = '@';

        if (pseudo.equals("While"))
            pseudoAsChar = '1';

        else if (pseudo.equals("Repeat2"))
            pseudoAsChar = '2';

        else if (pseudo.equals("Repeat3"))

            pseudoAsChar = '3';

        else if (pseudo.equals("Repeat4"))

            pseudoAsChar = '4';

        else if (pseudo.equals("Forever"))

            pseudoAsChar = '5';

        else if (pseudo.equals("Dark"))

            pseudoAsChar = 'a';

        else if (pseudo.equals("Light"))

            pseudoAsChar = 'b';

        else if (pseudo.equals("Near"))

            pseudoAsChar = 'c';

        else if (pseudo.equals("For"))

            pseudoAsChar = 'd';

        else if (pseudo.equals("Quiet"))

            pseudoAsChar = 'e';

        else if (pseudo.equals("Loud"))

            pseudoAsChar = 'f';

        else if (pseudo.equals("GoForward"))

            pseudoAsChar = 'k';

        else if (pseudo.equals("GoLeft"))

            pseudoAsChar = 'l';

        else if (pseudo.equals("GoBackward"))

            pseudoAsChar = 'm';

        else if (pseudo.equals("GoRight"))

            pseudoAsChar = 'n';

        else if (pseudo.equals("If"))

            pseudoAsChar = 'x';

        else if (pseudo.equals("StartOfBlock"))

            pseudoAsChar = '{';

        else if (pseudo.equals("EndOfBlock"))

            pseudoAsChar = '}';

        else if (pseudo.equals("StartStop"))

            pseudoAsChar = '-';

        else if (pseudo.equals("ManualControlMode"))

            pseudoAsChar = 'A';

        else if (pseudo.equals("LineFollowingMode"))

            pseudoAsChar = 'B';

        else if (pseudo.equals("ProgrammingMode"))

            pseudoAsChar = 'C';

        else if (pseudo.equals("RoombaMode"))

            pseudoAsChar = 'D';

        else if (pseudo.equals("GameMode"))

            pseudoAsChar = 'E';

        else if (pseudo.equals("OutOfMode"))

            pseudoAsChar = 'Z';


        return pseudoAsChar;
    }



}
