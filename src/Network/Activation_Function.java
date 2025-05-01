package Network;

public class Activation_Function {
    String activation_function;
    public Activation_Function(String activation_function){
        this.activation_function = activation_function;

    }
    public void forward(Knoten[] knoten){


        if(activation_function.equals("Sigmoid")){
            for(int i = 0; i < knoten.length; i++){
                knoten[i].setValue(1.0/(1.0 + Math.exp(-knoten[i].getValue()))); //Sigmoid activation
            }
        }

        if(activation_function.equals("Softmax") ){
            double sum = 0.0;
            for(int i = 0; i < knoten.length; i++){
                sum += Math.exp(knoten[i].getValue());
            }

            for(int i = 0; i < knoten.length; i++){
                knoten[i].setValue(Math.exp(knoten[i].getValue())/sum);
            }
        }


    }

    public void change_gradients(Knoten[] knoten) {

        //if(activation_function.equals("Softmax") ){
        //  Wird durch die Calculate loss Funktion implementiert (Kombination aus Cross-Entropy-Loss und Softmax-Funktion)
        //}

        if (activation_function.equals("Sigmoid")) {

            for (int i = 0; i < knoten.length; i++) {
                //Ableitung der Sigmoid-Funktion mit dem Gradienten des Knotens multipliziert (Kettenregel)
                knoten[i].multiply_value_grad(knoten[i].getValue() * (1.0 - knoten[i].getValue()));
            }
        }
    }
}

