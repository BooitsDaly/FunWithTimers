/*
This program has a color wheel rotate around while it reads in a file
@author Caitlyn Daly
@version 1.0
*/

import java.text.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class TimerFun extends JFrame {
private boolean goOn;
   //constructor for GUI
   public TimerFun() {

       setTitle("Fun with Timers");
       setLayout(new BorderLayout());

       JMenuBar jmb = new JMenuBar();
       setJMenuBar(jmb);

       JMenu file = new JMenu("File");
       JMenuItem exit = new JMenuItem("Exit");
       file.add(exit);
       jmb.add(file);

       JMenu help = new JMenu("Help");
       JMenuItem about = new JMenuItem("About");
       help.add(about);
       jmb.add(help);

       JPanel north = new JPanel();
       DateFormat date = new SimpleDateFormat("EEE, MM/dd/yyyy HH:mm:ss");
       Calendar calendar = Calendar.getInstance();
       JLabel clock = new JLabel(date.format(calendar.getTime()), JLabel.CENTER);
       Font font = new Font("Arial", Font.BOLD, 22);

       clock.setFont(font);
       clock.setForeground(Color.BLUE);

       north.add(clock, BorderLayout.NORTH);
       add(north, BorderLayout.NORTH);

       ActionListener swingAction = new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               DateFormat date = new SimpleDateFormat("EEE, MM/dd/yyyy HH:mm:ss");
               Calendar calendar2 = Calendar.getInstance();
               clock.setText(date.format(calendar2.getTime()));
           }
       };

        exit.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent ae) {
               System.exit(0);   
            }
         } );
       GridLayout grid = new GridLayout(0, 1);
       JPanel center = new JPanel();
       center.setLayout(grid);
       
       //making new panels for each color
       JPanel c1= new JPanel();
       JPanel c2= new JPanel();
       JPanel c3= new JPanel();
       JPanel c4= new JPanel();
       JPanel c5= new JPanel();
       JPanel c6= new JPanel();
       JPanel c7= new JPanel();
       
       //setting the colors in each panel
       c1.setBackground(Color.RED);
       c2.setBackground(Color.ORANGE);
       c3.setBackground(Color.YELLOW);
       c4.setBackground(Color.GREEN);
       c5.setBackground(Color.BLUE);
       c6.setBackground(new Color(75, 0, 130));
       c7.setBackground(new Color(235, 130, 235));
       
       
       //adding each jpanel into the center jpanel
       center.add(c1);
       center.add(c2);
       center.add(c3);
       center.add(c4);
       center.add(c5);
       center.add(c6);
       center.add(c7);
       
       //add to the frame
       add(center, BorderLayout.CENTER);
       
       //making a method for the timer to move background image from c1-c2 ect
       TimerTask rotate = new TimerTask(){
         public void run(){
            Color temp = c1.getBackground();
            c1.setBackground(c2.getBackground());
            c2.setBackground(c3.getBackground());
            c3.setBackground(c4.getBackground());
            c4.setBackground(c5.getBackground());
            c5.setBackground(c6.getBackground());
            c6.setBackground(c7.getBackground());
            c7.setBackground(temp);
         }
       };
       
       //setting the timing constraints for the run method
       final  int START_AFTER = 2000;
       final int DELAY = 1000;
       final int DELAY2 = 500;
       
       java.util.Timer rotate1 = new java.util.Timer();
       rotate1.scheduleAtFixedRate(rotate, START_AFTER, DELAY2);

       
       //about button function
       JFrame frame = new JFrame("JOptionPane showMessageDialog");
       about.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae){
            JOptionPane.showMessageDialog(frame, "Fun with timers and threads\nby: Caitlyn Daly");
         }
       });
       
       //making the loading bars in the south area
       JPanel south = new JPanel();
       south.setLayout(grid);
       
       //progress bars
       InnerProgBar bar2 = new InnerProgBar("UnabridgedDictionary", 213558);
       
       InnerProgBar bar1 = new InnerProgBar("words", 10000);
       
       south.add(bar1);
       south.add(bar2);
       
       add(south, BorderLayout.SOUTH);
       
       //create threads
       Thread th1 = new Thread(bar1);
       Thread th2 = new Thread(bar2);
       
       //start threads
       th1.start();
       th2.start();
       
       //thread 3 waits for thread 2 to stop then starts
       Thread th3 = new Thread(){
         public void run(){
            try{
               th2.join();
            }
            catch(InterruptedException ie){}
            rotate.cancel();
         }
       };
       th3.start();
       
       //GUI things
       setSize(400,350);
       setLocationRelativeTo(null);
       setVisible(true);
   }
   
   class InnerProgBar extends JPanel implements Runnable{
      private JProgressBar bar;
      private JLabel progress;
      private int lineNum;
      private String name;
      
      //constructor for the inner class
      public InnerProgBar(String _name, int lines){
         name = _name;
         lineNum = lines;
         bar = new JProgressBar();
         progress = new JLabel(name +"Progress: ");
         bar.setIndeterminate(true);
         bar.setStringPainted(true);
         bar.setMaximum((int)(new File(_name)).length());
         bar.setString("Opening file..");
         add(progress);
         add(bar);
         goOn = true;
      }
      
      //run method
      public void run(){
         try{
            Thread.sleep(2000);
         }
         catch(InterruptedException ie){
            System.out.println("Sleep interruption: " + ie.getMessage());
         }
         bar.setString(null);

         File readFile = new File(name +".txt");
         bar.setIndeterminate(false);
         int count = 1;
         bar.setStringPainted(true);
         
         try(BufferedReader br = new BufferedReader(new FileReader(readFile))){
            String line;
            String bigLine=null;
            while((line=br.readLine()) != null && goOn == true){
               bigLine+= line;
               count=bigLine.length()+2;
               
               bar.setValue(count);
               bar.setString(bar.getValue()+"%");

               
               System.out.println(line+"  "+bigLine.length());
               
               try{
                  Thread.sleep(1);
               }
               catch(InterruptedException ie){
                  System.out.println("Sleep interruption: " + ie.getMessage());
               }
            }
            System.out.print(readFile);
            if (goOn==true){
               goOn = false;
               try{
                  Thread.sleep(2000);
               }
               catch(InterruptedException ie){
                  System.out.println("Sleep interruption: " + ie.getMessage());
               }
               bar.setString("Finished");
               br.close();
            }
       
            else if(goOn==false){
               try{
                  Thread.sleep(2000);
               }
               catch(InterruptedException ie){
                  System.out.println("Sleep interruption: "+ ie.getMessage());
               }
               int value = bar.getValue();
               int max = bar.getMaximum();
               int percent = ((value*100)/max) + 1;
               bar.setString("Stopped at " + percent + "%");
               br.close();
            }
            
         }
         catch(Exception e){}
         bar.setIndeterminate(true);
         
      }
      
   }
   
   //main method
   public static void main(String []args){
      new TimerFun();
   }
}
