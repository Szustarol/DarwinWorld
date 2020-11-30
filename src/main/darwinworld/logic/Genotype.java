package main.darwinworld.logic;

import jdk.internal.util.xml.impl.Pair;
import main.darwinworld.map.MapDirection;

import java.util.Arrays;
import java.util.Random;

public class Genotype {

    private Random generator;
    private int[] geneCount;
    private int nGenes = 0;

    private void validate(){
        //genes flow down from neighbors if there is not enough of them
        boolean valid = false;
        while(!valid){
            valid = true;
            for(int i = 0; i < 8; i++){
                if(geneCount[i] == 0) {
                    valid = false;
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
        Genotype genotype = new Genotype();
        genotype.geneCount = Arrays.copyOf(genes, 8);
        genotype.validate();
        return genotype;
    }

    @Override
    public String toString(){
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
            int c = Math.floorMod(generator.nextInt(), 8);
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

    private int[] getSplitPositions(){
        int split = Math.floorMod(generator.nextInt(), nGenes-2)+1;
        int split2 = Math.floorMod(generator.nextInt(), nGenes-2)+1;
        while(split == split2){
            split2 = Math.floorMod(generator.nextInt(), nGenes-2)+1;
        }
        if(split2 > split){
            int temp = split;
            split = split2;
            split2 = temp;
        }
        return new int[]{split, split2};
    }

    public Genotype giveBirth(Genotype other){
        Genotype newGenotype = new Genotype();
        newGenotype.nGenes = this.nGenes;
        newGenotype.geneCount = new int[]{0,0,0,0,0,0,0,0};
        newGenotype.generator = new Random();

        int [] splits = getSplitPositions();

        int[] thisCountSums = new int[]{geneCount[0],0,0,0,0,0,0,0};
        int[] otherCountSums = new int[]{other.geneCount[0],0,0,0,0,0,0,0};
        for(int i = 1; i < 8; i++){
            thisCountSums[i] = thisCountSums[i-1] + geneCount[i];
            otherCountSums[i] = otherCountSums[i-1] + other.geneCount[i];
        }

        int thisidx = 0, otheridx = 0;
        for(int i = 0; i < nGenes; i++){
            while (thisCountSums[thisidx] <= i) thisidx++;
            while (otherCountSums[otheridx] <= i) otheridx++;
            if(i < splits[0] || i > splits[1]){//take from this
                newGenotype.geneCount[thisidx]++;
            }
            else{//take from the other
                newGenotype.geneCount[otheridx]++;
            }
        }
        newGenotype.validate();
        return newGenotype;
    }

    public MapDirection getMove(){
        int randomInt = Math.floorMod(generator.nextInt(), nGenes);
        int index = -1;
        while(randomInt >= 0){
            randomInt -= geneCount[++index];
        }
        return MapDirection.values()[index];
    }
}
