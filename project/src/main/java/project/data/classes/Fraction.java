package project.data.classes;

import java.util.HashSet;
import java.util.Set;

/**
 * class Fraction represents a fraction in the parliament
 */
public class Fraction extends PlenaryObject {
    /**
     * constructor for Fraction
     * @param name //name of the fraction in parliament
     */
    public Fraction(String name){
        this.name = name;
    }
    private String name;
    private Set<Speaker> members = new HashSet<Speaker>();
    public String getName(){
        return name;
    }
    public void addMember(Speaker speaker){
        members.add(speaker);
    }

    public Set<Speaker> getMembers(){
        return members;
    }
}
