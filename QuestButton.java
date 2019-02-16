import javax.swing.JButton;

/**
 *
 * @author Kevin Strileckis
 */
public class QuestButton extends JButton {
    private JButton button;
    private int type;
    private QuestWindow attachedWindow;
    private String textChange;
    private String imageURLChange;
    private String identity;
    /*
    Types:
        1 - Change Text
        2 - Change Image
        3 - Change both
    */
    
    /*These variables are not included in documentation*/
    private static int num = 0;
    /* * * * * */
    
    public QuestButton(QuestWindow q, int t, String title){
        this(q,t,title,"UNSET","UNSET");
    }
    public QuestButton(QuestWindow q, int t, String title, String s){
        super(title);
        if(t == 1){ //Text change button
            attachedWindow = q;
            type = t;
            textChange = s;
            imageURLChange = "UNSET";
        }
        else{
            attachedWindow = q;
            type = t;
            textChange = "UNSET";
            imageURLChange = s;
        }
        setIdentity();
    }
    public QuestButton(QuestWindow q, int t, String title, String text, String url){
        super(title);
        attachedWindow = q;
        type = t;
        textChange = text;
        imageURLChange = url;
        setIdentity();
    }
    private void setIdentity(){
        identity = Integer.toString(num++);
        System.out.println(identity);
    }
    public void setIdentity(String ident){
        identity = ident;
        System.out.println(identity);
    }
    public String getIdentity(){
        return identity;
    }
    
    public void setEffect(String s){
        if(type == 1){
            setTextChange(s);
        }
        if(type == 2){
            setImageURLChange(s);
        }
        if(type == 3){
            if(textChange.equals("UNSET")){
                setTextChange(s);
            }
            else{
                setImageURLChange(s);
            }
        }
    }
    public void setEffect(String s1, String s2){
        if(type == 1){
            setTextChange(s1);
        }
        if(type == 2){
            setImageURLChange(s2);
        }
        if(type == 3){
            setTextChange(s1);
            setImageURLChange(s2);
        }
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }


    /*Accessors and Mutators*/
    public void setType(int type) {
        this.type = type;
    }
    public String getTextChange() {
        return textChange;
    }
    public void setTextChange(String textChange) {
        this.textChange = textChange;
    }
    public String getImageURLChange() {
        return imageURLChange;
    }
    public void setImageURLChange(String imageURLChange) {
        this.imageURLChange = imageURLChange;
    }
    public JButton getJButton(){
        return button;
    }
    /* * * * * */
}
