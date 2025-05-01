package Network;

import Tetris.Experience;
import java.util.ArrayList;
import java.util.Random;

public class ReplayMemory {

    ArrayList<Experience> memory;
    int maxSize;
    Random r = new Random();

    public ReplayMemory(int maxSize){
        this.maxSize = maxSize;
        memory = new ArrayList<>();
        memory.ensureCapacity(maxSize);
    }

    public void add(Experience e){
        memory.add(e);

        if(memory.size() > maxSize){
            memory.removeFirst();
        }
    }

    public Experience sample (){
        return memory.get(r.nextInt(memory.size()));
    }



}
