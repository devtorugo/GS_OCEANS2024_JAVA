package fiap.ddd.gs.entities;

public class Imagens extends _BaseEntity{

    private String nomeArquivo;
    private double tamanhoArquivo;
    private Especimes especimes;

    public Imagens(){}

    public Imagens(int id, String nomeArquivo, double tamanhoArquivo, Especimes especimes) {
        super(id);
        this.nomeArquivo = nomeArquivo;
        this.tamanhoArquivo = tamanhoArquivo;
        this.especimes = especimes;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public double getTamanhoArquivo() {
        return tamanhoArquivo;
    }

    public void setTamanhoArquivo(double tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
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
                ", tamanhoAequivo=" + tamanhoArquivo +
                ", especimes=" + especimes +
                "} " + super.toString();
    }
}
