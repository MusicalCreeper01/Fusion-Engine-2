package keithcod.es.fusionengine.world.generation;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class Generator {

    public static List<Generator> generators = new ArrayList<>();

    public String name = "Unnamed Generator";
    public String expression = "";

    public Generator (String name, String expression){
        this.name = name;
        this.expression = expression;

        generators.add(this);
    }

    Evaluator evaluator;

    public boolean use(int x, int y, int z){
        if(evaluator == null){
            evaluator = new Evaluator();
            evaluator.putFunction(new FunctionPerlin());
        }

        String processed = expression;
        processed = processed.replaceAll("x", Integer.toString(x));
        processed = processed.replaceAll("y", Integer.toString(y));
        processed = processed.replaceAll("z", Integer.toString(z));

        try {
            boolean b = evaluator.getBooleanResult(processed);
//            System.out.println(processed + ": " + b);
            return b;
        }catch(EvaluationException ex){

            System.out.println("Error running generation evaluation for " + name + " (" + processed + ")! " + ex.getMessage());

            return false;
        }
        /*Expression e = new ExpressionBuilder(expression)
                .variables("x", "y", "z")
                .build()
                .setVariable("x", x)
                .setVariable("y", y)
                .setVariable("z", z);
        boolean result = e.evaluate();
        return result;*/
    }

}
