package fr.aureprod.tarkox.datatype;

public class ExtractionArea extends Area {
    private Integer waitTime;

    public ExtractionArea(Position min, Position max, Integer waitTime) {
        super(min, max);
        
        this.waitTime = waitTime;
    }

    public Integer getWaitTime() {
        return this.waitTime;
    }
}
