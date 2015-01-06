/*
	A basic extension of the java.applet.Applet class
 */

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class PostalBarCodeApplet extends Applet implements ActionListener
{
    
    private Button zipButton, barButton, clearButton;  // buttons
    private TextField zipText, barText; // text fields
    private Label zipLabel, barLabel;  // labels
    private Panel zipPanel, buttonPanel, barPanel;
    private String[] barValues = new String[10];
    
	public void init()
	{
	    // Instantiate buttons
	    zipButton = new Button ("Zip");
	    zipButton.addActionListener (this);
	    
	    barButton = new Button("Bar");
	    barButton.addActionListener (this);
	    
	    clearButton = new Button ("Clear");
	    clearButton.addActionListener (this);
		
            // Instantiate Text Fields
            zipText = new TextField("18512", 52);  
	    barText = new TextField("", 52);  
	    
	    // Instantiate labels
	    zipLabel = new Label("Zip Code: ");
	    barLabel = new Label("Postal Bar Code: ");
	    
	    // format GUI layout
	    zipPanel = new Panel();  // Zip Code components
	    zipPanel.add (zipLabel);
	    zipPanel.add (zipText);
	   
	    
	    buttonPanel = new Panel();  // Buttons
	    buttonPanel.add(barButton);
	    buttonPanel.add(clearButton);
	    buttonPanel.add(zipButton);
	    	    
	    barPanel = new Panel();
	    barPanel.add (barLabel);
	    barPanel.add (barText);
	    
	    // add components to applet
	    setLayout(new GridLayout(3, 1));
	    add (zipPanel);
	    add (buttonPanel);
	    add (barPanel);
	    
	    setSize(400, 250);
            //set values for barValues
            barValues[0] = "||:::";//11000;// 0
            barValues[1] = ":::||";//00011;// 1
            barValues[2] = "::|::";//00100;// 2 
            barValues[3] = "::||:";//00110;// 3
            barValues[4] = ":|::|";//01001;// 4 
            barValues[5] = ":|:|:";//01010;// 5 
            barValues[6] = ":||::";//01100;// 6
            barValues[7] = "|:::|";//10001;// 7
            barValues[8] = "|::|:";//10010;// 8
            barValues[9] = "|:|::";//10100;// 9
	}
	
	public String toZipCode (String bc)
	{
           String zipCodeText = new String("");//initiate string for zipcode
           int checkSum=0;
           if(bc.length()!= 32 && bc.length()!=52&& bc.length()!=47&&bc.length()!=27)//check for length
               return "Error 1, please enter a valid length barcode (5 Characters per 1 digit zip code including the check sum).";
           for(int i=0; i<bc.length(); i++)
           {
                if(bc.charAt(i)!=':' && bc.charAt(i)!='|')
                    return "Error 2, please only use ':' for 0 and '|' for 1";
           }//end for check for valid char
           if(!(bc.charAt(0)== '|'&& bc.charAt(bc.length()-1)== '|'))//checks for open and close frame
               return "Error 3, please use '|' at the beginning and end of the bar code.";
            bc = bc.substring(1, (bc.length()-1));//remove open and close frame as well as the check sum
            for(int i=0; i<bc.length(); i+=5)
            {
                for(int z=0; z<10;z++)
                {
                     if(bc.substring(i, (i+5)).equals(barValues[z]))
                     {
                        zipCodeText+=z;//end if
                        checkSum = checkSum + z;
                     }
                }//end for z
            }// end for i
            if((checkSum%10)!=0)
                return "Error 4, the Check Sum is not divisible by 10.";
            //System.out.println(checkSum);
            if(bc.length()==50 || bc.length()==30)//checks if there barcode had a checksum added
                zipCodeText = zipCodeText.substring(0,(zipCodeText.length()-1));//removes check sum from the output
            
            if(zipCodeText.length() > 5)//add a '-' to output if a 9 digit zip
                zipCodeText= zipCodeText.substring(0,5) + "-" + zipCodeText.substring(5,zipCodeText.length());
                        
	    return zipCodeText;
	}
	
	public String toPostalBarCode (String zc)
	{
            int checkDigit = 0;
            int checkSum=0;
            int inputZC = 0;
                    
            String barCodeText = new String("");
            
            if(zc.length()!= 5 && zc.length()!=10)
            {
                return "Error 1, please enter a '5' or '9' digit zip code.";
            }//end if length check
              
            if(zc.length()==10 &&zc.charAt(5) != '-')//check for dash in a 9 digit zipcode
                    return "Error 2, please enter a '-' for a 9 digit zipcode.";
            
            for(int i=0; i<zc.length();i++)//check if user inputed only numbers
            {
                 if(!Character.isDigit(zc.charAt(i))){
                    if (zc.length() == 5)
                       return "Error 3, please enter only numbers for the zip code.";
                    else
                    {
                       if (i !=5)
                          return "Error 4, please enter only numbers for the zip code, and a '-' for 9 digits.";
                    }
                }
            }//end for loop of isDigit() check
            
            for(int z=0; z<zc.length();z++)
            {
                if (!((zc.length() == 10) && (z==5))) // skip dash for 10-dight
                {
                    inputZC = Integer.parseInt(zc.substring(z, z+1)); // convert from char to number
                    barCodeText+=barValues[inputZC];
                    checkSum = checkSum + inputZC;
                }//end if 
            }//end for
            
            checkDigit = 10 - (checkSum %10);// calculate check digit
            //System.out.println(checkDigit);
            if(checkDigit<10)//only add checkDigit value if there is a remainder
                barCodeText+=barValues[checkDigit];//add check digit to barcode
            
            return "|"+barCodeText+"|";
	}
	
	public void actionPerformed (ActionEvent e)
	{
	    String barCodeText = "";
	    String zipCodeText = "";
	    
	    if (e.getSource() == zipButton)  // Zip Code button
	    {
	        zipCodeText = toZipCode(barText.getText());
	        zipText.setText(zipCodeText);
	    }
	    else if (e.getSource() == barButton) // Bar Code button
	    {
	        barCodeText = toPostalBarCode(zipText.getText());
	        barText.setText(barCodeText);
	    }
	    else // Clear button
	    {
	        barText.setText("");  // Write empty string to text fields
	        zipText.setText("");
	    }
	}
}
