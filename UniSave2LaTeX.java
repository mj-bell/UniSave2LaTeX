import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/*
* The contents of this file are subject to the LGPL License, Version 3.0.
*
* Copyright (C) 2013, Michael Bell, Newcastle University
*
* This program is free software: you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or (at your
* option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
* FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
* for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see http://www.gnu.org/licenses/.
*/
public class UniSave2LaTeX
{

    /**
     * Helper method to separate line identifier from the text
     */
    public static String[] spacing(String line) {

        String[] lines = new String[2];
        lines[0] = line.substring(0,4).trim();
        lines[1] = line.substring(4).trim();
        return lines;
    }

    /**
     * Spaces the line identifier from the text correctly
     */
    public static String comment(String line) {
        
        String[] lines = spacing(line);

        if(lines[0].contains("CC")) {
            if(!lines[1].contains("-!-")) { //Topic block identifier
                lines[1] = "~~~~" + lines[1];
            }
        }

        return lines[0] + "~~~" + lines[1];
    }

    /**
     * Takes a single argument -- name of a filename. The filename
     * should contain the "history view" of a UniSave file. Basically
     * this is the text copied and pasted from the web view when
     * comparing two versions. Then formats into necessary table for
     * LaTeX.
     */
    public static void main(String[] args) throws Exception {

        if(args.length <= 0) { 
            System.exit(0);
        } else { 

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("LaTeX-Output.txt")));
            
            //This is just so that it is referenced as Figure
            bw.write("\\begin{figure}[!htbp]");
            bw.newLine();
            bw.write("\\footnotesize");
            bw.write("\\centering $");
            bw.newLine();
            bw.write("\\texttt{%");
            bw.newLine();
            //Setup Table stuff
            bw.write("\\begin{tabular}{l m{13.5cm}}");
            bw.newLine();

            BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
            String line;
            int i = 1; //Line number
            while( (line = br.readLine()) != null) {
                if(line.startsWith("+")) { //Addition
                    bw.write("\\rowcolor{bgGreen} " + i + " & {\\color{fntGreen}" + UniSave2LaTeX.comment(line).replaceAll("_","\\\\_").trim() + "} \\\\");
                    bw.newLine();
                    i++;
                } else if (line.startsWith("-")) { //Removal
                    spacing(line);
                    bw.write("\\rowcolor{bgRed} " + i + " & {\\color{fntRed}" + UniSave2LaTeX.comment(line).replaceAll("_","\\\\_").trim() + "} \\\\");
                    bw.newLine();
                    i++;
                } else if (line.contains("…")) {
                    bw.write("& {\\color{fntDots} ~~~~~ $^{...}$ } \\\\[-1ex]");
                    bw.newLine();
                }
            }

            bw.write("\\end{tabular} } $"); //One } to close texttt
            bw.newLine();
            bw.write("\\caption[TODO: TOC Caption]{TODO: Figure Caption}");
            bw.newLine();
            bw.write("\\label{fig:TODOunisave}");
            bw.newLine();
            bw.write("\\end{figure}");

        //Tidy up
        bw.flush();
        bw.close();
        br.close();


        }



    }

}
