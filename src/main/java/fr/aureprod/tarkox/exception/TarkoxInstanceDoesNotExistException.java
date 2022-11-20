package fr.aureprod.tarkox.exception;

public class TarkoxInstanceDoesNotExistException extends TarkoxException {
    private String instanceName;
    
    public TarkoxInstanceDoesNotExistException(String instanceName) {
        super();

        this.instanceName = instanceName;
    }

    public String getInstanceName() {
        return instanceName;
    }
}
