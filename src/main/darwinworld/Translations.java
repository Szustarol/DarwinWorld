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
    "apply_config", "reset_config", "initial_setup", "engine_setup", "time_delay", "start_sim", "pause_sim", "step_sim",
    "animal_grass_chart", "epoch", "number", "animal_count", "grass_count", "animal_energy", "animal_position",
    "animal_genotype", "dominant_genotype", "avg_lifespan", "avg_children", "avg_energy", "food_starting",
    "click_trace", "select_epoch_n", "select_epoch_n_desc", "tracing_result", "n_children", "n_descendants",
    "n_epochs", "two_map", "select_epoch_n_desc2", "select_save_path"};

    public static String [] tr_pl_PL = {"Początkowa liczba zwierząt", "Szerokość mapy", "Wysokość mapy", "Procentowy rozmiar dżungli",
    "Wartość energetyczna pożywienia", "Dzienny spadek energii", "Zastosuj konfigurację", "Reset konfiguracji",
    "Ustawienia początkowe", "Ustawienia silnika", "Okres aktualizacji mapy (ms)", "Rozpocznij symulację",
    "Zatrzymaj symulację", "Wykonaj krok", "Ilość zwierząt oraz trawy na mapie", "Krok", "Ilość", "Ilość zwierząt",
    "Ilość trawy", "Energia zwierzęcia", "Pozycja zwierzęcia", "Genotyp zwierzęcia", "Genotyp dominujący",
    "Średni czas życia w populacji", "Średnia ilóść dzieci w populacji", "Średnia energia w populacji", "Poczatkowa energia",
    "Kliknij tutaj, aby śledzić zwierzę", "Wybierz liczbę kroków", "Wybierz liczbę kroków do śledzenia zwierzęcia",
    "Wyniki śledzenia", "Liczba dzieci", "Liczba potomków", "Liczba kroków", "Pokaż dwie mapy", "Wybierz liczbę kroków, po której zapisać statystyki",
    "Podaj ścieżkę zapisu"};


    public static void init(String lang){
        translations = new TreeMap<>();
        switch(lang){
            case "pl_PL":
                for(int i = 0; i < names.length; i++){
                    translations.put(names[i], tr_pl_PL[i]);
                }
                break;
            default:
                throw new IllegalArgumentException("This translation language is not supported");
        }
    }
}
