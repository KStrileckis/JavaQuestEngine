import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Kevin Strileckis
 */
public class QuestListener implements ActionListener {
    
    /*These variables are not included in documentation*/
    public QuestBehavior qb;
    /* * * * */
    
    private QuestWindow questWind;
    
    public QuestListener(QuestWindow t){
        super();
        questWind = t;
    }
    
    public void execute(String id){
      //Set QuestWindow's last identity to this button's identity
        questWind.setIndicator(id);
        questWind.choices.add(id);
        
        
        qb.performBehavior(id);
    }
    public void execute(String id, int type, String txt, String img){
        if(type == 1){
            questWind.setText(txt);
        }else if(type == 2){
            questWind.setImage(img);
        }else if(type == 3){
            questWind.setText(txt);
            questWind.setImage(img);
        }
        execute(id);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Entered actionPerformed");
        QuestButton q = (QuestButton)(e.getSource());
        
        
        execute(q.getIdentity(), q.getType(), q.getTextChange(), q.getImageURLChange());
    }
    
}
