package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * Représente une relation OpenStreetMap(OSM).
 * 
 * @author Morales Gonzalez Mikael (234747)
 * @author Charles Parzy Turlat (250628).
 */
public final class OSMRelation extends OSMEntity {

    private final List<Member> members;

    /**
     * Construit une relation étant donnés son identifiant unique, ses membres
     * et ses attributs.
     * 
     * @param long id l'identifiant
     * @param List
     *            <Member> members la liste de membres
     * @param Attributes
     *            attributes les attributs
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<Member>(
                members));
    }

    /**
     * Retourne la liste des membres de la relation.
     * 
     * @return List<Member> members
     */
    public List<Member> members() {
        return members;
    }

    /**
     * Représente un membre d'une relation OSM.
     * 
     * @author Charles Parzy Turlat (250628)
     * @author Morales Gonzalez Mikael (234747)
     */
    public static final class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;

        /**
         * Construit un membre ayant un type, un rôle et la valeur donnés.
         * 
         * @param type
         * @param role
         * @param member
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }

        /**
         * Retourne le type du membre.
         * 
         * @return type
         */
        public Type type() {
            return type;
        }

        /**
         * Retourne le rôle du membre.
         * 
         * @return role
         */
        public String role() {
            return role;
        }

        /**
         * Retourne le membre lui-même.
         * 
         * @return member
         */
        public OSMEntity member() {
            return member;
        }

        /**
         * Enumère les trois types de membre qu'une relation peut avoir.
         * (Noeuds, chemins et relations)
         * 
         * @author Charles Parzy Turlat (250628)
         * @author Morales Gonzalez Mikael (234747)
         */
        public enum Type {
            NODE, WAY, RELATION;
        }
    }

    /**
     * Bâtisseur de OSMRelation qui permet de construire une relation en
     * plusieurs étapes. Elle hérite du bâtisseur de OSMEntity.
     * 
     * @author Charles Parzy Turlat (250628)
     * @author Morales Gonzalez Mikael (234747)
     */
    public final static class Builder extends OSMEntity.Builder {
        private final List<Member> members;

        /**
         * Construit un bâtisseur pour une relation ayant l'identifiant donné,
         * elle fait appelle au constructeur de OSMEntity.Builder.
         * 
         * @param long id
         */
        public Builder(long id) {
            super(id);
            members = new ArrayList<Member>();
        }

        /**
         * Ajoute un nouveau membre de type et de rôle donnés à la relation.
         * 
         * @param Member
         *            .type type
         * @param String
         *            role
         * @param OSMEntity
         *            newMember
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }

        /**
         * Construit et retourne la relation ayant l'identifiant passé au
         * constructeur ainsi que les membres et les attributs ajoutés jusqu'à
         * présent au batîsseur. Elle lève une exception si la relation en
         * cours est incomplète.
         * 
         * @return objet de type OSMRelation
         */
        public OSMRelation build() {
            if (isIncomplete()) {
                throw new IllegalStateException();
            }
            return new OSMRelation(id, members, getAttributes());
        }
    }
}
