package fiap.ddd.gs.entities;

public class Imagens extends _BaseEntity{

    private String nomeArquivo;
    private double tamanhoAequivo;

    public Imagens(){}

    public Imagens(int id, String nomeArquivo, double tamanhoAequivo) {
        super(id);
        this.nomeArquivo = nomeArquivo;
        this.tamanhoAequivo = tamanhoAequivo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public double getTamanhoAequivo() {
        return tamanhoAequivo;
    }

    public void setTamanhoAequivo(double tamanhoAequivo) {
        this.tamanhoAequivo = tamanhoAequivo;
    }

    @Override
    public String toString() {
        return "Imagens{" +
                "nomeArquivo='" + nomeArquivo + '\'' +
                ", tamanhoAequivo=" + tamanhoAequivo +
                "} " + super.toString();
    }
}
