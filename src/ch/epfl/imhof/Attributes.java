package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Permet de décrire les caractéristiques non géométriques des entités
 * géométriques, (par exemple : couleurs, etc...).
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 */
public final class Attributes {
    private final Map<String, String> attributes;

    /**
     * Permet de construire un ensemble d'attributs clef/valeur en utilisant les
     * données présente dans la map donnée en paramètre.
     * 
     * @param attributes
     */
    public Attributes(Map<String, String> attributes) {
        this.attributes = Collections
                .unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * Retourne vrai si et seulement si l'ensemble d'attributs est vide retourne
     * faux sinon
     * 
     * @return un boolean : true/false
     */
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    /**
     * Retourne vrai si l'ensemble d'attributs contient la clef donnée.
     * 
     * @param String
     *            key
     * @return true/false
     */
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée, ou null si elle n'existe
     * pas.
     * 
     * @param key
     * @return valeur de la clef ou null
     */
    public String get(String key) {
        return attributes.get(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée en paramètres. Si celle ci
     * n'as pas de valeur associée, elle retourne defaultValue.
     * 
     * @param key
     * @param defaultValue
     * @return valeur de la clef ou defaultValue
     */
    public String get(String key, String defaultValue) {
        return attributes.getOrDefault(key, defaultValue);
    }

    /**
     * Retourne l'entier associé à la clef donnée en paramètres. Si celle ci
     * n'as pas de valeur associée ou elle n'est pas un entier valide, elle
     * retourne defaultValue.
     * 
     * @param key
     * @param defaultValue
     * @return valeur de la clef ou defaultValue
     */
    public int get(String key, int defaultValue) {
        if (contains(key)) {
            try {
                int i = Integer.parseInt(get(key));
                return i;
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Retourne une version filtrée des attributs ne contenant que ceux dont le
     * nom figure dans keysToKeep.
     * 
     * @param keysToKeep
     * @return new Attributes()
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Builder map = new Builder();
        for (String s : keysToKeep) {
            if (contains(s)) {
                map.put(s, get(s));
            }
        }
        return map.build();
    }

    /**
     * Bâtisseur de la classe Attributes qui permet de construire un nouvel
     * Attributes.
     * 
     * @author Charles Parzy Turlat (250628)
     * @author Mikael Morales Gonzalez (234747)
     */
    public final static class Builder {
        private final Map<String, String> myMap = new HashMap<String, String>();

        /**
         * Ajoute la pair key/value, à l'ensemble d'attributs en cours de
         * construction. Si l'attribut existait déjà, on change juste sa valeur.
         * 
         * @param key
         * @param value
         */
        public void put(String key, String value) {
            myMap.put(key, value);
        }

        /**
         * Construit un ensemble d'attributs contenant les paires key/value
         * ajoutées jusqu'à présent.
         * 
         * @return
         */
        public Attributes build() {
            return new Attributes(myMap);
        }
    }
}
