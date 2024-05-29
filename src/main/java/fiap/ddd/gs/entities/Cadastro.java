package fiap.ddd.gs.entities;

public class Cadastro extends _BaseEntity{
    private String nome;
    private String sobrenome;
    private String email;
    private String causaSocial;
    private String senha;

    public Cadastro() {}

    public Cadastro(int id, String nome, String sobrenome, String email, String causaSocial, String senha) {
        super(id);
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.causaSocial = causaSocial;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCausaSocial() {
        return causaSocial;
    }

    public void setCausaSocial(String causaSocial) {
        this.causaSocial = causaSocial;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Cadastro{" +
                "nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", email='" + email + '\'' +
                ", causaSocial='" + causaSocial + '\'' +
                ", senha='" + senha + '\'' +
                "} " + super.toString();
    }
}
