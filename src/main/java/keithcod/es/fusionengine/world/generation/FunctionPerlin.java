package keithcod.es.fusionengine.world.generation;

import jLibNoise.noise.module.Perlin;
import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.*;

import java.util.ArrayList;

/**
 * Created by ldd20 on 12/3/2016.
 */
public class FunctionPerlin implements Function {

    public String getName() {
        return "perlin";
    }

    /**
     * Executes the function for the specified argument. This method is called
     * internally by Evaluator.
     *
     * @param evaluator
     *            An instance of Evaluator.
     * @param arguments
     *            A string argument that will be converted into a string that is
     *            in reverse order. The string argument(s) HAS to be enclosed in
     *            quotes. White space that is not enclosed within quotes will be
     *            trimmed. Quote characters in the first and last positions of
     *            any string argument (after being trimmed) will be removed
     *            also. The quote characters used must be the same as the quote
     *            characters used by the current instance of Evaluator. If there
     *            are multiple arguments, they must be separated by a comma
     *            (",").
     *
     * @return The source string in reverse order.
     *
     * @exception FunctionException
     *                Thrown if the argument(s) are not valid for this function.
     */
    public FunctionResult execute(Evaluator evaluator, String arguments)
            throws FunctionException {


        Double result = null;
        ArrayList numbers = FunctionHelper.getDoubles(arguments, ',');
        if(numbers.size() < 3) {
            throw new FunctionException("Three numeric arguments are required.");
        } else {
            try {
                int seed =        ((Double)numbers.get(0)).intValue();
                double x =           ((Double)numbers.get(1)).doubleValue();
                double y =           ((Double)numbers.get(2)).doubleValue();

                double freq = .05d;
                double persistence = 0.1d;
                int octaves =     4;
                double lacunarity =  .01d;

                if(numbers.size() > 3)
                    freq =        ((Double)numbers.get(3)).doubleValue();
                if(numbers.size() > 4)
                    persistence = ((Double)numbers.get(4)).doubleValue();
                if(numbers.size() > 5)
                    octaves =     ((Double)numbers.get(5)).intValue();
                if(numbers.size() > 6)
                    lacunarity =  ((Double)numbers.get(6)).doubleValue();

                Perlin myModule = new Perlin();
                myModule.setSeed(seed);
                myModule.setFrequency(freq);
                myModule.setPersistence(persistence);
                myModule.setOctaveCount(octaves);
                myModule.setLacunarity(lacunarity);

                result = myModule.getValue((x), (y), .1);

            } catch (Exception var9) {
                throw new FunctionException("Two numeric arguments are required.", var9);
            }

            return new FunctionResult(result.toString(), 0);
        }


        /*String result = "";

        try {
            String stringValue = new Evaluator().evaluate(arguments, true,
                    false);

            String argumentOne = FunctionHelper.trimAndRemoveQuoteChars(
                    stringValue, evaluator.getQuoteCharacter());

            int size = argumentOne.length();
            for (int index = size - 1; index >= 0; index--) {
                result = result + argumentOne.charAt(index);
            }
        } catch (FunctionException fe) {
            throw new FunctionException(fe.getMessage(), fe);
        } catch (EvaluationException ee) {
            throw new FunctionException("Invalid expression in arguments.", ee);
        } catch (Exception e) {
            throw new FunctionException("One string argument is required.", e);
        }

        return new FunctionResult(result,
                FunctionConstants.FUNCTION_RESULT_TYPE_STRING);*/
    }

}
