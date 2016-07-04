package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * Détermine selon une entité attribuée, si elle doit être gardée ou non à
 * l'aide d'un Predicate.
 *
 * @author Charles Parzy Turlat (250628)
 * @author Morales Gonzalez Mikael (234747)
 */
public final class Filters {

    /**
     * Constructeur de Filters privé, afin que la classe soit non instanciable
     */
    private Filters() {
    }

    /**
     * 
     * @param name
     *            Valeur de la clé
     * @return Retourne un prédicat qui est vrai si et seulement si la valeur
     *         attribué à laquelle on l'applique possède un attribut portant le
     *         nom donnée en argument.
     */
    public static Predicate<Attributed<?>> tagged(String name) {
        return x -> x.hasAttribute(name);
    }

    /**
     * 
     * @param name
     *            Valeur de la clé
     * @param value
     *            Valeur associé à la clé
     * @return Retourne un prédicat qui est vrai si et seulement si la valeur
     *         attribué à laquelle on l'applique possède un attribut portant le
     *         nom donnée en argument, et si de plus la valeur associée à cet
     *         attribut fait partie de celle données.
     */
    public static Predicate<Attributed<?>> tagged(String name, String... value) {
        if (value.length <= 0)
            throw new IllegalArgumentException();
        return x -> {
            if (x.hasAttribute(name)) {
                for (String s : value) {
                    if (x.attributeValue(name).equals(s))
                        return true;
                }
            }
            return false;
        };
    }

    /**
     * 
     * @param layer
     * @return Retourne un prédicat qui n'est vrai que lorsqu'on l'applique à
     *         une entité attribuée appartenant à cette couche.
     */
    public static Predicate<Attributed<?>> onLayer(int layer) {
        return x -> x.attributeValue("layer", 0) == layer;
    }

}
