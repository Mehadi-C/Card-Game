import deckclass2.Cards;
import java.awt.*;
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import java.util.Random;

class DeckGUIDemo extends JFrame
{
    static Deck deck = new Deck ();

    //======================================================== constructor
    public DeckGUIDemo ()
    {
        // 1... Create/initialize components
        BtnListener btnListener = new BtnListener (); // listener for all buttons

        JButton shuffleBtn = new JButton ("Shuffle");
        shuffleBtn.addActionListener (btnListener);
        JButton sortBtn = new JButton ("Sort");
        sortBtn.addActionListener (btnListener);

        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        JPanel north = new JPanel (); // where the buttons, etc. will be
        north.setLayout (new FlowLayout ()); // Use FlowLayout for input area

        DrawArea board = new DrawArea (600, 400); // Area for cards to be displayed

        // 3... Add the components to the input area.
        north.add (shuffleBtn);
        north.add (sortBtn);
        content.add (north, "North"); // Input area
        content.add (board, "South"); // Deck display area

        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("Deck Demo");
        setSize (600, 500);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }

    // put ActionListener class for your buttons here
    class BtnListener implements ActionListener // Button menu
    {
        public void actionPerformed (ActionEvent e)
        {
            if (e.getActionCommand ().equals ("Shuffle"))
                deck.shuffle (); // shuffle deck
//            else if (e.getActionCommand ().equals ("Sort"))
//                deck.quickSort(); // shuffle deck
            repaint (); // do after each action taken to update deck
        }
    }

    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
            deck.show (g);
        }
    }

    //======================================================== method main
    public static void main (String[] args)
    {
        DeckGUIDemo window = new DeckGUIDemo ();
        window.setVisible (true);
    }
}

// -------------------------------------- Card Class ------------------------------------------------------
class Card
{
     protected int rank, suit;
    protected boolean faceup;
    private static Image cardback;
    private Image image;
    
    public Card( int cardNum )
    {
        rank= cardNum%13;
        suit= cardNum/13;
        faceup= true;
        
        image = null;
        try
        {
            image = ImageIO.read (new File ("cards/" + (cardNum + 1) + ".gif")); // load file into Image object
            cardback = ImageIO.read (new File ("cards/b.gif")); // load file into Image object
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }
    }
    
    public void show (Graphics g, int x, int y)  // draws card face up or face down
    {
        if (faceup)
            g.drawImage (image, x, y, null);
        else
            g.drawImage (cardback, x, y, null);

    }
    
    public boolean getFaceup()
    {
        return faceup;
    }
    
    public int getRank()
    {
        return rank;
    }
    
    public int getSuit()
    {
        return suit;
    }
    
    public void flip()
    {
        if( faceup == true )
            faceup= false;
        else
            faceup= true;
    }
    
    public boolean equals( Cards card)
    {
        if( rank == card.getRank() )
            return true;
        if( suit == card.getSuit() )
            return true;
        return false;
    }
}

// -------------------------------------- Deck Class ------------------------------------------------------
class Deck
{
    protected Cards[] deck;//creates an array called deck that contains Cards values
    
    public Deck()//constructor
    {
        deck= new Cards[52];//creates a deck array of size 52
        for( int x= 0; x < 52; x ++ )//declare x as 0; loop so long as x is less than 52; add x by 1
            deck[x]= new Cards(x);//deck[x] is equal to Cards(x)
    }
    
     public void show (Graphics g)  // draws card face up or face down
    {
        for (int c = 0 ; c < deck.length ; c++)//declare as int; loop so long as c is less than deck.length; add c by 1
        {
            deck [c].show (g, c % 13 * 20 + 150, c / 13 * 50 + 20);//display card as face up or facedown
        }
    }


    public void shuffle ()
    {
        Random r= new Random();
        int temp1, temp2;
        Cards temp3;
        Cards [] temp= new Cards[52];
        
        for( int x= 0; x < deck.length*3; x ++ )
        {
            temp1= r.nextInt(51);
            temp2= r.nextInt(51);
            temp3= deck[temp1];
            deck[temp1]= deck[temp2];
            deck[temp2]= temp3;
        }
    }
    
    public Cards deal()
    {
        Cards [] temp= new Cards[deck.length - 1];
        Cards tem= deck[0];
        for( int x= 0; x < temp.length; x ++ )
            temp[x]= deck[x + 1];
        deck= temp;
        return tem;
    }
    
    public Cards deal( int index )
    {
        if( index >= 0 && index <= deck.length)
        {
        Cards [] temp= new Cards[deck.length - 1];
        Cards tem= deck[index];
        for(int x= 0; x < index; x ++ )
            temp[x]= deck[x];
        for( int x= index; x < temp.length; x ++ )
            temp[x]= deck[x + 1];
        deck= temp;
        return tem;
        }
        
        return deal();
    }
    
    public void add( Cards card )
    {
        Cards [] temp= new Cards[deck.length + 1];
        for(int x= 0; x < deck.length; x ++ )
            temp[x]= deck[x];
        temp[deck.length]= card;
        deck= temp;
    }
    
    public int [] search( Cards card )
    {
        int factors= 0;
        
        for(int x= 0; x < deck.length; x ++ )
        {
            if( deck[x] == card )
                factors ++;
        }
        
        int [] sea= new int[factors];
        int y= 0;
        
        for(int x= 0; x < deck.length; x ++ )
        {
            if( deck[x] == card )
            {
                sea[y]= x;
                y ++;
            }
        }
        
        return sea;
    }
    
    public int getLength()
    {
        return deck.length;
    }
    
private void exchangeNumbers(int i, int j) {
Cards temp = deck[i];
deck[i] = deck[j];
deck[j] = temp;
}


    
    public void quickSort( int lowerIndex, int higherIndex )
    {

int i =lowerIndex;
int j = higherIndex;

int pivot = deck[(51)/2].getRank();

while (i <= j) {
    
while (deck[i].getRank() < pivot) {
i++;
}
while (deck[j].getRank() > pivot) {
j--;
}
if (i <= j) {
exchangeNumbers(i, j);

i++;
j--;
}
}
if (lowerIndex < j)
    quickSort(lowerIndex, j);
if (i < higherIndex)
    quickSort(i, higherIndex);
}

}

