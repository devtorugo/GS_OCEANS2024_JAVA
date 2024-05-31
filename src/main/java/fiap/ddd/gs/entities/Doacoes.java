package fiap.ddd.gs.entities;

import fiap.ddd.gs.services.ViaCepService;

public class Doacoes extends _BaseEntity {
    private String nomeDoador;
    private String cpf;
    private String cep;
    private double valorDoacao;
    private String descricao;
    private Login login;
    private ViaCepService.Endereco endereco;

    public Doacoes() {}

    public Doacoes(int id, String nomeDoador, String cpf, String cep, double valorDoacao, String descricao, Login login) {
        super(id);
        this.nomeDoador = nomeDoador;
        this.cpf = cpf;
        this.cep = cep;
        this.valorDoacao = valorDoacao;
        this.descricao = descricao;
        this.login = login;
    }

    public String getNomeDoador() { return nomeDoador; }
    public void setNomeDoador(String nomeDoador) { this.nomeDoador = nomeDoador; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public double getValorDoacao() { return valorDoacao; }
    public void setValorDoacao(double valorDoacao) { this.valorDoacao = valorDoacao; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Login getLogin() { return login; }
    public void setLogin(Login login) { this.login = login; }

    public ViaCepService.Endereco getEndereco() { return endereco; }
    public void setEndereco(ViaCepService.Endereco endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return "Doacoes{" +
                "nomeDoador='" + nomeDoador + '\'' +
                ", cpf='" + cpf + '\'' +
                ", cep='" + cep + '\'' +
                ", valorDoacao=" + valorDoacao +
                ", descricao='" + descricao + '\'' +
                ", login=" + login +
                ", endereco=" + endereco +
                "} " + super.toString();
    }
}
