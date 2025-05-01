package Network;

import java.lang.Math;

public class Knoten {
    //Implementierung eines Knotens eines Neuronalen Netzwerkes


    private int index;

    //Zahlenwert, den der Knoten annimmt/repräsentiert
    private double value;

    //Paramteter
    private Knoten [] input;
    private double [] weights;
    private double bias;

    //Gradienten
    private double [] weight_gradients;
    private double bias_gradient;
    private double value_gradient;


    //Konstruktoren
    public Knoten(double value){
        //Konstruktor um Knoten Objekte ohne gewichte zu erstellen (wird für die Umwandlung vom Input benötigt)
        this.value = value;
    }

    public Knoten(int index, double value, Knoten[] input, double[] weights, double bias, double[] weight_gradients, double bias_gradient, double value_gradient) {
        this.index = index;
        this.value = value;
        this.input = input;
        this.weights = weights;
        this.bias = bias;
        this.weight_gradients = weight_gradients;
        this.bias_gradient = bias_gradient;
        this.value_gradient = value_gradient;
    }

    public Knoten(int index, int anzahl_weights){
        this.index = index;

        //Zufällige initialisierung der Parameter

        bias =  Math.random()-0.5; //Wenn man anstatt dem Intervall [-0.5 ; 0.5] das intervall [0 ; 1] nimmt, lernt das Netzwerk nicht (finde ich interessant)
        weights = new double[anzahl_weights];
        weight_gradients = new double[anzahl_weights];

        for(int i = 0; i < anzahl_weights; i++){
            weights[i] = Math.random()-0.5; //Wenn man anstatt dem Intervall [-0.5 ; 0.5] das intervall [0 ; 1] nimmt, lernt das Netzwerk nicht (finde ich interessant)
        }


    }


    public double forward(Knoten [] input){

        value = 0.0;

        //Abspeichern für den Backward Pass
        this.input = input;


        for(int i = 0; i < input.length; i++){

            //Berechnung für den Forward Pass
            value += input[i].getValue() * weights[i];


        }
        value += bias;



        return value;
    }


    public void zero_grads(){
        //nullen aller Gradienten
        this.value_gradient = 0.0;
        this.bias_gradient = 0.0;
        for(int i = 0; i < weights.length; i++){
            weight_gradients[i] = 0.0;
        }
    }


    public void backward(){

        for(int i = 0; i < weight_gradients.length; i++){
            //Gradient des Gewichts ergibt sich aus dem Wert des inputs multipliziert mit dem Gradienten des Ergebnisses (Kettenregel)
            weight_gradients[i] += input[i].getValue() * value_gradient;
        }


        for(int i = 0; i < input.length; i++){
            //Verändern der Valuegradients der Knoten des vorherigen Layers
            //dies ist nötig, da die Knotenobjekte keinen direkten Zugriff auf den nächsten Layer haben,
            //und somit den eigenen Gradienten nicht selber berechnen können
            input[i].add_value_grad(weights[i]*value_gradient);
        }

        bias_gradient += value_gradient;

    }


    public void optimize(double stepsize){
        //Verändern der Parameter anhand der berechneten Gradienten (In entgegengesetzter Richtung)
        for(int i = 0; i < weights.length; i++) {
            weights[i]-= weight_gradients[i] * stepsize; //"stepsize" gibt das Lerntempo an
        }
        bias -= bias_gradient * stepsize;
    }



    //Methoden, um die Gradienten entweder mit einer Zahl zu multiplizieren oder zu addieren

    public void add_value_grad(double change){
        value_gradient += change;
    }
    public void multiply_value_grad(double change){
        value_gradient = value_gradient * change;
    }

    //Getter/Setter
    public double getValue(){
        return value;
    }
    public void setValue_gradient(double value_gradient) {
        this.value_gradient = value_gradient;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public double[] getWeights(){
        return weights;
    }
    public double getBias(){
        return bias;
    }
    public void setWeights(double[] weights){
        this.weights = weights;
    }
    public void setBias(double bias){
        this.bias = bias;
    }

    @Override
    public Knoten clone(){
        return new Knoten(index, value, input, weights, bias, weight_gradients, bias_gradient, value_gradient);
    }
}
