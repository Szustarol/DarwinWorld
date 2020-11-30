package test.darwinworld;

import main.darwinworld.logic.Genotype;
import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

public class GenotypeTests {
    @Test
    public void genotypeValidityTest(){
        Genotype genotype = Genotype.fromArray(new int[]{8,8,8,0,0});
        //after validation should not contain zeros
        for(int i : genotype.asArray()){
            assertNotEquals(i, 0);
        }
    }

    @Test
    public void genotypeBirthTest(){
        Genotype genotype = new Genotype(32);
        Genotype genotype2 = new Genotype(32);
        Genotype newGenotype = genotype.giveBirth(genotype2);
        for(int i : newGenotype.asArray()){
            assertNotEquals(i, 0);
        }
    }
}
