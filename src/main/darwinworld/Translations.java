package main.darwinworld;

import java.util.TreeMap;

public class Translations {
    public static TreeMap<String, String> translations;

    public static String getTranslation(String stringName){
        if(!translations.containsKey(stringName))
            throw new IllegalArgumentException("This translation was not found: "+ stringName);
        return translations.get(stringName);
    }

    public static String [] names = {"n_animals", "map_width", "map_height", "jungle_percent", "food_value", "food_decay",
    "apply_config", "reset_config", "initial_setup", "engine_setup", "time_delay", "start_sim", "pause_sim", "step_sim"};
    public static String [] tr_pl_PL = {"Początkowa liczba zwierząt", "Szerokość mapy", "Wysokość mapy", "Procentowy rozmiar dżungli",
    "Wartość energetyczna pożywienia", "Dzienny spadek energii", "Zastosuj konfigurację", "Reset konfiguracji",
    "Ustawienia początkowe", "Ustawienia silnika", "Okres aktualizacji mapy (ms)", "Rozpocznij symulację",
    "Zatrzymaj symulację", "Wykonaj krok"};


    public static void init(String lang){
        translations = new TreeMap<>();
        switch(lang){
            case "pl_PL":
                for(int i = 0; i < names.length; i++){
                    translations.put(names[i], tr_pl_PL[i]);
                }
                break;
        }
    }
}
