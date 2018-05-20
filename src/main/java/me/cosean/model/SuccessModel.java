package me.cosean.model;

public class SuccessModel {
    private double recall;
    private double precision;
    private double f_Meaure;

    public SuccessModel(double positive, double totalActual, double totalPredicted) {
        this.recall = positive / totalActual;
        this.precision = positive / totalPredicted;
        this.f_Meaure= (2*this.recall*this.precision)/(this.recall+this.precision);
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getF_Meaure() {
        return f_Meaure;
    }

    public void setF_Meaure(double f_Meaure) {
        this.f_Meaure = f_Meaure;
    }
}
