package fiap.ddd.gs.entities;

public class Login extends _BaseEntity{
    private String email;
    private String senha;
    private Cadastro cadastro;

    public Login(){}

    public Login(int id, String email, String senha, Cadastro cadastro) {
        super(id);
        this.email = email;
        this.senha = senha;
        this.cadastro = cadastro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cadastro getCadastro() {
        return cadastro;
    }

    public void setCadastro(Cadastro cadastro) {
        this.cadastro = cadastro;
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", cadastro=" + cadastro +
                "} " + super.toString();
    }
}
