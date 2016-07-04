package ch.epfl.imhof;

/**
 * Représente une entité de type T dotée d'attributs
 * 
 * @author Charles Parzy Turlat (250628)
 * @author Mikael Morales Gonzalez (234747)
 *
 * @param <T>
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;

    /**
     * Construit un objet Attributed en associant les paramètres aux champs de
     * la classe.
     * 
     * @param value
     *            de type<T>
     * @param attributes
     *            de type Attributes
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * Getter qui retourne la valeur à laquelle les attributs sont attachés
     * 
     * @return value
     */
    public T value() {
        return value;
    }

    /**
     * Getter qui retourne les attributs attachés à la valeur
     * 
     * @return attributes un objet de type Attributes
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * Retourne vrai si et seulement si les attributs incluent celui dont le nom
     * est passé en argument.
     * 
     * @param attributeName
     * @return true/false
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * Retourne la valeur associée à l'attribut donné en paramètre, ou null s'
     * il n'existe pas.
     * 
     * @param attributeName
     * @return valeur de attributeName ou null
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * Retourne la valeur associée à l'attribut donné, ou la valeur par défaut
     * s'il n'existe pas.
     * 
     * @param attributeName
     * @param defaultValue
     * @return valeur de attributeName ou defaultValue
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * Retourne la valeur entière associée à l'attribut donné, ou la valeur par
     * défaut s'il n'existe pas ou si la valeur associée n'est pas un entier
     * valide.
     * 
     * @param attributeName
     * @param defaultValue
     * @return valeur de attributeName ou defautlValue
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }
}
