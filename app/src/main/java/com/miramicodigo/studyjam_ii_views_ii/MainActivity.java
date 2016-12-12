package com.miramicodigo.studyjam_ii_views_ii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {
    private TextView tvResultado, tvUno, tvDos, tvTres, tvCuatro, tvCinco, tvSeis, tvSiete, tvOcho, tvNueve, tvCero, tvDivision, tvMultiplicacion, tvResta, tvSuma, tvComa;
    private String cadena = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        tvUno=(TextView)findViewById(R.id.tvUno);
        tvDos=(TextView)findViewById(R.id.tvDos);
        tvTres=(TextView)findViewById(R.id.tvTres);
        tvCuatro=(TextView)findViewById(R.id.tvCuatro);
        tvCinco=(TextView)findViewById(R.id.tvCinco);
        tvSeis=(TextView)findViewById(R.id.tvSeis);
        tvSiete=(TextView)findViewById(R.id.tvSiete);
        tvOcho=(TextView)findViewById(R.id.tvOcho);
        tvNueve=(TextView)findViewById(R.id.tvNueve);
        tvCero=(TextView)findViewById(R.id.tvCero);

        tvComa=(TextView)findViewById(R.id.tvComa);
        tvDivision=(TextView)findViewById(R.id.tvDivision);
        tvMultiplicacion=(TextView)findViewById(R.id.tvMultiplicacion);
        tvResta=(TextView)findViewById(R.id.tvResta);
        tvSuma=(TextView)findViewById(R.id.tvSuma);


    }

    public void crearCadena (View v)
    {
        TextView [] vecNumeros={tvUno, tvDos, tvTres, tvCuatro, tvCinco, tvSeis, tvSiete, tvOcho, tvNueve, tvCero, tvDivision, tvMultiplicacion, tvResta, tvSuma, tvComa};

        for( int i=0; i<vecNumeros.length; i++ )
        {
            if( v.getId() == vecNumeros[i].getId() )
            {
                cadena = cadena + vecNumeros[i].getText()+"";
            }
        }
       mostrar(cadena);
    }

    public void mostrar(String cad)
    {
        tvResultado=(TextView)findViewById(R.id.tvResultado);
        tvResultado.setText(cad);
    }

    public void borrar (View v)
    {
        if(!cadena.equals(""))
        {
            cadena = cadena.substring(0, cadena.length()-1);
            mostrar(cadena);
        }

    }

   public void calcular(View v)
   {
       //Depurar la expresion algebraica
       String expr = depurar(cadena);
       String[] arrayInfix = expr.split(" ");

       //Declaración de las pilas
       Stack < String > E = new Stack < String > (); //Pila entrada
       Stack < String > P = new Stack < String > (); //Pila temporal para operadores
       Stack < String > S = new Stack < String > (); //Pila salida

       //Añadir la array a la Pila de entrada (E)
       for (int i = arrayInfix.length - 1; i >= 0; i--) {
           E.push(arrayInfix[i]);
       }

       try {
           //Algoritmo Infijo a Postfijo
           while (!E.isEmpty()) {
               switch (pref(E.peek())){
                   case 1:
                       P.push(E.pop());
                       break;
                   case 3:
                   case 4:
                       while(pref(P.peek()) >= pref(E.peek())) {
                           S.push(P.pop());
                       }
                       P.push(E.pop());
                       break;
                   case 2:
                       while(!P.peek().equals("(")) {
                           S.push(P.pop());
                       }
                       P.pop();
                       E.pop();
                       break;
                   default:
                       S.push(E.pop());
               }
           }

           //Eliminacion de `impurezas´ en la expresiones algebraicas
           String infix = expr.replace(" ", "");
           String postfix = S.toString().replaceAll("[\\]\\[,]", "");

           //Mostrar resultados:
           String[] post = postfix.split(" ");
           calcular1(post);

       }catch(Exception ex){
           System.out.println("Error en la expresión algebraica");
           System.err.println(ex);
       }
   }

    //Depurar expresión algebraica
    private static String depurar(String s) {
        s = s.replaceAll("\\s+", ""); //Elimina espacios en blanco
        s = "(" + s + ")";
        String simbols = "+-x/()";
        String str = "";

        //Deja espacios entre operadores
        for (int i = 0; i < s.length(); i++) {
            if (simbols.contains("" + s.charAt(i))) {
                str += " " + s.charAt(i) + " ";
            }else str += s.charAt(i);
        }
        return str.replaceAll("\\s+", " ").trim();
    }

    //Jerarquia de los operadores
    private static int pref(String op) {
        int prf = 99;
        if (op.equals("^")) prf = 5;
        if (op.equals("x") || op.equals("/")) prf = 4;
        if (op.equals("+") || op.equals("-")) prf = 3;
        if (op.equals(")")) prf = 2;
        if (op.equals("(")) prf = 1;
        return prf;
    }

    public void calcular1(String[] cad)
    {
        //Declaración de las pilas
        Stack < String > E = new Stack < String > (); //Pila entrada
        Stack < String > P = new Stack < String > (); //Pila de operandos

        //Añadir post (array) a la Pila de entrada (E)
        for (int i = cad.length - 1; i >= 0; i--) {
            E.push(cad[i]);
        }

        //Algoritmo de Evaluación Postfija
        String operadores = "+-x/";
        while (!E.isEmpty()) {
            if (operadores.contains("" + E.peek())) {
                P.push(evaluar(E.pop(), P.pop(), P.pop()) + "");
            }else {
                P.push(E.pop());
            }
        }

       /* //Mostrar resultados:
        System.out.println("Expresion: " + expr);
        System.out.println("Resultado: " + P.peek());*/

        if( Double.parseDouble(P.peek()) % 1 == 0 )
        {
            P.push(P.peek().substring(0, P.peek().length() - 2));
            cadena = P.peek();
            mostrar(P.peek());
        }
        else
        {
            double numero = Math.rint(Double.parseDouble(P.peek())*100)/100;
            //Toast.makeText(getApplicationContext(),numero+"",Toast.LENGTH_LONG).show();
            cadena=numero+"";
            mostrar(numero+"");
        }

    }

    private static double evaluar(String op, String n2, String n1) {
        double num1 = Double.parseDouble(n1);
        double num2 = Double.parseDouble(n2);
        if (op.equals("+")) return (num1 + num2);
        if (op.equals("-")) return (num1 - num2);
        if (op.equals("x")) return (num1 * num2);
        if (op.equals("/")) return (num1 / num2);
        return 0;
    }

}



