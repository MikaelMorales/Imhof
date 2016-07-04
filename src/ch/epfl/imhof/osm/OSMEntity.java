package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * OSMEntity est la classe mère de toutes les données OpenStreetMap Elle
 * est abstraite et immuable.
 * 
 * @author Mikael Morales Gonzalez (234747)
 * @author Charles Parzy Turlat (250628)
 * 
 */
public abstract class OSMEntity {
    private final long id;
    private final Attributes attributes;

    /**
     *
     * Constructeur dont le seul but est d'être appelé par les constructeurs des
     * sous-classes
     * 
     * @param id
     *            (long) un entier représentant l'identifiant de l'entité
     * @param attributes
     *            (Attributes) les attributs caractérisant l'entité
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * 
     * @return id (long) getter retournant l'identifiant de l'entité.
     */
    public long id() {
        return id;
    }

    /**
     * 
     * @return attributes getter retournant les attributes caractérisant
     *         l'entité.
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * 
     * @param key
     *            une clé
     * @return boolean true si la clé appartient à attributes false sinon.
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * 
     * @param key
     *            une clé
     * @return la valeur associée à cette clé dans attributes.
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }

    /**
     * Builder imbriqué statiquement dans la classe OSMEntity classe mère des
     * builder des entités OpenStreetMap
     * 
     * @author Mikael Morales Gonzalez (234747)
     * @author Charles Parzy Turlat (250628)
     */
    public static abstract class Builder {
        /**
         * 
         * Ici id est protected afin qu'il puisse être appelé
         * dans les sous-classes de ce builder.
         */
        protected final long id;
        private boolean incomplete = false;
        private final Attributes.Builder attributes;

        /**
         * Constructeur du builder
         * 
         * @param id (long)
         *            l'identifiant
         */
        public Builder(long id) {
            this.id = id;
            attributes = new Attributes.Builder();
        }

        /**
         *  Ajoute la clé et sa valeur associée à la liste d'attributs.
         *  Si un attribut de même nom existe déjà, sa valeur est
         *  remplacée par celle donnée en paramètres.
         * 
         * @param key
         *            (String)
         * @param value
         *            (Value)
         * 
         */
        public void setAttribute(String key, String value) {
            attributes.put(key, value);
        }

        /**
         * Affecte la valeur true au boolean incomplete.
         */
        public void setIncomplete() {
            incomplete = true;
        }

        /**
         * Getter qui retourne le boolean incomplete.
         * 
         * @return incomplete (boolean)
         */
        public boolean isIncomplete() {
            return incomplete;
        }
        
        /**
         * Getter protected qui permet aux sous-classes d'avoir accès à
         * l'attribut de l'entité OSM en cours de construction.
         * @return attributes.build()
         */
        protected Attributes getAttributes() {
            return attributes.build();
        }
    }
}
