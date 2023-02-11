package project.data.classes;

public class Text extends PlenaryObject{
    private Speaker speaker;
    private Speech speech;
    private String text;

    public Text(Speaker speaker, Speech speech, String text){
        this.speaker = speaker;
        this.speech = speech;
        this.text = text;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public Speech getSpeech() {
        return speech;
    }

    public void setSpeech(Speech speech) {
        this.speech = speech;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
