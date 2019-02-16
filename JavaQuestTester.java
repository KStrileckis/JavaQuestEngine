/**
 *
 * @author Kevin Strileckis
 */
public class JavaQuestTester {

    public static void main(String[] args) {
        //Set up the game scene
        QuestWindow top = new QuestWindow("My Game");
        top.setText("There are two paths ahead of you. \nOne leads into a cave, the other into the valley.");
        top.setBackground("red");
        top.setFontSize(14);
        
        //Set up beginning functionality
        QuestButton button1 = new QuestButton(top, 1, "Go in the cave");
            button1.setTextChange("You entered the cave");
            button1.setIdentity("Cave");
        QuestButton button3 = new QuestButton(top, 1, "Go in the valley", "The valley is peaceful.");
            button3.setIdentity("Valley");
        
        top.addQuestButton(button1);
        top.addQuestButton(button3);
        
        top.darkenBackground();
        top.darkenBackground();
        top.darkenBackground();
        
        top.saveAsStart();
        top.setGrayImage();
        
        //Establish behaviors
        QuestBehaviorHandler handle = new QuestBehaviorHandler(top);
    }
    
}


class QuestBehaviorHandler extends QuestBehavior
{
    public QuestBehaviorHandler(QuestWindow t){
        super(t);
    }
    
    @Override
    public boolean performBehavior(String identity) {
        top.swapTextAndImage();
        
        /*Cave Path*/
        if(identity.equals("Cave")){
            top.clearButtons();
            QuestButton dance = new QuestButton(top, 1, "Dance", "You dance around.\nThere is a rustling near you.\n"
                    + "You investigate only to discover that it is a giant monster!");
            dance.setIdentity("Dance");
            top.addQuestButton(dance);
            QuestButton leave = new QuestButton(top, 1, "Leave", "You return to the outside world. \nYou are back at the fork.");
            leave.setIdentity("Leave");
            top.addQuestButton(leave);
        }
        else if(identity.equals("Leave")){
            top.restart("You return to the outside world. \nYou are back at the fork.\n\n");
        }
        else if(identity.equals("Leave2")){
            top.restart();
        }
        else if(identity.equals("Dance")){
            top.clearButtons();
            QuestButton ohno = new QuestButton(top, 1, "Oh no!", "You died");
            ohno.setIdentity("ohno");
            top.addQuestButton(ohno);
        }
        else if(identity.equals("ohno")){
            top.clearButtons();
            QuestButton leave = new QuestButton(top, 1, "Restart", "You return to the outside world. \nYou are back at the fork.");
            leave.setIdentity("Leave2");
            top.addQuestButton(leave);
            top.flashBackground();
        }
        /*Valley path*/
        if(identity.equals("Valley")){
            top.clearButtons();
            QuestButton smell = new QuestButton(top,1, "Smell the flowers", "You decide to stop and smell the \nflowers."
                    + " They are lovely.");
            smell.setIdentity("Smell");
            top.addQuestButton(smell);
            QuestButton sleep = new QuestButton(top,1, "Sleep amongst the beauty", "You lay down to snooze. It's a\nnice"
                    + " day. Good night.");
            sleep.setIdentity("Sleep");
            top.addQuestButton(sleep);
            QuestButton dig = new QuestButton(top, 1, "Dig a deep hole", "You decide to dig a deep hole in a clearing."
                    + " Your hole takes 2 hours to dig and \ngoes down twelve feet. "
                    + "It's been a long day, and you are very tired. ");
            dig.setIdentity("Dig");
            top.addQuestButton(dig);
        }
        else if(identity.equals("Smell")){
            top.clearButtons();
            QuestButton sleep = new QuestButton(top,1, "Sleep amongst the beauty", "You lay down to snooze. It's a\nnice"
                    + " day. Good night.");
            sleep.setIdentity("Sleep");
            top.addQuestButton(sleep);
        }
        else if(identity.equals("Sleep")){
            top.clearButtons();
            top.darkenBackground();
            top.darkenBackground();
            QuestButton leave = new QuestButton(top, 1, "Restart", "You return to the outside world. \nYou are back at the fork.");
            leave.setIdentity("Leave2");
            top.addQuestButton(leave);
        }
        //Valley -- dig path
        else if(identity.equals("Dig")){
            top.clearButtons();
            QuestButton contDig = new QuestButton(top, 1, "Continue Digging", "You keep digging."
                    + " \nEventually, your shovel strikes a hard object.");
            contDig.setIdentity("ContinueDig");
            top.addQuestButton(contDig);
        }
        else if(identity.equals("ContinueDig")){
            top.clearButtons();
            QuestButton investigateObj = new QuestButton(top, 1, "Investigate Object", "You look at the object.\n"
                    + "It's a treasure chest! You're rich and you win.");
            investigateObj.setIdentity("InvestObj");
            top.addQuestButton(investigateObj);
            QuestButton dance = new QuestButton(top, 1, "Dance", "You dance around.\nThere is a rustling near you.\n"
                    + "You investigate only to \ndiscover that it is a giant \nmonster!");
            dance.setIdentity("Dance");
            top.addQuestButton(dance);
        }
        else if(identity.equals("InvestObj")){
            top.clearButtons();
            QuestButton leave = new QuestButton(top, 1, "Restart", "You return to the outside world. \nYou are back at the fork.");
            leave.setIdentity("Leave2");
            top.addQuestButton(leave);
        }
        
        //Return false if none of the behvaiors were performed
        return false;
    }
    
}