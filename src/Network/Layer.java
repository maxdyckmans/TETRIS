package Network;

public class Layer {

    private static double loss;

    //Anzahl der Layer
    private static int count = 1;

    //Index der Layer
    private int index;

    //Information zu den Knoten
    private int anzahl_knoten;
    private Knoten knoten[];
    private Activation_Function activationFunction;

    //Information zu den anderen Layer
    private Layer next_layer;
    private int previous_anzahl_knoten;




    public Layer(int[] dimensionen){
        this.index = count;
        count++;

        //Relevanten Dimensionen des Netzwerks werden gespeichert
        anzahl_knoten = dimensionen[index];
        previous_anzahl_knoten = dimensionen[index - 1];

        //Knotenobjekte des Layers werden erstellt und in "knoten" gespeichert
        knoten = new Knoten[anzahl_knoten];


        //Initialisierung der Knoten
        for(int i = 0; i < anzahl_knoten; i++){
            knoten[i] = new Knoten(i, previous_anzahl_knoten);
        }

        //Wenn nach diesem Layer ein Weiterer Layer kommt...
        if(count < dimensionen.length){

            //...wird die Sigmoid-aktivierungsfunktion verwendet...
            activationFunction = new Activation_Function("Sigmoid");


            //...und der nächste Layer initialisiert
            next_layer = new Layer(dimensionen);
        }
        else{
            //Wenn nach diesem Layer kein weiterer Layer kommt, wird die Softmax-Funktion verwendet
            //activationFunction = new Activation_Function("Softmax");

            //Der count wird zurückgesetzt, da weitere Netzwerke erstellt werden könnten
            count = 1;
        }

    }

    public Layer(Layer next_layer,int index, int anzahl_knoten, Knoten[] knoten, Activation_Function activationFunction,  int previous_anzahl_knoten) {
        this.index = index;
        this.anzahl_knoten = anzahl_knoten;
        this.knoten = knoten;
        this.activationFunction = activationFunction;
        this.next_layer = next_layer;
        this.previous_anzahl_knoten = previous_anzahl_knoten;
    }


    public Knoten[] forward(Knoten [] input){
        //"input" sind die Knoten des vorherigen Layers


        for(int i = 0; i < knoten.length; i++){

            //Jeder Knoten des aktuellen Layers bekommt die Knoten des vorherigen Layers, um den eigenen Wert zu berechnen
            knoten[i].forward(input);
        }

        //Sobald die Knoten ihr "rohes" Output berechnet haben, wird dies durch die Aktivierungsfunktion verändert
        if(activationFunction != null){
            activationFunction.forward(knoten);
        }




        if(has_next()){
            //Ergebnis (in den Knoten) wird an den nächsten Layer weitergegeben
            return next_layer.forward(knoten);
        }
        else {
            //Ergebnis wird zurückgegeben
            return knoten;
        }

    }

    //Forwardmethode, um das Input (double) in Knotenobjekte umzuwandeln
    public double[] forward(double [] input){
        //Test, ob Dimensionen der Eingabe mit den Dimensionen des Inputlayers übereinstimmen
        if(input.length != previous_anzahl_knoten) {
            System.out.println("Ungülige Eingabe Dimension: " + input.length + "Erwartete Eingabe Dimension: " + previous_anzahl_knoten);
            return new double[0];
        }

        //Umformung der double Inputs in Knoten, um den forward pass zu vereinfachen
        // (ansonsten müsste man immer das Ergebnis aller Knoten in ein Array Speichern und weitergeben, was redundant wäre)
        Knoten [] transformed_input = new Knoten[input.length];
        for(int i = 0; i < input.length; i++){
            transformed_input[i] = new Knoten(1.0/(1.0 + Math.exp(-input[i]))); //Umformung in Knoten Objekt und Normaizsierung mit Sigmoid
        }

        //Aufruf der Eigentlichen forward Methode
        Knoten [] knoten_output = forward(transformed_input);


        //Umformen des vom Netzwerk generierten Outputs zurück in ein double-Array
        double [] double_output = new double[knoten_output.length];
        for(int i = 0; i < double_output.length; i++){
            double_output[i] = knoten_output[i].getValue();
        }







        return double_output;
    }

    public void zero_grads(){
        //Methode zum nullen der Gradienten (nach jedem Batch)

        for (int i = 0; i < knoten.length; i++){
            knoten[i].zero_grads();
        }
        if(this.has_next()){
            next_layer.zero_grads();
        }
    }

    public void backward(){
        //Backward pass muss von hinten anfangen, deswegen wird zunächst die Backward Methode des nächsten Layers aufgerufen
        if(this.has_next()){
            next_layer.backward();
        }

        //Gradient der Aktivierungsfunktion wird von der Klasse Aktivierungsfunktion in allen Knoten gesetzt
        if(activationFunction != null){
            activationFunction.change_gradients(knoten);
        }


        //Aufruf der Backwardmethode der Knoten
        for(int i = 0; i < knoten.length; i++){
            knoten[i].backward();
        }


    }


    public double calculate_loss(double [] labels, int action){

        loss = 0.0;

        //loss wird vom outputlayer berechnet
        if(this.has_next()) {
            return next_layer.calculate_loss(labels, action);
        }
        else{

            //Test, ob die gegebenen Labels passen
            if(labels.length != knoten.length){
                System.out.println("Unzlässige Dimensionen des Labels: " + labels.length + "Geforderte Dimension: " + knoten.length);
                return 0.0;
            }




            for(int i = 0; i < knoten.length; i++){

                if(i == action){
                    double diff = -(labels[i] - knoten[i].getValue());
                    loss = diff * diff; //MSE Loss
                    knoten[i].setValue_gradient(2*diff);
                }
                else{
                    knoten[i].setValue_gradient(0);
                }







            }
        }
        return loss;

    }

    public void optimize(double stepsize){

        //Aufruf der Optimisierungsfunktion in allen Knoten
        for(int i = 0; i < knoten.length; i++){
            knoten[i].optimize(stepsize);
        }
        if(this.has_next()){
            next_layer.optimize(stepsize);
        }
    }

    public boolean has_next(){
        return next_layer!=null;
    }

    //Getter-Methoden
    public Layer getNext_layer() {
        return next_layer;
    }
    public Knoten[] getKnoten(){
        return knoten;
    }

    public int getAnzahl_knoten() {
        return anzahl_knoten;
    }

    public int getPrevious_anzahl_knoten() {
        return previous_anzahl_knoten;
    }


    @Override
    public Layer clone(){
        if(this.has_next()){
            return new Layer(next_layer.clone(), index,anzahl_knoten,knoten.clone(), activationFunction, previous_anzahl_knoten);
        }
        else return  new Layer(null, index,anzahl_knoten,knoten.clone(), activationFunction, previous_anzahl_knoten);
    }
}



