package Network;

import Tetris.Experience;
import Tetris.TetrisEnvironment;
import Tetris.Transition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Main {
    public static Layer policyNetwork;
    public static Layer targetNetwork;
    public static TetrisEnvironment env;

    public static void main(String[] args) {

        env = new TetrisEnvironment("fast");

        //Initialisierung des Netzwerks
        policyNetwork = new Layer(new int[]{TetrisEnvironment.COLUMNS*TetrisEnvironment.ROWS + 8 , 128,64,32,6});
        targetNetwork = policyNetwork.clone();


        //Trainings Hyperparameter + Training
        int train_epochs = 1000;
        double stepsize = 0.000001; // 0.00001 -> Loss bei rund 6750
        int syncInterval = 16;




        //Training und Speichern des Netzwerks
        train(train_epochs,  stepsize, 0.99, 0.001, syncInterval);

        save_model(policyNetwork,"models/model_02" );

        env.initializeGraphics();
        env.setRenderMode("human");




        test(policyNetwork, 10);
    }






    public static void train( int train_epochs,  double stepsize, double discountFactor, double learningRate, int syncInterval){

        //Evaluierungsvariablen
        double loss;
        double current_average_loss = 0;
        double currentAverageReward = 0;
        double epsilon = 0.9;
        double endEpsilon = 0.1;
        double epsilonDecay = (epsilon - endEpsilon)/(train_epochs);

        Random r = new Random();

        ReplayMemory replayMemory = new ReplayMemory(100000);


        for(int i = 0; i < train_epochs; i++){

            Transition transition = env.reset();

            double [] state = copyFromIntArray(transition.state());

            while (!transition.terminated()){

                double [] qValues = policyNetwork.forward(state);


                //Determine action

                int action;

                if(Math.random() < epsilon){
                    action = r.nextInt(6);

                }
                else{

                    //Argmax des Outputs
                    action = argmax(qValues);
                }


                //Epsilon Decay



                transition = env.step(action);

                currentAverageReward += transition.reward();
//                System.out.println(transition.reward());


                double[] nextState = copyFromIntArray(transition.state());
                double [] nextQValues = targetNetwork.forward(nextState);
                //Berechnung der Targets



                if(transition.terminated()){
                    nextQValues[action] = transition.reward();
                }
                else{
                    nextQValues[action] = qValues[action] + learningRate * (transition.reward() + discountFactor * nextQValues[argmax(nextQValues)] - qValues[action]);
                }

                //Berechnung des Loss + Backwardpass
                loss = policyNetwork.calculate_loss(nextQValues, action);
                //System.out.println(loss);
                current_average_loss += loss;
                policyNetwork.backward();
                if(transition.reward() > 0){
                    Experience e = new Experience(state, action, transition.reward(), nextState, transition.terminated());
                    replayMemory.add(e);
                }

                if(replayMemory.memory.size() > 0){
                    for(int j = 0; j < 3; j++){
                        learnFromExperience(replayMemory, learningRate, discountFactor, stepsize);
                    }
                }






                state = nextState;
                policyNetwork.optimize(stepsize);
                policyNetwork.zero_grads();
            }




            //Erneutes Kopieren des Netzwerkes
            if(i % syncInterval == 0){
                targetNetwork = policyNetwork.clone();
            }




            //Alle 100 Epochen wird der Fortschritt auf der Konsole ausgegeben
            if(i %100 == 0 && i!=0){
                System.out.println("Epoch: " + i);
                System.out.println("Current Average Loss: " + current_average_loss/100);
                System.out.println("Current Average Reward: " + currentAverageReward/100);
                System.out.println("Epsilon:" + epsilon);
                System.out.println();
                current_average_loss = 0;
                currentAverageReward = 0;
            }

            epsilon -= epsilonDecay;

        }

    }

    public static void learnFromExperience(ReplayMemory replayMemory, double learningRate, double discountFactor, double stepsize){

        Experience e = replayMemory.sample();

        double [] qValues = policyNetwork.forward(e.state());
        double [] nextQValues = targetNetwork.forward(e.nextState());



        if(e.terminated()){
            nextQValues[e.action()] = e.reward();
        }
        else{
            nextQValues[e.action()] = qValues[e.action()] + learningRate * (e.reward() + discountFactor * argmax(nextQValues) - qValues[e.action()]);
        }


        policyNetwork.calculate_loss(nextQValues, e.action());
        policyNetwork.backward();
        policyNetwork.optimize(stepsize);
        policyNetwork.zero_grads();

    }

    public static double[] copyFromIntArray(int[] source) {
        double[] dest = new double[source.length];
        for(int i=0; i<source.length; i++) {
            dest[i] = source[i];
        }
        return dest;
    }

    public static int argmax(double[] array){
        int currentmax = 0;
        for(int j = 0; j < array.length; j++){

            if(array[currentmax] <array[j]){
                currentmax = j;
            }
        }
        return currentmax;
    }
    public static void test(Layer network, int test_epoch){




        for(int i = 0; i < test_epoch; i++) {
            Transition t = env.reset();
            while(!t.terminated()){
                double[] state = copyFromIntArray(t.state());
                double[] output = network.forward(state);
                int action = argmax(output);
                t = env.step(action);
            }
        }


    }

//
    public static void save_model(Layer network, String filepath){
        //Methode zum Speichern der Paramteter eines trainierten Modells in eine .txt Datei

        try (FileWriter writer = new FileWriter(filepath)) {

            int layer_count = 1;

            //Kopie des Netzwerkes, um nicht die referenz auf das erste Layer-Objekt zu behalten
            Layer network_copy = network;

            //Speichern der Input Dimensionen
            writer.write(String.valueOf(network_copy.getPrevious_anzahl_knoten()) + ",");

            //Einlesen der Dimensionen der Hidden-Layer + Zählen der Layer
            while(network_copy.has_next()){
                writer.write(String.valueOf(network_copy.getAnzahl_knoten()) + ",");
                network_copy = network_copy.getNext_layer();
                layer_count += 1;
            }

            //Die Output-Dimensionen werden separat gespeichert
            writer.write(String.valueOf(network_copy.getAnzahl_knoten()));
            writer.write("\n");

            //Erste Zeile des Dokuments gibt durch dieses Vorgehen die Dimensionen an: z.B. 784, 64, 64, 10



            //Einlesen der Parameter:

            //Iterieren über die Layer
            for(int k = 0; k <layer_count; k++){
                //k = aktueller Layer

                //Zugriff auf Knoten jedes Layers k
                Knoten[] knoten = network.getKnoten();


                //Iterieren über die Knoten
                for(int i = 0; i < knoten.length; i++){
                    //i = aktueller Knoten

                    //Zugriff auf die Gewichte des Knotens i
                    double[] weights = knoten[i].getWeights();

                    //Iterieren über das Gewichts-Array
                    for(int j = 0; j < weights.length; j++){
                        //j = aktuelles Gewicht


                        //Speichern der Gewichte
                        writer.write(String.valueOf(weights[j]));
                        writer.write(",");

                    }

                    //Seperates schreiben des Bias, da dieser nur einmal vorkommt
                    writer.write("\n");
                    String bias = String.valueOf(knoten[i].getBias());
                    writer.write(bias);
                    writer.write("\n");

                    //Die Parameter eines Knotens liegen folgendermaßen vor: gewicht_1, gewicht_2, ..., gewicht_j \n bias

                }
                network = network.getNext_layer();
            }
        }

        // Exception Thrown
        catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    public static Layer load_model(String filepath){
        //Laden der Parameter eines gespeicherten Modells aus einer .txt Datei

        try {
            BufferedReader reader =  new BufferedReader(new FileReader(filepath));

            //Einlesen der Dimensionen in der ersten Zeile
            //z.B. (784, 64, 64, 10)
            String [] string_dimensionen = reader.readLine().split(",");

            //Umwandlung von String-Array zum Integer-Array
            int [] dimensionen = new int[string_dimensionen.length];
            for (int i = 0; i < dimensionen.length; i++) {
                dimensionen[i] = Integer.parseInt(string_dimensionen[i]);
            }

            //Erstellen des Netzwerks, in das die Parameter geladen werden + Kopie des ersten Layer-Objekts, um die Referenz beizubehalten
            Layer network = new Layer(dimensionen);
            Layer network_copy = network;


            for(int i = 1; i < dimensionen.length; i++){
                //i = layer
                //i startet bei 1, da die erste Angabe die Input-Dimensionen sind, für die man keinen extra Layer instanziiert


                for(int j = 0; j < dimensionen[i]; j++){
                    //j = knoten



                    //Einlesen der weights des Layer j und Umwandeln in double
                    String[] string_weights = reader.readLine().split(",");
                    double [] weights = new double[string_weights.length];
                    for(int l = 0; l < weights.length; l++){
                        weights[l] = Double.parseDouble(string_weights[l]);
                    }

                    //Speichern der weights in den jeweiligen Knoten
                    network_copy.getKnoten()[j].setWeights(weights);

                    //Lesen des bias und Speichern im Netzwerk
                    String string_bias = reader.readLine();
                    network_copy.getKnoten()[j].setBias(Double.parseDouble(string_bias));

                }
                network_copy = network_copy.getNext_layer();
            }
            System.out.println(filepath + " geladen");
            System.out.println();
            return network;
        }

        catch (IOException e) {
            System.out.println("Fehelaaar");
            throw new RuntimeException(e);

        }


    }

}
