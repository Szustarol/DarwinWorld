package main.darwinworld.logic;

import main.darwinworld.map.MapDirection;
import main.darwinworld.math.Vector2D;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class Genotype {

    private Random generator;
    private int geneCount[];
    private int nGenes = 0;

    private void validate(){
        //genes flow down from neighbors if there is not enough of them
        boolean valid = false;
        while(!valid){
            valid = true;
            for(int i = 0; i < 8; i++){
                if(geneCount[i] == 0) {
                    int prev = Math.floorMod(i-1,8);
                    int next = Math.floorMod(i+1, 8);
                    if(geneCount[prev] > 0) {
                        geneCount[prev]--;
                        geneCount[i]++;
                    }
                    if(geneCount[next] > 0){
                        geneCount[next]--;
                        geneCount[i]++;
                    }
                }
            }
        }
    }

    public int[] asArray(){
        return Arrays.copyOf(geneCount, geneCount.length);
    }

    public static Genotype fromArray(int [] genes){
        Genotype g = new Genotype();
        g.geneCount = Arrays.copyOf(genes, 8);
        return g;
    }

    @Override
    public String toString(){
        String s = "";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < geneCount.length; i++){
            for(int j = 0; j < geneCount[i]; j++){
                sb.append(i);
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private Genotype(){
        //raw constructor for giveBirth
    }

    public Genotype(int nGenes){
        this.nGenes = nGenes;
        generator = new Random();
        geneCount = new int[]{0,0,0,0,0,0,0,0};

        for(int i = 0; i < nGenes; i++){
            Integer c = Math.floorMod(generator.nextInt(), 8);
            geneCount[c] += 1;
        }

        validate();
    }

    public boolean equals(Genotype other){
        if(other == null) return false;
        for(int i = 0; i < 8; i++){
            if(this.geneCount[i] != other.geneCount[i])
                return false;
        }
        return true;
    }

    public Genotype giveBirth(Genotype other){
        Genotype g = new Genotype();
        g.nGenes = this.nGenes;
        g.geneCount = new int[]{0,0,0,0,0,0,0,0};
        g.generator = new Random();
        int split = Math.floorMod(generator.nextInt(), nGenes-2)+1;
        int split2 = split;
        while(split == split2){
            split2 = Math.floorMod(generator.nextInt(), nGenes-2)+1;
        }
        if(split2 > split){
            int temp = split;
            split = split2;
            split2 = temp;
        }

        int thisCountSums[] = new int[]{geneCount[0],0,0,0,0,0,0,0};
        int otherCountSums[] = new int[]{other.geneCount[0],0,0,0,0,0,0,0};
        for(int i = 1; i < 8; i++){
            thisCountSums[i] = thisCountSums[i-1] + geneCount[i];
            otherCountSums[i] = otherCountSums[i-1] + other.geneCount[i];
        }


        int thisidx = 0, otheridx = 0;
        for(int i = 0; i < nGenes; i++){
            while (thisCountSums[thisidx] <= i) thisidx++;
            while (otherCountSums[otheridx] <= i) otheridx++;
            if(i < split || i > split2){//take from this
                g.geneCount[thisidx]++;
            }
            else{//take from the other
                g.geneCount[otheridx]++;
            }
        }
        g.validate();
        return g;
    }

    public MapDirection getMove(){
        int r = Math.floorMod(generator.nextInt(), nGenes);
        int index = -1;
        while(r >= 0){
            r -= geneCount[++index];
        }
        return MapDirection.values()[index];
    }
}
