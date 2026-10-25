package shared;

import java.io.Serializable;
public class operation implements Serializable{
     private static final long serialVersionUID = 1L;

    private double operande1;
    private double operande2;
    private String operateur;

    public operation(double operande1, String operateur, double operande2) {
        this.operande1 = operande1;
        this.operande2 = operande2;
        this.operateur = operateur;
    }

    public double getOperande1() { return operande1; }
    public double getOperande2() { return operande2; }
    public String getOperateur() { return operateur; }

    
}
