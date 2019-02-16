/**
 *
 * @author Kevin Strileckis
 */
public abstract class QuestBehavior {
    public QuestWindow top;
    
    public QuestWindow getWindow(){
        return top;
    }
    
    public QuestBehavior(QuestWindow t){
        top = t;
        top.listener.qb = this;
    }
    public abstract boolean performBehavior(String identity);
}
