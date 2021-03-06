import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.ColorConvertOp;
import java.awt.color.ColorSpace;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author Kevin Strileckis
 */
public class QuestWindow {
  private JFrame top;
  private JPanel image;
  private JTextPane text;
  private int width, height;
  private String creditsSummary_string;
  
  /*Button Indicator*/
  private String indicator;
  public void setIndicator(String id){
    indicator = id;
  }
  /*Returns the most recent button press indication*/
  public String getIndicator(){
    return indicator;
  }
  /* * * * * */
  
  /*These variables are not included in documentation*/
  public QuestListener listener;
  private JPanel buttons;
  private boolean imageOnLeft;
  private final int PANEL_WIDTH;
  private final int PANEL_HEIGHT;
  private String defaultImage = "images\\default.png";
  private JLabel startImg;
  private String startTxt;
  private QuestButton[] startButtons;
  private boolean startImageIsLeft;
  private Color startBackgroundColor;
  private String startFontName;
  private int startFontStyle, startFontSize;
  public ArrayList<String> choices;
  private JMenuBar menuBar;
  private JMenu data;
  private JMenuItem savedata, loaddata, restart, credits;
  private boolean soundPlaying, originalPlaying_boolean;
  private String currentSound_string, originalSound;
  /* * * * */
  
  /*Menu Methods*/
  //enableMenu and disableMenu are in the Panel Updating Methods
  /*Show credits*/
  public void displayCredits(){
    JOptionPane.showMessageDialog(null, creditsSummary_string, "Credits", JOptionPane.INFORMATION_MESSAGE);
  }
  /*Establish Credits*/
  public void setCredits(String c){
    creditsSummary_string = c;
  }
  /*Ask for information to save the game*/
  public boolean saveGame(){
    return saveGame("file");
  }
  /*Given a filename (String), will save choices to the file*/
  public boolean saveGame(String file){
    try{
      PrintWriter writer = new PrintWriter(file+".jqsav", "UTF-8");
      for(String s : choices)
      {
        writer.println(saveFileEncryptor(s));
      }
      writer.close();
    }
    catch(Exception e){
      System.out.println("Could not create save file.");
    }
    return true;
  }
  /*Ask for information to load the game*/
  public boolean loadGame(){
    return loadGame("file");
  }
  /*Given a filename (String), will load choices from the file*/
  public boolean loadGame(String file){
    file +=".jqsav";
    System.out.println("Loading "+file);
    try{
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = br.readLine();
      while(line != null){
        listener.execute(saveFileDecryptor(line));
        line = br.readLine();
      }
    }
    catch(Exception e){
      System.out.println("Could not find save file to load.");
    }
    top.validate();
    buttons.validate();
    text.validate();
    image.validate();
    return true;
  }
  public String saveFileEncryptor(String s){
    String sNew = "";
    char[] sArray = s.toCharArray();
    for(char c : sArray){
      sNew += (char)(c+1);
    }
    return sNew;
  }
  public String saveFileDecryptor(String s){
    String sNew = "";
    char[] sArray = s.toCharArray();
    for(char c : sArray){
      sNew += (char)(c-1);
    }
    return sNew;
  }
  /* * * * * */
  
  /*Sounds*/
  private Clip clip;
  public void playSound(String path) {
    try {
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
      clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();
      currentSound_string = path;
      soundPlaying = true;
    } catch(Exception ex) {
      System.out.println("Error with playing sound.");
      ex.printStackTrace();
    }
  }
  public void stopCurrentSound(){
    if(clip == null){
      return;
    }
    currentSound_string = "";
    soundPlaying = false;
    clip.stop();
  }
  public void restartCurrentSound(){
    if(clip == null){
      return;
    }
    clip.stop();
    clip.setFramePosition(0);
    clip.start();
  }
  public int getCurrentSoundFrame(){ return clip.getFramePosition(); }
  public int getFrameLength(){ return clip.getFrameLength(); }
  public void setCurrentSoundFrame(int frame){
    clip.setFramePosition(frame);
  }
  /* * * * * */
  
  /*Panel Updating Methods*/
  public void enableMenu(){
    top.setJMenuBar(menuBar);
    top.validate();
  }
  public void disableMenu(){
    top.setJMenuBar(null);
    top.validate();
  }
  public void saveAsStart(){
    startImg = ((JLabel)(image.getComponent(0)));
    startTxt = text.getText();
    startImageIsLeft = imageOnLeft;
    startBackgroundColor = top.getContentPane().getBackground();
    
    /*Font*/
    startFontName = text.getFont().getFontName();
    startFontStyle= text.getFont().getStyle();
    startFontSize = text.getFont().getSize();
    /**/
    
    //Get number of buttons
    int x = buttons.getComponentCount();
    startButtons = new QuestButton[x];
    for(int i=0; i<x; ++i){
      startButtons[i] = (QuestButton) buttons.getComponent(i);
    }
    
    /*Sounds*/
    originalSound = currentSound_string;
    originalPlaying_boolean = soundPlaying;
    /**/
  }
  public void restart(){
    stopCurrentSound();
    if(originalPlaying_boolean){
      playSound(originalSound);
    }
    image.removeAll();
    image.add(startImg);
    setText(startTxt);
    top.getContentPane().setBackground(startBackgroundColor);
    clearButtons();
    text.setFont(new Font(startFontName, startFontStyle, startFontSize));
    for(QuestButton b : startButtons){
      addQuestButton(b);
    }
    if(imageOnLeft != startImageIsLeft){
      swapTextAndImage();
    }
    buttons.revalidate();
    top.repaint();
  }
  public void restart(String addText){
    restart();
    setText(addText + startTxt);
  }
  public void swapTextAndImage(){
    int imgX, txtX;
    if(imageOnLeft){
      imgX = 1;
      txtX = 0;
    }
    else{
      imgX = 0;
      txtX = 1;
    }
    GridBagConstraints gbc = new GridBagConstraints();
    top.remove(image);
    top.remove(text);
    /* Image Panel */
    gbc.gridx = imgX;
    gbc.gridy = 0;
    top.add(image, gbc);
    /* * * * * */
    
    /* Text Panel */
    gbc.gridx = txtX;
    gbc.gridy = 0;
    top.add(text, gbc);
    /* * * * * */
    
    imageOnLeft = !imageOnLeft;
  }
  public boolean setGrayImage(){
    return setGrayImage(defaultImage);
  }
  public boolean setGrayImage(String url){
    ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    BufferedImage imageFile;
    try {                
      imageFile = ImageIO.read(new File(url));
    } catch (IOException ex) {
      return false;//Could not find file
    }
    op.filter(imageFile, imageFile);
    Image scaledImage = imageFile.getScaledInstance(PANEL_WIDTH, PANEL_HEIGHT,Image.SCALE_SMOOTH);
    JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
    image.removeAll();
    image.add(picLabel);
    image.revalidate();
    return true;
  }
  public boolean setByteImage(){
    return setByteImage(defaultImage);
  }
  public boolean setByteImage(String url){
    BufferedImage imageFile;
    try {                
      imageFile = ImageIO.read(new File(url));
    } catch (IOException ex) {
      return false;//Could not find file
    }
    imageFile = new BufferedImage(imageFile.getWidth(), imageFile.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
    Image scaledImage = imageFile.getScaledInstance(PANEL_WIDTH, PANEL_HEIGHT,Image.SCALE_SMOOTH);
    JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
    image.removeAll();
    image.add(picLabel);
    image.revalidate();
    return true;
  }
  public boolean setImage(){
    return setImage(defaultImage);
  }
  public boolean setImage(String url){
    BufferedImage imageFile;
    try {                
      imageFile = ImageIO.read(new File(url));
    } catch (IOException ex) {
      System.out.println("Did not find the file");
      return false;//Could not find file
    }
    Image scaledImage = imageFile.getScaledInstance(PANEL_WIDTH, PANEL_HEIGHT,Image.SCALE_SMOOTH);
    JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
    image.removeAll();
    image.add(picLabel);
    image.revalidate();
    return true;
  }
  public boolean setText(){
    return setText("Lorem ipsum");
  }
  public boolean setText(String s){
    text.setText(s);
    text.revalidate();
    top.setVisible(true);
    return true;
  }
  public boolean addQuestButton(QuestButton b){
    b.addActionListener(listener);
    buttons.add(b);
    buttons.revalidate();
    top.setVisible(true);
    return true;
  }
  public void clearButtons(){
    while(true){
      try{
        buttons.remove(buttons.getComponent(0));
      }
      catch(ArrayIndexOutOfBoundsException e){
        buttons.revalidate();
        top.setVisible(true);
        return;
      }
    }
    //buttons.removeAll();
  }
  /* * * * * */
  
  /*Other Cosmetic Methods*/
  public boolean setBackground(String color){
    Color c = Color.red;
    if(color.equalsIgnoreCase("red")){c = Color.red;}
    else if(color.equalsIgnoreCase("yellow")){c = Color.yellow;}
    else if(color.equalsIgnoreCase("black")){c = Color.black;}
    else if(color.equalsIgnoreCase("white")){c = Color.white;}
    else if(color.equalsIgnoreCase("green")){c = Color.green;}
    else if(color.equalsIgnoreCase("blue")){c = Color.blue;}
    else if(color.equalsIgnoreCase("cyan")){c = Color.cyan;}
    else if(color.equalsIgnoreCase("gray")){c = Color.gray;}
    else if(color.equalsIgnoreCase("lightgray")){c = Color.lightGray;}
    else if(color.equalsIgnoreCase("darkgray")){c = Color.darkGray;}
    else if(color.equalsIgnoreCase("pink")){c = Color.pink;}
    else if(color.equalsIgnoreCase("magenta")){c = Color.magenta;}
    else if(color.equalsIgnoreCase("orange")){c = Color.orange;}
    else if(color.equalsIgnoreCase("strick")){c = new Color(182,182,200);}
    else if(color.equalsIgnoreCase("strick2")){c = new Color(176,176,194);}
    else if(color.equalsIgnoreCase("strick3")){c = new Color(255,161,161);}
    top.getContentPane().setBackground(c);
    return true;
  }
  public boolean setBackground(double red, double green, double blue){
    float r = (float)red;
    float g = (float)green;
    float b = (float)blue;
    if(r > 255){r = 255;} else if(r < 0){r = 0;}
    if(g > 255){r = 255;} else if(r < 0){r = 0;}
    if(b > 255){r = 255;} else if(r < 0){r = 0;}
    
    Color c = new Color(r,g,b);
    top.getContentPane().setBackground(c);
    top.setVisible(true);
    return true;
  }
  public boolean darkenBackground(){
    Color c = top.getContentPane().getBackground();
    c = c.darker();
    top.getContentPane().setBackground(c);
    top.setVisible(true);
    return true;
  }
  public boolean lightenBackground(){
    Color c = top.getContentPane().getBackground();
    c = c.brighter();
    top.getContentPane().setBackground(c);
    top.setVisible(true);
    return true;
  }
  public boolean flashBackground(){
    final Timer timer = new Timer();
    Color originalColor = top.getContentPane().getBackground();
    timer.scheduleAtFixedRate(new TimerTask() {
      boolean flag = true;
      int x = 0;
      @Override
      public void run() {
        if(flag){lightenBackground();lightenBackground();lightenBackground();}
        if(!flag){darkenBackground();darkenBackground();darkenBackground();}
        top.revalidate();
        flag=!flag;
        x++;
        if(x == 10)
          timer.cancel();
      }
    }, 0, 250);
    top.getContentPane().setBackground(originalColor);
    return true;
  }
  public boolean setFontName(String name){
    /* Suggestions:
     * Elephant, Narkisim, Broadway
     */
    Font f = new Font(name, text.getFont().getStyle(), text.getFont().getSize());
    text.setFont(f);
    return true;
  }
  public boolean setFontSize(int size){
    Font f = new Font(text.getFont().getFontName(), text.getFont().getStyle(), size);
    text.setFont(f);
    return true;
  }
  /* * * * * */
  
  /* Constructors*/
  public QuestWindow(){
    this("Untitled Game");
  }
  public QuestWindow(int x, int y){
    this("Untitled Game", x, y);
  }
  public QuestWindow(String title){
    this(title, 495, 500);
  }
  public QuestWindow(String title, int x, int y){
    this(title, x, y, 0, 0);
  }
  public QuestWindow(String title, int x, int y, int startX, int startY){
    top = new JFrame(title);
    top.setPreferredSize(new Dimension(x,y));
    width = x;
    height = y;
    /*Set panel width/height*/
    PANEL_WIDTH = (int)(width*0.48);
    PANEL_HEIGHT = (int)(height*0.5);
    //
    listener = new QuestListener(this);
    
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    
    top.setLayout(gbl);
    
    /* Image Panel */
    imageOnLeft = true;
    gbc.gridx = 0;
    gbc.gridy = 0;
    image = new JPanel();
    image.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    System.out.println(setImage());
    top.add(image, gbc);
    /* * * * * */
    
    /* Text Panel */
    gbc.gridx = 1;
    gbc.gridy = 0;
    text = new JTextPane();
    text.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    text.setMinimumSize(new Dimension(PANEL_WIDTH-1, PANEL_HEIGHT-1));
    text.setMaximumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    //text.setWrapStyleWord(true);
    text.setEditable(false);
    text.setFont(new Font("Elephant", Font.PLAIN, 12));
    System.out.println(setText());
    top.add(text, gbc);
    /* * * * * */
    
    /* Buttons Panel */
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    buttons = new JPanel();
    top.add(buttons,gbc);
    /* * * * * */
    
    /*Create menu(s)*/
    menuBar = new JMenuBar();
    data = new JMenu("Data");
    menuBar.add(data);
    savedata = new JMenuItem("Save Progress");
    data.add(savedata);
    savedata.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        saveGame();
      }
    });
    loaddata = new JMenuItem("Load Progress");
    data.add(loaddata);
    loaddata.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        loadGame();
      }
    });
    credits = new JMenuItem("Credits");
    data.add(credits);
    credits.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        displayCredits();
      }
    });
    restart = new JMenuItem("Restart");
    data.add(restart);
    restart.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        restart();
      }
    });
    /* * * * * */
    
    /*Choices*/
    choices = new ArrayList<String>();
    /* * * * * */
    
    /*Sound variables*/
    soundPlaying = false;
    currentSound_string = "";
    /* * * * * */
    
    top.setLocation(startX, startY);
    top.pack();
    top.toFront();
    top.setVisible(true);
    top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    /*Set Indicator*/
    indicator = "UNSET";
    /* * * * * */
  }
  /* * * * * */
}
