package guitaschenrechner;
import java.util.Scanner;


/**
 * Beschreiben Sie hier die Klasse TRechner.
 * 
 * @author Thomas Kluth, Fabian Dütsch (Fehlerbehandlung) 
 * @version 0.9
 */
public class TR
{
    private enum TokenValue
    {
        ZAHL,
        PLUS,
        MINUS,
        MUL,
        DIV,
        ASSIGN,
        LK,
        RK
    }

    private TokenValue currTok;   
    private double numberValue;
    private int aktuellePosition;
    private String zuBerechnen; 
    private String fehlerBeschreibung;
    public static final String FEHLER="Fehler aufgetreten";

    public String rechne(String anweisung)
    {
        fehlerBeschreibung=null;
        aktuellePosition=0;
        currTok=null;
        numberValue=0.0;
        
        if(anweisung==null)
        {
            throw new NullPointerException("anweisung ist null");
        }
        else if(anweisung.length()==0)
        {
            fehlerBeschreibung="Es wurde keine Anweisung übergeben";
                return FEHLER;
        }
        else if(anweisung.charAt(anweisung.length()-1)!='=')
        {
            fehlerBeschreibung="Die Anweisung endet nicht mit '='";
                return FEHLER;
        }
        
        zuBerechnen=anweisung;
        double ergebnis;
        try
        {
            ergebnis=ausdruck ();
        }
        catch(UnsupportedOperationException uos)
        {
            return FEHLER;
        }
        
        if(aktuellePosition<zuBerechnen.length())
            {
                if(zuBerechnen.charAt(aktuellePosition-1)=='=')
                    fehlerBeschreibung="Syntaxfehler an der Position "
                         +aktuellePosition+": '=' ist nur am Ende erlaubt";
                else
                    fehlerBeschreibung=
                     "Syntaxfehler: '"+zuBerechnen.charAt(aktuellePosition-1)+"'"
                     +" ist an der Position "+aktuellePosition+" nicht erlaubt";
                return FEHLER;
            }
            return ergebnis+"";
    }
    
    public String gibFehlerBeschreibung()
    {
        return fehlerBeschreibung;
    }

    private TokenValue getToken()
    {
       char ch = zuBerechnen.charAt(aktuellePosition);
       switch (ch)
       {
          case '*':   
             currTok= TokenValue.MUL;
             break;
          case '/':   
             currTok= TokenValue.DIV;
             break;
          case '+':   
             currTok= TokenValue.PLUS;
             break;
          case '-':   
             currTok= TokenValue.MINUS;
             break;
          case '(':   
             currTok= TokenValue.LK;
             break;
          case ')':   
             currTok= TokenValue.RK;
             break;
          case '=':
             currTok= TokenValue.ASSIGN;   
             break;
          case '0':   
          case '1':   
          case '2':   
          case '3':   
          case '4':   
          case '5':   
          case '6':   
          case '7':   
          case '8':   
          case '9':   
          case ',':   
             numberValue=getNumber();
             currTok= TokenValue.ZAHL;
             return currTok;
          default:
            error("'"+ch+"' ist nicht im Eingabealphabet");
       }
       aktuellePosition++;
       return currTok;
    }            
    
    private double getNumber()
    {
        String num="";
        
        int pos=aktuellePosition;
        char ch = zuBerechnen.charAt(aktuellePosition);
        while (isPartOfNum(ch))
        {
            num += ch;
            ch = zuBerechnen.charAt(++aktuellePosition);
        }
        Scanner sc = new Scanner(num);
        if (sc.hasNextDouble())
            return sc.nextDouble();
        error(num+" ist keine gültige Zahl");
        return 0.0;
    }
        
    private boolean isPartOfNum(char ch)
    {
       switch (ch)
       {
          case '0':   
          case '1':   
          case '2':   
          case '3':   
          case '4':   
          case '5':   
          case '6':   
          case '7':   
          case '8':   
          case '9':   
          case ',':   
             return true;
          default:
             return false;
       }
    }
        
        
    private double ausdruck()
    {
        double left = term();
   
        while(true)
            switch (currTok)
            {
                case PLUS:
                    left += term();
                    break;
                case MINUS:
                    left -= term();
                    break;
                default:
                    return left;
           }
    }

    private double term()
    {               
       double left = primary();
       
       while(true)
          switch (currTok)
          {
             case MUL:
                 left *= primary();
                 break;
             case DIV:
                 int pos=aktuellePosition+1;
                 double divisor=primary();
                 if(divisor==0)
                    error("Division durch 0 an der Position "+pos
                      +" nicht möglich");
                 left /= divisor;
                 break;
             default:
                 return left;
          }
    }


    private double primary()
    {               
        getToken();
          
       switch (currTok)
       {
          case ZAHL:                                  
          {   
              double r = numberValue;
              getToken();
              return r;
          }
          case MINUS:
              return -primary();
          case LK:
          { 
              double r = ausdruck();
              if (currTok !=  TokenValue.RK)
                 error("Syntaxfehler: ')' wird an der Position "
                  +aktuellePosition+" benötigt");
              getToken();                             //Verschlucke ')'
              return r;
          }
          default:
          {
              error("Syntaxfehler: '"+zuBerechnen.charAt(aktuellePosition-1)
                   +"' ist an der Position "+aktuellePosition+" nicht erlaubt");
              return 0;
           }
       }
    }
    
    private void error(String fehlerMeldung)
    {
        fehlerBeschreibung=fehlerMeldung;
        throw new UnsupportedOperationException();
    }
}