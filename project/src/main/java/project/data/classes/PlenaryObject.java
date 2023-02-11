package project.data.classes;

public class PlenaryObject implements Comparable<PlenaryObject> {

    private String id;
    private int electionPeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getElectionPeriod() {
        return electionPeriod;
    }

    public void setElectionPeriod(int electionPeriod) {
        this.electionPeriod = electionPeriod;
    }

    @Override
    public int compareTo(PlenaryObject o) {
        return id.compareTo(o.getId());
    }
}
