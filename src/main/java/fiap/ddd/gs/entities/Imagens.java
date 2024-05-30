package fiap.ddd.gs.entities;

public class Imagens extends _BaseEntity{

    private String nomeArquivo;
    private double tamanhoAequivo;
    private Especimes especimes;

    public Imagens(){}

    public Imagens(int id, String nomeArquivo, double tamanhoAequivo, Especimes especimes) {
        super(id);
        this.nomeArquivo = nomeArquivo;
        this.tamanhoAequivo = tamanhoAequivo;
        this.especimes = especimes;
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

    public Especimes getEspecimes() {
        return especimes;
    }

    public void setEspecimes(Especimes especimes) {
        this.especimes = especimes;
    }

    @Override
    public String toString() {
        return "Imagens{" +
                "nomeArquivo='" + nomeArquivo + '\'' +
                ", tamanhoAequivo=" + tamanhoAequivo +
                ", especimes=" + especimes +
                "} " + super.toString();
    }
}
